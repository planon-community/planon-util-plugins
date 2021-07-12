package edu.planon.lib.client.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.dto.PnReferenceFieldDefDTO;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.esapi.ESContextUtil;
import edu.planon.lib.esapi.ESQueryUtils;
import edu.planon.lib.esapi.exception.ESApiException;
import nl.planon.enterprise.service.api.IPnESBODefinition;
import nl.planon.enterprise.service.api.IPnESDatabaseQuery;
import nl.planon.enterprise.service.api.IPnESFieldDefinition;
import nl.planon.enterprise.service.api.PnESBusinessException;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnReferenceFieldLookup {
	private List<String> fieldList;
	private HashSet<String> displayFields;
	private boolean displayAll;
	private IPnESBODefinition boDef;
	private IPnESDatabaseQuery query;
	private HashMap<String, PnFieldDefDTO> fieldDefMap = new HashMap<String, PnFieldDefDTO>();
	
	private String currFieldName = "";
	private IPnESFieldDefinition curFieldDef = null;
	
	public PnReferenceFieldLookup(PnReferenceFieldDefDTO referenceFieldDTO) throws PnClientException {
		try {
			String boPnName = referenceFieldDTO.getBoPnName();
			this.fieldList = Arrays.asList(referenceFieldDTO.getFields());
			this.displayFields = new HashSet<String>(this.fieldList);
			
			this.displayAll = this.displayFields.size() == 0;
			this.boDef = ESContextUtil.getContext().getBODefinition(boPnName);
			this.query = ESQueryUtils.getBOInQuickSelectionDataBaseQuery(boPnName);
			
			//Get Search fields
			int numSearchExpressions = this.query.getNumberOfSearchExpressions();
			for (int i = 0; i < numSearchExpressions; i++) {
				String fieldName = this.query.getSearchExpressionName(i);
				
				PnFieldDefDTO fieldDefDTO = this.getFieldDefDTO(fieldName, -1);
				if (fieldDefDTO != null) {
					fieldDefDTO.setQueryFilterField(true);
					this.fieldDefMap.put(fieldName, fieldDefDTO);
				}
			}
			
			//check other fields not in the search
			for (String fieldName : new ArrayList<String>(this.displayFields)) {
				PnFieldDefDTO fieldDefDTO = this.getFieldDefDTO(fieldName, -1);
				if (fieldDefDTO != null) {
					fieldDefDTO.setQueryFilterField(true);
					this.fieldDefMap.put(fieldName, fieldDefDTO);
				}
			}
			
			//Add result fields not in the query if display not set
			if (this.displayAll) {
				int numCols = this.query.getNumberOfColumns();
				for (int i = 0; i < numCols; i++) {
					String fieldName = this.query.getColumnName(i);
					
					if (!this.fieldDefMap.containsKey(fieldName)) {
						PnFieldDefDTO fieldDefDTO = this.getFieldDefDTO(fieldName, i);
						if (fieldDefDTO != null) {
							fieldDefDTO.setQueryFilterField(true);
							this.fieldDefMap.put(fieldName, fieldDefDTO);
						}
					}
				}
			}
			
		}
		catch (PnESBusinessException | ESApiException e) {
			throw new PnClientException(e.getMessage(), e);
		}
	}
	
	public List<PnFieldDefDTO> getFields() {
		ArrayList<PnFieldDefDTO> sortedFieldDefList = new ArrayList<PnFieldDefDTO>(this.fieldDefMap.size());
		
		for (String fieldName : this.fieldList) {
			PnFieldDefDTO fieldDef = this.fieldDefMap.remove(fieldName);
			if (fieldDef != null) {
				sortedFieldDefList.add(fieldDef);
			}
		}
		sortedFieldDefList.addAll(this.fieldDefMap.values());
		
		return sortedFieldDefList;
	}
	
	private IPnESFieldDefinition getFieldDefinition(String fieldName) {
		if (!this.currFieldName.equals(fieldName)) {
			this.currFieldName = fieldName;
			this.curFieldDef = this.boDef.getFieldDefinition(fieldName);
		}
		
		return this.curFieldDef;
	}
	
	private boolean hasFieldDefinition(String fieldName) {
		return this.getFieldDefinition(fieldName) != null;
	}
	
	private PnFieldDefDTO getFieldDefDTO(String fieldName, int colNum) throws PnESBusinessException {
		PnFieldDefDTO fieldDefDTO = null;
		IPnESFieldDefinition fieldDef = null;
		PnESValueType fieldType = null;
		
		int columnNumber = (colNum >= 0) ? colNum : this.query.getColumnNumber(fieldName);
		if (columnNumber >= 0) {
			//if also part of output
			fieldType = this.query.getColumnType(columnNumber);
			if (fieldType == null) {
				return null;
			}
			
			fieldDefDTO = new PnFieldDefDTO(fieldName, this.query.getColumnLabel(columnNumber), fieldType);
			fieldDefDTO.setQueryResultField(true);
			
			if (this.displayAll && PnFieldTypeUtils.isQueryResultDisplaySupported(fieldType)) {
				fieldDefDTO.setLookupDisplayField(true);
			}
		}
		else {
			//if filter only
			fieldDef = this.getFieldDefinition(fieldName);
			if (fieldDef == null) {
				return null;
			}
			
			fieldType = fieldDef.getValueType();
			if (fieldType == null) {
				return null;
			}
			
			fieldDefDTO = new PnFieldDefDTO(fieldName, fieldDef.getLabel(), fieldType);
		}
		
		if (this.hasFieldDefinition(fieldName)) {
			fieldDef = this.getFieldDefinition(fieldName);
			
			//handle reference fields
			if (fieldType.equals(PnESValueType.REFERENCE)) {
				fieldDefDTO.setReferenceFieldDef(new PnReferenceFieldDefDTO(fieldDef.getReferencedBOType().getPnName(), new String[0]));
			}
			
			fieldDefDTO.setInputLength(fieldDef.getInputLength());
		}
		
		//if a display field
		if (this.displayFields.remove(fieldName)) {
			fieldDefDTO.setLookupDisplayField(true);
		}
		
		return fieldDefDTO;
	}
}

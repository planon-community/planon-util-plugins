package edu.planon.lib.client.search.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.model.Model;

import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.search.dto.PnSearchFieldDTO;
import edu.planon.lib.client.search.dto.PnSearchOperatorDTO;
import nl.planon.enterprise.service.api.PnESOperator;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnSearchFilterModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean activeSearch = false;
	private Model<PnSearchFieldDTO> searchField;
	private Model<PnSearchOperatorDTO> searchOperator;
	
	private Model<Date> date1Model = new Model<Date>();
	private Model<Date> date2Model = new Model<Date>();
	private Model<String> string1Model = new Model<String>();
	private Model<String> string2Model = new Model<String>();
	private List<PnRecordDTO> referenceValueList;
	
	private String fieldName;
	private PnESOperator operator;
	private List<Object> values;
	
	public PnSearchFilterModel() {
		this.searchField = new Model<PnSearchFieldDTO>();
		this.searchOperator = new Model<PnSearchOperatorDTO>();
	}
	
	public PnSearchFilterModel(String fieldName, PnESOperator operator, List<Object> values) {
		this.fieldName = fieldName;
		this.operator = operator;
		this.values = values;
		this.activeSearch = true;
	}
	
	public boolean isActiveSearch() {
		return this.activeSearch;
	}
	
	public void setActiveSearch(boolean aActiveSearch) {
		this.activeSearch = aActiveSearch;
	}
	
	public PnSearchFieldDTO getSearchField() {
		return this.searchField.getObject();
	}
	
	public void setSearchField(PnSearchFieldDTO searchField) {
		this.searchField.setObject(searchField);
	}
	
	public PnSearchOperatorDTO getSearchOperator() {
		return this.searchOperator.getObject();
	}
	
	public void setSearchOperator(PnSearchOperatorDTO searchOperator) {
		this.searchOperator.setObject(searchOperator);
	}
	
	public String getFieldName() {
		if (this.fieldName != null) {
			return this.fieldName;
		}
		
		PnSearchFieldDTO searchField = this.searchField.getObject();
		if (searchField == null) {
			return null;
		}
		return searchField.getPnName();
	}
	
	public PnESOperator getOperator() {
		if (this.operator != null) {
			return this.operator;
		}
		
		PnSearchOperatorDTO searchOperator = this.searchOperator.getObject();
		if (searchOperator == null) {
			return null;
		}
		return searchOperator.getOperator();
	}
	
	public Model<Date> getDate1Model() {
		return this.date1Model;
	}
	
	public Model<Date> getDate2Model() {
		return this.date2Model;
	}
	
	public Model<String> getString1Model() {
		return this.string1Model;
	}
	
	public Model<String> getString2Model() {
		return this.string2Model;
	}
	
	public List<PnRecordDTO> getReferenceValueList() {
		return this.referenceValueList;
	}
	
	public void setReferenceValueList(List<PnRecordDTO> referenceValueList) {
		this.referenceValueList = referenceValueList;
	}
	
	public List<Object> getSearchValues() {
		if (this.values != null) {
			return this.values;
		}
		
		PnESValueType fieldType = this.getSearchField().getFieldType();
		List<Object> searchValues = new ArrayList<Object>();
		
		if (this.getOperator().equals(PnESOperator.BETWEEN)) {
			if (fieldType.equals(PnESValueType.DATE_NEUTRAL) || fieldType.equals(PnESValueType.DATE_TIME)
					|| fieldType.equals(PnESValueType.DATE_TIME_NEUTRAL)) {
				Date date1 = this.getDate1Model().getObject();
				Date date2 = this.getDate2Model().getObject();
				if (date1 != null && date2 != null) {
					searchValues.add(date1);
					searchValues.add(date2);
				}
			}
			else {
				String textValue1 = this.getString1Model().getObject();
				String textValue2 = this.getString2Model().getObject();
				if (textValue1 != null && !textValue1.isEmpty() && textValue2 != null && !textValue2.isEmpty()) {
					if (fieldType.equals(PnESValueType.BIG_DECIMAL)) {
						searchValues.add(new BigDecimal(textValue1));
						searchValues.add(new BigDecimal(textValue2));
					}
					else if (fieldType.equals(PnESValueType.INTEGER)) {
						searchValues.add(Integer.valueOf(textValue1));
						searchValues.add(Integer.valueOf(textValue2));
					}
					else {
						searchValues.add(textValue1);
						searchValues.add(textValue2);
					}
				}
			}
		}
		else {
			if (fieldType.equals(PnESValueType.STRING_REFERENCE)) {
				if (this.referenceValueList != null && !this.referenceValueList.isEmpty()) {
					for (PnRecordDTO table : this.referenceValueList) {
						searchValues.add(table.getFields()[0]);
					}
				}
			}
			else if (fieldType.equals(PnESValueType.REFERENCE)) {
				if (this.referenceValueList != null && !this.referenceValueList.isEmpty()) {
					for (PnRecordDTO table : this.referenceValueList) {
						searchValues.add(Integer.valueOf(table.getUniqueID()));
					}
				}
			}
			else if (fieldType.equals(PnESValueType.DATE_NEUTRAL) || fieldType.equals(PnESValueType.DATE_TIME)
					|| fieldType.equals(PnESValueType.DATE_TIME_NEUTRAL)) {
				Date date = this.getDate1Model().getObject();
				if (date != null) {
					searchValues.add(date);
				}
			}
			else {
				String textValue = this.getString1Model().getObject();
				if (textValue != null && !textValue.isEmpty()) {
					if (fieldType.equals(PnESValueType.BIG_DECIMAL)) {
						searchValues.add(new BigDecimal(textValue));
					}
					else if (fieldType.equals(PnESValueType.INTEGER)) {
						searchValues.add(Integer.valueOf(textValue));
					}
					
					else {
						searchValues.add(textValue);
					}
				}
			}
		}
		
		return searchValues;
	}
	
	public void clearSearchValues() {
		this.date1Model.setObject(null);
		this.date2Model.setObject(null);
		this.string1Model.setObject("");
		this.string2Model.setObject("");
		if (this.referenceValueList != null) {
			this.referenceValueList.clear();
		}
		if (this.values != null) {
			this.values.clear();
		}
	}
	
	@Override
	public String toString() {
		return "PnSearchFilterModel [activeSearch=" + this.activeSearch + ", fieldName=" + this.getFieldName() + ", operator=" + this.getOperator()
				+ ", values=" + this.getSearchValues() + "]";
	}
}

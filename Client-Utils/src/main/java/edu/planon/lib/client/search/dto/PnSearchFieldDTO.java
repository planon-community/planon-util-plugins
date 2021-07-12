package edu.planon.lib.client.search.dto;

import java.util.ArrayList;
import java.util.List;

import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnSearchFieldDTO extends PnFieldDefDTO {
	private static final long serialVersionUID = 1L;
	private static final List<PnESValueType> SUPPORTED_TYPE = new ArrayList<PnESValueType>();
	private int searchBarID = -1;
	private final boolean supported;
	
	public PnSearchFieldDTO(PnFieldDefDTO fieldDTO) {
		super(fieldDTO.getPnName(), fieldDTO.getLabel(), fieldDTO.getFieldType(), fieldDTO.getReferenceFieldDef());
		
		this.setRequired(fieldDTO.isRequired());
		this.setInputLength(fieldDTO.getInputLength());
		this.setDecimalScale(fieldDTO.getDecimalScale());
		this.setMultiSelect(fieldDTO.isMultiSelect());
		this.setExtendedField(fieldDTO.isExtendedField());
		this.setQueryResultField(fieldDTO.isQueryResultField());
		this.setQueryFilterField(fieldDTO.isQueryFilterField());
		this.setLookupDisplayField(fieldDTO.isLookupDisplayField());
		
		this.supported = fieldDTO.getFieldType() == null || SUPPORTED_TYPE.contains(fieldDTO.getFieldType());
	}
	
	public int getSearchBarID() {
		return this.searchBarID;
	}
	
	public void setSearchBarID(int aSearchBarID) {
		this.searchBarID = aSearchBarID;
	}
	
	public boolean isAvailable(int componentSearchID) {
		return this.supported && (this.searchBarID < 0 || this.searchBarID == componentSearchID);
	}
	
	public boolean isSupported() {
		return this.supported;
	}
	
	static {
		SUPPORTED_TYPE.add(PnESValueType.STRING);
		SUPPORTED_TYPE.add(PnESValueType.BIG_DECIMAL);
		SUPPORTED_TYPE.add(PnESValueType.STRING_REFERENCE);
		SUPPORTED_TYPE.add(PnESValueType.REFERENCE);
		SUPPORTED_TYPE.add(PnESValueType.DATE_NEUTRAL);
		SUPPORTED_TYPE.add(PnESValueType.DATE_TIME);
		SUPPORTED_TYPE.add(PnESValueType.DATE_TIME_NEUTRAL);
	}
}

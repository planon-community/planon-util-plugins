package edu.planon.lib.client.common.dto;

import java.util.List;

import org.apache.wicket.model.Model;

import edu.planon.lib.client.search.model.PnSearchFilterModel;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnReferenceFieldDTO extends PnFieldDTO<PnRecordListDTO> {
	private static final long serialVersionUID = 1L;
	private Model<String> displayValueModel = new Model<String>();
	private List<PnSearchFilterModel> defaultSearchFilter;
	
	@SuppressWarnings("serial")
	public PnReferenceFieldDTO(String pnName, String label, String pnBOName, String... referenceFields) {
		super(PnRecordListDTO.class, pnName, label, PnESValueType.REFERENCE, new PnReferenceFieldDefDTO(pnBOName, referenceFields));
		this.setValue(new PnRecordListDTO() {});
	}
	
	public void setValue(PnRecordDTO value) {
		this.getValue().clear();
		this.getValue().add(value);
		
		this.getDisplayValueModel().setObject(this.getDisplayValue());
	}
	
	public void setValues(List<PnRecordDTO> values) {
		this.getValue().clear();
		this.getValue().addAll(values);
		
		this.getDisplayValueModel().setObject(this.getDisplayValue());
	}
	
	public Model<String> getDisplayValueModel() {
		return this.displayValueModel;
	}
	
	protected String getDisplayValue() {
		if (this.getValue().isEmpty()) {
			return null;
		}
		if (this.getValue().size() == 1) {
			PnRecordDTO tableDTO = this.getValue().get(0);
			String[] fields = tableDTO.getFields();
			StringBuilder displayValue = new StringBuilder();
			if (fields[0] != null && !fields[0].isEmpty()) {
				displayValue.append(fields[0]);
			}
			if (fields.length > 1 && fields[1] != null && !fields[1].isEmpty()) {
				displayValue = displayValue.length() > 0 ? displayValue.append(", ") : displayValue;
				displayValue.append(fields[1]);
			}
			return displayValue.toString();
		}
		return "Multiple values selected";
	}
	
	public List<PnRecordDTO> getSelectedList() {
		return this.getValue();
	}
	
	public List<PnSearchFilterModel> getDefaultSearchFilter() {
		return this.defaultSearchFilter;
	}
	
	public void setDefaultSearchFilter(List<PnSearchFilterModel> aDefaultSearchFilter) {
		this.defaultSearchFilter = aDefaultSearchFilter;
	}
}

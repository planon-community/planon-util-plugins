package edu.planon.lib.client.dto;

import java.io.Serializable;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnFieldDefDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String pnName;
	private String label;
	private PnESValueType fieldType;
	private Integer inputLength;
	private int decimalScale = 2;
	private boolean required;
	private boolean multiSelect;
	private boolean extended = false;
	
	public PnFieldDefDTO(String pnName) {
		this.pnName = pnName;
	}
	
	public PnFieldDefDTO(String pnName, String label) {
		this(pnName);
		this.label = label;
	}
	
	public PnFieldDefDTO(String pnName, String label, PnESValueType fieldType) {
		this(pnName, label);
		this.fieldType = fieldType;
	}
	
	public String getPnName() {
		return pnName;
	}
	
	public void setPnName(String pnName) {
		this.pnName = pnName;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public PnESValueType getFieldType() {
		return fieldType;
	}
	
	public void setFieldType(PnESValueType fieldType) {
		this.fieldType = fieldType;
	}
	
	public boolean isRequired() {
		return required;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public Integer getInputLength() {
		return this.inputLength;
	}
	
	public void setInputLength(Integer inputLength) {
		this.inputLength = inputLength;
	}
	
	public int getDecimalScale() {
		return this.decimalScale;
	}
	
	public void setDecimalScale(int scale) {
		this.decimalScale = scale;
	}
	
	public boolean isMultiSelect() {
		return this.multiSelect;
	}
	
	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}
	
	public boolean isExtended() {
		return this.extended;
	}
	
	public void setExtended(boolean extended) {
		this.extended = extended;
	}
}

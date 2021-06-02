package edu.planon.lib.client.dto;

import java.io.Serializable;
import org.apache.wicket.model.Model;

import nl.planon.enterprise.service.api.PnESValueType;

public class PnFieldDTO<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;
	private String pnName;
	private String label;
	private PnESValueType fieldType;
	private Integer inputLength;
	private int decimalScale = 2;
	private boolean required;
	private boolean enabled = true;
	private Model<T> value = new Model<T>();
	
	public PnFieldDTO(String pnName) {
		this(pnName, null);
		this.pnName = pnName;
	}
	
	public PnFieldDTO(String pnName, String label) {
		this(pnName, label, null);
	}
	
	public PnFieldDTO(String pnName, String label, PnESValueType fieldType) {
		this.pnName = pnName;
		this.label = label;
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
	
	public Model<T> getValueModel() {
		return value;
	}
	
	public T getValue() {
		return value.getObject();
	}
	
	public void setValue(T value) {
		this.value.setObject(value);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
}

package edu.planon.lib.client.common.dto;

import java.io.Serializable;
import java.util.Objects;

import nl.planon.enterprise.service.api.PnESValueType;

public class PnFieldDefDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String pnName;
	private String label;
	private final PnESValueType fieldType;
	protected PnReferenceFieldDefDTO referenceFieldDef;
	private Integer inputLength;
	private int decimalScale = 2;
	private boolean required;
	private boolean multiSelect = false;
	private boolean extendedField = false;
	private boolean lookupDisplayField = false;
	private boolean queryFilterField = false;
	private boolean queryResultField = false;
	
	public PnFieldDefDTO(String pnName, String label, PnESValueType fieldType) {
		this.pnName = pnName;
		this.label = label;
		this.fieldType = fieldType;
	}
	
	public PnFieldDefDTO(String pnName, String label, PnESValueType fieldType, PnReferenceFieldDefDTO referenceFieldDef) {
		this(pnName, label, fieldType);
		this.referenceFieldDef = referenceFieldDef;
	}
	
	/**
	 * @return The Planon system name of the field.
	 */
	public String getPnName() {
		return this.pnName;
	}
	
	/**
	 * @return The display name / label of the field.
	 */
	public String getLabel() {
		return this.label;
	}
	
	/**
	 * Sets the display name / label of the field.
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * @return The type of value this field represents. Note: This is the value type used by the API, it
	 *         is sub-set of the actual field types in Planon.
	 */
	public PnESValueType getFieldType() {
		return this.fieldType;
	}
	
	/**
	 * @return The details of the reference field. (Only applicable if this field represents a
	 *         <code>REFERENCE</code> or <code>STRING_REFERENCE</code> field type.)
	 */
	public PnReferenceFieldDefDTO getReferenceFieldDef() {
		return this.referenceFieldDef;
	}
	
	/**
	 * Sets the details of the referenceField. (Only applicable if this field represents a
	 * <code>REFERENCE</code> or <code>STRING_REFERENCE</code> field type.)
	 * 
	 * @param referenceFieldDef
	 */
	public void setReferenceFieldDef(PnReferenceFieldDefDTO referenceFieldDef) {
		this.referenceFieldDef = referenceFieldDef;
	}
	
	/**
	 * @return True if this field must be filled out to submit the record.
	 */
	public boolean isRequired() {
		return this.required;
	}
	
	/**
	 * Sets whether or not this field must be filled out to submit the record.
	 * 
	 * @param required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	/**
	 * @return The maximum input length allowed by the field.
	 */
	public Integer getInputLength() {
		return this.inputLength;
	}
	
	/**
	 * Sets the maximum input length of the field. If the input length is greater than 300 it is
	 * considered an extended field by default unless later overridden by
	 * {@link #setExtendedField(boolean)}.
	 * 
	 * @param inputLength
	 */
	public void setInputLength(Integer inputLength) {
		this.inputLength = inputLength;
		
		if (inputLength != null && inputLength > 300) {
			this.setExtendedField(true);
		}
	}
	
	/**
	 * @return The number of digits to the right of the decimal point. (Only applicable if this field
	 *         represents a <code>BigDecimal</code> field.)
	 */
	public int getDecimalScale() {
		return this.decimalScale;
	}
	
	/**
	 * Sets the number of digits to the right of the decimal point. (Only applicable if this field
	 * represents a <code>BigDecimal</code> field.)
	 * 
	 * @param scale
	 */
	public void setDecimalScale(int scale) {
		this.decimalScale = scale;
	}
	
	/**
	 * @return True if the field is a multi-select reference field
	 */
	public boolean isMultiSelect() {
		return this.multiSelect;
	}
	
	/**
	 * Sets whether or not this field is a multi-select reference field
	 * 
	 * @param multiSelect
	 */
	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}
	
	/**
	 * @return True if the field is an extended field (e.g. a comment field)
	 */
	public boolean isExtendedField() {
		return this.extendedField;
	}
	
	/**
	 * Sets whether or not this field is an extended field (e.g. a comment field). If the field has an
	 * length greater than 300 it is considered an extended field by default unless later overridden by
	 * this method.
	 * 
	 * @param extendedField
	 */
	public void setExtendedField(boolean extendedField) {
		this.extendedField = extendedField;
	}
	
	/**
	 * @return True if the field is returned in the query results
	 */
	public boolean isQueryResultField() {
		return this.queryResultField;
	}
	
	/**
	 * Sets whether or not this field is returned in the query results
	 * 
	 * @param queryResultField
	 */
	public void setQueryResultField(boolean queryResultField) {
		this.queryResultField = queryResultField;
	}
	
	/**
	 * @return True if this field can be used in a query filter.
	 */
	public boolean isQueryFilterField() {
		return this.queryFilterField;
	}
	
	/**
	 * Sets whether or not this field can be used in a query filter.
	 * 
	 * @param queryFilterField
	 */
	public void setQueryFilterField(boolean queryFilterField) {
		this.queryFilterField = queryFilterField;
	}
	
	/**
	 * @return True if this field is displayed as a part of a reference field.
	 */
	public boolean isLookupDisplayField() {
		return this.lookupDisplayField;
	}
	
	/**
	 * Sets whether or not this field is displayed as a part of a reference field.
	 * 
	 * @param lookupDisplayField
	 */
	public void setLookupDisplayField(boolean lookupDisplayField) {
		this.lookupDisplayField = lookupDisplayField;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.pnName);
	}
	
	@Override
	public String toString() {
		return "PnFieldDefDTO [pnName=" + this.pnName + ", label=" + this.label + ", fieldType=" + this.fieldType + "]";
	}
}

package edu.planon.lib.client.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.wicket.model.Model;

import edu.planon.lib.client.exception.PnClientException;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnFieldDTO<T extends Serializable> extends PnFieldDefDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Class<T> dataType;
	private boolean enabled = true;
	private Model<T> valueModel = new Model<T>();
	private T minValue;
	private T maxValue;
	
	public static PnFieldDTO<?> create(String pnName, String label, PnESValueType fieldType) throws PnClientException {
		switch (fieldType) {
			case BIG_DECIMAL:
				return new PnFieldDTO<BigDecimal>(BigDecimal.class, pnName, label, fieldType);
			case DATE_NEUTRAL:
			case DATE_TIME:
			case DATE_TIME_NEUTRAL:
			case TIME_NEUTRAL:
				return new PnFieldDTO<Date>(Date.class, pnName, label, fieldType);
			case INTEGER:
				return new PnFieldDTO<Integer>(Integer.class, pnName, label, fieldType);
			case STRING:
				return new PnFieldDTO<String>(String.class, pnName, label, fieldType);
			case REFERENCE:
			case STRING_REFERENCE:
			case ATTRIBUTES:
			case AUTOCAD_FILE:
			case BOOLEAN:
			case DATABASE_QUERY:
			case DOCUMENT_FILE:
			case GPS:
			case IMAGE_FILE:
			case PERIOD:
			case SECUREDOCUMENT:
			default:
				throw new PnClientException("The field type "+fieldType.toString()+" is not currently supported.");
		}
	}
	
	public PnFieldDTO(Class<T> dataType, String pnName) {
		this(dataType, pnName, null, null);
	}
	
	public PnFieldDTO(Class<T> dataType, String pnName, String label) {
		this(dataType, pnName, label, null);
	}
	
	public PnFieldDTO(Class<T> dataType, String pnName, String label, PnESValueType fieldType) {
		super(pnName, label, fieldType);
		this.dataType = dataType;
	}
	
	public Class<T> getDataType() {
		return dataType;
	}
	
	public Model<T> getValueModel() {
		return valueModel;
	}
	
	public T getValue() {
		return valueModel.getObject();
	}
	
	public void setValue(T value) {
		this.valueModel.setObject(value);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public T getMinValue() {
		return minValue;
	}
	
	public void setMinValue(T minValue) {
		this.minValue = minValue;
	}
	
	public T getMaxValue() {
		return maxValue;
	}
	
	public void setMaxValue(T maxValue) {
		this.maxValue = maxValue;
	}
}

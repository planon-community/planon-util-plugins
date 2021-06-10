package edu.planon.lib.client.dto;

import java.io.Serializable;
import org.apache.wicket.model.Model;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnFieldDTO<T extends Serializable> extends PnFieldDefDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Class<T> dataType;
	private boolean enabled = true;
	private Model<T> valueModel = new Model<T>();
	
	public PnFieldDTO(String pnName) {
		this(pnName, null, null);
	}
	
	public PnFieldDTO(String pnName, String label) {
		this(pnName, label, null);
	}
	
	public PnFieldDTO(String pnName, String label, PnESValueType fieldType) {
		super(pnName, label, fieldType);
	}
	
	
	public PnFieldDTO(String pnName, String label, PnESValueType fieldType, Class<T> dataType) {
		this(pnName, label, fieldType);
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
}

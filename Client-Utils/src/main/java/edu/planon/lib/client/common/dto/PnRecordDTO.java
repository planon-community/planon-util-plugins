package edu.planon.lib.client.common.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class PnRecordDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private final int primaryKey;
	private String uniqueID;
	private final String[] fields;
	
	public PnRecordDTO(int primaryKey, String... aFields) {
		this.primaryKey = primaryKey;
		this.fields = aFields;
	}
	
	public int getPrimaryKey() {
		return this.primaryKey;
	}
	
	public String getUniqueID() {
		if (this.uniqueID == null) {
			this.uniqueID = Integer.toString(this.primaryKey);
		}
		return this.uniqueID;
	}
	
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}
	
	public String[] getFields() {
		return this.fields.clone();
	}
	
	public String getTextValue() {
		if (this.fields == null) {
			return "";
		}
		if (this.fields.length > 1 && this.fields[1] != null && !this.fields[1].isEmpty()) {
			return this.fields[0] + ", " + this.fields[1].trim();
		}
		return this.fields[0];
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getUniqueID());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PnRecordDTO)) {
			return false;
		}
		PnRecordDTO other = (PnRecordDTO)obj;
		return Objects.equals(this.getUniqueID(), other.getUniqueID());
	}
	
	@Override
	public String toString() {
		return "PnRecordDTO [primaryKey=" + this.primaryKey + ", uniqueID=" + this.uniqueID + ", fields=" + Arrays.toString(this.fields) + "]";
	}
}

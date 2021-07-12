package edu.planon.lib.client.common.dto;

import java.io.Serializable;
import java.util.List;

import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.common.util.PnReferenceFieldLookup;

public class PnReferenceFieldDefDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String boPnName;
	private String[] fields;
	private List<PnFieldDefDTO> fieldsList;
	
	public PnReferenceFieldDefDTO(String boPnName, String... fields) {
		this.boPnName = boPnName;
		this.fields = fields;
	}
	
	public PnReferenceFieldDefDTO(String boPnName, List<PnFieldDefDTO> fieldsList) {
		this.boPnName = boPnName;
		this.fieldsList = fieldsList;
	}
	
	public String getBoPnName() {
		return this.boPnName;
	}
	
	public String[] getFields() {
		return this.fields.clone();
	}
	
	public List<PnFieldDefDTO> getFieldsList() throws PnClientException {
		if (this.fieldsList == null) {
			this.fieldsList = new PnReferenceFieldLookup(this).getFields();
		}
		return this.fieldsList;
	}
}

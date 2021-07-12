package edu.planon.lib.client.search.dto;

import java.io.Serializable;
import java.util.Objects;

import nl.planon.enterprise.service.api.PnESOperator;

public class PnSearchOperatorDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String label;
	private final PnESOperator operator;
	
	public PnSearchOperatorDTO(String label, PnESOperator operator) {
		this.label = label;
		this.operator = operator;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public PnESOperator getOperator() {
		return this.operator;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.operator);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PnSearchOperatorDTO)) {
			return false;
		}
		PnSearchOperatorDTO other = (PnSearchOperatorDTO)obj;
		return this.operator == other.operator;
	}
}

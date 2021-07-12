package edu.planon.lib.client.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.planon.lib.client.search.dto.PnSearchOperatorDTO;
import nl.planon.enterprise.service.api.PnESOperator;
import nl.planon.enterprise.service.api.PnESValueType;

public final class PnOperatorUtils {
	private static final Map<PnESOperator, PnSearchOperatorDTO> OPERATOR_MAP = new HashMap<PnESOperator, PnSearchOperatorDTO>();
	
	public static List<PnSearchOperatorDTO> getOperatorsForType(PnESValueType type) {
		ArrayList<PnSearchOperatorDTO> operatorList = new ArrayList<PnSearchOperatorDTO>(15);
		if (type == null || PnESValueType.STRING.equals(type)) {
			operatorList.add(OPERATOR_MAP.get(PnESOperator.LESS));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.LESS_EQUAL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NOT_EQUAL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.EQUAL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.GREATER));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.GREATER_EQUAL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.BETWEEN));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.CONTAINS));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NOT_NULL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NOT_CONTAINS));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NULL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NOT_ENDSWITH));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NOT_STARTSWITH));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.ENDSWITH));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.STARTSWITH));
		}
		else if (PnESValueType.BIG_DECIMAL.equals(type) || PnESValueType.DATE_NEUTRAL.equals(type) || PnESValueType.DATE_TIME.equals(type)
				|| PnESValueType.DATE_TIME_NEUTRAL.equals(type)) {
			operatorList.add(OPERATOR_MAP.get(PnESOperator.LESS));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.LESS_EQUAL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NOT_EQUAL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.EQUAL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.GREATER));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.GREATER_EQUAL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.BETWEEN));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NOT_NULL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NULL));
		}
		else if (PnESValueType.STRING_REFERENCE.equals(type) || PnESValueType.REFERENCE.equals(type)) {
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NOT_EQUAL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.EQUAL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NOT_NULL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NULL));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.IN));
			operatorList.add(OPERATOR_MAP.get(PnESOperator.NOT_IN));
		}
		return operatorList;
	}
	
	static {
		OPERATOR_MAP.put(PnESOperator.LESS, new PnSearchOperatorDTO("<", PnESOperator.LESS));
		OPERATOR_MAP.put(PnESOperator.LESS_EQUAL, new PnSearchOperatorDTO("<=", PnESOperator.LESS_EQUAL));
		OPERATOR_MAP.put(PnESOperator.EQUAL, new PnSearchOperatorDTO("=", PnESOperator.EQUAL));
		OPERATOR_MAP.put(PnESOperator.GREATER, new PnSearchOperatorDTO(">", PnESOperator.GREATER));
		OPERATOR_MAP.put(PnESOperator.GREATER_EQUAL, new PnSearchOperatorDTO(">=", PnESOperator.GREATER_EQUAL));
		OPERATOR_MAP.put(PnESOperator.NOT_EQUAL, new PnSearchOperatorDTO("<> Or empty", PnESOperator.NOT_EQUAL));
		OPERATOR_MAP.put(PnESOperator.BETWEEN, new PnSearchOperatorDTO("Between", PnESOperator.BETWEEN));
		OPERATOR_MAP.put(PnESOperator.CONTAINS, new PnSearchOperatorDTO("Contains", PnESOperator.CONTAINS));
		OPERATOR_MAP.put(PnESOperator.NOT_NULL, new PnSearchOperatorDTO("Contains a value", PnESOperator.NOT_NULL));
		OPERATOR_MAP.put(PnESOperator.NOT_CONTAINS, new PnSearchOperatorDTO("Does not contain or is empty", PnESOperator.NOT_CONTAINS));
		OPERATOR_MAP.put(PnESOperator.NULL, new PnSearchOperatorDTO("Does not contain value", PnESOperator.NULL));
		OPERATOR_MAP.put(PnESOperator.NOT_ENDSWITH, new PnSearchOperatorDTO("Does not end with or is empty", PnESOperator.NOT_ENDSWITH));
		OPERATOR_MAP.put(PnESOperator.NOT_STARTSWITH, new PnSearchOperatorDTO("Does not start with or is empty", PnESOperator.NOT_STARTSWITH));
		OPERATOR_MAP.put(PnESOperator.ENDSWITH, new PnSearchOperatorDTO("Ends with", PnESOperator.ENDSWITH));
		OPERATOR_MAP.put(PnESOperator.STARTSWITH, new PnSearchOperatorDTO("Starts with", PnESOperator.STARTSWITH));
		OPERATOR_MAP.put(PnESOperator.IN, new PnSearchOperatorDTO("In", PnESOperator.IN));
		OPERATOR_MAP.put(PnESOperator.NOT_IN, new PnSearchOperatorDTO("Not in or empty", PnESOperator.NOT_IN));
	}
}

package edu.planon.lib.client.common.util;

import java.util.Comparator;
import java.util.List;

import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.dto.PnQueryParamDTO;
import edu.planon.lib.client.common.dto.PnRecordDTO;

public class PnTableDTOListComparator implements Comparator<PnRecordDTO> {
	private Integer index;
	private boolean ascending;
	
	public PnTableDTOListComparator(List<PnFieldDefDTO> tableHeaderList, PnQueryParamDTO queryParam) {
		this.ascending = queryParam.isAscending();
		for (int i = 0; i < tableHeaderList.size(); i++) {
			PnFieldDefDTO fieldDef = tableHeaderList.get(i);
			if (fieldDef != null && fieldDef.getPnName().equals(queryParam.getProperty())) {
				this.index = i;
				break;
			}
		}
	}
	
	@Override
	public int compare(PnRecordDTO tbl1, PnRecordDTO tbl2) {
		if (this.index != null && tbl1.getFields()[this.index] != null && tbl2.getFields()[this.index] != null) {
			return (this.ascending ? 1 : -1) * (tbl1.getFields()[this.index].compareToIgnoreCase(tbl2.getFields()[this.index]));
		}
		return 0;
	}
}

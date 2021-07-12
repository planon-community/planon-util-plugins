package edu.planon.lib.client.table;

import org.apache.wicket.model.Model;

import edu.planon.lib.client.common.dto.PnRecordDTO;

public class PnDataTableRowModel extends Model<PnRecordDTO> {
	private static final long serialVersionUID = 1L;
	private final int rowNumber;
	
	public PnDataTableRowModel(PnRecordDTO rowDTO, int rowNumber) {
		super(rowDTO);
		this.rowNumber = rowNumber;
	}
	
	public int getRowNumber() {
		return this.rowNumber;
	}
	
	public String getColumnValue(int column) {
		String[] rowData = this.getObject().getFields();
		if (column < rowData.length) {
			return rowData[column];
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		return this.getObject().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof PnDataTableRowModel)) {
			return false;
		}
		PnDataTableRowModel other = (PnDataTableRowModel)obj;
		return this.getObject().equals(other.getObject());
	}
}

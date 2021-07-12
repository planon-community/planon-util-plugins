package edu.planon.lib.client.table.selection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PnTableSingleSelectionModel implements IPnTableSelectionModel {
	private static final long serialVersionUID = 1L;
	private int selectedRow = NOT_SET;
	
	@Override
	public void setSelection(int index) {
		this.selectedRow = index;
	}
	
	@Override
	public int getSelectedRow() {
		return this.selectedRow;
	}
	
	@Override
	public List<Integer> getSelectedRows() {
		if (this.hasSelection()) {
			return Arrays.asList(this.selectedRow);
		}
		return Collections.emptyList();
	}
	
	@Override
	public void clear() {
		this.selectedRow = NOT_SET;
	}
	
	@Override
	public boolean hasSelection() {
		return this.selectedRow > NOT_SET;
	}
	
	@Override
	public boolean isRowSelected(int index) {
		return index == this.selectedRow;
	}
	
	@Override
	public boolean isMultipleSelect() {
		return false;
	}
}

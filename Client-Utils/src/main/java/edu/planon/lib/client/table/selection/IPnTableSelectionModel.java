package edu.planon.lib.client.table.selection;

import java.io.Serializable;
import java.util.List;

public interface IPnTableSelectionModel extends Serializable {
	public static final int NOT_SET = -1;
	
	public void setSelection(int index);
	
	public int getSelectedRow();
	
	public List<Integer> getSelectedRows();
	
	public void clear();
	
	public boolean hasSelection();
	
	public boolean isRowSelected(int index);
	
	public boolean isMultipleSelect();
}

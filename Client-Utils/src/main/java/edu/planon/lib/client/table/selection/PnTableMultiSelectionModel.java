package edu.planon.lib.client.table.selection;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class PnTableMultiSelectionModel implements IPnTableSelectionModel {
	private static final long serialVersionUID = 1L;
	private final BitSet selectionBitSet = new BitSet();
	private int previousClick = NOT_SET;
	
	@Override
	public void setSelection(int index) {
		this.previousClick = index;
		if (this.hasSelection()) {
			this.selectionBitSet.clear();
		}
		if (index != NOT_SET) {
			this.selectionBitSet.set(index);
		}
	}
	
	//done
	@Override
	public int getSelectedRow() {
		int first = this.selectionBitSet.nextSetBit(0);
		if (first == this.selectionBitSet.length()) {
			return first;
		}
		return NOT_SET;
	}
	
	@Override
	public List<Integer> getSelectedRows() {
		ArrayList<Integer> results = new ArrayList<>(this.selectionBitSet.cardinality());
		int lastBit = this.selectionBitSet.nextSetBit(0);
		
		while (lastBit >= 0) {
			results.add(lastBit);
			lastBit = this.selectionBitSet.nextSetBit(lastBit + 1);
		}
		
		return results;
	}
	
	//done
	@Override
	public void clear() {
		this.previousClick = NOT_SET;
		this.selectionBitSet.clear();
	}
	
	//done
	@Override
	public boolean hasSelection() {
		return !this.selectionBitSet.isEmpty();
	}
	
	//done
	@Override
	public boolean isRowSelected(int index) {
		return this.selectionBitSet.get(index);
	}
	
	//done
	@Override
	public boolean isMultipleSelect() {
		return true;
	}
	
	//done
	public void addSelectedRow(int index) {
		this.selectionBitSet.set(index);
		//don't set previous click since this was done programmatically
	}
	
	//done
	public void setSelectedRange(int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
			return;
		}
		
		this.selectionBitSet.set(fromIndex, toIndex + 1);
	}
	
	//done
	public void toggleRowSelection(int index) {
		this.previousClick = index;
		this.selectionBitSet.flip(index);
	}
	
	//done
	public int getPreviousClick() {
		return this.previousClick;
	}
}

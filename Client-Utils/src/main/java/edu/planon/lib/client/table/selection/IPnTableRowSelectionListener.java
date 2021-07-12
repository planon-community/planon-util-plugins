package edu.planon.lib.client.table.selection;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

import edu.planon.lib.client.common.exception.PnClientException;

public interface IPnTableRowSelectionListener extends Serializable {
	public static enum SelectionEventType {
		ADD,
		REPLACE,
		REMOVE;
	}
	
	default public void orderChanged(AjaxRequestTarget target) {}
	
	default public void selectionChanged(int fromRow, int toRow, SelectionEventType type, AjaxRequestTarget target) {}
	
	default public void doubleClicked(int rowIndex, AjaxRequestTarget target) throws PnClientException {}
	
	public static IPnTableRowSelectionListener onDoubleClick(DoubleClickHandler handler) {
		return new IPnTableRowSelectionListener() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void doubleClicked(int rowIndex, AjaxRequestTarget target) throws PnClientException {
				handler.accept(rowIndex, target);
			}
		};
	}
	
	@FunctionalInterface
	public interface DoubleClickHandler extends Serializable {
		void accept(int rowIndex, AjaxRequestTarget target) throws PnClientException;
	}
}

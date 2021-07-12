package edu.planon.lib.client.table;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import edu.planon.lib.client.common.behavior.PnCSSAttributeAppender;
import edu.planon.lib.client.common.dto.PnRecordDTO;

public class PnDataTableRowItem extends Item<PnRecordDTO> {
	private static final long serialVersionUID = 1L;
	public static final String CLASS_EVEN = "even";
	public static final String CLASS_ODD = "odd";
	public static final String CLASS_SELECTED = "selected";
	
	public PnDataTableRowItem(String wicketId, int index, PnDataTableRowModel model) {
		super(wicketId, index, model);
		this.setOutputMarkupId(true);
	}
	
	public final PnDataTableRowModel getRowModel() {
		return (PnDataTableRowModel)this.getModel();
	}
	
	protected final PnDataTable getTable() {
		return this.findParent(PnDataTable.class);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("class", (this.getIndex() % 2 == 0 ? CLASS_EVEN : CLASS_ODD) + " loading-icon-aware");
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		this.add(new AttributeModifier("role", "button"));
		this.add(new PnCSSAttributeAppender(new PnWebRowSelectorModel(this, this.getTable())));
	}
	
	protected static class PnWebRowSelectorModel implements IModel<String> {
		private static final long serialVersionUID = 1L;
		private final PnDataTableRowItem rowItem;
		private final PnDataTable table;
		
		protected PnWebRowSelectorModel(PnDataTableRowItem aRowItem, PnDataTable aTable) {
			this.rowItem = aRowItem;
			this.table = aTable;
		}
		
		@Override
		public String getObject() {
			return this.isRowInSelection() ? PnDataTableRowItem.CLASS_SELECTED : null;
		}
		
		private boolean isRowInSelection() {
			return this.table.isRowSelectionAllowed() && this.table.isRowSelected(this.rowItem.getRowModel().getRowNumber());
		}
	}
}

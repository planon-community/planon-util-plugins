package edu.planon.lib.client.table;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import edu.planon.lib.client.common.behavior.PnCSSAttributeAppender;
import edu.planon.lib.client.common.component.PnTitledLabel;
import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.util.PnFieldTypeUtils;

public class PnDataTableColumnModel implements IColumn<PnRecordDTO, String> {
	private static final long serialVersionUID = 1L;
	private final int columnIndex;
	private final PnFieldDefDTO fieldDefDTO;
	
	public PnDataTableColumnModel(PnFieldDefDTO fieldDefDTO, int columnIndex) {
		this.fieldDefDTO = fieldDefDTO;
		this.columnIndex = columnIndex;
	}
	
	@Override
	public Component getHeader(String wicketId) {
		return new PnTitledLabel(wicketId, this.fieldDefDTO.getLabel());
	}
	
	@Override
	public String getSortProperty() {
		return this.fieldDefDTO.getPnName();
	}
	
	@Override
	public void populateItem(Item<ICellPopulator<PnRecordDTO>> cellItem, String wicketId, IModel<PnRecordDTO> model) {
		PnDataTableRowModel rowModel = (PnDataTableRowModel)model;
		PnTitledLabel label = new PnTitledLabel(wicketId, rowModel.getColumnValue(this.columnIndex));
		label.add(new PnCSSAttributeAppender("truncate-text"));
		cellItem.add(label);
	}
	
	@Override
	public void detach() {}
	
	public int getColumnNumber() {
		return this.columnIndex;
	}
	
	public int getDefaultWidth() {
		return PnFieldTypeUtils.getDefaultWidth(this.fieldDefDTO.getFieldType());
	}
}

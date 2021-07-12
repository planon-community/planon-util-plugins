package edu.planon.lib.client.table.toolbar;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.table.PnDataTable;

public class PnDataTableNoRecordsToolbar extends AbstractToolbar {
	private static final long serialVersionUID = 1L;
	
	public PnDataTableNoRecordsToolbar(PnDataTable table) {
		this(table, Model.of("Nothing found"));
	}
	
	public PnDataTableNoRecordsToolbar(PnDataTable table, IModel<String> messageModel) {
		super(table);
		WebMarkupContainer td = new WebMarkupContainer("td");
		this.add(td);
		td.add(new AttributeModifier("colspan", Model.of(String.valueOf(table.getColumns().size()))));
		td.add(new Label("msg", messageModel));
	}
	
	@Override
	public boolean isVisible() {
		return this.getTable().getRowCount() == 0L;
	}
}

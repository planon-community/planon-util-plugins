package edu.planon.lib.client.table.toolbar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.table.PnDataTable;
import edu.planon.lib.client.table.PnDataTableColumnModel;

public class PnDataTableHeaders extends AbstractToolbar {
	private static final long serialVersionUID = 1L;
	
	public PnDataTableHeaders(final PnDataTable table) {
		super(table);
		RefreshingView<PnDataTableColumnModel> headers = new RefreshingView<PnDataTableColumnModel>("headers") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected Iterator<IModel<PnDataTableColumnModel>> getItemModels() {
				List<PnDataTableColumnModel> columns = table.getModel().getColumns();
				ArrayList<IModel<PnDataTableColumnModel>> columnsModels = new ArrayList<>(columns.size());
				for (PnDataTableColumnModel column : columns) {
					columnsModels.add(Model.of(column));
				}
				
				return columnsModels.iterator();
			}
			
			@Override
			protected void populateItem(Item<PnDataTableColumnModel> item) {
				item.add(new PnDataTableHeader("header", table, item.getModel(), table.getModel()));
				item.setRenderBodyOnly(true);
			}
		};
		this.add(headers);
	}
}

package edu.planon.lib.client.table.toolbar;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.sort.AjaxOrderByLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.IModel;
import org.wicketstuff.wiquery.core.javascript.JsQuery;
import org.wicketstuff.wiquery.ui.resizable.ResizableAlsoResize;

import edu.planon.lib.client.common.behavior.PnCSSAttributeModifier;
import edu.planon.lib.client.common.behavior.PnResizableAjaxBehavior;
import edu.planon.lib.client.table.PnDataTable;
import edu.planon.lib.client.table.PnDataTableColumnModel;

public class PnDataTableHeader extends Border {
	private static final long serialVersionUID = 1L;
	private final PnDataTable table;
	
	public PnDataTableHeader(String wicketId, final PnDataTable table, IModel<PnDataTableColumnModel> model, ISortStateLocator<String> stateLocator) {
		super(wicketId, model);
		this.table = table;
		
		this.setOutputMarkupId(true);
		
		PnDataTableColumnModel column = model.getObject();
		String property = column.getSortProperty();
		
		WebMarkupContainer border = new WebMarkupContainer("border");
		
		this.add(column.getHeader("label"));
		
		if (column.isSortable()) {
			String sortCSSClassName = "col-" + property;
			SortOrder sortDir = stateLocator.getSortState().getPropertySortOrder(property);
			if (sortDir == SortOrder.ASCENDING) {
				sortCSSClassName = "wicket_orderDown col-" + property;
			}
			else if (sortDir == SortOrder.DESCENDING) {
				sortCSSClassName = "wicket_orderUp col-" + property;
			}
			
			AjaxOrderByLink<String> link = new AjaxOrderByLink<String>("orderByLink", property, stateLocator) {
				private static final long serialVersionUID = 1L;
				
				@Override
				public void onClick(AjaxRequestTarget target) {
					PnDataTableHeader.this.table.fireOrderChangedEvent(target);
				}
			};
			link.add(new PnCSSAttributeModifier(sortCSSClassName));
			border.add(new PnCSSAttributeModifier(sortCSSClassName));
			border.add(link);
		}
		else {
			WebMarkupContainer link = new WebMarkupContainer("orderByLink");
			link.setRenderBodyOnly(true);
			border.add(link);
		}
		
		this.addToBorder(border);
		
		PnResizableAjaxBehavior resizableBehavior = this.createResizableBehavior(property);
		resizableBehavior.setHandles(PnResizableAjaxBehavior.Handle.E);
		resizableBehavior.setMinWidth(15);
		resizableBehavior.setAlsoResize(new ResizableAlsoResize(new JsQuery(this).$().render(false).toString()));
		border.add(resizableBehavior);
		
		String defaultWidth = "100%";
		int width = column.getDefaultWidth();
		if (table.getColumns().size() > 0) {
			width = (width > 0) ? width : 60;
			defaultWidth = width + "px";
		}
		border.add(this.createInitialWidthBehavior(defaultWidth));
		this.add(this.createInitialWidthBehavior(defaultWidth));
	}
	
	private Behavior createInitialWidthBehavior(String defaultWidth) {
		return new Behavior() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onComponentTag(Component aComponent, ComponentTag aTag) {
				aTag.put("style", "width: " + defaultWidth);
			}
		};
	}
	
	private PnResizableAjaxBehavior createResizableBehavior(String aColumnID) {
		return new PnResizableAjaxBehavior() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onResize(AjaxRequestTarget aAjaxRequestTarget, double aWidth, double aHeight) {}
			
			@Override
			public boolean canCallListener(Component aComponent) {
				return this.isEnabled(aComponent) && aComponent.isVisibleInHierarchy();
			}
		};
	}
}

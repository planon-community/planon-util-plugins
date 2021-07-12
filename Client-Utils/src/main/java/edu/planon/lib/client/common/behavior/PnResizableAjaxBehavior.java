package edu.planon.lib.client.common.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.wicketstuff.wiquery.ui.resizable.ResizableContainment;
import org.wicketstuff.wiquery.ui.resizable.ResizableHandles;
import org.wicketstuff.wiquery.ui.resizable.ResizableStopAjaxBehavior;

public abstract class PnResizableAjaxBehavior extends ResizableStopAjaxBehavior {
	private static final long serialVersionUID = 1L;
	
	public PnResizableAjaxBehavior setContainment(Component component) {
		component.setOutputMarkupId(true);
		this.setContainment(new ResizableContainment("'#" + component.getMarkupId() + "'"));
		return this;
	}
	
	public PnResizableAjaxBehavior setContainmentParent() {
		this.setContainment(new ResizableContainment(ResizableContainment.ElementEnum.PARENT));
		return this;
	}
	
	public PnResizableAjaxBehavior setHandles(Handle... aHandles) {
		StringBuilder builder = new StringBuilder();
		for (Handle handle : aHandles) {
			if (builder.length() > 0) {
				builder.append(',');
			}
			builder.append('\'').append(handle.toString().toLowerCase()).append('\'');
		}
		this.setHandles(new ResizableHandles(builder.toString()));
		return this;
	}
	
	protected abstract void onResize(AjaxRequestTarget var1, double var2, double var4);
	
	@Override
	protected void resizeTop(AjaxRequestTarget aTarget, Component aSource, double aHeight, double aWidth) {
		this.onResize(aTarget, aWidth, aHeight);
	}
	
	public enum Handle {
		E,
		W,
		S,
		N,
		NW,
		NE,
		SW,
		SE,
		ALL;
	}
}

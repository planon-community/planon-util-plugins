package edu.planon.lib.client.common.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;

public class PnCSSAttributeAppender extends AttributeAppender {
	private static final long serialVersionUID = 1L;
	
	public PnCSSAttributeAppender(String cssClassName) {
		super("class", cssClassName, " ");
	}
	
	public PnCSSAttributeAppender(IModel<String> model) {
		super("class", model, " ");
	}
	
	public static PnCSSAttributeAppender whenDisabled(String cssClassName) {
		return new PnCSSAttributeAppender(cssClassName) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component aComponent) {
				return !aComponent.isEnabled();
			}
		};
	}
	
	public static PnCSSAttributeAppender whenDisabled(IModel<String> model) {
		return new PnCSSAttributeAppender(model) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isEnabled(Component aComponent) {
				return !aComponent.isEnabled();
			}
		};
	}
}

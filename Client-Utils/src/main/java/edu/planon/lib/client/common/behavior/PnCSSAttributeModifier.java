package edu.planon.lib.client.common.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;

public class PnCSSAttributeModifier extends AttributeModifier {
	private static final long serialVersionUID = 1L;

	public PnCSSAttributeModifier(String cssClassName) {
		super("class", cssClassName);
	}
	
	public PnCSSAttributeModifier(IModel<String> model) {
		super("class", model);
	}
}

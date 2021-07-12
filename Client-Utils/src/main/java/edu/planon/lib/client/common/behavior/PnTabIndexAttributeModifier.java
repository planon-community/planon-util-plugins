package edu.planon.lib.client.common.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class PnTabIndexAttributeModifier extends AttributeModifier {
	private static final long serialVersionUID = 1L;
	
	public PnTabIndexAttributeModifier() {
		this(Model.of("0"));
	}
	
	public PnTabIndexAttributeModifier(boolean aIsAllowed) {
		this(Model.of("-1"));
	}
	
	public PnTabIndexAttributeModifier(IModel<String> aTextModel) {
		super("tabindex", aTextModel);
	}
}

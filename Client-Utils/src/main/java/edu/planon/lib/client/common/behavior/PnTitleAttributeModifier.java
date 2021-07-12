package edu.planon.lib.client.common.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class PnTitleAttributeModifier extends AttributeModifier {
	private static final long serialVersionUID = 1L;
	
	public PnTitleAttributeModifier(String toolTip) {
		this(Model.of(toolTip));
	}
	
	public PnTitleAttributeModifier(IModel<?> toolTipModel) {
		super("title", toolTipModel);
	}
}

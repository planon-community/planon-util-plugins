package edu.planon.lib.client.common.component;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.common.behavior.PnTitleAttributeModifier;

public class PnTitledLabel extends Label {
	private static final long serialVersionUID = 1L;
	private IModel<String> titleModel;
	
	public PnTitledLabel(String wicketId) {
		this(wicketId, Model.of(""));
	}
	
	public PnTitledLabel(String wicketId, String model) {
		this(wicketId, Model.of(model));
	}
	
	public PnTitledLabel(String wicketId, IModel<String> model) {
		super(wicketId, model);
		assert model != null;
		this.setEscapeModelStrings(true);
		this.addTitleAttribute();
	}
	
	public PnTitledLabel(String wicketId, IModel<String> labelModel, IModel<String> titleModel) {
		this(wicketId, labelModel);
		this.titleModel = titleModel;
	}
	
	public PnTitledLabel(String wicketId, String labelModel, IModel<String> titleModel) {
		this(wicketId, Model.of(labelModel), titleModel);
	}
	
	public PnTitledLabel(String wicketId, IModel<String> labelModel, String titleModel) {
		this(wicketId, labelModel, Model.of(titleModel));
	}
	
	public PnTitledLabel(String wicketId, String labelModel, String titleModel) {
		this(wicketId, Model.of(labelModel), Model.of(titleModel));
	}
	
	public PnTitledLabel setTitle(IModel<String> titleModel) {
		this.titleModel = titleModel;
		this.addTitleAttribute();
		return this;
	}
	
	private void addTitleAttribute() {
		List<PnTitleAttributeModifier> existingBehaviors = this.getBehaviors(PnTitleAttributeModifier.class);
		if (existingBehaviors != null && !existingBehaviors.isEmpty()) {
			this.remove(existingBehaviors.toArray(new PnTitleAttributeModifier[existingBehaviors.size()]));
		}
		this.add(new PnTitleAttributeModifier((this.titleModel != null) ? this.titleModel : this.getDefaultModel()));
	}
	
	@Override
	protected void onModelChanged() {
		super.onModelChanged();
		if (this.titleModel == null) {
			this.addTitleAttribute();
		}
	}
}

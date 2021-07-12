package edu.planon.lib.client.test.wcx.settings;

import java.io.IOException;
import edu.planon.lib.common.BaseProperties;
import edu.planon.lib.common.exception.PropertyNotDefined;

public class SettingsTestWCX extends BaseProperties {
	private static final long serialVersionUID = 795518343379718128L;
	private static final String POPUP_TITLE = "popup.title";
	private static final String REF_LABEL = "referenceField.label";
	private static final String REF_BO = "referenceField.bo.PnName";
	private static final String REF_BO_FIELDS = "referenceField.displayFields.PnNames";
	
	private static final String KEY_BUTTON_OK = "btn.ok";
	private static final String KEY_BUTTON_CANCEL = "btn.cancel";
	private static final String DEFAULT_BUTTON_OK = "Ok";
	private static final String DEFAULT_BUTTON_CANCEL = "Cancel";
	
	public SettingsTestWCX(String parameters) throws IOException, PropertyNotDefined {
		super(parameters);
	}
	
	public String getPopupTitle(String defaultVal) {
		return this.getStringProperty(POPUP_TITLE, defaultVal);
	}
	
	public String getReferenceFieldLabel() throws PropertyNotDefined {
		return this.getStringProperty(REF_LABEL);
	}
	
	public String getReferenceFieldBOPnName() throws PropertyNotDefined {
		return this.getStringProperty(REF_BO);
	}
	
	public String[] getReferenceFieldDisplayFields() {
		return this.getProperty(REF_BO_FIELDS, "").split("\\s*;\\s*");
	}
	
	public String getButtonOkText() {
		return this.getStringProperty(KEY_BUTTON_OK, DEFAULT_BUTTON_OK);
	}
	
	public String getButtonCancelText() {
		return this.getStringProperty(KEY_BUTTON_CANCEL, DEFAULT_BUTTON_CANCEL);
	}
}

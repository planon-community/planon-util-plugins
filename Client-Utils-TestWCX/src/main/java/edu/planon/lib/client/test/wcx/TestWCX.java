package edu.planon.lib.client.test.wcx;

import java.io.IOException;

import org.apache.wicket.markup.html.panel.Panel;

import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.common.exception.PnClientRuntimeException;
import edu.planon.lib.client.panel.ErrorPopup;
import edu.planon.lib.client.test.wcx.exception.WCXException;
import edu.planon.lib.client.test.wcx.settings.SettingsTestWCX;
import edu.planon.lib.client.test.wcx.ui.TestWCXPanel;
import edu.planon.lib.common.exception.PropertyNotDefined;
import nl.planon.hera.webclientextension.wcxinterface.IViewableWebClientExtension;
import nl.planon.util.pnlogging.PnLogger;
import nl.planon.zeus.clientextension.cxinterface.ICXContext;

public class TestWCX implements IViewableWebClientExtension {
	private static final PnLogger LOGGER = PnLogger.getLogger(TestWCX.class);
	private static final String DESCRIPTION = "WCX for testing Client-Utils";
	private static final String DEF_TITLE = "Test WCX";
	private String title;
	private ICXContext cxContext;
	private String params;
	
	@Override
	public Panel getExtensionPanel(String panelId) {
		try {
			if (this.cxContext.getNumberOfSelectedBOs() > 1) {
				throw new WCXException("This extension does't work for multiple records.");
			}
			
			SettingsTestWCX settings = new SettingsTestWCX(this.params);
			this.title = settings.getPopupTitle(DEF_TITLE);
			
			return new TestWCXPanel(panelId, settings, this.cxContext);
		}
		catch (IOException | PropertyNotDefined | WCXException | PnClientRuntimeException | PnClientException e) {
			LOGGER.error("Error", e);
			this.title = "Test WCX - Error";
			return new ErrorPopup(panelId, e);
		}
	}
	
	@Override
	public void refresh() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Refreshing TestWCX");
		}
	}
	
	@Override
	public void execute(ICXContext cxContext, String aParameter) {
		this.cxContext = cxContext;
		this.params = aParameter;
	}
	
	@Override
	public String getTitle() {
		return this.title;
	}
	
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
	
}

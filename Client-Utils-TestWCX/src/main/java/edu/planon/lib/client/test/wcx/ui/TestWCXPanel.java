package edu.planon.lib.client.test.wcx.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;

import edu.planon.lib.client.common.behavior.CloseModalBehavior;
import edu.planon.lib.client.common.dto.PnFieldDTO;
import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.field.PnFieldPanel;
import edu.planon.lib.client.panel.AbstractPanel;
import edu.planon.lib.client.panel.InfoPopup;
import edu.planon.lib.client.test.wcx.dao.TestDAO;
import edu.planon.lib.client.test.wcx.exception.WCXException;
import edu.planon.lib.client.test.wcx.settings.SettingsTestWCX;
import nl.planon.enterprise.service.api.PnESActionNotFoundException;
import nl.planon.enterprise.service.api.PnESBusinessException;
import nl.planon.enterprise.service.api.PnESFieldNotFoundException;
import nl.planon.util.pnlogging.PnLogger;
import nl.planon.zeus.clientextension.cxinterface.ICXContext;

public class TestWCXPanel extends AbstractPanel implements IMarkupCacheKeyProvider {
	private static final long serialVersionUID = -1L;
	private static final PnLogger LOGGER = PnLogger.getLogger(TestWCXPanel.class);
	private static final String CSS_FILE = "css/extension-styles.css";
	protected final SettingsTestWCX settings;
	private TestDAO dao;
	
	public TestWCXPanel(String id, SettingsTestWCX settings, ICXContext cxContext) throws PnClientException {
		super(id);
		this.settings = settings;
		this.dao = new TestDAO(settings, cxContext);
		
		IAjaxEventListener updateListener = (event, sourceComponent, target) -> target.add(this);
		
		List<PnFieldDTO<?>> fieldList = this.dao.getFields();
		
		RefreshingView<?> refreshingView = new RefreshingView<PnFieldDTO<?>>("fieldRepeater") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected Iterator<IModel<PnFieldDTO<?>>> getItemModels() {
				return new ModelIteratorAdapter<PnFieldDTO<?>>(fieldList) {
					
					@Override
					protected IModel<PnFieldDTO<?>> model(PnFieldDTO<?> object) {
						return Model.of(object);
					}
				};
			}
			
			@Override
			protected void populateItem(Item<PnFieldDTO<?>> item) {
				PnFieldPanel field = new PnFieldPanel("field", item.getModelObject());
				field.addEventListener(updateListener);
				item.add(field);
			}
			
		};
		this.add(refreshingView);
		
		this.initBaseButtonPanel(settings);
	}
	
	private void okActionHandler(AjaxRequestTarget target) {
		try {
			//this.dao.validate();
			ArrayList<String> resultMessages = this.dao.reportValues();
			
			this.setClosePopup(true);
			ModalWindow popupWindow = this.getPopupWindow();
			popupWindow.setContent(new InfoPopup(popupWindow.getContentId(), resultMessages));
			popupWindow.setTitle("Success");
			popupWindow.show(target);
			this.setClosePopup(true);
			
			//this.cxContext.refreshBOGrid();
		}
		catch (WCXException | PnESBusinessException | PnESActionNotFoundException | PnESFieldNotFoundException e) {
			LOGGER.error("Error in okActionHandler", e);
			this.showError(target, e);
		}
	}
	
	private void initBaseButtonPanel(SettingsTestWCX settings) {
		// get labels from settings 
		String okBtnText = settings.getButtonOkText();
		String cancelBtnText = settings.getButtonCancelText();
		
		Button btnOk = new Button("btnOk", Model.of(okBtnText));
		btnOk.add(AjaxEventBehavior.onEvent("click", target -> this.okActionHandler(target)));
		btnOk.setOutputMarkupPlaceholderTag(true);
		this.add(btnOk);
		
		Button btnCancel = new Button("btnCancel", Model.of(cancelBtnText));
		btnCancel.add(new CloseModalBehavior("click"));
		this.add(btnCancel);
	}
	
	@Override
	public final void renderHead(IHeaderResponse response) {
		response.render(CssHeaderItem.forReference(new CssResourceReference(TestWCXPanel.class, CSS_FILE)));
	}
	
	// Avoid markup caching for this component
	@Override
	public String getCacheKey(MarkupContainer arg0, Class<?> arg1) {
		return null;
	}
}

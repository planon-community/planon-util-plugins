package edu.planon.lib.client.search.editor;

import java.util.Date;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.TextField;

import edu.planon.lib.client.common.behavior.PnCSSAttributeAppender;
import edu.planon.lib.client.common.behavior.PnComponentUpdatingBehavior;
import edu.planon.lib.client.common.component.AjaxIconLink;
import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.event.PnAjaxEventCollector;
import edu.planon.lib.client.common.util.PnFieldTypeUtils;
import edu.planon.lib.client.field.editor.IPnDateField;
import edu.planon.lib.client.field.editor.listener.PnDateFieldEditorLinkListener;
import edu.planon.lib.client.panel.AbstractPanel;
import edu.planon.lib.client.search.model.PnSearchFilterModel;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnMultiValueEditor extends AbstractPanel implements IPnSearchEditor, IPnDateField {
	private static final long serialVersionUID = 1L;
	private PnSearchFilterModel searchFilter;
	private TextField<?> editor1;
	private TextField<?> editor2;
	private AjaxIconLink editorLink1;
	private AjaxIconLink editorLink2;
	private final PnAjaxEventCollector eventCollector = new PnAjaxEventCollector();
	
	public PnMultiValueEditor(String wicketId, PnSearchFilterModel searchFilter) {
		super(wicketId);
		this.searchFilter = searchFilter;
		
		this.setOutputMarkupId(true);
		this.add(new PnCSSAttributeAppender("multiEditor2"));
		this.initComponent();
	}
	
	private void initComponent() {
		this.createEditorLinks("editorLink1", "editorLink2");
		
		PnESValueType fieldType = this.searchFilter.getSearchField().getFieldType();
		if (fieldType.equals(PnESValueType.DATE_NEUTRAL)) {
			this.editor1 = new DateTextField("textEditor1", this.searchFilter.getDate1Model(), "MM/dd/yyyy");
			this.editor2 = new DateTextField("textEditor2", this.searchFilter.getDate2Model(), "MM/dd/yyyy");
			this.editorLink1.addEventListener(new PnDateFieldEditorLinkListener(this, (DateTextField)this.editor1, 305));
			this.editorLink2.addEventListener(new PnDateFieldEditorLinkListener(this, (DateTextField)this.editor2, 305));
		}
		else if (fieldType.equals(PnESValueType.DATE_TIME) || fieldType.equals(PnESValueType.DATE_TIME_NEUTRAL)) {
			this.editor1 = new DateTextField("textEditor1", this.searchFilter.getDate1Model(), "MM/dd/yyyy HH:mm");
			this.editor2 = new DateTextField("textEditor2", this.searchFilter.getDate2Model(), "MM/dd/yyyy HH:mm");
			this.editorLink1.addEventListener(new PnDateFieldEditorLinkListener(this, (DateTextField)this.editor1, 305));
			this.editorLink2.addEventListener(new PnDateFieldEditorLinkListener(this, (DateTextField)this.editor2, 305));
		}
		else {
			this.editor1 = new TextField<String>("textEditor1", this.searchFilter.getString1Model());
			this.editor2 = new TextField<String>("textEditor2", this.searchFilter.getString2Model());
		}
		
		PnComponentUpdatingBehavior fieldBehavior1 = new PnComponentUpdatingBehavior("change");
		fieldBehavior1.addEventListener((event, sourceComponent, target) -> {
			this.searchFilter.setActiveSearch(false);
			target.add(this.editor1);
		});
		fieldBehavior1.addEventListener(this.eventCollector);
		this.editor1.add(fieldBehavior1);
		this.editor1.setOutputMarkupId(true);
		this.add(this.editor1);
		
		PnComponentUpdatingBehavior fieldBehavior2 = new PnComponentUpdatingBehavior("change");
		fieldBehavior2.addEventListener((event, sourceComponent, target) -> {
			this.searchFilter.setActiveSearch(false);
			target.add(this.editor2);
		});
		fieldBehavior2.addEventListener(this.eventCollector);
		this.editor2.add(fieldBehavior2);
		this.editor2.setOutputMarkupId(true);
		this.add(this.editor2);
	}
	
	private void createEditorLinks(String wicketId1, String wicketId2) {
		String iconName = PnFieldTypeUtils.getEditorReferenceIcon(this.searchFilter.getSearchField().getFieldType());
		String tooltip = PnFieldTypeUtils.getEditorReferenceTooltip(this.searchFilter.getSearchField().getFieldType());
		
		this.editorLink1 = new AjaxIconLink(wicketId1, "editor-link pnicon-" + iconName);
		this.editorLink1.add(new AttributeAppender("title", tooltip, " "));
		this.editorLink1.setVisible(false);
		this.add(this.editorLink1);
		
		this.editorLink2 = new AjaxIconLink(wicketId2, "editor-link pnicon-" + iconName);
		this.editorLink2.add(new AttributeAppender("title", tooltip, " "));
		this.editorLink2.setVisible(false);
		this.add(this.editorLink2);
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		PnESValueType fieldType = this.searchFilter.getSearchField().getFieldType();
		boolean enableEditorLink = fieldType.equals(PnESValueType.DATE_NEUTRAL) || fieldType.equals(PnESValueType.DATE_TIME)
				|| fieldType.equals(PnESValueType.DATE_TIME_NEUTRAL);
		this.editorLink1.setVisible(enableEditorLink);
		this.editor1.setEnabled(!enableEditorLink);
		this.editorLink2.setVisible(enableEditorLink);
		this.editor2.setEnabled(!enableEditorLink);
		this.getPopupWindow().setEnabled(enableEditorLink);
	}
	
	@Override
	public Date getMinDate() {
		return null;
	}
	
	@Override
	public Date getMaxDate() {
		return null;
	}
	
	@Override
	public PnFieldDefDTO getFieldDefDTO() {
		return this.searchFilter.getSearchField();
	}
	
	@Override
	public void addEventListener(IAjaxEventListener eventListener) {
		this.eventCollector.addEventListener(eventListener);
	}
	
	@Override
	public void addEventListener(List<IAjaxEventListener> eventListeners) {
		this.eventCollector.addEventListener(eventListeners);
	}
	
	@Override
	public List<IAjaxEventListener> getEventListeners() {
		return this.eventCollector.getEventListeners();
	}
}

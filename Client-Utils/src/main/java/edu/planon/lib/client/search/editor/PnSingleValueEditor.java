package edu.planon.lib.client.search.editor;

import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import edu.planon.lib.client.common.behavior.PnComponentUpdatingBehavior;
import edu.planon.lib.client.common.component.AjaxIconLink;
import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.dto.PnReferenceFieldDefDTO;
import edu.planon.lib.client.common.event.IAjaxEventListener;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.common.util.PnFieldTypeUtils;
import edu.planon.lib.client.field.editor.IPnDateField;
import edu.planon.lib.client.field.editor.listener.PnDateFieldEditorLinkListener;
import edu.planon.lib.client.panel.AbstractPanel;
import edu.planon.lib.client.referencepicker.AbstractPnReferencePickerPanel;
import edu.planon.lib.client.referencepicker.PnReferenceMultiPickerPanel;
import edu.planon.lib.client.referencepicker.PnReferenceSinglePickerPanel;
import edu.planon.lib.client.search.model.PnSearchFilterModel;
import nl.planon.enterprise.service.api.PnESOperator;
import nl.planon.enterprise.service.api.PnESValueType;
import nl.planon.util.pnlogging.PnLogger;

public class PnSingleValueEditor extends AbstractPanel implements IPnSearchEditor, IPnDateField {
	private static final long serialVersionUID = 1L;
	private PnSearchFilterModel searchFilter;
	private TextField<?> editor;
	private Model<String> displayModel = Model.of("");
	private AjaxIconLink editorLink;
	private PnComponentUpdatingBehavior fieldBehavior;
	
	public PnSingleValueEditor(String wicketId, PnSearchFilterModel searchFilter) {
		super(wicketId);
		this.searchFilter = searchFilter;
		
		this.setOutputMarkupId(true);
		this.initComponents();
	}
	
	private void initComponents() {
		this.add(this.createEditorLink("editorLink"));
		
		PnESValueType fieldType = this.searchFilter.getSearchField().getFieldType();
		if (fieldType.equals(PnESValueType.DATE_NEUTRAL)) {
			this.editor = new DateTextField("textEditor", this.searchFilter.getDate1Model(), "MM/dd/yyyy");
			this.editorLink.addEventListener(new PnDateFieldEditorLinkListener(this, (DateTextField)this.editor, 305));
		}
		else if (fieldType.equals(PnESValueType.DATE_TIME) || fieldType.equals(PnESValueType.DATE_TIME_NEUTRAL)) {
			this.editor = new DateTextField("textEditor", this.searchFilter.getDate1Model(), "MM/dd/yyyy HH:mm");
			this.editorLink.addEventListener(new PnDateFieldEditorLinkListener(this, (DateTextField)this.editor, 305));
		}
		else if (fieldType.equals(PnESValueType.STRING_REFERENCE) || fieldType.equals(PnESValueType.REFERENCE)) {
			this.editor = new TextField<String>("textEditor", this.displayModel);
			this.editorLink.addEventListener((event, sourceComponent, target) -> this.onReferenceEditorLinkClick(target));
		}
		else {
			this.editor = new TextField<String>("textEditor", this.searchFilter.getString1Model());
		}
		
		// make the component update the model when the users clicks out of the input
		this.fieldBehavior = new PnComponentUpdatingBehavior("change");
		this.fieldBehavior.addEventListener((event, sourceComponent, target) -> {
			this.searchFilter.setActiveSearch(false);
		});
		this.editor.add(this.fieldBehavior);
		this.editor.setOutputMarkupId(true);
		this.add(this.editor);
	}
	
	private AjaxIconLink createEditorLink(String wicketId) {
		String iconName = PnFieldTypeUtils.getEditorReferenceIcon(this.searchFilter.getSearchField().getFieldType());
		String tooltip = PnFieldTypeUtils.getEditorReferenceTooltip(this.searchFilter.getSearchField().getFieldType());
		
		this.editorLink = new AjaxIconLink(wicketId, "editor-link pnicon-" + iconName);
		this.editorLink.add(new AttributeAppender("title", tooltip, " "));
		this.editorLink.setVisible(false);
		return this.editorLink;
	}
	
	private void onReferenceEditorLinkClick(final AjaxRequestTarget target) {
		ModalWindow popupWindow = this.getPopupWindow();
		
		boolean isMultiSelect = this.searchFilter.getOperator().equals(PnESOperator.IN) || this.searchFilter.getOperator().equals(PnESOperator.NOT_IN);
		try {
			PnReferenceFieldDefDTO referenceField = this.searchFilter.getSearchField().getReferenceFieldDef();
			if (referenceField == null) {
				throw new PnClientException("Reference BO definition not found in input list.");
			}
			
			AbstractPnReferencePickerPanel referencePicker;
			if (isMultiSelect) {
				referencePicker = new PnReferenceMultiPickerPanel(popupWindow.getContentId(), referenceField);
			}
			else {
				referencePicker = new PnReferenceSinglePickerPanel(popupWindow.getContentId(), referenceField);
			}
			
			referencePicker.addEventListener((event, sourceComponent, eventTarget) -> {
				List<PnRecordDTO> referenceValueList = referencePicker.getSelectedRecords();
				this.searchFilter.setReferenceValueList(referenceValueList);
				if (referenceValueList.isEmpty()) {
					this.displayModel.setObject("");
				}
				else if (referenceValueList.size() == 1) {
					this.displayModel.setObject(referenceValueList.get(0).getFields()[0]);
				}
				else {
					this.displayModel.setObject("Multiple values selected");
				}
				eventTarget.add(this.editor);
			});
			
			popupWindow.setContent(referencePicker);
			referencePicker.setSelectedRecords(this.searchFilter.getReferenceValueList(), target);
		}
		catch (PnClientException exception) {
			PnLogger.getLogger(this.getClass()).error(exception);
			this.showError(target, exception);
		}
		popupWindow.setInitialHeight(500);
		popupWindow.setInitialWidth(isMultiSelect ? 900 : 500);
		popupWindow.setTitle(this.searchFilter.getSearchField().getLabel());
		popupWindow.setOutputMarkupId(true);
		popupWindow.setVisible(true);
		popupWindow.setVisibilityAllowed(true);
		popupWindow.show(target);
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		PnESValueType fieldType = this.searchFilter.getSearchField().getFieldType();
		PnESOperator operator = this.searchFilter.getOperator();
		boolean enableEditorLink = (PnESValueType.STRING_REFERENCE.equals(fieldType) || PnESValueType.REFERENCE.equals(fieldType)
				|| PnESValueType.DATE_NEUTRAL.equals(fieldType) || PnESValueType.DATE_TIME.equals(fieldType)
				|| PnESValueType.DATE_TIME_NEUTRAL.equals(fieldType)) && !PnESOperator.NULL.equals(operator) && !PnESOperator.NOT_NULL.equals(operator);
		this.editorLink.setVisible(enableEditorLink);
		this.getPopupWindow().setEnabled(enableEditorLink);
		this.editor.setEnabled(!PnESOperator.NULL.equals(operator) && !PnESOperator.NOT_NULL.equals(operator) && !PnESOperator.IN.equals(operator)
				&& !PnESOperator.NOT_IN.equals(operator) && !enableEditorLink);
	}
	
	@Override
	public PnFieldDefDTO getFieldDefDTO() {
		return this.searchFilter.getSearchField();
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
	public void addEventListener(IAjaxEventListener eventListener) {
		this.fieldBehavior.addEventListener(eventListener);
	}
	
	@Override
	public void addEventListener(List<IAjaxEventListener> eventListeners) {
		this.fieldBehavior.addEventListener(eventListeners);
	}
	
	@Override
	public List<IAjaxEventListener> getEventListeners() {
		return this.fieldBehavior.getEventListeners();
	}
}

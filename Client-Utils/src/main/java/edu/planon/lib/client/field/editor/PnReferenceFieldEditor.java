package edu.planon.lib.client.field.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import edu.planon.lib.client.common.behavior.PnComponentUpdatingBehavior;
import edu.planon.lib.client.common.component.AjaxIconLink;
import edu.planon.lib.client.common.dto.PnFieldDefDTO;
import edu.planon.lib.client.common.dto.PnQueryParamDTO;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.dto.PnReferenceFieldDTO;
import edu.planon.lib.client.common.dto.PnReferenceFieldDefDTO;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.common.exception.PnClientRuntimeException;
import edu.planon.lib.client.common.util.PnQueryBOUtils;
import edu.planon.lib.client.referencepicker.AbstractPnReferencePickerPanel;
import edu.planon.lib.client.referencepicker.PnReferenceMultiPickerPanel;
import edu.planon.lib.client.referencepicker.PnReferenceSinglePickerPanel;
import edu.planon.lib.client.search.model.PnSearchFilterModel;
import nl.planon.enterprise.service.api.PnESOperator;
import nl.planon.util.pnlogging.PnLogger;

public class PnReferenceFieldEditor extends AbstractPnFieldEditor {
	private static final long serialVersionUID = 1L;
	private PnReferenceFieldDTO referenceDTO;
	private PnComponentUpdatingBehavior fieldBehavior;
	private FormComponent<?> editor;
	
	public PnReferenceFieldEditor(String wicketId, PnReferenceFieldDTO referenceDTO) {
		super(wicketId, referenceDTO);
		this.referenceDTO = referenceDTO;
		
		this.setOutputMarkupId(true);
		
		String updateEvent;
		if (this.fieldDTO.isMultiSelect()) {
			this.editor = new TextField<String>("fieldValueEditor", referenceDTO.getDisplayValueModel());
			updateEvent = "blur";
			this.editor.add(new AttributeModifier("readonly", "readonly"));
		}
		else {
			PropertyModel<PnRecordDTO> valueModel = PropertyModel.of(referenceDTO.getValueModel(), "0");
			this.editor = new PnAutoCompleteTextField("fieldValueEditor", valueModel, referenceDTO, createAutoCompleteSettings(0, 1));
			this.editor.add(new AttributeModifier("maxlength", new PropertyModel<String>(referenceDTO, "inputLength")));
			updateEvent = "change";
		}
		
		this.editor.setOutputMarkupId(true);
		
		this.editor.setLabel(new PropertyModel<String>(this.fieldDTO, "label"));
		this.editor.setRequired(this.fieldDTO.isRequired());
		
		// make the component update the model when the users clicks out of the input
		this.fieldBehavior = new PnComponentUpdatingBehavior(updateEvent);
		this.fieldBehavior.addEventListener((event, sourceComponent, target) -> {
			target.add(this.editor);
		});
		this.editor.add(this.fieldBehavior);
		this.setEventSource(this.fieldBehavior);
		
		AjaxIconLink refButton = this.getReferenceButton();
		refButton.addEventListener((event, sourceComponent, target) -> this.openEditor(target));
		
		this.add(this.editor);
	}
	
	private void openEditor(AjaxRequestTarget editorTarget) {
		try {
			AbstractPnReferencePickerPanel pickerPanel;
			if (this.referenceDTO.isMultiSelect()) {
				pickerPanel = new PnReferenceMultiPickerPanel(this.getPopupWindow().getContentId(), this.referenceDTO.getReferenceFieldDef());
			}
			else {
				pickerPanel = new PnReferenceSinglePickerPanel(this.getPopupWindow().getContentId(), this.referenceDTO.getReferenceFieldDef());
			}
			
			pickerPanel.addEventListener((event, sourceComponent, target) -> {
				this.referenceDTO.setValues(pickerPanel.getSelectedRecords());
				target.add(this.editor);
			});
			
			pickerPanel.setDefaultSearchFilter(this.referenceDTO.getDefaultSearchFilter());
			pickerPanel.addEventListener(this.getEventListeners());
			pickerPanel.setSelectedRecords(this.referenceDTO.getSelectedList(), editorTarget);
			
			this.getPopupWindow().setContent(pickerPanel);
		}
		catch (PnClientException exception) {
			PnLogger.getLogger(this.getClass()).error(exception);
			this.showError(editorTarget, exception);
		}
		
		this.getPopupWindow().setInitialHeight(500);
		this.getPopupWindow().setInitialWidth(this.referenceDTO.isMultiSelect() ? 900 : 500);
		this.getPopupWindow().setTitle(new PropertyModel<String>(this.referenceDTO, "label"));
		this.getPopupWindow().setVisible(true);
		this.getPopupWindow().setVisibilityAllowed(true);
		this.getPopupWindow().show(editorTarget);
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		this.getReferenceButton().setVisible(this.getParent().isEnabled());
	}
	
	public class PnAutoCompleteTextField extends AutoCompleteTextField<PnRecordDTO> {
		private static final long serialVersionUID = 1L;
		private final PnReferenceFieldDTO referenceDTO;
		private PnReferenceFieldDefDTO referenceFieldDef;
		
		private List<PnSearchFilterModel> defaultSearchFilter;
		private final PnQueryParamDTO queryParam;
		private final List<PnFieldDefDTO> fieldsList;
		private final List<PnRecordDTO> optionList = new ArrayList<PnRecordDTO>();
		
		public PnAutoCompleteTextField(String id, IModel<PnRecordDTO> model, PnReferenceFieldDTO referenceDTO, AutoCompleteSettings settings) {
			super(id, model, PnRecordDTO.class, new PnAutoCompleteTextRenderer(), settings);
			
			this.referenceDTO = referenceDTO;
			this.referenceFieldDef = referenceDTO.getReferenceFieldDef();
			
			try {
				this.fieldsList = this.referenceFieldDef.getFieldsList();
				//PnReferenceFieldUtils.getFieldsFromBO(this.referenceFieldDef);
				if (this.fieldsList.isEmpty()) {
					throw new PnClientRuntimeException(
							"fields list should not be empty or provided fields must be available on query, Please check configuration for reference field "
									+ this.referenceDTO.getPnName());
				}
				this.queryParam = new PnQueryParamDTO(0L, 10L, this.fieldsList.get(0).getPnName(), true);
			}
			catch (PnClientException exception) {
				throw new PnClientRuntimeException(exception);
			}
		}
		
		@Override
		protected Iterator<PnRecordDTO> getChoices(String input) {
			this.optionList.clear();
			if (input == null || input.isEmpty()) {
				return this.optionList.iterator();
			}
			
			ArrayList<Object> inputValues = new ArrayList<Object>();
			inputValues.add(input);
			
			this.defaultSearchFilter = this.getDefaultSearchFilter();
			ArrayList<PnSearchFilterModel> searchFilter = new ArrayList<PnSearchFilterModel>(this.defaultSearchFilter);
			searchFilter.add(new PnSearchFilterModel(this.fieldsList.get(0).getPnName(), PnESOperator.CONTAINS, inputValues));
			
			try {
				List<PnRecordDTO> dataRows = PnQueryBOUtils.getRowsFromBO(this.referenceFieldDef.getBoPnName(), this.queryParam, searchFilter, this.fieldsList);
				this.optionList.addAll(dataRows);
				if (this.optionList.size() < 10 && this.fieldsList.size() > 1) {
					searchFilter.clear();
					searchFilter.addAll(this.defaultSearchFilter);
					searchFilter.add(new PnSearchFilterModel(this.fieldsList.get(1).getPnName(), PnESOperator.CONTAINS, inputValues));
					dataRows = PnQueryBOUtils.getRowsFromBO(this.referenceFieldDef.getBoPnName(), this.queryParam, searchFilter, this.fieldsList);
					for (int index = 0; index + this.optionList.size() < 10 && index < dataRows.size(); ++index) {
						if (this.optionList.contains(dataRows.get(index))) {
							continue;
						}
						this.optionList.add(dataRows.get(index));
					}
				}
			}
			catch (PnClientException exception) {
				PnLogger.getLogger(PnAutoCompleteTextField.class).error(exception);
			}
			return this.optionList.iterator();
		}
		
		@Override
		protected IConverter<?> createConverter(Class<?> type) {
			if (PnRecordDTO.class.isAssignableFrom(type)) {
				return new IConverter<PnRecordDTO>() {
					private static final long serialVersionUID = 1L;
					
					@Override
					public PnRecordDTO convertToObject(String value, Locale locale) throws ConversionException {
						List<PnRecordDTO> optionList = PnAutoCompleteTextField.this.getOptionList();
						for (PnRecordDTO tableDTO : optionList) {
							if (!tableDTO.getTextValue().equals(value)) {
								continue;
							}
							return tableDTO;
						}
						return null;
					}
					
					@Override
					public String convertToString(PnRecordDTO value, Locale locale) {
						return (value == null) ? null : value.getTextValue();
					}
				};
			}
			
			return null;
		}
		
		public List<PnRecordDTO> getOptionList() {
			return this.optionList;
		}
		
		public List<PnSearchFilterModel> getDefaultSearchFilter() {
			if (this.defaultSearchFilter == null) {
				this.defaultSearchFilter = this.referenceDTO.getDefaultSearchFilter();
				if (this.defaultSearchFilter == null) {
					this.defaultSearchFilter = new ArrayList<PnSearchFilterModel>();
				}
			}
			return this.defaultSearchFilter;
		}
	}
	
	protected static AutoCompleteSettings createAutoCompleteSettings(int autoCompleteNumChars, int autoCompleteWaitTime) {
		AutoCompleteSettings autoCompleteSettings = new AutoCompleteSettings();
		autoCompleteSettings.setCssClassName("pnAutoCompleter");
		autoCompleteSettings.setMinInputLength(autoCompleteNumChars);
		autoCompleteSettings.setThrottleDelay(autoCompleteWaitTime);
		autoCompleteSettings.setUseSmartPositioning(true);
		autoCompleteSettings.setPreselect(true);
		return autoCompleteSettings;
	}
	
	protected class PnAutoCompleteTextRenderer extends AbstractAutoCompleteTextRenderer<PnRecordDTO> {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void renderHeader(Response aResponse) {
			aResponse.write("<ul class='pn-suggestions'>");
		}
		
		@Override
		protected CharSequence getOnSelectJavaScriptExpression(PnRecordDTO aItem) {
			return "$(initialElement).focus(); input";
		}
		
		@Override
		protected String getTextValue(PnRecordDTO tableDTO) {
			return tableDTO.getTextValue();
		}
	}
	
	@Override
	public FormComponent<?> getFormComponent() {
		return this.editor;
	}
	
	@Override
	public boolean isEditorEnabled() {
		return this.editor.isEnabled();
	}
}

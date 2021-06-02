package edu.planon.lib.client.datepicker;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Request;
import org.wicketstuff.wiquery.core.javascript.JsScopeContext;
import org.wicketstuff.wiquery.ui.datepicker.DateOption;
import org.wicketstuff.wiquery.ui.datepicker.InlineDatePicker;
import org.wicketstuff.wiquery.ui.datepicker.scope.JsScopeUiDatePickerDateTextEvent;

import edu.planon.lib.client.action.CloseModalBehavior;
import edu.planon.lib.client.action.IAjaxEventListener;

public class PnDatePickerPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private InlineDatePicker<Date> datePicker;
	private ArrayList<IAjaxEventListener> completeListeners = new ArrayList<IAjaxEventListener>();
	private IModel<Date> dateModel;
	
	public PnDatePickerPanel(String id, IModel<Date> dateModel) {
		super(id);
		this.dateModel = dateModel;
		
		this.datePicker = createDatePicker("datePicker");
		
		this.add(new AttributeAppender("class", "PlanonWebBaseDialog PnWebBaseDialog PnWebFieldValueDialog PnWebDateDialog", " "));
		
		initBaseButtonPanel();
	}
	
	private InlineDatePicker<Date> createDatePicker(String id) {
		InlineDatePicker<Date> datePicker = new InlineDatePicker<Date>(id);
		datePicker.setDefaultModel(this.dateModel);
		datePicker.add(new AttributeAppender("class", "pnwidatepicker", " "));
		datePicker.setDefaultDate(new DateOption(dateModel.getObject()));
		datePicker.setChangeMonth(true);
		datePicker.setChangeYear(true);
		datePicker.setShowOtherMonths(true);
		datePicker.setSelectOtherMonths(true);
		datePicker.setGotoCurrent(true);
		
		AbstractDefaultAjaxBehavior onSelectEvent = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void respond(AjaxRequestTarget target) {
				Request request = this.getComponent().getRequest();
				String dateValue = request.getRequestParameters().getParameterValue("DATE").toString();
				Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dateValue, new ParsePosition(0));
				datePicker.setDefaultModelObject(date);
			}
			
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				attributes.getDynamicExtraParameters().add("return {'DATE': dateText}");
			}
			
		};
		datePicker.add(onSelectEvent);
		
		datePicker.setOnSelectEvent(new JsScopeUiDatePickerDateTextEvent() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void execute(JsScopeContext aScopeContext) {
				aScopeContext.append(onSelectEvent.getCallbackScript());
			}
		});
		
		this.add(datePicker);
		return datePicker;
	}
	
	private void initBaseButtonPanel() {
		//today
		Button btnToday = new Button("btnToday", Model.of("Today"));
		btnToday.add(new AjaxEventBehavior("click") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				PnDatePickerPanel.this.datePicker.setDefaultModelObject(new Date());
				for (IAjaxEventListener listener : PnDatePickerPanel.this.completeListeners) {
					listener.onEvent(this.getEvent(), target);
				}
				ModalWindow.closeCurrent(target);
			}
		});
		this.add(btnToday);
		
		//Ok
		Button btnOk = new Button("btnOk", Model.of("Ok"));
		btnOk.add(new AjaxEventBehavior("click") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				for (IAjaxEventListener listener : PnDatePickerPanel.this.completeListeners) {
					listener.onEvent(this.getEvent(), target);
				}
				ModalWindow.closeCurrent(target);
			}
		});
		this.add(btnOk);
		
		//Cancel
		Button btnCancel = new Button("btnCancel", Model.of("Cancel"));
		btnCancel.add(new CloseModalBehavior("click"));
		this.add(btnCancel);
	}
	
	public PnDatePickerPanel addUpdateListener(IAjaxEventListener listener) {
		completeListeners.add(listener);
		return this;
	}
}

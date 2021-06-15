package edu.planon.lib.client.panel.datepicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;

import edu.planon.lib.client.common.behavior.PnComponentUpdatingBehavior;
import edu.planon.lib.client.exception.PnClientException;

public class PnDateTimePickerPanel extends PnDatePickerPanel {
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	private IModel<Date> dateModel;
	private Model<String> timeModel;
	private TextField<String> timeField;
	
	public PnDateTimePickerPanel(String wicketId, IModel<Date> dateModel, Date initialDate) {
		super(wicketId, dateModel, initialDate);
		this.dateModel = dateModel;
		
		this.timeModel = Model.of(TIME_FORMAT.format(dateModel.getObject()));
		
		this.timeField = new TextField<String>("timeField", timeModel);
		this.timeField.setOutputMarkupId(true);
		this.timeField.add(new PnComponentUpdatingBehavior("blur"));
		this.add(this.timeField);
	}
	
	@Override
	protected void beforeUpdate() throws PnClientException {
		String dateStr = DATE_FORMAT.format(dateModel.getObject());
		String timeStr = timeModel.getObject();
		String dateTimeStr = dateStr + ' ' + timeStr;
		
		try {
			Date dateTime = DATETIME_FORMAT.parse(dateTimeStr);
			dateModel.setObject(dateTime);
		}
		catch (ParseException exception) {
			throw new PnClientException("Unable to parse time: \"" + dateTimeStr + "\"");
		}
	}
	
	public void renderHead(IHeaderResponse response) {
		response.render(CssHeaderItem.forReference(new CssResourceReference(PnDateTimePickerPanel.class, "PnDateTimePickerPanel.css")));
	}
}

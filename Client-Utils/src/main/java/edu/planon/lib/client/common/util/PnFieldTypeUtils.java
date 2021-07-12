package edu.planon.lib.client.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import nl.planon.enterprise.service.api.IPnESResultSet;
import nl.planon.enterprise.service.api.PnESFieldNotFoundException;
import nl.planon.enterprise.service.api.PnESValueType;

public class PnFieldTypeUtils {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0");
	//REFERENCE, STRING_REFERENCE, PERIOD, SECUREDOCUMENT, AUTOCAD_FILE, DOCUMENT_FILE, IMAGE_FILE, INTEGER, STRING, BIG_DECIMAL, BOOLEAN, 
	//DATE_NEUTRAL, TIME_NEUTRAL, DATE_TIME, DATE_TIME_NEUTRAL, DATABASE_QUERY, GPS, ATTRIBUTES
	
	public static boolean hasDialog(PnESValueType fieldType) {
		switch (fieldType) {
			case REFERENCE:
			case DATE_NEUTRAL:
			case DATE_TIME:
			case DATE_TIME_NEUTRAL:
			case TIME_NEUTRAL:
				return true;
			case AUTOCAD_FILE:
			case DOCUMENT_FILE:
			case SECUREDOCUMENT:
			case IMAGE_FILE:
			case PERIOD:
			case ATTRIBUTES:
			case BIG_DECIMAL:
			case BOOLEAN:
			case DATABASE_QUERY:
			case GPS:
			case INTEGER:
			case STRING:
			case STRING_REFERENCE:
			default:
				return false;
		}
	}
	
	public static String getEditorInfoIcon(PnESValueType fieldType) {
		switch (fieldType) {
			case IMAGE_FILE:
				return "image-alt";
			case AUTOCAD_FILE:
			case DOCUMENT_FILE:
			case SECUREDOCUMENT:
				return "file-lines";
			default:
				return "info-box";
		}
	}
	
	public static String getEditorInfoTooltip(PnESValueType fieldType) {
		switch (fieldType) {
			case AUTOCAD_FILE:
			case DOCUMENT_FILE:
			case SECUREDOCUMENT:
			case IMAGE_FILE:
				return "Download";
			default:
				return "Information";
		}
	}
	
	public static String getEditorReferenceIcon(PnESValueType fieldType) {
		switch (fieldType) {
			case DATE_TIME:
			case DATE_TIME_NEUTRAL:
			case DATE_NEUTRAL:
				return "calendar-clock-alt";
			case TIME_NEUTRAL:
				return "clock-outline";
			case AUTOCAD_FILE:
			case DOCUMENT_FILE:
			case SECUREDOCUMENT:
			case IMAGE_FILE:
				return "folder-open";
			case PERIOD:
				return "calendar-week";
			default:
				return "squares-two";
		}
	}
	
	public static String getEditorReferenceTooltip(PnESValueType fieldType) {
		switch (fieldType) {
			case IMAGE_FILE:
			case AUTOCAD_FILE:
			case DOCUMENT_FILE:
			case SECUREDOCUMENT:
				return "Select a file";
			case DATE_TIME:
			case DATE_TIME_NEUTRAL:
				return "Select a date & time";
			case DATE_NEUTRAL:
				return "Select a date";
			case TIME_NEUTRAL:
				return "Select a time";
			default:
				return "Select a value";
		}
	}
	
	public static int getDefaultWidth(PnESValueType fieldType) {
		switch (fieldType) {
			case BOOLEAN:
				return 20;
			case DATE_NEUTRAL:
			case TIME_NEUTRAL:
				return 40;
			case BIG_DECIMAL:
			case DATE_TIME:
			case DATE_TIME_NEUTRAL:
			case INTEGER:
				return 80;
			case REFERENCE:
			case STRING:
			case STRING_REFERENCE:
				return 200;
			case AUTOCAD_FILE:
			case DOCUMENT_FILE:
			case SECUREDOCUMENT:
			case IMAGE_FILE:
				return 500;
			case PERIOD:
			case ATTRIBUTES:
			case DATABASE_QUERY:
			case GPS:
			default:
				return 60;
		}
	}
	
	public static boolean isQueryResultDisplaySupported(PnESValueType fieldType) {
		switch (fieldType) {
			case STRING:
			case DOCUMENT_FILE:
			case IMAGE_FILE:
			case STRING_REFERENCE:
			case BIG_DECIMAL:
			case DATE_NEUTRAL:
			case DATE_TIME:
			case DATE_TIME_NEUTRAL:
			case INTEGER:
				return true;
			case REFERENCE:
			case ATTRIBUTES:
			case AUTOCAD_FILE:
			case BOOLEAN:
			case DATABASE_QUERY:
			case GPS:
			case PERIOD:
			case SECUREDOCUMENT:
			case TIME_NEUTRAL:
			default:
				return false;
		}
	}
	
	public static String getFieldFromQuery(PnESValueType fieldType, IPnESResultSet resultSet, String columnName)
			throws IllegalStateException, PnESFieldNotFoundException {
		switch (fieldType) {
			case STRING:
			case DOCUMENT_FILE:
			case IMAGE_FILE:
				return resultSet.getString(columnName);
			case STRING_REFERENCE:
				return resultSet.getStringReference(columnName);
			case BIG_DECIMAL:
				BigDecimal bigDecimal = resultSet.getBigDecimal(columnName);
				return bigDecimal == null ? "" : DECIMAL_FORMAT.format(bigDecimal);
			case DATE_NEUTRAL:
				Date date = resultSet.getDate(columnName);
				return date == null ? "" : DATE_FORMAT.format(date);
			case DATE_TIME:
			case DATE_TIME_NEUTRAL:
				Date dateTime = resultSet.getDateTime(columnName);
				return dateTime == null ? "" : DATETIME_FORMAT.format(dateTime);
			case REFERENCE:
				Integer reference = resultSet.getReference(columnName);
				return (reference == null) ? "" : reference.toString();
			case INTEGER:
				Integer intValue = resultSet.getInteger(columnName);
				return (intValue == null) ? "" : intValue.toString();
			case ATTRIBUTES:
			case AUTOCAD_FILE:
			case BOOLEAN:
			case DATABASE_QUERY:
			case GPS:
			case PERIOD:
			case SECUREDOCUMENT:
			case TIME_NEUTRAL:
			default:
				return "Field Type Not Supported.";
		}
	}
}

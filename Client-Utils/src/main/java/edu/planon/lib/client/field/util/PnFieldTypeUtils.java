package edu.planon.lib.client.field.util;

import nl.planon.enterprise.service.api.PnESValueType;

public class PnFieldTypeUtils {
	//REFERENCE, STRING_REFERENCE, PERIOD, SECUREDOCUMENT, AUTOCAD_FILE, DOCUMENT_FILE, IMAGE_FILE, INTEGER, STRING, BIG_DECIMAL, BOOLEAN, 
	//DATE_NEUTRAL, TIME_NEUTRAL, DATE_TIME, DATE_TIME_NEUTRAL, DATABASE_QUERY, GPS, ATTRIBUTES
	
	
	public static boolean hasDialog(PnESValueType fieldType) {
		switch(fieldType) {
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
		switch(fieldType) {
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
		switch(fieldType) {
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
		switch(fieldType) {
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
		switch(fieldType) {
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
}

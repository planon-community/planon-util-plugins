package edu.planon.lib.common;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import edu.planon.lib.common.exception.PropertyNotDefined;

public class BaseProperties extends Properties {
	private static final long serialVersionUID = 1L;
	public static final List<String> TRUE_VALUES = Collections.unmodifiableList(Arrays.asList("true", "t", "y", "yes", "1"));

	public BaseProperties(String propertiesText) throws IOException {
		if (this.hasConfiguration(propertiesText)) {
			this.load(new StringReader(propertiesText));
		}
	}
	
	private boolean hasConfiguration(String arguments) {
		return arguments != null && !BaseProperties.isBlank(arguments);
	}

	private static boolean isBlank(CharSequence charseq) {
		if (charseq == null || charseq.length() == 0) {
			return true;
		}
		int strLen = charseq.length();
		for (int i = 0; i < strLen; ++i) {
			if (Character.isWhitespace(charseq.charAt(i)))
				continue;
			return false;
		}
		return true;
	}
	
	private void assertKeyExists(String key) throws PropertyNotDefined {
		if (!this.containsKey(key)) {
			String msg = String.format("Missing property: %s", key);
			throw new PropertyNotDefined(msg);
		}
	}
	
	public String getStringProperty(String key) throws PropertyNotDefined {
		this.assertKeyExists(key);
		return this.getStringProperty(key, null);
	}
	
	public String getStringProperty(String key, String defaultValue) {
		String property = this.getProperty(key, defaultValue);
		return property == null ? property : property.trim();
	}
	
	public int getIntegerProperty(String key) throws PropertyNotDefined {
		this.assertKeyExists(key);
		return this.getIntegerProperty(key, 0);
	}

	public int getIntegerProperty(String key, int defaultValue) {
		String propertyValue = this.getStringProperty(key, Integer.toString(defaultValue));
		return Integer.parseInt(propertyValue);
	}
	
	public boolean getBooleanProperty(String key) throws PropertyNotDefined {
		this.assertKeyExists(key);
		return this.getBooleanProperty(key, false);
	}

	public boolean getBooleanProperty(String key, boolean defaultValue) {
		String propertyValue = this.getStringProperty(key, Boolean.toString(defaultValue));
		return TRUE_VALUES.contains(propertyValue.toLowerCase());
	}
}

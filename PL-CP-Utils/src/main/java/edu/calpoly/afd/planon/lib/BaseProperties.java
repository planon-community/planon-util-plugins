package edu.calpoly.afd.planon.lib;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import edu.calpoly.afd.planon.lib.exception.PropertyNotDefined;

public class BaseProperties extends Properties {
	private static final long serialVersionUID = 1L;

	public BaseProperties(String parameters) throws IOException {
		this.load(new StringReader(parameters));
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
}

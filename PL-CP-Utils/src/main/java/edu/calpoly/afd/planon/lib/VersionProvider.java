package edu.calpoly.afd.planon.lib;

import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

public final class VersionProvider {
	private static final VersionProvider INSTANCE = new VersionProvider();
	private String build;
	private String version;
	
	
	private VersionProvider() {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("version-information");
			if (Objects.nonNull(bundle) && bundle.containsKey("application.version")) {
				this.version = bundle.getString("application.version");
			}
			if (Objects.nonNull(bundle) && bundle.containsKey("application.version")) {
				this.build = bundle.getString("application.build");
			}
		} catch (MissingResourceException e) {
			return;
		}
	}

	public static String getVersion() {
		return VersionProvider.INSTANCE.version;
	}
	
	public static String getBuild() {
		return VersionProvider.INSTANCE.build;
	}

}

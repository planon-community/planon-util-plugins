package edu.calpoly.afd.planon.lib;

import java.io.IOException;
import java.util.Objects;

import edu.calpoly.afd.planon.lib.VersionProvider;
import edu.calpoly.afd.planon.lib.exception.PropertyNotDefined;
import nl.planon.hades.userextension.uxinterface.*;

public abstract class BaseSX implements IUserExtension {
	private String description;
	
	public BaseSX(String description) {
		this.description = description;
	}
	
	protected abstract void execute(IUXBusinessObject newBO, IUXBusinessObject oldBO, IUXContext context, String parameters)
			throws PropertyNotDefined, IOException;
	
	public final void executeUX(IUXBusinessObject newBO, IUXBusinessObject oldBO, IUXContext context, String parameters) {
		try {
			this.execute(newBO, oldBO, context, parameters);
		} catch (PropertyNotDefined e) {
			context.addError(900, e.getMessage());
		} catch (IOException e) {
			context.addError(900, e.getMessage());
		}
	}
	
	public String getDescription() {
		if (Objects.isNull(VersionProvider.getBuild())) {
			if (Objects.isNull(VersionProvider.getVersion())) {
				return this.description;
			}
			return VersionProvider.getVersion() + " - " + this.description;
		}
		return VersionProvider.getVersion() + " ["+ VersionProvider.getBuild() + "] - " + this.description;
	}
	
	
}

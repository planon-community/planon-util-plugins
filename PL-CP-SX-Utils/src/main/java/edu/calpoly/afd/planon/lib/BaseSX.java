package edu.calpoly.afd.planon.lib;

import java.io.IOException;
import java.util.Objects;
import java.util.Arrays;
import edu.calpoly.afd.planon.lib.VersionProvider;
import edu.calpoly.afd.planon.lib.exception.PropertyNotDefined;
import edu.calpoly.afd.planon.lib.exception.SXException;
import nl.planon.hades.userextension.uxinterface.*;

public abstract class BaseSX implements IUserExtension {
	private String description;
	
	public BaseSX(String description) {
		this.description = description;
	}
	
	protected abstract void execute(IUXBusinessObject newBO, IUXBusinessObject oldBO, IUXContext context, String parameters)
			throws PropertyNotDefined, SXException, IOException;
	
	public final void executeUX(IUXBusinessObject newBO, IUXBusinessObject oldBO, IUXContext context, String parameters) {
		try {
			this.execute(newBO, oldBO, context, parameters);
		} catch (PropertyNotDefined e) {
			context.addError(900, e.getMessage());
		} catch (SXException e) {
			e.toError(context);
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
	
	public final void checkBOType(IUXBusinessObject bo, String... requiredBOTypeNames) throws SXException {
		if (requiredBOTypeNames == null) {
			throw new IllegalArgumentException("checkBOType: requiredBOTypeNames is required");
		}
		String boTypeName = bo.getTypeName();
		String boSystemTypeName = bo.getBODefinition().getSystemBOType();
		
		boolean typeMatches = Arrays.asList(requiredBOTypeNames).contains(boTypeName);
		
		if(!boTypeName.equals(boSystemTypeName)) {
			typeMatches = typeMatches || Arrays.asList(requiredBOTypeNames).contains(boSystemTypeName);
		}
		
		if(!typeMatches) {
			throw new SXException(1, "Invalid type for BO "+boTypeName);
		}
	}
}

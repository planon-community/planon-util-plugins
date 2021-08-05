package edu.planon.lib.sx;

import java.io.IOException;
import java.util.Objects;

import edu.planon.lib.common.VersionProvider;
import edu.planon.lib.common.exception.PropertyNotDefined;
import edu.planon.lib.sx.exception.SXException;
import nl.planon.hades.userextension.uxinterface.ITaskUserExtension;
import nl.planon.hades.userextension.uxinterface.IUXContext;

public abstract class BaseTUX implements ITaskUserExtension {
	public abstract String getSXDescription();
	
	protected abstract void execute(IUXContext context, String parameters) throws PropertyNotDefined, SXException, IOException;
	
	@Override
	public final void executeUX(IUXContext context, String parameters) {
		try {
			this.execute(context, parameters);
		}
		catch (IOException | PropertyNotDefined e) {
			context.addError(900, e.getMessage());
		}
		catch (SXException e) {
			e.toError(context);
		}
	}
	
	@Override
	public String getDescription() {
		if (Objects.isNull(VersionProvider.getBuild())) {
			if (Objects.isNull(VersionProvider.getVersion())) {
				return this.getSXDescription();
			}
			return VersionProvider.getVersion() + " - " + this.getSXDescription();
		}
		return VersionProvider.getVersion() + " [" + VersionProvider.getBuild() + "] - " + this.getSXDescription();
	}
}

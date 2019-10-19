package edu.calpoly.afd.planon.lib.logger;

import nl.planon.util.pnlogging.PnLogger;

public class ConsoleLogger {
	private final PnLogger pnLogger;
	private boolean forceDebugLogging;

	public ConsoleLogger(Class<?> clazz) {
		this.pnLogger = PnLogger.getLogger(clazz);
	}

	public boolean isDebugEnabled() {
		return this.pnLogger.isDebugEnabled() || this.forceDebugLogging;
	}

	public boolean isInfoEnabled() {
		return this.pnLogger.isInfoEnabled();
	}

	public boolean isWarnEnabled() {
		return this.pnLogger.isWarnEnabled();
	}

	public boolean isErrorEnabled() {
		return this.pnLogger.isErrorEnabled();
	}

	public boolean isFatalEnabled() {
		return this.pnLogger.isFatalEnabled();
	}

	public void debug(String text) {
		if (this.forceDebugLogging) {
			this.info(text);
		} else {
			this.pnLogger.debug((Object) text);
		}
	}

	public void debug(String text, Throwable throwable) {
		if (this.forceDebugLogging) {
			this.info(text, throwable);
		} else {
			this.pnLogger.debug((Object) text, throwable);
		}
	}

	public void info(String text) {
		this.pnLogger.info((Object) text);
	}

	public void info(String text, Throwable throwable) {
		this.pnLogger.info((Object) text, throwable);
	}

	public void warn(String text) {
		this.pnLogger.warn((Object) text);
	}

	public void warn(String text, Throwable throwable) {
		this.pnLogger.warn((Object) text, throwable);
	}

	public void error(String text) {
		this.pnLogger.error((Object) text);
	}

	public void error(String text, Throwable throwable) {
		this.pnLogger.error((Object) text, throwable);
	}

	public void fatal(String text) {
		this.pnLogger.fatal((Object) text);
	}

	public void fatal(String text, Throwable throwable) {
		this.pnLogger.fatal((Object) text, throwable);
	}

	public boolean isForceDebugLogging() {
		return this.forceDebugLogging;
	}

	public void setForceDebugLogging(boolean forceDebugLogging) {
		this.forceDebugLogging = forceDebugLogging;
	}
}
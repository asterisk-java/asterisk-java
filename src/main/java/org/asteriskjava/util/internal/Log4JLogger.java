/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.asteriskjava.util.internal;

import java.io.Serializable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.asteriskjava.util.Log;

/**
 * Implementation of {@link Log} that maps directly to a Log4J
 * <strong>Logger</strong>.
 * <p>
 * Initial configuration of the corresponding Logger instances should be done in
 * the usual manner, as outlined in the Log4J documentation.
 * <p>
 * More or less "stolen" from Apache's commons-logging.
 * 
 * @author <a href="mailto:sanders@apache.org">Scott Sanders</a>
 * @author Rod Waldhoff
 * @author Robert Burrell Donkin
 * @version $Id$
 */
public class Log4JLogger implements Log, Serializable
{

	// ------------------------------------------------------------- Attributes

	/**
	 * The serial version identifier.
	 */
	private static final long serialVersionUID = 3545240215095883829L;

	/** The fully qualified name of the Log4JLogger class. */
	private static final String FQCN = Log4JLogger.class.getName();

	/** Log to this logger */
	private transient final AbstractLogger logger;

	/**
	 * Base constructor.
	 */
	public Log4JLogger(Class<?> clazz)
	{
		this.logger = (AbstractLogger) LogManager.getLogger(clazz.getName());
	}

	// --------------------------------------------------------- Implementation

	/**
	 * Log a message to the Log4j Logger with <code>TRACE</code> priority.
	 * Currently logs to <code>DEBUG</code> level in Log4J.
	 */
	public void trace(Object message)
	{
		logger.logIfEnabled(FQCN, Level.TRACE, null, message, (Throwable) null);
	}

	/**
	 * Log an error to the Log4j Logger with <code>TRACE</code> priority.
	 * Currently logs to <code>DEBUG</code> level in Log4J.
	 */
	public void trace(Object message, Throwable t)
	{
		logger.logIfEnabled(FQCN, Level.TRACE, null, message, t);
	}

	/**
	 * Log a message to the Log4j Logger with <code>DEBUG</code> priority.
	 */
	@Override
	public void debug(Object message)
	{
		logger.logIfEnabled(FQCN, Level.DEBUG, null, message, (Throwable) null);
	}

	/**
	 * Log an error to the Log4j Logger with <code>DEBUG</code> priority.
	 */
	@Override
	public void debug(Object message, Throwable t)
	{
		logger.logIfEnabled(FQCN, Level.DEBUG, null, message, t);
	}

	/**
	 * Log a message to the Log4j Logger with <code>INFO</code> priority.
	 */
	@Override
	public void info(Object message)
	{
		logger.logIfEnabled(FQCN, Level.INFO, null, message, (Throwable) null);

	}

	/**
	 * Log an error to the Log4j Logger with <code>INFO</code> priority.
	 */
	public void info(Object message, Throwable t)
	{
		logger.logIfEnabled(FQCN, Level.INFO, null, message, t);
	}

	/**
	 * Log a message to the Log4j Logger with <code>WARN</code> priority.
	 */
	@Override
	public void warn(Object message)
	{
		logger.logIfEnabled(FQCN, Level.WARN, null, message, (Throwable) null);
	}

	/**
	 * Log an error to the Log4j Logger with <code>WARN</code> priority.
	 */
	@Override
	public void warn(Object message, Throwable t)
	{
		logger.logIfEnabled(FQCN, Level.WARN, null, message, t);
	}

	/**
	 * Log a message to the Log4j Logger with <code>ERROR</code> priority.
	 */
	@Override
	public void error(Object message)
	{
		logger.logIfEnabled(FQCN, Level.ERROR, null, message, (Throwable) null);
	}

	/**
	 * Log an error to the Log4j Logger with <code>ERROR</code> priority.
	 */
	@Override
	public void error(Object message, Throwable t)
	{
		logger.logIfEnabled(FQCN, Level.ERROR, null, message, t);
	}

	/**
	 * Log a message to the Log4j Logger with <code>FATAL</code> priority.
	 */
	public void fatal(Object message)
	{
		logger.logIfEnabled(FQCN, Level.FATAL, null, message, (Throwable) null);
	}

	/**
	 * Log an error to the Log4j Logger with <code>FATAL</code> priority.
	 */
	public void fatal(Object message, Throwable t)
	{
		logger.logIfEnabled(FQCN, Level.FATAL, null, message, t);
	}

	/**
	 * Return the native Logger instance we are using.
	 */
	public final Logger getLogger()
	{
		return logger;
	}

	/**
	 * Check whether the Log4j Logger used is enabled for <code>DEBUG</code>
	 * priority.
	 */
	@Override
	public boolean isDebugEnabled()
	{
		return logger.isDebugEnabled();
	}

	/**
	 * Check whether the Log4j Logger used is enabled for <code>ERROR</code>
	 * priority.
	 */
	public boolean isErrorEnabled()
	{
		return logger.isErrorEnabled();

	}

	/**
	 * Check whether the Log4j Logger used is enabled for <code>FATAL</code>
	 * priority.
	 */
	public boolean isFatalEnabled()
	{
		return logger.isFatalEnabled();

	}

	/**
	 * Check whether the Log4j Logger used is enabled for <code>INFO</code>
	 * priority.
	 */
	public boolean isInfoEnabled()
	{
		return logger.isInfoEnabled();
	}

	/**
	 * Check whether the Log4j Logger used is enabled for <code>TRACE</code>
	 * priority. For Log4J, this returns the value of
	 * <code>isDebugEnabled()</code>
	 */
	public boolean isTraceEnabled()
	{
		return logger.isDebugEnabled();
	}

	/**
	 * Check whether the Log4j Logger used is enabled for <code>WARN</code>
	 * priority.
	 */
	public boolean isWarnEnabled()
	{
		return logger.isWarnEnabled();
	}
}

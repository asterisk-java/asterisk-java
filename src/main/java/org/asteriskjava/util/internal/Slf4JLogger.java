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

import org.asteriskjava.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 * Implementation of {@link org.asteriskjava.util.Log} that maps to a SLF4J
 * <strong>Logger</strong>.
 * <p>
 *
 * @version $Id$
 */
public class Slf4JLogger implements Log, Serializable
{
    /**
     * The serial version identifier.
     */
    private static final long serialVersionUID = 0L;

    /** Log to this logger */
    private transient Logger logger = null;

    static String FQCN = Slf4JLogger.class.getName();

    /** Logger name */
    private Class< ? > clazz = null;

    public Slf4JLogger()
    {
    }

    /**
     * Base constructor.
     */
    public Slf4JLogger(Class< ? > clazz)
    {
        this.clazz = clazz;
        this.logger = getLogger();
    }

    /**
     * Log a message to the SLF4J Logger with <code>TRACE</code> priority.
     * Currently logs to <code>DEBUG</code> level in SLF4J.
     */
    public void trace(Object message)
    {
        getLogger().trace(message.toString());
    }

    /**
     * Log an error to the SLF4J Logger with <code>TRACE</code> priority.
     */
    public void trace(Object message, Throwable t)
    {
        getLogger().trace(message.toString(), t);
    }

    /**
     * Log a message to the SLF4J Logger with <code>DEBUG</code> priority.
     */
    public void debug(Object message)
    {
        getLogger().debug(message.toString());
    }

    /**
     * Log an error to the SLF4J Logger with <code>DEBUG</code> priority.
     */
    public void debug(Object message, Throwable t)
    {
        getLogger().debug(message.toString(), t);
    }

    /**
     * Log a message to the SLF4J Logger with <code>INFO</code> priority.
     */
    public void info(Object message)
    {
        getLogger().info(message.toString());
    }

    /**
     * Log an error to the SLF4J Logger with <code>INFO</code> priority.
     */
    public void info(Object message, Throwable t)
    {
        getLogger().info(message.toString(), t);
    }

    /**
     * Log a message to the SLF4J Logger with <code>WARN</code> priority.
     */
    public void warn(Object message)
    {
        getLogger().warn(message.toString());
    }

    /**
     * Log an error to the SLF4J Logger with <code>WARN</code> priority.
     */
    public void warn(Object message, Throwable t)
    {
        getLogger().warn(message.toString(), t);
    }

    /**
     * Log a message to the SLF4J Logger with <code>ERROR</code> priority.
     */
    public void error(Object message)
    {
        getLogger().error(message.toString());
    }

    /**
     * Log an error to the SLF4J Logger with <code>ERROR</code> priority.
     */
    public void error(Object message, Throwable t)
    {
        getLogger().error(message.toString(), t);
    }

    /**
     * Log a message to the SLF4J Logger with <code>FATAL</code> priority.
     * <p>
     * Currently uses the <code>ERROR</code> priority in SLF4J.
     */
    public void fatal(Object message)
    {
        getLogger().error(message.toString());
    }

    /**
     * Log an error to the SLF4J Logger with <code>FATAL</code> priority.
     * <p>
     * Currently uses the <code>ERROR</code> priority in SLF4J.
     */
    public void fatal(Object message, Throwable t)
    {
        getLogger().error(message.toString(), t);
    }

    /**
     * Return the native Logger instance we are using.
     */
    public final Logger getLogger()
    {
        if (logger == null)
        {
            logger = LoggerFactory.getLogger(clazz);
        }
        if (logger instanceof LocationAwareLogger)
        {
            return new LocationAwareWrapper(FQCN, (LocationAwareLogger) logger);
        }
        return this.logger;
    }

    /**
     * Check whether the SLF4J Logger used is enabled for <code>DEBUG</code>
     * priority.
     */
    public boolean isDebugEnabled()
    {
        return getLogger().isDebugEnabled();
    }

    /**
     * Check whether the SLF4J Logger used is enabled for <code>ERROR</code>
     * priority.
     */
    public boolean isErrorEnabled()
    {
        return getLogger().isErrorEnabled();
    }

    /**
     * Check whether the SLF4J Logger used is enabled for <code>FATAL</code>
     * priority. For SLF4J, this returns the value of
     * <code>isErrorEnabled()</code>
     */
    public boolean isFatalEnabled()
    {
        return isErrorEnabled();
    }

    /**
     * Check whether the SLF4J Logger used is enabled for <code>INFO</code>
     * priority.
     */
    public boolean isInfoEnabled()
    {
        return getLogger().isInfoEnabled();
    }

    /**
     * Check whether the SLF4J Logger used is enabled for <code>TRACE</code>
     * priority.
     */
    public boolean isTraceEnabled()
    {
        return getLogger().isDebugEnabled();
    }

    /**
     * Check whether the SLF4J Logger used is enabled for <code>WARN</code>
     * priority.
     */
    public boolean isWarnEnabled()
    {
        return getLogger().isWarnEnabled();
    }
}

/*
 * JavaLoggingLog.java
 *
 * Created on April 14, 2005, 4:28 PM
 */

package org.asteriskjava.util.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.asteriskjava.util.Log;


/**
 * Log implementation that uses the java.util.logging package.
 * 
 * @author drach
 */
public class JavaLoggingLog implements Log
{
    /**
     * The underlying commons-logging Log object to use.
     */
    private Logger log;

    /**
     * Creates a new JavaLoggingLog obtained from java.util.logging for the
     * given class.
     * 
     * @param clazz the class to log for.
     */
    public JavaLoggingLog(Class clazz)
    {
        log = Logger.getLogger(clazz.getName());
    }

    public void debug(Object obj)
    {
        log.fine(obj.toString());
    }

    public void info(Object obj)
    {
        log.info(obj.toString());
    }

    public void warn(Object obj)
    {
        log.warning(obj.toString());
    }

    public void warn(Object obj, Throwable ex)
    {
        log.log(Level.WARNING, obj.toString(), ex);
    }

    public void error(Object obj)
    {
        log.severe(obj.toString());
    }

    public void error(Object obj, Throwable ex)
    {
        log.log(Level.SEVERE, obj.toString(), ex);
    }
}

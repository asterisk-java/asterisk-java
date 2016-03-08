package org.asteriskjava.util.internal;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

public class LocationAwareWrapper implements Logger
{

    private String FQCN;
    private LocationAwareLogger logger;

    public LocationAwareWrapper(String FQCN, LocationAwareLogger logger)
    {
        this.FQCN = FQCN;
        this.logger = logger;
    }

    @Override
    public String getName()
    {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled()
    {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg)
    {
        logger.log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, null, null);

    }

    @Override
    public void trace(String format, Object arg)
    {
        logger.trace(format, arg);

    }

    @Override
    public void trace(String format, Object arg1, Object arg2)
    {
        logger.trace(format, arg1, arg2);

    }

    @Override
    public void trace(String format, Object... arguments)
    {
        logger.trace(format, arguments);

    }

    @Override
    public void trace(String msg, Throwable t)
    {
        logger.log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, null, t);

    }

    @Override
    public boolean isTraceEnabled(Marker marker)
    {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg)
    {
        logger.log(marker, FQCN, LocationAwareLogger.TRACE_INT, msg, null, null);

    }

    @Override
    public void trace(Marker marker, String format, Object arg)
    {
        logger.trace(marker, format, arg);

    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2)
    {
        logger.trace(marker, format, arg1, arg2);

    }

    @Override
    public void trace(Marker marker, String format, Object... arguments)
    {
        logger.trace(marker, format, arguments);

    }

    @Override
    public void trace(Marker marker, String msg, Throwable t)
    {
        logger.log(marker, FQCN, LocationAwareLogger.TRACE_INT, msg, null, t);

    }

    @Override
    public boolean isDebugEnabled()
    {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg)
    {
        logger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, null, null);

    }

    @Override
    public void debug(String format, Object arg)
    {
        logger.debug(format, arg);

    }

    @Override
    public void debug(String format, Object arg1, Object arg2)
    {
        logger.debug(format, arg1, arg2);

    }

    @Override
    public void debug(String format, Object... arguments)
    {
        logger.debug(format, arguments);

    }

    @Override
    public void debug(String msg, Throwable t)
    {
        logger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, null, t);

    }

    @Override
    public boolean isDebugEnabled(Marker marker)
    {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg)
    {
        logger.log(marker, FQCN, LocationAwareLogger.DEBUG_INT, msg, null, null);

    }

    @Override
    public void debug(Marker marker, String format, Object arg)
    {
        logger.debug(marker, format, arg);

    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2)
    {
        logger.debug(marker, format, arg1, arg2);

    }

    @Override
    public void debug(Marker marker, String format, Object... arguments)
    {
        logger.debug(marker, format, arguments);

    }

    @Override
    public void debug(Marker marker, String msg, Throwable t)
    {
        logger.log(marker, FQCN, LocationAwareLogger.DEBUG_INT, msg, null, t);

    }

    @Override
    public boolean isInfoEnabled()
    {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg)
    {
        logger.log(null, FQCN, LocationAwareLogger.INFO_INT, msg, null, null);

    }

    @Override
    public void info(String format, Object arg)
    {
        logger.info(format, arg);

    }

    @Override
    public void info(String format, Object arg1, Object arg2)
    {
        logger.info(format, arg1, arg2);

    }

    @Override
    public void info(String format, Object... arguments)
    {
        logger.info(format, arguments);

    }

    @Override
    public void info(String msg, Throwable t)
    {
        logger.log(null, FQCN, LocationAwareLogger.INFO_INT, msg, null, t);

    }

    @Override
    public boolean isInfoEnabled(Marker marker)
    {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg)
    {
        logger.log(marker, FQCN, LocationAwareLogger.INFO_INT, msg, null, null);

    }

    @Override
    public void info(Marker marker, String format, Object arg)
    {
        logger.info(marker, format, arg);

    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2)
    {
        logger.info(marker, format, arg1, arg2);

    }

    @Override
    public void info(Marker marker, String format, Object... arguments)
    {
        logger.info(marker, format, arguments);

    }

    @Override
    public void info(Marker marker, String msg, Throwable t)
    {
        logger.log(marker, FQCN, LocationAwareLogger.INFO_INT, msg, null, t);

    }

    @Override
    public boolean isWarnEnabled()
    {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg)
    {
        logger.log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);

    }

    @Override
    public void warn(String format, Object arg)
    {
        logger.warn(format, arg);

    }

    @Override
    public void warn(String format, Object arg1, Object arg2)
    {
        logger.warn(format, arg1, arg2);

    }

    @Override
    public void warn(String format, Object... arguments)
    {
        logger.warn(format, arguments);

    }

    @Override
    public void warn(String msg, Throwable t)
    {
        logger.log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, t);

    }

    @Override
    public boolean isWarnEnabled(Marker marker)
    {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg)
    {
        logger.log(marker, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);

    }

    @Override
    public void warn(Marker marker, String format, Object arg)
    {
        logger.warn(marker, format, arg);

    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2)
    {
        logger.warn(marker, format, arg1, arg2);

    }

    @Override
    public void warn(Marker marker, String format, Object... arguments)
    {
        logger.warn(marker, format, arguments);

    }

    @Override
    public void warn(Marker marker, String msg, Throwable t)
    {
        logger.log(marker, FQCN, LocationAwareLogger.WARN_INT, msg, null, t);

    }

    @Override
    public boolean isErrorEnabled()
    {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg)
    {
        logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, null);

    }

    @Override
    public void error(String format, Object arg)
    {
        logger.error(format, arg);

    }

    @Override
    public void error(String format, Object arg1, Object arg2)
    {
        logger.error(format, arg1, arg2);

    }

    @Override
    public void error(String format, Object... arguments)
    {
        logger.error(format, arguments);

    }

    @Override
    public void error(String msg, Throwable t)
    {
        logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, t);

    }

    @Override
    public boolean isErrorEnabled(Marker marker)
    {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg)
    {
        logger.log(marker, FQCN, LocationAwareLogger.ERROR_INT, msg, null, null);

    }

    @Override
    public void error(Marker marker, String format, Object arg)
    {
        logger.error(marker, format, arg);

    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2)
    {
        logger.error(marker, format, arg1, arg2);

    }

    @Override
    public void error(Marker marker, String format, Object... arguments)
    {
        logger.error(marker, format, arguments);

    }

    @Override
    public void error(Marker marker, String msg, Throwable t)
    {
        logger.log(marker, FQCN, LocationAwareLogger.ERROR_INT, msg, null, t);

    }
    // Logger logger = LogManager.getLogger();
}

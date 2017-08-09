package org.asteriskjava.pbx.agi.config;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Allows for auto configuration of the AGI script.<br>
 * <br>
 * Provides a getParameter(..) method which verifies the existence of the
 * parameter. <br>
 * <br>
 * Requires an implementing agi to provide it's name and parameter list via
 * getScriptName() and getParameters.
 * 
 * @author rsutton
 */
abstract public class ServiceAgiScriptImpl extends BaseAgiScript implements ServiceAgiScript
{
    static transient Log logger = LogFactory.getLog(ServiceAgiScriptImpl.class);
    protected AgiRequest request;
    protected AgiChannel channel;

    public synchronized void service(AgiRequest request, AgiChannel channel) throws AgiException
    {
        this.request = request;
        this.channel = channel;
        service();
    }

    /**
     * field variables request and channel will be set prior to calling
     * service() AgiRequest request AgiChannel channel
     * 
     * @throws AgiException
     */

    public abstract void service() throws AgiException;

    protected String required(String name, String parameter) throws AgiException
    {
        if (parameter == null || parameter.length() == 0)
            throw new AgiException("Required parameter " + name + " missing.");
        return parameter;
    }

    protected String getParameter(String name) throws AgiException
    {
        return required(name, request.getParameter(name));
    }

    public abstract String getScriptName();

    public abstract String[] getParameters();

}

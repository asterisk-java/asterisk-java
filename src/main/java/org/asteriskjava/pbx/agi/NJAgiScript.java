package org.asteriskjava.pbx.agi;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

abstract public class NJAgiScript extends BaseAgiScript
{
    static transient Logger logger = Logger.getLogger(NJAgiScript.class);
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

    public void setVariable(String name, String value) throws AgiException
    {
        logger.debug("AGIVar set: " + name + "=(" + value + ")");
        super.setVariable(name, value);
    }

}

/*
 * Copyright 2004-2006 by S. Brett Sutton. Commercial support is provided by
 * Asterisk I.T. http://www.asteriskit.com.au
 * 
 * The contents of this file are subject to a modified GPL Version 2 License or
 * later version at your discretion.
 * 
 * The sole modification to the GPL is a limitation on use.
 * 
 * The limitation is that AsterFax (and its source components) may only be used
 * on a single Channel as defined in AsterFax.xml. The implications of the
 * limitation is that the free verions of AsterFax may only be used to send or
 * receive a single fax at a time.
 * 
 * Any copied or modified versions of the AsterFax source must retain this
 * limitation.
 * 
 * If you wish to use multiple channels then you can purchase a commercial
 * license by emailing sales@asteriskit.com.au
 * 
 * Contributor(s): all the names of the contributors are added in the source
 * code where applicable.
 */
package org.asteriskjava.pbx.agi.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;
import org.asteriskjava.fastagi.MappingStrategy;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class AgiMappingStragegy implements MappingStrategy
{

    static private final Log logger = LogFactory.getLog(AgiMappingStragegy.class);

    private Map<String, Class<ServiceAgiScript>> handlers = new HashMap<>();

    @Override
    public AgiScript determineScript(AgiRequest request, AgiChannel channel)
    {
        AgiScript ret = null;

        String script = request.getScript();
        if (script.indexOf(".") != -1)
        {
            script = script.substring(0, script.indexOf("."));
        }

        if (script.startsWith("/"))
        {
            // this is specifically for the "FastAgiSimulator"
            script = script.substring(1);
        }
        Iterator<String> itr = request.getParameterMap().keySet().iterator();
        logger.debug("*********************************");
        logger.debug("script " + script);
        while (itr.hasNext())
        {
            String key = itr.next();
            String val = request.getParameter(key);
            if (key.compareToIgnoreCase("cardNumber") == 0)
                val = "suppressed";
            logger.debug(key + ": " + val);
        }
        logger.debug("******");

        if (handlers.containsKey(script))
        {
            try
            {
                ret = handlers.get(script).newInstance();
            }
            catch (InstantiationException e)
            {
                logger.error(e, e);
            }
            catch (IllegalAccessException e)
            {
                logger.error(e, e);
            }
        }

        return ret;
    }

    /**
     * this will be a pluggable extension system for the agi core, it's still a
     * work in progress and is not useable
     * 
     * @param handler
     * @throws DuplicateScriptException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public void addServiceAgiScript(Class< ? extends ServiceAgiScript> handler)
            throws DuplicateScriptException, InstantiationException, IllegalAccessException
    {

        logger.info("loading agi handler {}" + handler.getCanonicalName());
        ServiceAgiScript tmpHandler = handler.newInstance();
        if (handlers.containsKey(tmpHandler.getScriptName()))
        {
            throw new DuplicateScriptException("Script " + tmpHandler.getScriptName() + " already exists");
        }
        String[] parameters = tmpHandler.getParameters();
        String sample = "Agi(agi://localhost/" + tmpHandler.getScriptName() + ".agi";
        logger.info("********************************************");
        logger.info("registered new agi script: " + tmpHandler.getScriptName());
        for (int i = 0; i < parameters.length; i++)
        {
            logger.info("parameter: " + parameters[i]);
            if (i == 0)
                sample += "?" + parameters[i] + "=testdata";
            else
                sample += "&" + parameters[i] + "=testdata";
        }

        logger.info("sample usage...");
        logger.info(sample + ")");
        Class<ServiceAgiScript> handler2 = (Class<ServiceAgiScript>) handler;
        handlers.put(tmpHandler.getScriptName(), handler2);
    }

}

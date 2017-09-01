/*
 * Copyright 2004-2006 by S. Brett Sutton. 
 * Commercial support is provided by Asterisk I.T. http://www.asteriskit.com.au
 *
 * The contents of this file are subject to a modified GPL Version 2 License 
 * or later version at your discretion.
 * 
 * The sole modification to the GPL is a limitation on use.
 * 
 * The limitation is that AsterFax (and its source components) may only be used on a single Channel 
 * as defined in AsterFax.xml.
 * The implications of the limitation is that the free verions of AsterFax may only be used to send
 * or receive a single fax at a time.
 *
 * Any copied or modified versions of the AsterFax source must retain this limitation.
 * 
 * If you wish to use multiple channels then you can purchase a commercial license
 * by emailing sales@asteriskit.com.au
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 */
package org.asteriskjava.pbx.agi.config;

import java.util.List;

import javax.naming.ConfigurationException;

import org.asteriskjava.fastagi.DefaultAgiServer;

public class ConfiguableAGIServer extends DefaultAgiServer implements Runnable
{

    public ConfiguableAGIServer(AgiConfiguration configuration)
            throws DuplicateScriptException, InstantiationException, IllegalAccessException, ConfigurationException
    {
        AgiMappingStragegy mappingStrategy = new AgiMappingStragegy();
        setMaximumPoolSize(500);
        loadHandlers(mappingStrategy, configuration);

        this.setMappingStrategy(mappingStrategy);
        new Thread(this, "AGIServer:" + this.getPort()).start();

    }

    private void loadHandlers(AgiMappingStragegy mappingStrategy, AgiConfiguration configuration)
            throws DuplicateScriptException, InstantiationException, IllegalAccessException
    {
        List<Class< ? extends ServiceAgiScript>> handlers = configuration.getAgiHandlers();
        for (Class< ? extends ServiceAgiScript> clazz : handlers)
        {
            mappingStrategy.addServiceAgiScript(clazz);
        }
    }

}

/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.fastagi;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A MappingStrategy that is configured via a resource bundle.<br>
 * The resource bundle contains the script part of the url as key and the fully
 * qualified class name of the corresponding AGIScript as value.<br>
 * Example:
 * 
 * <pre>
 * leastcostdial.agi = com.example.fastagi.LeastCostDialAGIScript
 * hello.agi = com.example.fastagi.HelloAGIScript
 * </pre>
 * 
 * LeastCostDialAGIScript and HelloAGIScript must both implement the AGIScript
 * interface and have a default constructor with no parameters.<br>
 * The resource bundle (properties) file is called
 * <code>fastagi-mapping.properties</code> by default and must be available on 
 * the classpath.
 * 
 * @author srt
 * @version $Id: ResourceBundleMappingStrategy.java,v 1.6 2006/01/12 12:54:31 srt Exp $
 */
public class ResourceBundleMappingStrategy extends AbstractMappingStrategy
{
    private static final String DEFAULT_RESOURCE_BUNDLE_NAME = "fastagi-mapping";
    private String resourceBundleName;
    private Map<String, String> mappings;
    private Map<String, AGIScript> instances;
    private boolean shareInstances;

    /**
     * Creates a new ResourceBundleMappingStrategy.
     */
    public ResourceBundleMappingStrategy()
    {
        this(DEFAULT_RESOURCE_BUNDLE_NAME);
    }

    /**
     * Creates a new ResourceBundleMappingStrategy with the given basename
     * of the resource bundle to use.
     * 
     * @param resourceBundleName basename of the resource bundle to use
     */
    public ResourceBundleMappingStrategy(String resourceBundleName)
    {
        this(resourceBundleName, true);
    }

    /**
     * Creates a new ResourceBundleMappingStrategy.
     * 
     * @param shareInstances <code>true</code> to use shared instances,
     *                       <code>false</code> to create a new instance for
     *                       each request.
     * @since 0.3
     */
    public ResourceBundleMappingStrategy(boolean shareInstances)
    {
        this(DEFAULT_RESOURCE_BUNDLE_NAME, shareInstances);
    }

    /**
     * Creates a new ResourceBundleMappingStrategy with the given basename
     * of the resource bundle to use.
     * 
     * @param resourceBundleName basename of the resource bundle to use
     * @param shareInstances <code>true</code> to use shared instances,
     *                       <code>false</code> to create a new instance for
     *                       each request.
     * @since 0.3
     */
    public ResourceBundleMappingStrategy(String resourceBundleName, boolean shareInstances)
    {
        this.resourceBundleName = resourceBundleName;
        this.shareInstances = shareInstances;
    }

    /**
     * Sets the basename of the resource bundle to use.<br>
     * Default is "fastagi-mapping".
     * 
     * @param resourceBundleName basename of the resource bundle to use
     */
    public void setResourceBundleName(String resourceBundleName)
    {
        this.resourceBundleName = resourceBundleName;
        synchronized (this)
        {
            this.mappings = null;
            this.instances = null;
        }
    }

    /**
     * Sets whether to use shared instances or not. If set to <code>true</code>
     * all AGIRequests are served by the same instance of an
     * AGIScript, if set to <code>false</code> a new instance is created for
     * each request.<br>
     * Default is <code>true</code>.
     * 
     * @param shareInstances <code>true</code> to use shared instances,
     *                       <code>false</code> to create a new instance for
     *                       each request.
     * @since 0.3
     */
    public void setShareInstances(boolean shareInstances)
    {
        this.shareInstances = shareInstances;
    }

    private synchronized void loadResourceBundle()
    {
        ResourceBundle resourceBundle;
        Enumeration keys;

        mappings = new HashMap<String, String>();
        if (shareInstances)
        {
            instances = new HashMap<String, AGIScript>();
        }

        try
        {
            resourceBundle = ResourceBundle.getBundle(resourceBundleName);
        }
        catch (MissingResourceException e)
        {
            logger.info("Resource bundle '" + resourceBundleName + "' not found.");
            return;
        }

        keys = resourceBundle.getKeys();

        while (keys.hasMoreElements())
        {
            String scriptName;
            String className;
            AGIScript agiScript;

            scriptName = (String) keys.nextElement();
            className = resourceBundle.getString(scriptName);

            mappings.put(scriptName, className);

            if (shareInstances)
            {
                agiScript = createAGIScriptInstance(className);
                if (agiScript == null)
                {
                    continue;
                }
                instances.put(scriptName, agiScript);
            }

            logger.info("Added mapping for '" + scriptName + "' to class " + className);
        }
    }

    public AGIScript determineScript(AGIRequest request)
    {
        synchronized (this)
        {
            if (mappings == null || (shareInstances && instances == null))
            {
                loadResourceBundle();
            }
        }

        if (shareInstances)
        {
            return instances.get(request.getScript());
        }
        else
        {
            return createAGIScriptInstance(mappings.get(request.getScript()));
        }
    }
}

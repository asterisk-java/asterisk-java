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


import junit.framework.TestCase;

import org.asteriskjava.fastagi.AGIRequest;
import org.asteriskjava.fastagi.AGIScript;
import org.asteriskjava.fastagi.ResourceBundleMappingStrategy;

public class ResourceBundleMappingStrategyTest extends TestCase
{
    private ResourceBundleMappingStrategy mappingStrategy;

    protected void setUp() throws Exception
    {
        super.setUp();
        this.mappingStrategy = new ResourceBundleMappingStrategy();
        this.mappingStrategy.setResourceBundleName("test-mapping");
    }

    public void testDetermineScript()
    {
        AGIScript scriptFirstPass;
        AGIScript scriptSecondPass;
        AGIRequest request;

        request = new SimpleAGIRequest();

        scriptFirstPass = mappingStrategy.determineScript(request);
        scriptSecondPass = mappingStrategy.determineScript(request);

        assertEquals("incorrect script determined",
                scriptFirstPass.getClass(), HelloAGIScript.class);

        assertTrue("script instances are not cached",
                scriptFirstPass == scriptSecondPass);
    }

    public void testDetermineScriptWithResourceBundleUnavailable()
    {
        AGIRequest request;

        request = new SimpleAGIRequest();

        mappingStrategy
                .setResourceBundleName("net.sf.asterisk.fastagi.unavailable");
        assertNull(mappingStrategy.determineScript(request));
    }
}

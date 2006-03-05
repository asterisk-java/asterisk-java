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

public class ClassNameMappingStrategyTest extends TestCase
{
    private ClassNameMappingStrategy mappingStrategy;

    protected void setUp() throws Exception
    {
        super.setUp();
        this.mappingStrategy = new ClassNameMappingStrategy();
    }

    public void testDetermineScript()
    {
        AGIScript scriptFirstPass;
        AGIScript scriptSecondPass;
        AGIRequest request;

        request = new SimpleAGIRequest() {
            public String getScript()
            {
                return "org.asteriskjava.fastagi.HelloAGIScript";
            }
        };

        scriptFirstPass = mappingStrategy.determineScript(request);
        scriptSecondPass = mappingStrategy.determineScript(request);

        assertEquals("incorrect script determined",
                scriptFirstPass.getClass(), HelloAGIScript.class);

        assertTrue("script instances are not cached",
                scriptFirstPass == scriptSecondPass);
    }
}

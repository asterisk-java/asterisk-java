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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassNameMappingStrategyTest {
    private ClassNameMappingStrategy mappingStrategy;

    @BeforeEach
    void setUp() {
        this.mappingStrategy = new ClassNameMappingStrategy();
    }

    @Test
    void testDetermineScript() {
        AgiScript scriptFirstPass;
        AgiScript scriptSecondPass;
        AgiRequest request;

        request = new SimpleAgiRequest() {
            @Override
            public String getScript() {
                return "org.asteriskjava.fastagi.HelloAgiScript";
            }
        };

        scriptFirstPass = mappingStrategy.determineScript(request);
        scriptSecondPass = mappingStrategy.determineScript(request);

        assertEquals(scriptFirstPass.getClass(), HelloAgiScript.class, "incorrect script determined");

        assertTrue(scriptFirstPass == scriptSecondPass, "script instances are not cached");
    }

    @Test
    void testDetermineScriptWithNonSharedInstance() {
        AgiScript scriptFirstPass;
        AgiScript scriptSecondPass;
        AgiRequest request;

        mappingStrategy.setShareInstances(false);
        request = new SimpleAgiRequest() {
            @Override
            public String getScript() {
                return "org.asteriskjava.fastagi.HelloAgiScript";
            }
        };

        scriptFirstPass = mappingStrategy.determineScript(request);
        scriptSecondPass = mappingStrategy.determineScript(request);

        assertEquals(scriptFirstPass.getClass(), HelloAgiScript.class, "incorrect script determined");

        assertTrue(scriptFirstPass != scriptSecondPass, "returned a shared instance");
    }
}

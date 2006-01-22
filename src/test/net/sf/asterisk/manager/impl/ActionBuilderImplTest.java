/*
 * Copyright  2004-2005 Stefan Reuter
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
package net.sf.asterisk.manager.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.asterisk.AsteriskVersion;
import net.sf.asterisk.manager.ActionBuilder;
import net.sf.asterisk.manager.action.AbstractManagerAction;
import net.sf.asterisk.manager.action.AgentsAction;
import net.sf.asterisk.manager.action.OriginateAction;

public class ActionBuilderImplTest extends TestCase
{
    private ActionBuilder actionBuilder;

    public void setUp()
    {
        this.actionBuilder = new ActionBuilderImpl();
    }

    public void testBuildAction()
    {
        MyAction myAction;
        String actual;

        myAction = new MyAction();
        myAction.setFirstProperty("first value");
        myAction.setSecondProperty(new Integer(2));
        myAction.setNonPublicProperty("private");

        actual = actionBuilder.buildAction(myAction);

        assertTrue("Action name missing", actual.indexOf("action: My\r\n") >= 0);
        assertTrue("First property missing", actual
                .indexOf("firstproperty: first value\r\n") >= 0);
        assertTrue("Second property missing", actual
                .indexOf("secondproperty: 2\r\n") >= 0);
        assertTrue("Missing trailing CRNL CRNL", actual.endsWith("\r\n\r\n"));
        assertEquals("Incorrect length", 61, actual.length());
    }

    public void testBuildActionWithNullValue()
    {
        MyAction myAction;
        String actual;

        myAction = new MyAction();
        myAction.setFirstProperty("first value");

        actual = actionBuilder.buildAction(myAction);

        assertTrue("Action name missing", actual.indexOf("action: My\r\n") >= 0);
        assertTrue("First property missing", actual
                .indexOf("firstproperty: first value\r\n") >= 0);
        assertTrue("Missing trailing CRNL CRNL", actual.endsWith("\r\n\r\n"));
        assertEquals("Incorrect length", 42, actual.length());
    }

    public void testBuildEventGeneratingAction()
    {
        AgentsAction action;
        String actual;

        action = new AgentsAction();

        actual = actionBuilder.buildAction(action);

        assertTrue("Action name missing",
                actual.indexOf("action: Agents\r\n") >= 0);
        assertTrue("Action contains actionCompleteEventClass property", actual
                .indexOf("actioncompleteeventclass:") == -1);
        assertTrue("Missing trailing CRNL CRNL", actual.endsWith("\r\n\r\n"));
    }

    public void testBuildActionWithVariablesForAsterisk10()
    {
        OriginateAction originateAction;
        String actual;

        originateAction = new OriginateAction();
        originateAction.setVariable("var1=value1|var2=value2");

        actual = actionBuilder.buildAction(originateAction);

        assertTrue("Incorrect mapping of variable property for Asterisk 1.0",
                actual.indexOf("variable: var1=value1|var2=value2\r\n") >= 0);
    }

    public void testBuildActionWithVariablesForAsterisk10WithNullValues()
    {
        OriginateAction originateAction;
        String actual;

        originateAction = new OriginateAction();
        originateAction.setVariable("var1=value1|var2=|var3=value3");

        actual = actionBuilder.buildAction(originateAction);

        assertTrue(
                "Incorrect mapping of variable property for Asterisk 1.0",
                actual.indexOf("variable: var1=value1|var2=|var3=value3\r\n") >= 0);
    }

    public void testBuildActionWithVariablesForAsterisk12()
    {
        OriginateAction originateAction;
        String actual;

        originateAction = new OriginateAction();
        originateAction.setVariable("var1=value1|var2=value2");

        actionBuilder.setTargetVersion(AsteriskVersion.ASTERISK_1_2);
        actual = actionBuilder.buildAction(originateAction);

        assertTrue(
                "Incorrect mapping of variable property for Asterisk 1.2",
                actual
                        .indexOf("variable: var1=value1\r\nvariable: var2=value2\r\n") >= 0);
    }

    public void testBuildActionWithVariablesForAsterisk12WithNullValues()
    {
        OriginateAction originateAction;
        String actual;

        originateAction = new OriginateAction();
        originateAction.setVariable("var1=value1|var2=|var3=value3");

        actionBuilder.setTargetVersion(AsteriskVersion.ASTERISK_1_2);
        actual = actionBuilder.buildAction(originateAction);

        assertTrue(
                "Incorrect mapping of variable property for Asterisk 1.2",
                actual
                        .indexOf("variable: var1=value1\r\nvariable: var2=\r\nvariable: var3=value3\r\n") >= 0);
    }

    public void testBuildActionWithVariableMapForAsterisk12()
    {
        OriginateAction originateAction;
        Map map;
        String actual;

        originateAction = new OriginateAction();

        map = new LinkedHashMap();
        map.put("var1", "value1");
        map.put("VAR2", "value2");

        originateAction.setVariables(map);

        actionBuilder.setTargetVersion(AsteriskVersion.ASTERISK_1_2);
        actual = actionBuilder.buildAction(originateAction);

        assertTrue(
                "Incorrect mapping of variable property for Asterisk 1.2",
                actual.indexOf("variable: var1=value1\r\nvariable: VAR2=value2\r\n") >= 0);
    }

    class MyAction extends AbstractManagerAction
    {
        private static final long serialVersionUID = 3257568425345102641L;
        private String firstProperty;
        private Integer secondProperty;
        private String nonPublicProperty;

        public String getAction()
        {
            return "My";
        }

        public String getFirstProperty()
        {
            return firstProperty;
        }

        public void setFirstProperty(String firstProperty)
        {
            this.firstProperty = firstProperty;
        }

        public Integer getSecondProperty()
        {
            return secondProperty;
        }

        public void setSecondProperty(Integer secondProperty)
        {
            this.secondProperty = secondProperty;
        }

        protected String getNonPublicProperty()
        {
            return nonPublicProperty;
        }

        protected void setNonPublicProperty(String privateProperty)
        {
            this.nonPublicProperty = privateProperty;
        }

        public String get()
        {
            return "This method must not be considered a getter";
        }

        public String getIndexedProperty(int i)
        {
            return "This method must not be considered a getter relevant for building the action";
        }
    }
}

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
package org.asteriskjava.manager.internal;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.ami.action.api.AbstractManagerAction;
import org.asteriskjava.manager.action.AgentsAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.SipNotifyAction;
import org.asteriskjava.manager.action.UserEventAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionBuilderImplTest {
    private ActionBuilderImpl actionBuilder;

    @BeforeEach
    void setUp() {
        this.actionBuilder = new ActionBuilderImpl();
    }

    @Test
    void testBuildAction() {
        MyAction myAction;
        String actual;

        myAction = new MyAction();
        myAction.setActionId("id-1");
        myAction.setFirstProperty("first value");
        myAction.setSecondProperty(2);
        myAction.setNonPublicProperty("private");

        actual = actionBuilder.buildAction(myAction);

        assertTrue(actual.indexOf("action: My\r\n") >= 0, "Action name missing");
        assertTrue(actual.indexOf("firstproperty: first value\r\n") >= 0, "First property missing");
        assertTrue(actual.indexOf("secondproperty: 2\r\n") >= 0, "Second property missing");
        assertTrue(actual.endsWith("\r\n\r\n"), "Missing trailing CRNL CRNL");
        assertEquals(77, actual.length(), "Incorrect length");
    }

    @Test
    void testBuildActionWithNullValue() {
        MyAction myAction;
        String actual;

        myAction = new MyAction();
        myAction.setActionId("id-1");
        myAction.setFirstProperty("first value");

        actual = actionBuilder.buildAction(myAction);

        assertTrue(actual.indexOf("action: My\r\n") >= 0, "Action name missing");
        assertTrue(actual.indexOf("firstproperty: first value\r\n") >= 0, "First property missing");
        assertTrue(actual.endsWith("\r\n\r\n"), "Missing trailing CRNL CRNL");
        assertEquals(58, actual.length(), "Incorrect length");
    }

    @Test
    void testBuildEventGeneratingAction() {
        AgentsAction action;
        String actual;

        action = new AgentsAction();

        actual = actionBuilder.buildAction(action);

        assertTrue(actual.indexOf("action: Agents\r\n") >= 0, "Action name missing");
        assertTrue(actual.indexOf("actioncompleteeventclass:") == -1, "Action contains actionCompleteEventClass property");
        assertTrue(actual.endsWith("\r\n\r\n"), "Missing trailing CRNL CRNL");
    }

    @Test
    void testBuildUserEventAction() {
        UserEventAction action;
        action = new UserEventAction();

        MyUserEvent event;
        event = new MyUserEvent(this);
        action.setUserEvent(event);

        Map<String, String> mapMemberTest = new LinkedHashMap<String, String>();
        mapMemberTest.put("Key1", "Value1");
        mapMemberTest.put("Key2", "Value2");
        mapMemberTest.put("Key3", "Value3");

        event.setStringMember("stringMemberValue");
        event.setMapMember(mapMemberTest);

        String actual = actionBuilder.buildAction(action);
        assertTrue(actual.indexOf("action: UserEvent\r\n") >= 0, "Action name missing");
        assertTrue(actual.indexOf("UserEvent: myuser\r\n") >= 0, "Event name missing");
        assertTrue(actual.indexOf("stringmember: stringMemberValue\r\n") >= 0, "Regular member missing");
        assertTrue(actual.indexOf("mapmember: Key1=Value1|Key2=Value2|Key3=Value3\r\n") >= 0, "Map member missing");
        assertTrue(actual.endsWith("\r\n\r\n"), "Missing trailing CRNL CRNL");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildActionWithVariablesForAsterisk10() {
        OriginateAction originateAction;
        String actual;

        originateAction = new OriginateAction();
        originateAction.setVariable("var1=value1|var2=value2");

        actual = actionBuilder.buildAction(originateAction);

        assertTrue(actual.indexOf("variable: var1=value1|var2=value2\r\n") >= 0, "Incorrect mapping of variable property for Asterisk 1.0");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildActionWithVariablesForAsterisk10WithNullValues() {
        OriginateAction originateAction;
        String actual;

        originateAction = new OriginateAction();
        originateAction.setVariable("var1=value1|var2=|var3=value3");

        actual = actionBuilder.buildAction(originateAction);

        assertTrue(actual.indexOf("variable: var1=value1|var2=|var3=value3\r\n") >= 0, "Incorrect mapping of variable property for Asterisk 1.0");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildActionWithVariablesForAsterisk12() {
        OriginateAction originateAction;
        String actual;

        originateAction = new OriginateAction();
        originateAction.setVariable("var1=value1|var2=value2");

        actionBuilder.setTargetVersion(AsteriskVersion.ASTERISK_1_2);
        actual = actionBuilder.buildAction(originateAction);

        assertTrue(actual.indexOf("variable: var1=value1\r\nvariable: var2=value2\r\n") >= 0, "Incorrect mapping of variable property for Asterisk 1.2");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildActionWithVariablesForAsterisk12WithNullValues() {
        OriginateAction originateAction;
        String actual;

        originateAction = new OriginateAction();
        originateAction.setVariable("var1=value1|var2=|var3=value3");

        actionBuilder.setTargetVersion(AsteriskVersion.ASTERISK_1_2);
        actual = actionBuilder.buildAction(originateAction);

        assertTrue(actual.indexOf("variable: var1=value1\r\nvariable: var2=\r\nvariable: var3=value3\r\n") >= 0, "Incorrect mapping of variable property for Asterisk 1.2");
    }

    @Test
    void testBuildActionWithVariableMapForAsterisk12() {
        OriginateAction originateAction;
        Map<String, String> map;
        String actual;

        originateAction = new OriginateAction();

        map = new LinkedHashMap<String, String>();
        map.put("var1", "value1");
        map.put("VAR2", "value2");

        originateAction.setVariables(map);

        actionBuilder.setTargetVersion(AsteriskVersion.ASTERISK_1_2);
        actual = actionBuilder.buildAction(originateAction);

        assertTrue(actual.indexOf("variable: var1=value1\r\nvariable: VAR2=value2\r\n") >= 0, "Incorrect mapping of variable property for Asterisk 1.2");
    }

    @Test
    void testBuildActionForSipNotifyAction() {
        SipNotifyAction action;
        String actual;

        action = new SipNotifyAction("peer");
        action.setVariable("var1", "value1");
        action.setVariable("var2", "value2");

        actionBuilder.setTargetVersion(AsteriskVersion.ASTERISK_1_6);
        actual = actionBuilder.buildAction(action);

        assertTrue(actual.indexOf("variable: var1=value1\r\nvariable: var2=value2\r\n") >= 0, "Incorrect mapping of variable property");
    }

    @Test
    void testBuildActionWithAnnotatedGetter() {
        AnnotatedAction action = new AnnotatedAction("value1", "value2", "value3");
        String actual = actionBuilder.buildAction(action);

        assertTrue(actual.indexOf("property-1: value1\r\n") >= 0, "Incorrect mapping of property with annotated getter");
    }

    @Test
    void testDetermineSetterName() {
        assertEquals("setProperty1", ActionBuilderImpl.determineSetterName("getProperty1"));
        assertEquals("setProperty1", ActionBuilderImpl.determineSetterName("isProperty1"));
    }

    @Test
    void testBuildActionWithAnnotatedSetter() {
        AnnotatedAction action = new AnnotatedAction("value1", "value2", "value3");
        String actual = actionBuilder.buildAction(action);

        assertTrue(actual.indexOf("property-2: value2\r\n") >= 0, "Incorrect mapping of property with annotated setter");
    }

    @Test
    void testDetermineFieldName() {
        assertEquals("property1", ActionBuilderImpl.determineFieldName("getProperty1"));
        assertEquals("property1", ActionBuilderImpl.determineFieldName("isProperty1"));
        assertEquals("property1", ActionBuilderImpl.determineFieldName("setProperty1"));
    }

    @Test
    void testBuildActionWithAnnotatedField() {
        AnnotatedAction action = new AnnotatedAction("value1", "value2", "value3");
        String actual = actionBuilder.buildAction(action);

        assertTrue(actual.indexOf("property-3: value3\r\n") >= 0, "Incorrect mapping of property with annotated field");
    }

    class MyAction extends AbstractManagerAction {
        private static final long serialVersionUID = 3257568425345102641L;
        private String firstProperty;
        private Integer secondProperty;
        private String nonPublicProperty;

        @Override
        public String getAction() {
            return "My";
        }

        public String getFirstProperty() {
            return firstProperty;
        }

        public void setFirstProperty(String firstProperty) {
            this.firstProperty = firstProperty;
        }

        public Integer getSecondProperty() {
            return secondProperty;
        }

        public void setSecondProperty(Integer secondProperty) {
            this.secondProperty = secondProperty;
        }

        protected String getNonPublicProperty() {
            return nonPublicProperty;
        }

        protected void setNonPublicProperty(String privateProperty) {
            this.nonPublicProperty = privateProperty;
        }

        public String get() {
            return "This method must not be considered a getter";
        }

        public String getIndexedProperty(int i) {
            return "This method must not be considered a getter relevant for building the action";
        }
    }
}

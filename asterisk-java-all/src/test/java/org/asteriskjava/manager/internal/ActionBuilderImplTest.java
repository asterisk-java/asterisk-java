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
import org.asteriskjava.ami.action.AbstractManagerAction;
import org.asteriskjava.ami.action.EventMask;
import org.asteriskjava.ami.action.LoginAction;
import org.asteriskjava.ami.databind.AsteriskObjectMapper;
import org.asteriskjava.manager.action.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.AsteriskVersion.ASTERISK_1_2;
import static org.asteriskjava.ami.action.EventMask.agent;
import static org.asteriskjava.ami.action.EventMask.agi;
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
        myAction.setFirstProperty("first value");
        myAction.setSecondProperty(2);
        myAction.setNonPublicProperty("private");

        actual = actionBuilder.buildAction(myAction);

        AsteriskObjectMapper asteriskObjectMapper = new AsteriskObjectMapper();
        String newActual = asteriskObjectMapper.writeValue(myAction);

        assertThat(actual).isEqualTo("action: My\r\n" +
            "firstproperty: first value\r\n" +
            "secondproperty: 2\r\n\r\n");

        String expected = "Action: My\r\n" +
            "FirstProperty: first value\r\n" +
            "SecondProperty: 2\r\n\r\n";
        assertThat(newActual).isEqualTo(expected);

        assertTrue(actual.indexOf("action: My\r\n") >= 0, "Action name missing");
        assertTrue(actual.indexOf("firstproperty: first value\r\n") >= 0, "First property missing");
        assertTrue(actual.indexOf("secondproperty: 2\r\n") >= 0, "Second property missing");
        assertTrue(actual.endsWith("\r\n\r\n"), "Missing trailing CRNL CRNL");
        assertEquals(61, actual.length(), "Incorrect length");
    }

    @Test
    void testBuildActionWithNullValue() {
        MyAction myAction;
        String actual;

        myAction = new MyAction();
        myAction.setFirstProperty("first value");

        actual = actionBuilder.buildAction(myAction);

        assertThat(actual).isEqualTo("action: My\r\n" +
            "firstproperty: first value\r\n\r\n");

        assertTrue(actual.indexOf("action: My\r\n") >= 0, "Action name missing");
        assertTrue(actual.indexOf("firstproperty: first value\r\n") >= 0, "First property missing");
        assertTrue(actual.endsWith("\r\n\r\n"), "Missing trailing CRNL CRNL");
        assertEquals(42, actual.length(), "Incorrect length");
    }

    @Test
    void testBuildEventGeneratingAction() {
        AgentsAction action;
        String actual;

        action = new AgentsAction();

        actual = actionBuilder.buildAction(action);

        assertThat(actual).isEqualTo("action: Agents\r\n\r\n");

        assertThat(actual).contains("action: Agents\r\n");
        assertThat(actual).doesNotContain("actioncompleteeventclass: ");
        assertThat(actual).endsWith("\r\n\r\n");
    }

    @Test
    void testBuildUpdateConfigAction() {
        UpdateConfigAction action;
        action = new UpdateConfigAction();
        action.setSrcFilename("sourcefile.conf");
        action.setDstFilename("destfile.conf");
        action.setReload(true);
        action.addCommand(UpdateConfigAction.ACTION_NEWCAT, "testcategory", null, null, null);

        String actual = actionBuilder.buildAction(action);

        assertThat(actual).isEqualTo("action: UpdateConfig\r\n" +
            "srcfilename: sourcefile.conf\r\n" +
            "reload: Yes\r\n" +
            "dstfilename: destfile.conf\r\n" +
            "Action-000000: NewCat\r\n" +
            "Cat-000000: testcategory\r\n\r\n");

        assertThat(actual).contains("action: UpdateConfig\r\n");
        assertThat(actual).contains("srcfilename: sourcefile.conf\r\n");
        assertThat(actual).contains("dstfilename: destfile.conf\r\n");
        assertThat(actual).contains("reload: Yes\r\n");
        assertThat(actual).doesNotContain("Action-0:");
        assertThat(actual).doesNotContain("action: Action");
        assertThat(actual).contains("Cat-000000: testcategory");
    }

    @Test
    void testBuildUserEventAction() {
        UserEventAction action;
        action = new UserEventAction();

        MyUserEvent event;
        event = new MyUserEvent(this);
        action.setUserEvent(event);

        Map<String, String> mapMemberTest = new LinkedHashMap<>();
        mapMemberTest.put("Key1", "Value1");
        mapMemberTest.put("Key2", "Value2");
        mapMemberTest.put("Key3", "Value3");

        event.setStringMember("stringMemberValue");
        event.setMapMember(mapMemberTest);

        String actual = actionBuilder.buildAction(action);

        assertThat(actual).isEqualTo("action: UserEvent\r\n" +
            "UserEvent: myuser\r\n" +
            "mapmember: Key1=Value1|Key2=Value2|Key3=Value3\r\n" +
            "source: org.asteriskjava.manager.internal.ActionBuilderImplTest@313b2ea6\r\n" +
            "stringmember: stringMemberValue\r\n\r\n");

        assertThat(actual).contains("action: UserEvent\r\n");
        assertThat(actual).contains("UserEvent: myuser\r\n");
        assertThat(actual).contains("stringmember: stringMemberValue\r\n");
        assertThat(actual).contains("mapmember: Key1=Value1|Key2=Value2|Key3=Value3\r\n");
        assertThat(actual).endsWith("\r\n\r\n");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildActionWithVariablesForAsterisk10() {
        OriginateAction originateAction;
        String actual;

        originateAction = new OriginateAction();
        originateAction.setVariable("var1=value1|var2=value2");

        actual = actionBuilder.buildAction(originateAction);

        assertThat(actual).isEqualTo("action: Originate\r\n" +
            "variable: var1=value1|var2=value2\r\n\r\n");

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

        assertThat(actual).isEqualTo("action: Originate\r\n" +
            "variable: var1=value1|var2=|var3=value3\r\n\r\n");

        assertTrue(actual.indexOf("variable: var1=value1|var2=|var3=value3\r\n") >= 0, "Incorrect mapping of variable property for Asterisk 1.0");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildActionWithVariablesForAsterisk12() {
        OriginateAction originateAction;
        String actual;

        originateAction = new OriginateAction();
        originateAction.setVariable("var1=value1|var2=value2");

        actionBuilder.setTargetVersion(ASTERISK_1_2);
        actual = actionBuilder.buildAction(originateAction);

        assertThat(actual).isEqualTo("action: Originate\r\n" +
            "variable: var1=value1\r\n" +
            "variable: var2=value2\r\n\r\n");

        assertTrue(actual.indexOf("variable: var1=value1\r\nvariable: var2=value2\r\n") >= 0, "Incorrect mapping of variable property for Asterisk 1.2");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testBuildActionWithVariablesForAsterisk12WithNullValues() {
        OriginateAction originateAction;
        String actual;

        originateAction = new OriginateAction();
        originateAction.setVariable("var1=value1|var2=|var3=value3");

        actionBuilder.setTargetVersion(ASTERISK_1_2);
        actual = actionBuilder.buildAction(originateAction);

        assertThat(actual).isEqualTo("action: Originate\r\n" +
            "variable: var1=value1\r\n" +
            "variable: var2=\r\n" +
            "variable: var3=value3\r\n\r\n");

        assertTrue(actual.indexOf("variable: var1=value1\r\nvariable: var2=\r\nvariable: var3=value3\r\n") >= 0, "Incorrect mapping of variable property for Asterisk 1.2");
    }

    @Test
    void testBuildActionWithVariableMapForAsterisk12() {
        OriginateAction originateAction;
        Map<String, String> map;
        String actual;

        originateAction = new OriginateAction();

        map = new LinkedHashMap<>();
        map.put("var1", "value1");
        map.put("VAR2", "value2");

        originateAction.setVariables(map);

        actionBuilder.setTargetVersion(ASTERISK_1_2);
        actual = actionBuilder.buildAction(originateAction);

        assertThat(actual).isEqualTo("action: Originate\r\n" +
            "variable: var1=value1\r\n" +
            "variable: VAR2=value2\r\n\r\n");

        assertThat(actual).contains("variable: var1=value1\r\n");
        assertThat(actual).contains("variable: VAR2=value2\r\n");
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

        assertThat(actual).isEqualTo("action: SipNotify\r\n" +
            "variable: var1=value1\r\n" +
            "variable: var2=value2\r\n" +
            "channel: peer\r\n\r\n");

        assertThat(actual).contains("variable: var1=value1\r\n");
        assertThat(actual).contains("variable: var2=value2\r\n");
    }

    @Test
    void testBuildActionWithAnnotatedGetter() {
        AnnotatedAction action = new AnnotatedAction("value1", "value2", "value3");
        String actual = actionBuilder.buildAction(action);

        assertThat(actual).isEqualTo("action: Custom\r\n" +
            "property-2: value2\r\n" +
            "property-1: value1\r\n" +
            "property-3: value3\r\n\r\n");

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

        assertThat(actual).isEqualTo("action: Custom\r\n" +
            "property-2: value2\r\n" +
            "property-1: value1\r\n" +
            "property-3: value3\r\n\r\n");

        assertThat(actual).contains("property-2: value2\r\n");
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

        assertThat(actual).isEqualTo("action: Custom\r\n" +
            "property-2: value2\r\n" +
            "property-1: value1\r\n" +
            "property-3: value3\r\n\r\n");

        assertTrue(actual.indexOf("property-3: value3\r\n") >= 0, "Incorrect mapping of property with annotated field");
    }

    @Test
    void shouldUseSerializer() {
        //given
        EnumSet<EventMask> events = EnumSet.of(agent, agi);

        LoginAction action = new LoginAction();
        action.setEvents(events);

        //when
        String actual = actionBuilder.buildAction(action);

        //then
        assertThat(actual).isEqualTo("action: Login\r\n" +
            "events: agent,agi\r\n\r\n");
    }

    @Test
    void shouldMapValue() {
        //given
        SipNotifyAction action = new SipNotifyAction("channel");
        action.setVariable("v1", "1");
        action.setVariable("v2", "2");

        actionBuilder.setTargetVersion(ASTERISK_1_2);

        //when
        String actual = actionBuilder.buildAction(action);

        //then
        assertThat(actual).isEqualTo("action: SipNotify\r\n" +
            "variable: v1=1\r\n" +
            "variable: v2=2\r\n" +
            "channel: channel\r\n\r\n");
    }

    @Test
    void shouldHandleMultilineContent() {
        //given
        SipNotifyAction action = new SipNotifyAction("channel");
        action.setVariable("Content", "value1\nvalue2\nvalue3");

        actionBuilder.setTargetVersion(ASTERISK_1_2);

        //when
        String actual = actionBuilder.buildAction(action);

        //then
        assertThat(actual).isEqualTo("action: SipNotify\r\n" +
            "variable: Content=value1\r\n" +
            "variable: Content=value2\r\n" +
            "variable: Content=value3\r\n" +
            "channel: channel\r\n\r\n");
    }

    @Test
    void shouldHandleInternalActionId() {
        //given
        AnnotatedAction action = new AnnotatedAction("value1", "value2", "value3");

        //when
        String actual = actionBuilder.buildAction(action, "internalId-1");

        //then
        assertThat(actual).isEqualTo("action: Custom\r\n" +
            "actionid: internalId-1#\r\n" +
            "property-2: value2\r\n" +
            "property-1: value1\r\n" +
            "property-3: value3\r\n\r\n");
    }

    @Test
    void shouldUseActionId() {
        //given
        AnnotatedAction action = new AnnotatedAction("value1", "value2", "value3");
        action.setActionId("actionId-1");

        //when
        String actual = actionBuilder.buildAction(action);

        //then
        assertThat(actual).isEqualTo("action: Custom\r\n" +
            "actionid: actionId-1\r\n" +
            "property-2: value2\r\n" +
            "property-1: value1\r\n" +
            "property-3: value3\r\n\r\n");
    }

    static class MyAction extends AbstractManagerAction {
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

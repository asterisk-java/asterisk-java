package org.asteriskjava.ami.databind;

import org.asteriskjava.ami.action.ChallengeAction;
import org.asteriskjava.ami.action.LoginAction;
import org.asteriskjava.ami.databind.serializer.AsteriskSerializers;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.asteriskjava.ami.action.AuthType.MD5;
import static org.asteriskjava.ami.action.EventMask.message;
import static org.asteriskjava.ami.action.EventMask.originate;
import static org.asteriskjava.core.NewlineDelimiter.CRLF;

class AsteriskObjectMapperTest {
    private final AsteriskObjectMapper asteriskObjectMapper = new AsteriskObjectMapper(new AsteriskGenerator(CRLF), new AsteriskSerializers());

    @Test
    void shouldGenerateSimpleAction() {
        //given
        ChallengeAction challengeAction = new ChallengeAction();
        challengeAction.setActionId("id-1");
        challengeAction.setAuthType(MD5);

        //when
        String challengeActionString = asteriskObjectMapper.writeValue(challengeAction);

        //then
        String expected = "Action: Challenge" + CRLF.getPattern();
        expected += "ActionID: id-1" + CRLF.getPattern();
        expected += "AuthType: MD5" + CRLF.getPattern();
        assertThat(challengeActionString).isEqualTo(expected);
    }

    @Test
    void shouldGenerateActionWithSerializer() {
        //given
        LoginAction loginAction = new LoginAction("jon", "secret");
        loginAction.setActionId("id-1");
        loginAction.setEvents(EnumSet.of(message, originate));

        //when
        String loginActionString = asteriskObjectMapper.writeValue(loginAction);

        //then
        String expected = "Action: Login" + CRLF.getPattern();
        expected += "ActionID: id-1" + CRLF.getPattern();
        expected += "Username: jon" + CRLF.getPattern();
        expected += "Secret: secret" + CRLF.getPattern();
        expected += "Events: originate,message" + CRLF.getPattern();
        assertThat(loginActionString).isEqualTo(expected);
    }

    @Test
    void shouldGenerateActionWithMap() {
        //given
        LoginAction loginAction = new LoginAction("jon", "secret");
        loginAction.setActionId("id-1");
        loginAction.setEvents(EnumSet.of(message, originate));

        //when
        String loginActionString = asteriskObjectMapper.writeValue(loginAction);

        //then
        String expected = "Action: Login" + CRLF.getPattern();
        expected += "ActionID: id-1" + CRLF.getPattern();
        expected += "Username: jon" + CRLF.getPattern();
        expected += "Secret: secret" + CRLF.getPattern();
        expected += "Events: originate,message" + CRLF.getPattern();
        assertThat(loginActionString).isEqualTo(expected);
    }
}

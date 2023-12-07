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

import org.asteriskjava.ami.action.response.ChallengeResponse;
import org.asteriskjava.ami.action.response.ManagerResponse;
import org.asteriskjava.ami.action.response.ResponseType;
import org.asteriskjava.manager.response.ExtensionStateResponse;
import org.asteriskjava.manager.response.MailboxCountResponse;
import org.asteriskjava.manager.response.MailboxStatusResponse;
import org.asteriskjava.manager.response.ManagerError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseBuilderImplTest {
    private ResponseBuilderImpl responseBuilder;
    private Map<String, Object> attributes;

    @BeforeEach
    void setUp() {
        this.responseBuilder = new ResponseBuilderImpl();
        this.attributes = new HashMap<String, Object>();
    }

    @Test
    void testBuildResponse() {
        ManagerResponse response;

        attributes.put("response", "Success");

        response = responseBuilder.buildResponse(ManagerResponse.class, attributes);
        assertEquals(ManagerResponse.class, response.getClass(), "Response of wrong type");
        assertEquals(ResponseType.Success, response.getResponse(), "Response not set correctly");
    }

    @Test
    void testBuildResponseWithoutResponseClass() {
        ManagerResponse response;

        attributes.put("response", "Success");

        response = responseBuilder.buildResponse(null, attributes);
        assertEquals(ManagerResponse.class, response.getClass(), "Response of wrong type");
        assertEquals(ResponseType.Success, response.getResponse(), "Response not set correctly");
    }

    @Test
    void testBuildError() {
        ManagerResponse response;

        attributes.put("response", "Error");
        attributes.put("message", "Missing action in request");

        response = responseBuilder.buildResponse(ManagerResponse.class, attributes);
        assertEquals(ManagerError.class, response.getClass(), "Response of wrong type");
        assertEquals("Missing action in request", response.getMessage(), "Message not set correctly");
    }

    @Test
    void testBuildErrorWithActionId() {
        ManagerResponse response;

        attributes.put("response", "Error");
        attributes.put("actionid", "1234");
        attributes.put("message", "Missing action in request");

        response = responseBuilder.buildResponse(ManagerResponse.class, attributes);
        assertEquals("1234", response.getActionId(), "ActionId not set correctly");
    }

    @Test
    void testBuildChallengeResponse() {
        ManagerResponse response;

        attributes.put("response", "Success");
        attributes.put("challenge", "131494410");

        response = responseBuilder.buildResponse(ChallengeResponse.class, attributes);
        assertEquals(ChallengeResponse.class, response.getClass(), "Response of wrong type");
        assertEquals("131494410", ((ChallengeResponse) response).getChallenge(), "Challenge not set correctly");
    }

    @Test
    void testBuildMailboxStatusResponse() {
        ManagerResponse response;

        attributes.put("response", "Success");
        attributes.put("message", "Mailbox Status");
        attributes.put("mailbox", "123");
        attributes.put("waiting", "1");

        response = responseBuilder.buildResponse(MailboxStatusResponse.class, attributes);
        assertEquals(MailboxStatusResponse.class, response.getClass(), "Response of wrong type");

        MailboxStatusResponse mailboxStatusResponse = (MailboxStatusResponse) response;
        assertEquals("123", mailboxStatusResponse.getMailbox(), "Mailbox not set correctly");
        assertEquals(Boolean.TRUE, mailboxStatusResponse.getWaiting(), "Waiting not set correctly");
    }

    @Test
    void testBuildMailboxStatusResponseWithNoWaiting() {
        ManagerResponse response;

        attributes.put("response", "Success");
        attributes.put("message", "Mailbox Status");
        attributes.put("mailbox", "123,user2");
        attributes.put("waiting", "0");

        response = responseBuilder.buildResponse(MailboxStatusResponse.class, attributes);
        assertEquals(MailboxStatusResponse.class, response.getClass(), "Response of wrong type");

        MailboxStatusResponse mailboxStatusResponse = (MailboxStatusResponse) response;
        assertEquals("123,user2", mailboxStatusResponse.getMailbox(), "Mailbox not set correctly");
        assertEquals(Boolean.FALSE, mailboxStatusResponse.getWaiting(), "Waiting not set correctly");
    }

    @Test
    void testBuildMailboxCountResponse() {
        ManagerResponse response;

        attributes.put("response", "Success");
        attributes.put("message", "Mailbox Message Count");
        attributes.put("mailbox", "123@myctx");
        attributes.put("newmessages", "2");
        attributes.put("oldmessages", "5");

        response = responseBuilder.buildResponse(MailboxCountResponse.class, attributes);
        assertEquals(MailboxCountResponse.class, response.getClass(), "Response of wrong type");

        MailboxCountResponse mailboxCountResponse = (MailboxCountResponse) response;
        assertEquals("123@myctx", mailboxCountResponse.getMailbox(), "Mailbox not set correctly");
        assertEquals(Integer.valueOf(2), mailboxCountResponse.getNewMessages(), "New messages not set correctly");
        assertEquals(Integer.valueOf(5), mailboxCountResponse.getOldMessages(), "Old messages set correctly");
    }

    @Test
    void testBuildExtensionStateResponse() {
        ManagerResponse response;

        attributes.put("response", "Success");
        attributes.put("message", "Extension Status");
        attributes.put("exten", "1");
        attributes.put("context", "default");
        attributes.put("hint", "");
        attributes.put("status", "-1");

        response = responseBuilder.buildResponse(ExtensionStateResponse.class, attributes);
        assertEquals(ExtensionStateResponse.class, response.getClass(), "Response of wrong type");

        ExtensionStateResponse extensionStateResponse = (ExtensionStateResponse) response;
        assertEquals("1", extensionStateResponse.getExten(), "Exten not set correctly");
        assertEquals("default", extensionStateResponse.getContext(), "Context not set correctly");
        assertEquals("", extensionStateResponse.getHint(), "Hint not set correctly");
        assertEquals(Integer.valueOf(-1), extensionStateResponse.getStatus(), "Status not set correctly");
    }
}

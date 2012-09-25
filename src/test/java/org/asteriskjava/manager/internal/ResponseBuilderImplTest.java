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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.response.ChallengeResponse;
import org.asteriskjava.manager.response.ExtensionStateResponse;
import org.asteriskjava.manager.response.MailboxCountResponse;
import org.asteriskjava.manager.response.MailboxStatusResponse;
import org.asteriskjava.manager.response.ManagerError;
import org.asteriskjava.manager.response.ManagerResponse;
import org.junit.Before;
import org.junit.Test;

public class ResponseBuilderImplTest
{
    private ResponseBuilderImpl responseBuilder;
    private Map<String, Object> attributes;

    @Before
    public void setUp()
    {
        this.responseBuilder = new ResponseBuilderImpl();
        this.attributes = new HashMap<String, Object>();
    }

    @Test
    public void testBuildResponse()
    {
        ManagerResponse response;

        attributes.put("response", "Success");

        response = responseBuilder.buildResponse(ManagerResponse.class, attributes);
        assertEquals("Response of wrong type", ManagerResponse.class, response.getClass());
        assertEquals("Response not set correctly", "Success", response.getResponse());
    }

    @Test
    public void testBuildResponseWithoutResponseClass()
    {
        ManagerResponse response;

        attributes.put("response", "Success");

        response = responseBuilder.buildResponse(null, attributes);
        assertEquals("Response of wrong type", ManagerResponse.class, response.getClass());
        assertEquals("Response not set correctly", "Success", response.getResponse());
    }

    @Test
    public void testBuildError()
    {
        ManagerResponse response;

        attributes.put("response", "Error");
        attributes.put("message", "Missing action in request");

        response = responseBuilder.buildResponse(ManagerResponse.class, attributes);
        assertEquals("Response of wrong type", ManagerError.class, response.getClass());
        assertEquals("Message not set correctly", "Missing action in request", response.getMessage());
    }

    @Test
    public void testBuildErrorWithActionId()
    {
        ManagerResponse response;

        attributes.put("response", "Error");
        attributes.put("actionid", "1234");
        attributes.put("message", "Missing action in request");

        response = responseBuilder.buildResponse(ManagerResponse.class, attributes);
        assertEquals("ActionId not set correctly", "1234", response.getActionId());
    }

    @Test
    public void testBuildChallengeResponse()
    {
        ManagerResponse response;

        attributes.put("response", "Success");
        attributes.put("challenge", "131494410");

        response = responseBuilder.buildResponse(ChallengeResponse.class, attributes);
        assertEquals("Response of wrong type", ChallengeResponse.class, response.getClass());
        assertEquals("Challenge not set correctly", "131494410", ((ChallengeResponse) response).getChallenge());
    }

    @Test
    public void testBuildMailboxStatusResponse()
    {
        ManagerResponse response;

        attributes.put("response", "Success");
        attributes.put("message", "Mailbox Status");
        attributes.put("mailbox", "123");
        attributes.put("waiting", "1");

        response = responseBuilder.buildResponse(MailboxStatusResponse.class, attributes);
        assertEquals("Response of wrong type", MailboxStatusResponse.class, response.getClass());

        MailboxStatusResponse mailboxStatusResponse = (MailboxStatusResponse) response;
        assertEquals("Mailbox not set correctly", "123", mailboxStatusResponse.getMailbox());
        assertEquals("Waiting not set correctly", Boolean.TRUE, mailboxStatusResponse.getWaiting());
    }

    @Test
    public void testBuildMailboxStatusResponseWithNoWaiting()
    {
        ManagerResponse response;

        attributes.put("response", "Success");
        attributes.put("message", "Mailbox Status");
        attributes.put("mailbox", "123,user2");
        attributes.put("waiting", "0");

        response = responseBuilder.buildResponse(MailboxStatusResponse.class, attributes);
        assertEquals("Response of wrong type", MailboxStatusResponse.class, response.getClass());

        MailboxStatusResponse mailboxStatusResponse = (MailboxStatusResponse) response;
        assertEquals("Mailbox not set correctly", "123,user2", mailboxStatusResponse.getMailbox());
        assertEquals("Waiting not set correctly", Boolean.FALSE, mailboxStatusResponse.getWaiting());
    }

    @Test
    public void testBuildMailboxCountResponse()
    {
        ManagerResponse response;

        attributes.put("response", "Success");
        attributes.put("message", "Mailbox Message Count");
        attributes.put("mailbox", "123@myctx");
        attributes.put("newmessages", "2");
        attributes.put("oldmessages", "5");

        response = responseBuilder.buildResponse(MailboxCountResponse.class, attributes);
        assertEquals("Response of wrong type", MailboxCountResponse.class, response.getClass());

        MailboxCountResponse mailboxCountResponse = (MailboxCountResponse) response;
        assertEquals("Mailbox not set correctly", "123@myctx", mailboxCountResponse.getMailbox());
        assertEquals("New messages not set correctly", Integer.valueOf(2), mailboxCountResponse.getNewMessages());
        assertEquals("Old messages set correctly", Integer.valueOf(5), mailboxCountResponse.getOldMessages());
    }

    @Test
    public void testBuildExtensionStateResponse()
    {
        ManagerResponse response;

        attributes.put("response", "Success");
        attributes.put("message", "Extension Status");
        attributes.put("exten", "1");
        attributes.put("context", "default");
        attributes.put("hint", "");
        attributes.put("status", "-1");

        response = responseBuilder.buildResponse(ExtensionStateResponse.class, attributes);
        assertEquals("Response of wrong type", ExtensionStateResponse.class, response.getClass());

        ExtensionStateResponse extensionStateResponse = (ExtensionStateResponse) response;
        assertEquals("Exten not set correctly", "1", extensionStateResponse.getExten());
        assertEquals("Context not set correctly", "default", extensionStateResponse.getContext());
        assertEquals("Hint not set correctly", "", extensionStateResponse.getHint());
        assertEquals("Status not set correctly", Integer.valueOf(-1), extensionStateResponse.getStatus());
    }
}

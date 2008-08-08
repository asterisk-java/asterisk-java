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

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.asteriskjava.manager.response.*;


/**
 * Default implementation of the ResponseBuilder interface.
 * 
 * @see org.asteriskjava.manager.response.ManagerResponse
 * @author srt
 * @version $Id$
 */
class ResponseBuilderImpl implements ResponseBuilder
{
    /**
     * Constructs an instance of ManagerResponse based on a map of attributes.
     * 
     * @param attributes the attributes and their values. The keys of this map must be all lower
     * case.
     * @return the response with the given attributes.
     */
    public ManagerResponse buildResponse(final Map<String, String> attributes)
    {
        ManagerResponse response;
        String responseType;
        String proxyResponseType;
        
        responseType = attributes.get("response");
        proxyResponseType = attributes.get("proxyresponse");

        // determine type
        if ("error".equalsIgnoreCase(responseType))
        {
            response = new ManagerError();
        }
        else if (attributes.containsKey("challenge"))
        {
            final ChallengeResponse challengeResponse = new ChallengeResponse();
            challengeResponse.setChallenge(attributes.get("challenge"));
            response = challengeResponse;
        }
        else if ("Follows".equals(responseType) && attributes.containsKey("result"))
        {
            final CommandResponse commandResponse = new CommandResponse();

            List<String> result = new ArrayList<String>();
            for (String resultLine : attributes.get("result").split("\n"))
            {
                // on error there is a leading space
                if (!resultLine.equals("--END COMMAND--") && !resultLine.equals(" --END COMMAND--"))
                {
                    //logger.info("Adding '" + resultLine + "'");
                    result.add(resultLine);
                }
            }
            commandResponse.setResult(result);
            response = commandResponse;
        }
        else if (attributes.containsKey("mailbox") && attributes.containsKey("waiting"))
        {
            final MailboxStatusResponse mailboxStatusResponse = new MailboxStatusResponse();
            mailboxStatusResponse.setMailbox(attributes.get("mailbox"));
            
            if ("1".equals(attributes.get("waiting")))
            {
                mailboxStatusResponse.setWaiting(Boolean.TRUE);
            }
            else
            {
                mailboxStatusResponse.setWaiting(Boolean.FALSE);
            }
            
            response = mailboxStatusResponse;
        }
        else if (attributes.containsKey("mailbox") && attributes.containsKey("newmessages")
                && attributes.containsKey("oldmessages"))
        {
            final MailboxCountResponse mailboxCountResponse = new MailboxCountResponse();
            mailboxCountResponse.setMailbox(attributes.get("mailbox"));
            mailboxCountResponse.setNewMessages(Integer.valueOf(attributes.get("newmessages")));
            mailboxCountResponse.setOldMessages(Integer.valueOf(attributes.get("oldmessages")));
            response = mailboxCountResponse;
        }
        else if (attributes.containsKey("exten") && attributes.containsKey("context") && attributes.containsKey("hint")
                && attributes.containsKey("status"))
        {
            final ExtensionStateResponse extensionStateResponse = new ExtensionStateResponse();
            extensionStateResponse.setExten(attributes.get("exten"));
            extensionStateResponse.setContext(attributes.get("context"));
            extensionStateResponse.setHint(attributes.get("hint"));
            extensionStateResponse.setStatus(Integer.valueOf(attributes.get("status")));
            response = extensionStateResponse;
        }
        else if(attributes.containsKey("line-000000-000000"))
        {
        	// this attribute will be there if the file has any lines at all
        	response = new GetConfigResponse();
        }
        else
        {
            response = new ManagerResponse();
        }

        // fill known attributes
        if (responseType != null)
        {
            response.setResponse(responseType);
        }
        else if (proxyResponseType != null)
        {
            response.setResponse(proxyResponseType);
        }

        // clone this map as it is reused by the ManagerReader
        response.setAttributes(new HashMap<String, String>(attributes));

        if (attributes.containsKey("actionid"))
        {
            response.setActionId(attributes.get("actionid"));
        }
        if (attributes.containsKey("message"))
        {
            response.setMessage(attributes.get("message"));
        }
        if (attributes.containsKey("uniqueid"))
        {
            response.setUniqueId(attributes.get("uniqueid"));
        }

        return response;
    }
}

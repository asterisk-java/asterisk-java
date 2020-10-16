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
package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.EndpointDetailComplete;
import org.asteriskjava.manager.event.ResponseEvent;

/**
 * Retrieves details about a given endpoint.
 * <p>
 * For the given endpoint, the call will return several events, including
 * EndpointDetail, AorDetail, AuthDetail, TransportDetail, ContactStatusDetail,and IdentifyDetail.
 * 
 * Some events may appear multiple times.
 * 
 * When all events have been reported a EndpointDetailComplete is
 * sent.
 * <p>
 * Available since Asterisk 12 Permission required: write=system
 *
 * @author Steve Sether
 * @version $Id$
 * @see org.asteriskjava.manager.event.ContactStatusDetail
 * @see org.asteriskjava.manager.event.AorDetail
 * @see org.asteriskjava.manager.event.EndpointDetail
 * @see org.asteriskjava.manager.event.TransportDetail
 * @see org.asteriskjava.manager.event.AuthDetail
 * @see org.asteriskjava.manager.event.EndpointDetailComplete
 * @since 12
 */
public class PJSipShowEndpointAction extends AbstractManagerAction implements EventGeneratingAction
{
	/**
     * Serial version identifier.
     */
	private static final long serialVersionUID = -5508189961610900058L;
	private String endpoint;
    

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	/**
     * Creates a new SipPeersAction.
     */
    public PJSipShowEndpointAction()
    {

    }

    @Override
    public String getAction()
    {
        return "PJSIPShowEndpoint";
    }

    public Class< ? extends ResponseEvent> getActionCompleteEventClass()
    {
        return EndpointDetailComplete.class;
    }
}

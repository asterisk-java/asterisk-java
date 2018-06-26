/*
 *  Copyright 2018 IPerity BV
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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Send a NOTIFY to either an endpoint or an arbitrary URI.
 * <p>
 * Either a URI, a channel or an endpoint can be specified.
 * <p>
 * <p>
 * See: https://wiki.asterisk.org/wiki/display/AST/Asterisk+15+ManagerAction_PJSIPNotify
 */
public class PJSIPNotifyAction extends AbstractManagerAction implements ManagerAction
{
	private static final long serialVersionUID = 8198467461743334704L;
	private String channel;
	private String endpoint;
	private String uri;
	private Map<String, String> variables;

	public PJSIPNotifyAction()
	{
		super();
	}

	@Override
	public String getAction()
	{
		return "PJSIPNotify";
	}

	/**
	 * Get the PJSIP channel to send the NOTIFY on.
	 *
	 * @return
	 */
	public String getChannel()
	{
		return this.channel;
	}

	/**
	 * Get the PJSIP endpoint to which to send the NOTIFY.
	 *
	 * @return
	 */
	public String getEndpoint()
	{
		return this.endpoint;
	}

	/**
	 * Get the URI to which to send the NOTIFY.
	 *
	 * @return
	 */
	public String getUri()
	{
		return this.uri;
	}

	/**
	 * Set the PJSIP channel name to send the NOTIFY on. This must be a PJSIP channel,
	 * ie. start with "PJSIP/".
	 *
	 * @param channel
	 */
	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	/**
	 * Set the PJSIP endpoint to send to NOTIFY to.
	 *
	 * @param endpoint
	 */
	public void setEndpoint(String endpoint)
	{
		this.endpoint = endpoint;
	}

	/**
	 * Set an abritrary URI to send the NOTIFY to.
	 *
	 * @param uri
	 */
	public void setUri(String uri)
	{
		this.uri = uri;
	}

	/**
	 * Add a variable.
	 * <p>
	 * Use the 'content' variable to specify the NOTIFY body.
	 *
	 * @param name
	 * @param value
	 */
	public void setVariable(String name, String value)
	{
		if (this.variables == null)
		{
			this.variables = new LinkedHashMap<>();
		}

		this.variables.put(name, value);
	}

	/**
	 * Set all variables. See {@link #setVariable(String, String)}.
	 *
	 * @param variables
	 */
	public void setVariables(Map<String, String> variables)
	{
		this.variables = variables;
	}

	/**
	 * Get the variables.
	 *
	 * @return
	 */
	public Map<String, String> getVariables()
	{
		return this.variables;
	}

}

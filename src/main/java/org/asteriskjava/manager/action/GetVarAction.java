/*
 *  Copyright  2004-2006 Stefan Reuter
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

/**
 * The GetVarAction queries for a global or local channel variable.<br>
 * Reading global variables is supported since Asterisk 1.2.
 * 
 * @author srt
 * @version $Id: GetVarAction.java,v 1.4 2005/11/02 20:02:37 srt Exp $
 */
public class GetVarAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 5239805071977668779L;
    private String channel;
    private String variable;

    /**
     * Creates a new empty GetVarAction.
     */
    public GetVarAction()
    {

    }

    /**
     * Creates a new GetVarAction that queries for the given global variable.
     * 
     * @param variable the name of the global variable to query.
     * @since 0.2
     */
    public GetVarAction(String variable)
    {
        this.variable = variable;
    }

    /**
     * Creates a new GetVarAction that queries for the given local channel
     * variable.
     * 
     * @param channel the name of the channel, for example "SIP/1234-9cd".
     * @param variable the name of the variable to query.
     * @since 0.2
     */
    public GetVarAction(String channel, String variable)
    {
        this.channel = channel;
        this.variable = variable;
    }

    /**
     * Returns the name of this action, i.e. "GetVar".
     */
    public String getAction()
    {
        return "GetVar";
    }

    /**
     * Returns the name of the channel if you query for a local channel variable
     * or <code>null</code> if it is a global variable.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel if you query for a local channel variable.
     * Leave empty to query for a global variable.
     * 
     * @param channel the channel if you query for a local channel variable or
     *            <code>null</code> to query for a gloabl variable.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Retruns the name of the variable to query.
     */
    public String getVariable()
    {
        return variable;
    }

    /**
     * Sets the name of the variable to query.
     */
    public void setVariable(String variable)
    {
        this.variable = variable;
    }
}

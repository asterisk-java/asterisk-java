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
package org.asteriskjava.manager.event;

/**
 * A NewChannelEvent is triggered when a new channel is created.
 * <p>
 * It is implemented in <code>channel.c</code>
 *
 * @author srt
 * @version $Id$
 */
public class NewChannelEvent extends AbstractChannelStateEvent
{
    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 1L;

    private String accountCode;
    private String language;
    private String linkedid;

    public NewChannelEvent(Object source)
    {
        super(source);
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    /**
     * Returns the account code of the new channel.
     * <p>
     * This property is available since Asterisk 1.6.
     *
     * @return the account code of the new channel.
     * @since 1.0.0
     */
    public String getAccountCode()
    {
        return accountCode;
    }

    /**
     * Sets the account code of the new channel.
     *
     * @param accountCode the account code of the new channel.
     * @since 1.0.0
     */
    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getLinkedid()
    {
        return linkedid;
    }

    public void setLinkedid(String linkedid)
    {
        this.linkedid = linkedid;
    }
    
    
}

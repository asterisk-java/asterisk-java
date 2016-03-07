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
 * A NewExtenEvent is triggered when a channel is connected to a new extension.
 * <p>
 * It is implemented in <code>pbx.c</code>
 * 
 * @author srt
 * @version $Id$
 */
public class NewExtenEvent extends ManagerEvent
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = -467486409866099387L;

    private String uniqueId;
    private String extension;
    private String application;
    private String appData;
    private String channel;
    private String language;
    private String accountCode;
    private String linkedId;

    /**
     * @param source
     */
    public NewExtenEvent(Object source)
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
     * Returns the unique id of the channel.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets the unique id of the channel.
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    /**
     * Returns the name of the application that is executed.
     */
    public String getApplication()
    {
        return application;
    }

    /**
     * Sets the name of the application that is executed.
     */
    public void setApplication(String application)
    {
        this.application = application;
    }

    /**
     * Returns the parameters passed to the application that is executed. The
     * parameters are separated by a '|' character.
     */
    public String getAppData()
    {
        return appData;
    }

    /**
     * Sets the parameters passed to the application that is executed.
     */
    public void setAppData(String appData)
    {
        this.appData = appData;
    }

    /**
     * Returns the name of the channel.
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel.
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the extension.
     */
    public String getExtension()
    {
        return extension;
    }

    /**
     * Sets the extension.
     */
    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getLinkedId()
    {
        return linkedId;
    }

    public void setLinkedId(String linkedId)
    {
        this.linkedId = linkedId;
    }
    
}

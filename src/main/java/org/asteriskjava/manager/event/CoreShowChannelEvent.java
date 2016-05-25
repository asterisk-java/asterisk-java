/*
 *  Copyright 2009 Sebastian.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.asteriskjava.manager.event;

/**
 * A CoreShowChannelEvent is triggered for each active channel in response to a
 * CoreShowChannelsAction.
 *
 * @author sebastian gutierrez
 * @version $Id$
 * @see org.asteriskjava.manager.action.CoreShowChannelsAction
 * @since 1.0.0
 */
public class CoreShowChannelEvent extends ResponseEvent
{
    /**
     * Serializable version identifier.
     */
    static final long serialVersionUID = 0L;

    private String uniqueid;
    private String channel;
    private String extension;
    private String application;
    private String applicationdata;
    private String duration;
    private String accountcode;
    private String bridgedChannel;
    private String bridgeid;
    private String linkedid;
    private String language;


    public CoreShowChannelEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the channel state
     *
     * @return channel state
     */

    /**
     * Returns the Account code
     *
     * @return accountcode
     */
    public String getAccountcode()
    {
        return accountcode;
    }

    public void setAccountcode(String accountcode)
    {
        this.accountcode = accountcode;
    }

    /**
     * Returns the Aplication is runnning that channel at that time
     *
     * @return aplication name
     */
    public String getApplication()
    {
        return application;
    }

    public void setApplication(String application)
    {
        this.application = application;
    }

    /**
     * Returns the Aplication Data is runnning that channel at that time this is
     * the parameters passed to that dialplan application
     *
     * @return aplication data
     */
    public String getApplicationdata()
    {
        return applicationdata;
    }

    public void setApplicationdata(String applicationdata)
    {
        this.applicationdata = applicationdata;
    }

    /**
     * Returns the Bridged Channel if is bridged to one
     *
     * @return Channel name
     */
    public String getBridgedChannel()
    {
        return bridgedChannel;
    }

    public void setBridgedChannel(String bridgedChannel)
    {
        this.bridgedChannel = bridgedChannel;
    }

    /**
     * Returns the Bridged UniqueID
     *
     * @return uniqueid
     */
    @Deprecated
    public String getBridgeduniqueid()
    {
        return bridgeid;
    }

    @Deprecated
    public void setBridgeduniqueid(String bridgeduniqueid)
    {
        this.bridgeid = bridgeduniqueid;
    }

    /**
     * Returns the Bridged UniqueID Case params name return "bridgeid"
     *
     * @return uniqueid
     */
    public String getBridgeid()
    {
        return bridgeid;
    }

    public void setBridgeid(String bridgeid)
    {
        this.bridgeid = bridgeid;
    }

    /**
     * Returns the Originate Channel name
     *
     * @return Channel name
     */
    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the duration of the call
     *
     * @return duration
     */
    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    /**
     * Returns the Extension dialed
     *
     * @return extension
     */
    public String getExtension()
    {
        return extension;
    }

    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    /**
     * Returns the Uniqueid
     *
     * @return uniqueid
     */
    public String getUniqueid()
    {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid)
    {
        this.uniqueid = uniqueid;
    }


    /**
     * Returns the Channel LinkedID
     *
     * @return linkedid
     */
    public String getLinkedid() {
        return linkedid;
    }

    public void setLinkedid(String linkedid) {
        this.linkedid = linkedid;
    }


    /**
     * Returns the Channel Language
     *
     * @return language
     */
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


}

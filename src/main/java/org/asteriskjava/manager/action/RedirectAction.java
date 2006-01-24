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
 * Redirects a given channel (and an optional additional channel) to a new
 * extension.
 * 
 * @author srt
 * @version $Id: RedirectAction.java,v 1.4 2005/08/07 16:43:29 srt Exp $
 */
public class RedirectAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 1869279324159418150L;

    private String channel;
    private String extraChannel;
    private String exten;
    private String context;
    private Integer priority;

    /**
     * Creates a new empty RedirectAction.
     */
    public RedirectAction()
    {

    }

    /**
     * Creates a new RedirectAction that redirects the given channel to the
     * given context, extension, priority triple.
     * 
     * @param channel the name of the channel to redirect
     * @param context the destination context
     * @param exten the destination extension
     * @param priority the destination priority
     * @since 0.2
     */
    public RedirectAction(String channel, String context, String exten,
            Integer priority)
    {
        this.channel = channel;
        this.context = context;
        this.exten = exten;
        this.priority = priority;
    }

    /**
     * Creates a new RedirectAction that redirects the given channels to the
     * given context, extension, priority triple.
     * 
     * @param channel the name of the first channel to redirect
     * @param extraChannel the name of the second channel to redirect
     * @param context the destination context
     * @param exten the destination extension
     * @param priority the destination priority
     * @since 0.2
     */
    public RedirectAction(String channel, String extraChannel, String context,
            String exten, Integer priority)
    {
        this.channel = channel;
        this.extraChannel = extraChannel;
        this.context = context;
        this.exten = exten;
        this.priority = priority;
    }

    /**
     * Returns the name of this action, i.e. "Redirect".
     */
    public String getAction()
    {
        return "Redirect";
    }

    /**
     * Returns name of the channel to redirect.
     * 
     * @return the name of the channel to redirect
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Sets the name of the channel to redirect.
     * 
     * @param channel the name of the channel to redirect
     */
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the name of the additional channel to redirect.
     * 
     * @return the name of the additional channel to redirect
     */
    public String getExtraChannel()
    {
        return extraChannel;
    }

    /**
     * Sets the name of the additional channel to redirect.
     * 
     * @param extraChannel the name of the additional channel to redirect
     */
    public void setExtraChannel(String extraChannel)
    {
        this.extraChannel = extraChannel;
    }

    /**
     * Returns the destination context.
     * 
     * @return the destination context
     */
    public String getContext()
    {
        return context;
    }

    /**
     * Sets the destination context.
     * 
     * @param context the destination context
     */
    public void setContext(String context)
    {
        this.context = context;
    }

    /**
     * Returns the destination extension.
     * 
     * @return the destination extension
     */
    public String getExten()
    {
        return exten;
    }

    /**
     * Sets the destination extension.
     * 
     * @param exten the destination extension
     */
    public void setExten(String exten)
    {
        this.exten = exten;
    }

    /**
     * Returns the destination priority.
     * 
     * @return the destination priority
     */
    public Integer getPriority()
    {
        return priority;
    }

    /**
     * Sets the destination priority.
     * 
     * @param priority the destination priority
     */
    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }
}

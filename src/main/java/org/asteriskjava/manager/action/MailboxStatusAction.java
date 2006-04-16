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

/**
 * The MailboxStatusAction checks if a mailbox contains waiting messages.<br>
 * The MailboxStatusAction returns a MailboxStatusResponse.
 * 
 * @see org.asteriskjava.manager.response.MailboxStatusResponse
 * @author srt
 * @version $Id$
 */
public class MailboxStatusAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = -3845028207155711950L;

    private String mailbox;

    /**
     * Creates a new empty MailboxStatusAction.
     */
    public MailboxStatusAction()
    {

    }

    /**
     * Creates a new MailboxStatusAction that checks for waiting messages in the
     * given mailbox.
     * 
     * @param mailbox the name of the mailbox to check.<br>
     *            This can either be only the number of the mailbox or a string
     *            of the form mailboxnumber@context.If no context is specified
     *            "default" is assumed.
     * @since 0.2
     */
    public MailboxStatusAction(String mailbox)
    {
        this.mailbox = mailbox;
    }

    /**
     * Returns the name of this action, i.e. "MailboxStatus".
     */
    public String getAction()
    {
        return "MailboxStatus";
    }

    /**
     * Returns the name of the mailbox to query.
     */
    public String getMailbox()
    {
        return mailbox;
    }

    /**
     * Sets the name of the mailbox to query.<br>
     * This can either be only the name of the mailbox or a string of the form
     * mailboxnumber@context. If no context is specified "default" is assumed.<br>
     * Multiple mailboxes may be given, separated by ','. In this case the
     * action checks whether at least one of the given mailboxes has waiting
     * messages.<br>
     * This property is mandatory.<br>
     * Example: "1234,1235@mycontext"
     */
    public void setMailbox(String mailbox)
    {
        this.mailbox = mailbox;
    }
}

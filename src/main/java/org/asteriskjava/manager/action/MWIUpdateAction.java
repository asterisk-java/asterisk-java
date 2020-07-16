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

public class MWIUpdateAction extends AbstractManagerAction
{
    static final long serialVersionUID = 1L;
    private String mailbox;
    private int oldMessages;
    private int newMessages;

    /**
     * Creates a new MWIUpdateAction.
     */
    public MWIUpdateAction()
    {

    }

    public MWIUpdateAction(String mailbox, int oldMessages, int newMessages)
    {
        this.mailbox = mailbox;
        this.oldMessages = oldMessages;
        this.newMessages = newMessages;

    }

    public String getMailbox()
    {
        return mailbox;
    }

    public void setMailbox(String mailbox)
    {
        this.mailbox = mailbox;
    }

    public int getOldMessages()
    {
        return oldMessages;
    }

    public void setOldMessages(int oldMessages)
    {
        this.oldMessages = oldMessages;
    }

    public int getNewMessages()
    {
        return newMessages;
    }

    public void setNewMessages(int newMessages)
    {
        this.newMessages = newMessages;
    }

    @Override
    public String getAction()
    {
        return "MWIUpdate";
    }

}

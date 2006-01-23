/*
 * Copyright  2004-2005 Stefan Reuter
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

import org.asteriskjava.manager.event.DBGetResponseEvent;

/**
 * Retrieves an entry in the Asterisk database for a given family and key.<br>
 * If an entry is found a DBGetResponseEvent is sent by Asterisk containing the
 * value, otherwise a ManagerError indicates that no entry matches.<br>
 * Available since Asterisk 1.2
 * 
 * @see org.asteriskjava.manager.event.DBGetResponseEvent
 * @author srt
 * @version $Id: DBGetAction.java,v 1.4 2005/08/07 00:09:42 srt Exp $
 * @since 0.2
 */
public class DBGetAction extends AbstractManagerAction
        implements
            EventGeneratingAction
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = 921037572305993779L;
    private String family;
    private String key;

    /**
     * Creates a new empty DBGetAction.
     */
    public DBGetAction()
    {

    }

    /**
     * Creates a new DBGetAction that retrieves the value of the database entry
     * with the given key in the given family.
     * 
     * @param family the family of the key
     * @param key the key of the entry to retrieve
     * @since 0.2
     */
    public DBGetAction(String family, String key)
    {
        this.family = family;
        this.key = key;
    }

    public String getAction()
    {
        return "DBGet";
    }

    /**
     * Returns the family of the key.
     * 
     * @return the family of the key.
     */
    public String getFamily()
    {
        return family;
    }

    /**
     * Sets the family of the key.
     * 
     * @param family the family of the key.
     */
    public void setFamily(String family)
    {
        this.family = family;
    }

    /**
     * Returns the the key of the entry to retrieve.
     * 
     * @return the key of the entry to retrieve.
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Sets the key of the entry to retrieve.
     * 
     * @param key the key of the entry to retrieve.
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    public Class getActionCompleteEventClass()
    {
        return DBGetResponseEvent.class;
    }
}

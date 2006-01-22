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
package net.sf.asterisk.manager.action;

/**
 * The ExtensionStateAction queries the state of an extension in a given context.
 * 
 * @author srt
 * @version $Id: ExtensionStateAction.java,v 1.3 2005/07/16 13:19:34 srt Exp $
 */
public class ExtensionStateAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 6537408784388696403L;

    private String exten;
    private String context;

    /**
     * Returns the name of this action, i.e. "ExtensionState".
     */
    public String getAction()
    {
        return "ExtensionState";
    }

    /**
     * Returns the extension to query.
     */
    public String getExten()
    {
        return exten;
    }

    /**
     * Sets the extension to query.
     */
    public void setExten(String exten)
    {
        this.exten = exten;
    }

    /**
     * Returns the name of the context that contains the extension to query.
     */
    public String getContext()
    {
        return context;
    }

    /**
     * Sets the name of the context that contains the extension to query.
     */
    public void setContext(String context)
    {
        this.context = context;
    }
}

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
package net.sf.asterisk.manager.response;

/**
 * @author srt
 * @version $Id: ExtensionStateResponse.java,v 1.3 2005/07/18 12:11:05 srt Exp $
 */
public class ExtensionStateResponse extends ManagerResponse
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = -2044248427247227390L;
    private String exten;
    private String context;
    private String hint;
    private Integer status;

    public String getExten()
    {
        return exten;
    }

    public void setExten(String exten)
    {
        this.exten = exten;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext(String context)
    {
        this.context = context;
    }

    public String getHint()
    {
        return hint;
    }

    public void setHint(String hint)
    {
        this.hint = hint;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public String toString()
    {
        StringBuffer sb;

        sb = new StringBuffer(getClass().getName() + ": ");
        sb.append("actionId='" + getActionId() + "'; ");
        sb.append("message='" + getMessage() + "'; ");
        sb.append("response='" + getResponse() + "'; ");
        sb.append("uniqueId='" + getUniqueId() + "'; ");
        sb.append("exten='" + getExten() + "'; ");
        sb.append("context='" + getContext() + "'; ");
        sb.append("hint='" + getHint() + "'; ");
        sb.append("status='" + getStatus() + "'; ");
        sb.append("systemHashcode=" + System.identityHashCode(this));

        return sb.toString();
    }
}

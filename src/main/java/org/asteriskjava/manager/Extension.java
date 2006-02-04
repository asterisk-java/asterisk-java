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
package org.asteriskjava.manager;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents an Asterisk dialplan entry.
 * 
 * @author srt
 * @version $Id: Extension.java,v 1.5 2005/08/08 06:10:18 srt Exp $
 */
public class Extension implements Serializable
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 768239042942945744L;
    private final Date date;
    private final String context;
    private final String extension;
    private final Integer priority;
    private final String application;
    private final String appData;

    /**
     * @param date
     * @param context
     * @param extension
     * @param priority
     */
    public Extension(Date date, String context, String extension,
            Integer priority)
    {
        this(date, context, extension, priority, null, null);
    }

    /**
     * @param date
     * @param context
     * @param extension
     * @param priority
     * @param application
     * @param appData
     */
    public Extension(Date date, String context, String extension,
            Integer priority, String application, String appData)
    {
        this.date = date;
        this.context = context;
        this.extension = extension;
        this.priority = priority;
        this.application = application;
        this.appData = appData;
    }

    public Date getDate()
    {
        return date;
    }

    public String getContext()
    {
        return context;
    }

    public String getExtension()
    {
        return extension;
    }

    public Integer getPriority()
    {
        return priority;
    }

    public String getApplication()
    {
        return application;
    }

    public String getAppData()
    {
        return appData;
    }

    public String toString()
    {
        StringBuffer sb;

        sb = new StringBuffer(getClass().getName() + ": ");
        sb.append("date='" + getDate() + "'; ");
        sb.append("context='" + getContext() + "'; ");
        sb.append("extension='" + getExtension() + "'; ");
        sb.append("priority='" + getPriority() + "'; ");
        sb.append("application='" + getApplication() + "'; ");
        sb.append("appData=" + getAppData() + "; ");
        sb.append("systemHashcode=" + System.identityHashCode(this));

        return sb.toString();
    }
}

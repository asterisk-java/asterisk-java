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

import java.util.HashMap;
import java.util.Map;

/**
 * The SkypeAccountPropertyAction sets properties for a Skype for Asterisk user.<p>
 * Available with Skype for Asterisk.
 *
 * @since 1.0.0
 */
public class SkypeAccountPropertyAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 1L;
    private String user;
    private Map<String, String> variables = new HashMap<>();

    public static final String PROPERTY_SKYPENAME = "skypename";
    public static final String PROPERTY_TIMEZONE = "timezone";
    public static final String PROPERTY_AVAILABILITY = "availability";
    public static final String PROPERTY_FULLNAME = "fullname";
    public static final String PROPERTY_LANGUAGE = "language";
    public static final String PROPERTY_COUNTRY = "country";
    public static final String PROPERTY_PHONE_HOME = "phone_home";
    public static final String PROPERTY_PHONE_OFFICE = "phone_office";
    public static final String PROPERTY_PHONE_MOBILE = "phone_mobile";
    public static final String PROPERTY_ABOUT = "about";

    /**
     * Creates a new SkypeAccountPropertyAction.
     */
    public SkypeAccountPropertyAction()
    {

    }

    /**
     * Creates a new SkypeAccountPropertyAction that sets the given properties for the given Skype for
     * Asterisk user.
     *
     * @param user      the Skype username of the user to add the buddy to.
     * @param variables the properties to set. Must not be <code>null</code>
     */
    public SkypeAccountPropertyAction(String user, Map<String, String> variables)
    {
        this.user = user;
        setVariables(variables);
    }

    /**
     * Returns the Skype username of the Skype for Asterisk user.<p>
     * This property is mandatory.
     *
     * @return the Skype username of the Skype for Asterisk user.
     */
    public String getUser()
    {
        return user;
    }

    /**
     * Sets the Skype username of the Skype for Asterisk user.
     *
     * @param user the Skype username of the Skype for Asterisk user.
     */
    public void setUser(String user)
    {
        this.user = user;
    }

    public Map<String, String> getVariables()
    {
        return new HashMap<>(variables);
    }

    public void setVariables(Map<String, String> variables)
    {
        if (variables == null)
        {
            throw new IllegalArgumentException("Variables cannot be null");
        }
        this.variables = new HashMap<>(variables);
    }

    public void setTimezone(String timezone)
    {
        variables.put(PROPERTY_TIMEZONE, timezone);
    }

    public void setSkypename(String skypename)
    {
        variables.put(PROPERTY_SKYPENAME, skypename);
    }

    public void setAvailability(String availability)
    {
        variables.put(PROPERTY_AVAILABILITY, availability);
    }

    public void setFullname(String fullname)
    {
        variables.put(PROPERTY_FULLNAME, fullname);
    }

    /**
     * Sets the ISO language code.
     *
     * @param language the ISO language code.
     */
    public void setLanguage(String language)
    {
        variables.put(PROPERTY_LANGUAGE, language);
    }

    /**
     * Sets the country code.
     *
     * @param country the country code.
     */
    public void setCountry(String country)
    {
        variables.put(PROPERTY_COUNTRY, country);
    }

    public void setPhoneHome(String phoneHome)
    {
        variables.put(PROPERTY_PHONE_HOME, phoneHome);
    }

    public void setPhoneOffice(String phoneOffice)
    {
        variables.put(PROPERTY_PHONE_OFFICE, phoneOffice);
    }

    public void setPhoneMobile(String phoneMobile)
    {
        variables.put(PROPERTY_PHONE_MOBILE, phoneMobile);
    }

    public void setAbout(String about)
    {
        variables.put(PROPERTY_ABOUT, about);
    }

    /**
     * Returns the name of this action, i.e. "SkypeAccountProperty".
     */
    @Override
    public String getAction()
    {
        return "SkypeAccountProperty";
    }
}

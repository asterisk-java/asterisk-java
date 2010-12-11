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
package org.asteriskjava.manager.response;

/**
 * Corresponds to a SkypeBuddyAction and contains the details of a Skype buddy.
 *
 * @see org.asteriskjava.manager.action.SkypeBuddyAction
 * @since 1.0.0
 */
public class SkypeBuddyResponse extends ManagerResponse
{
    private static final long serialVersionUID = 0L;

    private String skypename;
    private String timezone;
    private String availability;
    private String fullname;
    private String language;
    private String country;
    private String phoneHome;
    private String phoneOffice;
    private String phoneMobile;
    private String about;

    public String getSkypename()
    {
        return skypename;
    }

    public void setSkypename(String skypename)
    {
        this.skypename = skypename;
    }

    public String getTimezone()
    {
        return timezone;
    }

    public void setTimezone(String timezone)
    {
        this.timezone = timezone;
    }

    public String getAvailability()
    {
        return availability;
    }

    public void setAvailability(String availability)
    {
        this.availability = availability;
    }

    public String getFullname()
    {
        return fullname;
    }

    public void setFullname(String fullname)
    {
        this.fullname = fullname;
    }

    /**
     * Returns the ISO language code.
     *
     * @return the ISO language code.
     */
    public String getLanguage()
    {
        return language;
    }

    /**
     * Sets the ISO language code.
     *
     * @param language the ISO language code.
     */
    public void setLanguage(String language)
    {
        this.language = language;
    }

    /**
     * Returns the ISO country code.
     *
     * @return the ISO country code (in lower case).
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * Sets the country code.
     *
     * @param country the country code.
     */
    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getPhoneHome()
    {
        return phoneHome;
    }

    public void setPhoneHome(String phoneHome)
    {
        this.phoneHome = phoneHome;
    }

    public String getPhoneOffice()
    {
        return phoneOffice;
    }

    public void setPhoneOffice(String phoneOffice)
    {
        this.phoneOffice = phoneOffice;
    }

    public String getPhoneMobile()
    {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile)
    {
        this.phoneMobile = phoneMobile;
    }

    public String getAbout()
    {
        return about;
    }

    public void setAbout(String about)
    {
        this.about = about;
    }
}
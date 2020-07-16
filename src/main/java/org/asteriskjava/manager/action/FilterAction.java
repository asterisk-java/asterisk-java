/*
 *  Copyright 2019 Richie Chauhan
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
 * The FilterAction dynamically adds filters for the current user in a manager session.
 * The filters added are only used for the current session. Once the connection is closed the filters are removed.
 * 
 * This command requires the user has read access to specific events(manager.conf) and system permission 
 * because it can be used to create filters that may bypass filters defined in manager.conf 
 * 
 * There are 2 Types of filters: 
 * Blacklist: The event you no longer want to receive. It starts with an exclamation. eg. "!RTCPReceived".  
 * Whitelist: The event you are interested in receiving. eg. "RTCPReceived". 
 * 
 * Regular expressions can be used for the filters.  
 * 
 * Evaluation of the filters is as follows:
 * If no filters are configured all events are reported as normal.
 * If there are white filters only: Then it is implied that all events are black listed and only the specified white filter events are returned.
 * If there are black filters only: Then it is implied that all events are white listed and only the specified black filter events are removed. 
 * If there are both white and black filters: Then it is implied that all events are black listed and only the specified white filter events are returned, 
 *                                            and then specific blacklisted events are further removed. (Limited use cases)
 * 
 * @author aavaz
 * @version $Id$
 */
public class FilterAction extends AbstractManagerAction
{
    static final long serialVersionUID = 5537508784388696503L;

    private String operation = "Add";//No other option other than Add at the moment.
    private String filter;

    /**
     * Creates a new FilterAction.
     */
    public FilterAction()
    {
    }

    /**
     * Creates a new FilterAction with the filter specified
     *
     * @param filter the whitelist or blacklist filter to be added. 
     * @since ???
     */
    public FilterAction(String filter)
    {
        this.filter = filter;
    }

    /**
     * Returns the name of this action, i.e. "Filter".
     */
    @Override
    public String getAction()
    {
        return "Filter";
    }

    /**
     * Returns the filter operation which is "Add" by default.
     *
     * @return the filter operation which is "Add" by default
     */
    public String getOperation()
    {
        return operation;
    }    
    
    /**
     * Returns the filter.
     *
     * @return the filter.
     */
    public String getFilter()
    {
        return filter;
    }

    /**
     * Sets the filter. 
     *
     * @param filter add the whitelist or blacklist event to be filtered. 
     */
    public void setFilter(String filter)
    {
        this.filter = filter;
    }

}


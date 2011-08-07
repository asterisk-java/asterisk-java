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
package org.asteriskjava.manager.event;

/**
 * A FaxDocumentStatusEvent is an event of Digium's Fax For Asterisk add-on.
 */
public class T38FaxStatusEvent extends AbstractFaxEvent
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = -1L;
    private String maxLag;
    private String totalLag;
    private String averageLag;
    private Integer totalEvents;
    private String t38SessionDuration;
    private Integer t38PacketsSent;
    private Integer t38OctetsSent;
    private String averageTxDataRate;
    private Integer t38PacketsReceived;
    private Integer t38OctetsReceived;
    private String averageRxDataRate;
    private Integer jitterBufferOverflows;
    private Integer minimumJitterSpace;
    private Integer unrecoverablePackets;

    public T38FaxStatusEvent(Object source)
    {
        super(source);
    }


    /**
     * @return the maxLag
     */
    public String getMaxLag()
    {
        return maxLag;
    }

    /**
     * @param maxLag the maxLag to set
     */
    public void setMaxLag(String maxLag)
    {
        this.maxLag = maxLag;
    }

    /**
     * @return the totalLag
     */
    public String getTotalLag()
    {
        return totalLag;
    }

    /**
     * @param totalLag the totalLag to set
     */
    public void setTotalLag(String totalLag)
    {
        this.totalLag = totalLag;
    }

    /**
     * @return the averageLag
     */
    public String getAverageLag()
    {
        return averageLag;
    }

    /**
     * @param averageLag the averageLag to set
     */
    public void setAverageLag(String averageLag)
    {
        this.averageLag = averageLag;
    }

    /**
     * @return the totalEvents
     */
    public Integer getTotalEvents()
    {
        return totalEvents;
    }

    /**
     * @param totalEvents the totalEvents to set
     */
    public void setTotalEvents(Integer totalEvents)
    {
        this.totalEvents = totalEvents;
    }

    /**
     * @return the t38SessionDuration
     */
    public String getT38SessionDuration()
    {
        return t38SessionDuration;
    }

    /**
     * @param t38SessionDuration the t38SessionDuration to set
     */
    public void setT38SessionDuration(String t38SessionDuration)
    {
        this.t38SessionDuration = t38SessionDuration;
    }

    /**
     * @return the t38PacketsSent
     */
    public Integer getT38PacketsSent()
    {
        return t38PacketsSent;
    }

    /**
     * @param t38PacketsSent the t38PacketsSent to set
     */
    public void setT38PacketsSent(Integer t38PacketsSent)
    {
        this.t38PacketsSent = t38PacketsSent;
    }

    /**
     * @return the t38OctetsSent
     */
    public Integer getT38OctetsSent()
    {
        return t38OctetsSent;
    }

    /**
     * @param t38OctetsSent the t38OctetsSent to set
     */
    public void setT38OctetsSent(Integer t38OctetsSent)
    {
        this.t38OctetsSent = t38OctetsSent;
    }

    /**
     * @return the averageTxDataRate
     */
    public String getAverageTxDataRate()
    {
        return averageTxDataRate;
    }

    /**
     * @param averageTxDataRate the averageTxDataRate to set
     */
    public void setAverageTxDataRate(String averageTxDataRate)
    {
        this.averageTxDataRate = averageTxDataRate;
    }

    /**
     * @return the t38PacketsReceived
     */
    public Integer getT38PacketsReceived()
    {
        return t38PacketsReceived;
    }

    /**
     * @param t38PacketsReceived the t38PacketsReceived to set
     */
    public void setT38PacketsReceived(Integer t38PacketsReceived)
    {
        this.t38PacketsReceived = t38PacketsReceived;
    }

    /**
     * @return the t38OctetsReceived
     */
    public Integer getT38OctetsReceived()
    {
        return t38OctetsReceived;
    }

    /**
     * @param t38OctetsReceived the t38OctetsReceived to set
     */
    public void setT38OctetsReceived(Integer t38OctetsReceived)
    {
        this.t38OctetsReceived = t38OctetsReceived;
    }

    /**
     * @return the averageRxDataRate
     */
    public String getAverageRxDataRate()
    {
        return averageRxDataRate;
    }

    /**
     * @param averageRxDataRate the averageRxDataRate to set
     */
    public void setAverageRxDataRate(String averageRxDataRate)
    {
        this.averageRxDataRate = averageRxDataRate;
    }

    /**
     * @return the jitterBufferOverflows
     */
    public Integer getJitterBufferOverflows()
    {
        return jitterBufferOverflows;
    }

    /**
     * @param jitterBufferOverflows the jitterBufferOverflows to set
     */
    public void setJitterBufferOverflows(Integer jitterBufferOverflows)
    {
        this.jitterBufferOverflows = jitterBufferOverflows;
    }

    /**
     * @return the minimumJitterSpace
     */
    public Integer getMinimumJitterSpace()
    {
        return minimumJitterSpace;
    }

    /**
     * @param minimumJitterSpace the minimumJitterSpace to set
     */
    public void setMinimumJitterSpace(Integer minimumJitterSpace)
    {
        this.minimumJitterSpace = minimumJitterSpace;
    }

    /**
     * @return the unrecoverablePackets
     */
    public Integer getUnrecoverablePackets()
    {
        return unrecoverablePackets;
    }

    /**
     * @param unrecoverablePackets the unrecoverablePackets to set
     */
    public void setUnrecoverablePackets(Integer unrecoverablePackets)
    {
        this.unrecoverablePackets = unrecoverablePackets;
    }

    // convenience methods
    public Integer getTotalLagInMilliSeconds()
    {
        final String totalLagStripped = stripUnit(this.totalLag);
        return totalLagStripped == null ? null : Integer.valueOf(totalLagStripped);
    }

    public Integer getMaxLagInMilliSeconds()
    {
        final String maxLagStripped = stripUnit(this.maxLag);
        return maxLagStripped == null ? null : Integer.valueOf(maxLagStripped);
    }

    public Double getT38SessionDurationInSeconds()
    {
        final String t38SessionDurationStripped = stripUnit(this.t38SessionDuration);
        return t38SessionDurationStripped == null ? null : Double.valueOf(t38SessionDurationStripped);
    }

    public Double getAverageLagInMilliSeconds()
    {
        final String averageLagStripped = stripUnit(this.averageLag);
        return averageLagStripped == null ? null : Double.valueOf(averageLagStripped);
    }

    public Integer getAverageTxDataRateInBps()
    {
        final String averageTxDataRateStripped = stripUnit(this.averageTxDataRate);
        return averageTxDataRateStripped == null ? null : Integer.valueOf(averageTxDataRateStripped);
    }

    public Integer getAverageRxDataRateInBps()
    {
        final String averageRxDataRateStripped = stripUnit(this.averageRxDataRate);
        return averageRxDataRateStripped == null ? null : Integer.valueOf(averageRxDataRateStripped);
    }

    String stripUnit(String s)
    {
        if (s == null || s.length() == 0)
        {
            return null;
        }

        int index = s.indexOf(' ');
        if (index < 0)
        {
            return s;
        }
        return s.substring(0, index);
    }
}

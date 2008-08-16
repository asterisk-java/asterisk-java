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

import java.net.InetAddress;

/**
 * An RTCPReceivedEvent is triggered when Asterisk receives an RTCP message.<p>
 * Available since Asterisk 1.6<p>
 * It is implemented in <code>main/rtp.c</code>
 *
 * @author srt
 * @version $Id$
 * @since 1.0.0
 */
public class RtcpReceivedEvent extends AbstractRtcpEvent
{
    private static final long serialVersionUID = 1L;

    /**
     * Payload identifier for a sender report.
     */
    public static final int PT_SENDER_REPORT = 200;

    /**
     * Payload identifier for a receiver report.
     */
    public static final int PT_RECEIVER_REPORT = 201;
    public static final int PT_H261_FUR = 192;

    private InetAddress fromAddress;
    private Integer fromPort;
    private Integer pt;
    private Integer receptionReports;
    private Integer senderSsrc;
    private Integer packetsLost;
    private Long highestSequence;
    private Long squenceNumberCycles;
    private Double lastSr;
    private Integer rtt;

    public RtcpReceivedEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the IP address the RTCP message has been received from.
     *
     * @return the IP address the RTCP message has been received from.
     */
    public InetAddress getFromAddress()
    {
        return fromAddress;
    }

    /**
     * Returns the port of the RTCP message has been received from.
     *
     * @return the port of the RTCP message has been received from.
     */
    public Integer getFromPort()
    {
        return fromPort;
    }

    public void setFrom(String from)
    {
        // Format is "%s:%d"
        this.fromAddress = stringToAddress(from);
        this.fromPort = stringToPort(from);
    }

    /**
     * Indicates the format of the payload, typical values are 200 for sender reports and
     * 201 for receiver reports.
     *
     * @return the format of the payload.
     * @see #PT_SENDER_REPORT
     * @see #PT_RECEIVER_REPORT
     */
    public Integer getPt()
    {
        return pt;
    }

    public void setPt(String ptString)
    {
        // Format is "PT: %d(%s)"
        if (ptString == null || ptString.length() == 0)
        {
            this.pt = null;
            return;
        }

        if (ptString.indexOf('(') > 0)
        {
            this.pt = Integer.parseInt(ptString.substring(0, ptString.indexOf('(')));
        }
        else
        {
            this.pt = Integer.parseInt(ptString);
        }
    }

    public Integer getReceptionReports()
    {
        return receptionReports;
    }

    public void setReceptionReports(Integer receptionReports)
    {
        this.receptionReports = receptionReports;
    }

    /**
     * Returns the synchronization source identifier of the sender.
     *
     * @return the synchronization source identifier of the sender.
     */
    public Integer getSenderSsrc()
    {
        return senderSsrc;
    }

    public void setSenderSsrc(Integer senderSsrc)
    {
        this.senderSsrc = senderSsrc;
    }

    /**
     * Returns the number of packets lost so far.
     *
     * @return the number of packets lost.
     */
    public Integer getPacketsLost()
    {
        return packetsLost;
    }

    public void setPacketsLost(Integer packetsLost)
    {
        this.packetsLost = packetsLost;
    }

    public Long getHighestSequence()
    {
        return highestSequence;
    }

    public void setHighestSequence(Long highestSequence)
    {
        this.highestSequence = highestSequence;
    }

    public Long getSquenceNumberCycles()
    {
        return squenceNumberCycles;
    }

    public void setSquenceNumberCycles(Long squenceNumberCycles)
    {
        this.squenceNumberCycles = squenceNumberCycles;
    }

    public Double getLastSr()
    {
        return lastSr;
    }

    public void setLastSr(Double lastSr)
    {
        this.lastSr = lastSr;
    }

    /**
     * Returns the round trip time.
     *
     * @return the round trip time in seconds, may be <code>null</code>.
     */
    public Integer getRtt()
    {
        return rtt;
    }

    public void setRtt(String rttString)
    {
        this.rtt = secStringToInteger(rttString);
    }
}
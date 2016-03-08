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
 * An RtcpSentEvent is triggered when Asterisk sends an RTCP message.<p>
 * Available since Asterisk 1.6<p>
 * It is implemented in <code>main/rtp.c</code>
 *
 * @author srt
 * @version $Id$
 * @since 1.0.0
 */
public class RtcpSentEvent extends AbstractRtcpEvent
{
    private static final long serialVersionUID = 1L;

    private InetAddress fromAddress;
    private Integer fromPort;
    private Long pt;
    private InetAddress toAddress;
    private Integer toPort;
    private Long ourSsrc;
    private Double sentNtp;
    private Long sentRtp;
    private Long sentPackets;
    private Long sentOctets;
    private Long cumulativeLoss;
    private Long theirLastSr;

    private String channel;
    private String language;
    private String report0SequenceNumberCycles;
    private String ssrc;
    private String linkedId;
    private String report0lsr;
    private String report0Sourcessrc;
    private Double report0dlsr;
    private String uniqueid;
    private Integer reportCount;
    private Integer report0CumulativeLost;
    private Integer report0FractionLost;
    private Integer report0iaJitter;
    private Integer report0HighestSequence;
    private String accountCode;
    
    public RtcpSentEvent(Object source)
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
     * @see #RtcpReceivedEvent.PT_SENDER_REPORT
     * @see #RtcpReceivedEvent.PT_RECEIVER_REPORT
     */
    public Long getPt()
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
            this.pt = Long.parseLong(ptString.substring(0, ptString.indexOf('(')));
        }
        else
        {
            this.pt = Long.parseLong(ptString);
        }
    }
    
    
    /**
     * Returns the IP address the RTCP message has been sent to.
     *
     * @return the IP address the RTCP message has been sent to.
     */
    public InetAddress getToAddress()
    {
        return toAddress;
    }

    /**
     * Returns the port the RTCP message has been sent to.
     *
     * @return the port the RTCP message has been sent to.
     */
    public Integer getToPort()
    {
        return toPort;
    }

    public void setTo(String to)
    {
        // Format is "%s:%d"
        this.toAddress = stringToAddress(to);
        this.toPort = stringToPort(to);
    }

    /**
     * Returns our synchronization source identifier that uniquely identifies the source of a stream.
     * @return our synchronization source identifier.
     */
    public Long getOurSsrc()
    {
        return ourSsrc;
    }

    public void setOurSsrc(Long ourSsrc)
    {
        this.ourSsrc = ourSsrc;
    }

    public Double getSentNtp()
    {
        return sentNtp;
    }

    public void setSentNtp(Double sentNtp)
    {
        this.sentNtp = sentNtp;
    }

    public Long getSentRtp()
    {
        return sentRtp;
    }

    public void setSentRtp(Long sentRtp)
    {
        this.sentRtp = sentRtp;
    }

    /**
     * Returns the number of packets sent.
     *
     * @return the number of packets sent.
     */
    public Long getSentPackets()
    {
        return sentPackets;
    }

    public void setSentPackets(Long sentPackets)
    {
        this.sentPackets = sentPackets;
    }

    /**
     * Returns the number of octets (bytes) sent.
     *
     * @return the number of octets (bytes) sent.
     */
    public Long getSentOctets()
    {
        return sentOctets;
    }

    public void setSentOctets(Long sentOctets)
    {
        this.sentOctets = sentOctets;
    }

    public Long getCumulativeLoss()
    {
        return cumulativeLoss;
    }

    public void setCumulativeLoss(Long cumulativeLoss)
    {
        this.cumulativeLoss = cumulativeLoss;
    }

    public Long getTheirLastSr()
    {
        return theirLastSr;
    }

    public void setTheirLastSr(Long theirLastSr)
    {
        this.theirLastSr = theirLastSr;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getReport0SequenceNumberCycles()
    {
        return report0SequenceNumberCycles;
    }

    public void setReport0SequenceNumberCycles(String report0SequenceNumberCycles)
    {
        this.report0SequenceNumberCycles = report0SequenceNumberCycles;
    }

    public String getSsrc()
    {
        return ssrc;
    }

    public void setSsrc(String ssrc)
    {
        this.ssrc = ssrc;
    }

    public String getLinkedId()
    {
        return linkedId;
    }

    public void setLinkedId(String linkedId)
    {
        this.linkedId = linkedId;
    }

    public String getReport0lsr()
    {
        return report0lsr;
    }

    public void setReport0lsr(String report0lsr)
    {
        this.report0lsr = report0lsr;
    }

    public String getReport0Sourcessrc()
    {
        return report0Sourcessrc;
    }

    public void setReport0Sourcessrc(String report0Sourcessrc)
    {
        this.report0Sourcessrc = report0Sourcessrc;
    }

    public Double getReport0dlsr()
    {
        return report0dlsr;
    }

    public void setReport0dlsr(Double report0dlsr)
    {
        this.report0dlsr = report0dlsr;
    }

    public String getUniqueid()
    {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid)
    {
        this.uniqueid = uniqueid;
    }

    public Integer getReportCount()
    {
        return reportCount;
    }

    public void setReportCount(Integer reportCount)
    {
        this.reportCount = reportCount;
    }

    public Integer getReport0CumulativeLost()
    {
        return report0CumulativeLost;
    }

    public void setReport0CumulativeLost(Integer report0CumulativeLost)
    {
        this.report0CumulativeLost = report0CumulativeLost;
    }

    public Integer getReport0FractionLost()
    {
        return report0FractionLost;
    }

    public void setReport0FractionLost(Integer report0FractionLost)
    {
        this.report0FractionLost = report0FractionLost;
    }

    public Integer getReport0iaJitter()
    {
        return report0iaJitter;
    }

    public void setReport0iaJitter(Integer report0iaJitter)
    {
        this.report0iaJitter = report0iaJitter;
    }

    public Integer getReport0HighestSequence()
    {
        return report0HighestSequence;
    }

    public void setReport0HighestSequence(Integer report0HighestSequence)
    {
        this.report0HighestSequence = report0HighestSequence;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }
}
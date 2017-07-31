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
    private static final long serialVersionUID = 2L;

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
    private Long pt;
    private Long receptionReports;
    private Long senderSsrc;
    private Long packetsLost;
    private Long highestSequence;
    private Long sequenceNumberCycles;
    private Double lastSr;
    private Double rtt;
    
    private String channel;
    private String language;    
    private String report0SequenceNumberCycles;
    private String ssrc;
    private String linkedId;
    private String report0lsr;
    private Long sentOctets;
    private String report0Sourcessrc;
    private Double report0dlsr;
    private String uniqueid;
    private Integer reportCount;
    private Integer report0CumulativeLost;
    private Integer report0FractionLost;
    private Long report0iaJitter;
    private Integer report0HighestSequence;
    private InetAddress toAddress;
    private Integer toPort;
    private String sentntp;
    private Integer sentPackets;
    private Long sentrtp;
    private String accountCode;
    

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
     * Indicates the format of the payload, typical values are 200 for sender reports and
     * 201 for receiver reports.
     *
     * @return the format of the payload.
     * @see #PT_SENDER_REPORT
     * @see #PT_RECEIVER_REPORT
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

        try
        {
            if (ptString.indexOf('(') > 0)
            {
                this.pt = Long.parseLong(ptString.substring(0, ptString.indexOf('(')));
            }
            else
            {
                this.pt = Long.parseLong(ptString);
            }

        } catch (NumberFormatException e)
        {
            throw new NumberFormatException(String.format("Input string [%s] is not a parsable long", ptString));
        }
    }

    public Long getReceptionReports()
    {
        return receptionReports;
    }

    public void setReceptionReports(Long receptionReports)
    {
        this.receptionReports = receptionReports;
    }

    /**
     * Returns the synchronization source identifier of the sender.
     *
     * @return the synchronization source identifier of the sender.
     */
    public Long getSenderSsrc()
    {
        return senderSsrc;
    }

    public void setSenderSsrc(Long senderSsrc)
    {
        this.senderSsrc = senderSsrc;
    }

    /**
     * Returns the number of packets lost so far.
     *
     * @return the number of packets lost.
     */
    public Long getPacketsLost()
    {
        return packetsLost;
    }

    public void setPacketsLost(Long packetsLost)
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

    public Long getSequenceNumberCycles()
    {
        return sequenceNumberCycles;
    }

    public void setSequenceNumberCycles(Long sequenceNumberCycles)
    {
        this.sequenceNumberCycles = sequenceNumberCycles;
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
    public Double getRtt()
    {
        return rtt;
    }

    public void setRtt(String rttString)
    {
        this.rtt = secStringToDouble(rttString);
    }
    
    public Long getRttAsMillseconds()
    {
    	return (long) (rtt * 1000);
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

    public String getReport0lsr()
    {
        return report0lsr;
    }

    public void setReport0lsr(String report0lsr)
    {
        this.report0lsr = report0lsr;
    }

    public Long getSentOctets()
    {
        return sentOctets;
    }

    public void setSentOctets(Long sentOctets)
    {
        this.sentOctets = sentOctets;
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

    public Long getReport0iaJitter()
    {
        return report0iaJitter;
    }

    public void setReport0iaJitter(Long report0iaJitter)
    {
        this.report0iaJitter = report0iaJitter;
    }

    public InetAddress getToAddress()
    {
        return toAddress;
    }

    public String getSentntp()
    {
        return sentntp;
    }

    public void setSentntp(String sentntp)
    {
        this.sentntp = sentntp;
    }

    public Long getSentrtp()
    {
        return sentrtp;
    }

    public void setSentrtp(Long sentrtp)
    {
        this.sentrtp = sentrtp;
    }

    public Integer getReportCount()
    {
        return reportCount;
    }

    public void setReportCount(Integer reportCount)
    {
        this.reportCount = reportCount;
    }

    public Integer getReport0HighestSequence()
    {
        return report0HighestSequence;
    }

    public void setReport0HighestSequence(Integer report0HighestSequence)
    {
        this.report0HighestSequence = report0HighestSequence;
    }

    public String getLinkedId()
    {
        return linkedId;
    }

    public void setLinkedId(String linkedId)
    {
        this.linkedId = linkedId;
    }

    public Integer getSentPackets()
    {
        return sentPackets;
    }

    public void setSentPackets(Integer sentPackets)
    {
        this.sentPackets = sentPackets;
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

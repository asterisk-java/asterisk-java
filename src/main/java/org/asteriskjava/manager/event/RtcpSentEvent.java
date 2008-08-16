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

    private InetAddress toAddress;
    private Integer toPort;
    private Integer ourSsrc;
    private Double sentNtp;
    private Integer sentRtp;
    private Integer sentPackets;
    private Integer sentOctets;
    private Integer cumulativeLoss;
    private Integer theirLastSr;

    public RtcpSentEvent(Object source)
    {
        super(source);
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
    public Integer getOurSsrc()
    {
        return ourSsrc;
    }

    public void setOurSsrc(Integer ourSsrc)
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

    public Integer getSentRtp()
    {
        return sentRtp;
    }

    public void setSentRtp(Integer sentRtp)
    {
        this.sentRtp = sentRtp;
    }

    /**
     * Returns the number of packets sent.
     *
     * @return the number of packets sent.
     */
    public Integer getSentPackets()
    {
        return sentPackets;
    }

    public void setSentPackets(Integer sentPackets)
    {
        this.sentPackets = sentPackets;
    }

    /**
     * Returns the number of octets (bytes) sent.
     *
     * @return the number of octets (bytes) sent.
     */
    public Integer getSentOctets()
    {
        return sentOctets;
    }

    public void setSentOctets(Integer sentOctets)
    {
        this.sentOctets = sentOctets;
    }

    public Integer getCumulativeLoss()
    {
        return cumulativeLoss;
    }

    public void setCumulativeLoss(Integer cumulativeLoss)
    {
        this.cumulativeLoss = cumulativeLoss;
    }

    public Integer getTheirLastSr()
    {
        return theirLastSr;
    }

    public void setTheirLastSr(Integer theirLastSr)
    {
        this.theirLastSr = theirLastSr;
    }
}
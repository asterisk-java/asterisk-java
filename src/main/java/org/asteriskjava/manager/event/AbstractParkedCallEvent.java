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
 * Abstract base class for several call parking related events.
 *
 * @author srt
 * @version $Id$
 * @since 0.2
 */
public abstract class AbstractParkedCallEvent extends ManagerEvent
{
    private static final long serialVersionUID = 0L;
    private String uniqueId;
    // Previously: Channel
    private String parkeeChannel;
    private Integer parkeeChannelState;
    private String parkeeChannelStateDesc;
    // previously CallerID
    private String parkeeCallerIDNum;
    // Previously CallerIDName
    private String parkeeCallerIDName;
    private String parkeeConnectedLineNum;
    private String parkeeConnectedLineName;
    private String parkeeLanguage;
    private String parkeeAccountCode;
    private String parkeeContext;
    private String parkeeExten;
    private Integer parkeePriority;
    private String parkeeUniqueid;
    // Previously: From
    private String parkerDialString;
    private String parkingLot;
    // previously Exten
    private String parkingSpace;
    private Long parkingTimeout;
    private Long parkingDuration;

    protected AbstractParkedCallEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the name of the channel that parked the call. use
     * getParkerDialString() instead
     */
    @Deprecated
    public String getFrom()
    {
        return getParkerDialString();
    }

    /**
     * Sets the name of the channel that parked the call.
     */
    @Deprecated
    public void setFrom(String from)
    {
        this.setParkerDialString(from);
    }

    /**
     * Returns the parking lot.
     * <p>
     * Available since Asterisk 1.6.
     *
     * @return the parking lot.
     * @since 1.0.0
     */
    public String getParkingLot()
    {
        return parkingLot;
    }

    /**
     * Sets the parking lot.
     *
     * @param parkingLot the parking lot.
     */
    public void setParkingLot(String parkingLot)
    {
        this.parkingLot = parkingLot;
    }

    /**
     * Returns the unique id of the parked channel.
     * <p>
     * Note: This property is not set properly by all versions of Asterisk, see
     * <a href="http://bugs.digium.com/view.php?id=13323">http://bugs.digium.
     * com/ view.php?id=13323</a> and
     * <a href="http://bugs.digium.com/view.php?id=13358" >http://bugs.digium.
     * com/view.php?id=13358</a> for more information. Use {@link #getChannel()}
     * instead.
     *
     * @return the unique id of the parked channel.
     */
    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    @Deprecated
    public void setChannel(String channel)
    {
        this.setParkeeChannel(channel);
    }

    /**
     * use getParkeeChannel() instead
     * 
     * @return
     */
    @Deprecated
    public String getChannel()
    {
        return this.getParkeeChannel();
    }

    public String getParkeeChannel()
    {
        return parkeeChannel;
    }

    public void setParkeeChannel(String parkeeChannel)
    {
        this.parkeeChannel = parkeeChannel;
    }

    public Integer getParkeeChannelState()
    {
        return parkeeChannelState;
    }

    public void setParkeeChannelState(Integer parkeeChannelState)
    {
        this.parkeeChannelState = parkeeChannelState;
    }

    public String getParkeeChannelStateDesc()
    {
        return parkeeChannelStateDesc;
    }

    public void setParkeeChannelStateDesc(String parkeeChannelStateDesc)
    {
        this.parkeeChannelStateDesc = parkeeChannelStateDesc;
    }

    public String getParkeeCallerIDNum()
    {
        return parkeeCallerIDNum;
    }

    public void setParkeeCallerIDNum(String parkeeCallerIDNum)
    {
        this.parkeeCallerIDNum = parkeeCallerIDNum;
    }

    public String getParkeeCallerIDName()
    {
        return parkeeCallerIDName;
    }

    public void setParkeeCallerIDName(String parkeeCallerIDName)
    {
        this.parkeeCallerIDName = parkeeCallerIDName;
    }

    public String getParkeeConnectedLineNum()
    {
        return parkeeConnectedLineNum;
    }

    public void setParkeeConnectedLineNum(String parkeeConnectedLineNum)
    {
        this.parkeeConnectedLineNum = parkeeConnectedLineNum;
    }

    public String getParkeeConnectedLineName()
    {
        return parkeeConnectedLineName;
    }

    public void setParkeeConnectedLineName(String parkeeConnectedLineName)
    {
        this.parkeeConnectedLineName = parkeeConnectedLineName;
    }

    public String getParkeeLanguage()
    {
        return parkeeLanguage;
    }

    public void setParkeeLanguage(String parkeeLanguage)
    {
        this.parkeeLanguage = parkeeLanguage;
    }

    public String getParkeeAccountCode()
    {
        return parkeeAccountCode;
    }

    public void setParkeeAccountCode(String parkeeAccountCode)
    {
        this.parkeeAccountCode = parkeeAccountCode;
    }

    public String getParkeeContext()
    {
        return parkeeContext;
    }

    public void setParkeeContext(String parkeeContext)
    {
        this.parkeeContext = parkeeContext;
    }

    public String getParkeeExten()
    {
        return parkeeExten;
    }

    public void setParkeeExten(String parkeeExten)
    {
        this.parkeeExten = parkeeExten;
    }

    public Integer getParkeePriority()
    {
        return parkeePriority;
    }

    public void setParkeePriority(Integer parkeePriority)
    {
        this.parkeePriority = parkeePriority;
    }

    public String getParkeeUniqueid()
    {
        return parkeeUniqueid;
    }

    public void setParkeeUniqueid(String parkeeUniqueid)
    {
        this.parkeeUniqueid = parkeeUniqueid;
    }

    public String getParkerDialString()
    {
        return parkerDialString;
    }

    public void setParkerDialString(String parkerDialString)
    {
        this.parkerDialString = parkerDialString;
    }

    public String getParkinglot()
    {
        return parkingLot;
    }

    public void setParkinglot(String parkinglot)
    {
        this.parkingLot = parkinglot;
    }

    public String getParkingSpace()
    {
        return parkingSpace;
    }

    public void setParkingSpace(String parkingSpace)
    {
        this.parkingSpace = parkingSpace;
    }

    public Long getParkingTimeout()
    {
        return parkingTimeout;
    }

    public void setParkingTimeout(Long parkingTimeout)
    {
        this.parkingTimeout = parkingTimeout;
    }

    public Long getParkingDuration()
    {
        return parkingDuration;
    }

    public void setParkingDuration(Long parkingDuration)
    {
        this.parkingDuration = parkingDuration;
    }

    @Deprecated
    public String getCallerId()
    {
        return getParkeeCallerIDNum();
    }

    @Deprecated
    public void setCallerId(String callerId)
    {
        setParkeeCallerIDNum(callerId);
    }
}

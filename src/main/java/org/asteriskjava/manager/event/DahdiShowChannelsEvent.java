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
 * A DahdiShowChannelsEvent is triggered in response to a DahdiShowChannelsAction and shows the state of
 * a Dahdi channel.
 * 
 * @see org.asteriskjava.manager.action.DahdiShowChannelsAction
 * 
 * @author srt
 * @version $Id$
 */
public class DahdiShowChannelsEvent extends ResponseEvent
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = -3613642267527361400L;
    private Integer Dahdichannel;
    private String signalling;
    private String signallingcode;
    private Boolean dnd;
    private String alarm;
    private String uniqueid;
    private String accountcode;
    private String channel;

    public String getAccountcode() {
        return accountcode;
    }

    public void setAccountcode(String accountcode) {
        this.accountcode = accountcode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getDahdichannel() {
        return Dahdichannel;
    }

    public void setDahdichannel(Integer Dahdichannel) {
        this.Dahdichannel = Dahdichannel;
    }

    public String getSignallingcode() {
        return signallingcode;
    }

    public void setSignallingcode(String signallingcode) {
        this.signallingcode = signallingcode;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }


    

    /**
     * @param source
     */
    public DahdiShowChannelsEvent(Object source)
    {
        super(source);
    }

  
    /**
     * Returns the signalling of this Dahdi channel.<p>
     * Possible values are:
     * <ul>
     * <li>E &amp; M Immediate</li>
     * <li>E &amp; M Wink</li>
     * <li>E &amp; M E1</li>
     * <li>Feature Group D (DTMF)</li>
     * <li>Feature Group D (MF)</li>
     * <li>Feature Group B (MF)</li>
     * <li>E911 (MF)</li>
     * <li>FXS Loopstart</li>
     * <li>FXS Groundstart</li>
     * <li>FXS Kewlstart</li>
     * <li>FXO Loopstart</li>
     * <li>FXO Groundstart</li>
     * <li>FXO Kewlstart</li>
     * <li>PRI Signalling</li>
     * <li>R2 Signalling</li>
     * <li>SF (Tone) Signalling Immediate</li>
     * <li>SF (Tone) Signalling Wink</li>
     * <li>SF (Tone) Signalling with Feature Group D (DTMF)</li>
     * <li>SF (Tone) Signalling with Feature Group D (MF)</li>
     * <li>SF (Tone) Signalling with Feature Group B (MF)</li>
     * <li>GR-303 Signalling with FXOKS</li>
     * <li>GR-303 Signalling with FXSKS</li>
     * <li>Pseudo Signalling</li>
     * </ul>
     */
    public String getSignalling()
    {
        return signalling;
    }

    /**
     * Sets the signalling of this Dahdi channel.
     */
    public void setSignalling(String signalling)
    {
        this.signalling = signalling;
    }

    /**
     * Returns whether dnd (do not disturb) is enabled for this Dahdi channel.
     * 
     * @return Boolean.TRUE if dnd is enabled, Boolean.FALSE if it is disabled,
     *         <code>null</code> if not set.
     * @since 0.3
     */
    public Boolean getDnd()
    {
        return dnd;
    }

    /**
     * Sets whether dnd (do not disturb) is enabled for this Dahdi channel.
     * 
     * @param dnd Boolean.TRUE if dnd is enabled, Boolean.FALSE if it is disabled.
     * @since 0.3
     */
    public void setDnd(Boolean dnd)
    {
        this.dnd = dnd;
    }

    /**
     * Returns the alarm state of this Dahdi channel.<p>
     * This may be one of
     * <ul>
     * <li>Red Alarm</li>
     * <li>Yellow Alarm</li>
     * <li>Blue Alarm</li>
     * <li>Recovering</li>
     * <li>Loopback</li>
     * <li>Not Open</li>
     * <li>No Alarm</li>
     * </ul>
     */
    public String getAlarm()
    {
        return alarm;
    }

    /**
     * Sets the alarm state of this Dahdi channel.
     */
    public void setAlarm(String alarm)
    {
        this.alarm = alarm;
    }
}

package org.asteriskjava.pbx.internal.managerAPI;

import java.util.HashMap;

import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.agi.AgiChannelActivityDial;
import org.asteriskjava.pbx.agi.AgiChannelActivityVoicemail;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class RedirectCall
{
    /*
     * this class generates and issues ActionEvents to asterisk through the
     * manager. This is the asterisk coal face.
     */
    private static final Log logger = LogFactory.getLog(RedirectCall.class);

    static public void setAutoAnswer(final HashMap<String, String> myVars, final AsteriskSettings settings)
    {
        myVars.put(AsteriskPBX.getSIPADDHeader(false, true), settings.getAutoAnswer());
        RedirectCall.logger.debug("auto answer"); //$NON-NLS-1$
    }

    public boolean redirect(final Channel channel, final EndPoint targetEndPoint, final String context,
            final boolean autoAnswer) throws PBXException
    {
        // Set or clear the auto answer header.
        // Clearing is important as it may have been set during the
        // initial answer sequence. If we don't clear it then then transfer
        // target will be auto-answered, which is fun but bad.
        String sipHeader = "";
        if (autoAnswer)
        {
            sipHeader = PBXFactory.getActiveProfile().getAutoAnswer();
        }

        /*
         * redirects the specified channel to the specified extension. Returns
         * true or false reflecting success.
         */
        redirect(channel, new AgiChannelActivityDial(targetEndPoint.getFullyQualifiedName(), sipHeader));

        RedirectCall.logger.debug("redirected " + channel + " to " + targetEndPoint); //$NON-NLS-1$ //$NON-NLS-2$
        return true;
    }

    public boolean redirectToVoicemail(final Channel channel, final EndPoint mailbox) throws PBXException
    {

        redirect(channel, new AgiChannelActivityVoicemail(mailbox.getFullyQualifiedName()));
        return true;
    }

    public void redirect(Channel channel, AgiChannelActivityAction channelActivityHold) throws PBXException
    {
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        if (!pbx.moveChannelToAgi(channel))
        {
            throw new PBXException("Channel: " + channel + " couldn't be moved to agi");
        }
        if (!pbx.waitForChannelToQuiescent(channel, 3000))
            throw new PBXException("Channel: " + channel + " cannot be redirected as it is still in transition.");

        channel.setCurrentActivityAction(channelActivityHold);

    }

}

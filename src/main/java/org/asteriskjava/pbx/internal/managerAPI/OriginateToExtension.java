package org.asteriskjava.pbx.internal.managerAPI;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.NewChannelListener;
import org.asteriskjava.pbx.PBXFactory;

public class OriginateToExtension extends OriginateBaseClass
{

    /*
     * this class generates and issues ActionEvents to asterisk through the
     * manager. This is the asterisk coal face.
     */

    public OriginateToExtension(final NewChannelListener listener)
    {
        super(listener, null, null);

    }

    public OriginateResult originate(final EndPoint localHandset, final EndPoint targetExtension, final boolean autoAnswer,
            final CallerID callerID, final String context)
    {
        /*
         * A new call is originated on the nominated channel to the specified
         * extension.
         */
        OriginateBaseClass.logger.debug("originate connecting localHandset " + localHandset + " to Extension " //$NON-NLS-1$ //$NON-NLS-2$
                + targetExtension + " autoAnswer " + autoAnswer); //$NON-NLS-1$
        final AsteriskSettings profile = PBXFactory.getActiveProfile();

        final HashMap<String, String> myVars = new HashMap<>(1);
        if (autoAnswer == true)
        {
            RedirectCall.setAutoAnswer(myVars, profile);
        }

        return this.originate(localHandset, targetExtension, myVars, callerID, null, false, context);
    }

    public OriginateResult originate(final EndPoint localHandset, final EndPoint targetExtension, final boolean autoAnswer,
            final String context, final CallerID callerID, Integer timeout, final boolean hideCallerId,
            Map<String, String> channelVarsToSet)
    {
        /*
         * A new call is originated on the nominated channel to the specified
         * extension.
         */
        OriginateBaseClass.logger.debug("originate connection localHandset " + localHandset + " to Extension " //$NON-NLS-1$ //$NON-NLS-2$
                + targetExtension + " autoAnswer " + autoAnswer); //$NON-NLS-1$
        final AsteriskSettings profile = PBXFactory.getActiveProfile();

        final HashMap<String, String> myVars = new HashMap<>(1);
        if (autoAnswer == true)
        {
            RedirectCall.setAutoAnswer(myVars, profile);
        }
        if (channelVarsToSet != null)
        {
            myVars.putAll(channelVarsToSet);
        }

        return this.originate(localHandset, targetExtension, myVars, callerID, timeout, hideCallerId, context);
    }

    @Override
    public ListenerPriority getPriority()
    {
        return ListenerPriority.NORMAL;
    }

}

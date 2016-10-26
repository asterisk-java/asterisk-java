package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class StatusAction extends AbstractManagerAction implements EventGeneratingAction
{
	// used when the status of a single channel has been requested.
	final private Channel _channel;

	public StatusAction()
	{
		this._channel = null;
	}

	public StatusAction(Channel channel)
	{
		AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

		// channel specific request only available since 1.6
		if (pbx.getVersion().isAtLeast(AsteriskVersion.ASTERISK_1_6))
			this._channel = channel;
		else
			this._channel = null;
	}

	public org.asteriskjava.manager.action.ManagerAction getAJAction()
	{
		final org.asteriskjava.manager.action.StatusAction action;
		if (_channel == null)
			action = new org.asteriskjava.manager.action.StatusAction();
		else
			action = new org.asteriskjava.manager.action.StatusAction(_channel.getChannelName());

		return action;
	}

	@Override
	public org.asteriskjava.manager.action.EventGeneratingAction getAJEventGeneratingAction()
	{
		final org.asteriskjava.manager.action.StatusAction action = (org.asteriskjava.manager.action.StatusAction) getAJAction();
		action.setActionId(getActionId());

		return action;
	}

	public String toString()
	{
		return "StatusAction"; //$NON-NLS-1$
	}

}

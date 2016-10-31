package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.DTMFTone;

public class PlayDtmfAction extends AbstractManagerAction
{

	private final Channel _channel;
	private final DTMFTone _tone;

	public PlayDtmfAction(final Channel channel, final DTMFTone tone)
	{
		this._channel = channel;
		this._tone = tone;
	}

	@Override
	public ManagerAction getAJAction()
	{
		final org.asteriskjava.manager.action.PlayDtmfAction action = new org.asteriskjava.manager.action.PlayDtmfAction();
		action.setActionId(this.getActionId());
		action.setChannel(this._channel.getChannelName());
		action.setDigit(this._tone.toString());

		return action;
	}

	public String toString()
	{
		return "PlayDtmfAction: channel=" + this._channel + " tone=" + this._tone; //$NON-NLS-1$ //$NON-NLS-2$
	}

}

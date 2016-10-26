package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.pbx.Channel;

public class MonitorAction extends AbstractManagerAction
{

	private final Channel _channel;
	private final String _file;
	private final String _format;
	private final boolean _mix;

	public MonitorAction(final Channel channel, final String file, final String format, final boolean mix)
	{
		this._channel = channel;
		this._file = file;
		this._format = format;
		this._mix = mix;
	}

	@Override
	public org.asteriskjava.manager.action.ManagerAction getAJAction()
	{
		final org.asteriskjava.manager.action.MonitorAction action = new org.asteriskjava.manager.action.MonitorAction(
				this._channel.getChannelName(), this._file, this._format, this._mix);
		action.setActionId(this.getActionId());

		return action;
	}

	public String toString()
	{
		return "MonitorAction: channel=" + this._channel + " file=" + this._file; //$NON-NLS-1$ //$NON-NLS-2$
	}

}

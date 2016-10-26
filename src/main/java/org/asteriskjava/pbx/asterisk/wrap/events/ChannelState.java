package org.asteriskjava.pbx.asterisk.wrap.events;

/**
 * Holds the state of a channel as used by NewStateEvent and NewChannelEvent.
 * 
 * @author bsutton
 *
 */
public enum ChannelState
{
	Down, Rsrvd, OffHook, Dialing, Ring, Ringing, Up, Busy, DialingOffhook("Dialing Offhook"), PreRing("Pre-ring"); //$NON-NLS-1$ //$NON-NLS-2$

	String _text;

	static ChannelState valueOfDesc(String description)
	{
		ChannelState theState = null;

		for (ChannelState aState : ChannelState.values())
		{
			if (aState._text.compareToIgnoreCase(description) == 0)
			{
				theState = aState;
				break;
			}
		}

		return theState;

	}

	ChannelState(String text)
	{
		this._text = text;
	}

	ChannelState()
	{
		this._text = this.name();
	}

}
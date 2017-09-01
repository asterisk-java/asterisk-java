package org.asteriskjava.pbx.asterisk.wrap.actions;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;

public class RedirectAction extends AbstractManagerAction
{

    private final Channel _channel;
    private final String _context;
    private final EndPoint _exten;
    private final Integer _priority;
    private Channel _extraChannel;
    private String _extraContext;
    private EndPoint _extraExten;
    private Integer _extraPriority;

    public RedirectAction(final Channel channel, final String context, final EndPoint extension, final int priority)
    {
        if (channel == null)
        {
            throw new NullPointerException("channel cannot be null");
        }
        if (context == null)
        {
            throw new NullPointerException("context cannot be null");
        }
        if (extension == null)
        {
            throw new NullPointerException("extension cannot be null");
        }

        this._channel = channel;
        this._context = context;
        this._exten = extension;
        this._priority = priority;
    }

    @Override
    public String toString()
    {
        return "RedirectAction: chanel=" + this._channel + " context=" + this._context //$NON-NLS-1$ //$NON-NLS-2$
                + " exten=" + this._exten + " priority=" + this._priority //$NON-NLS-1$ //$NON-NLS-2$
                + " extraContext=" + this._extraContext //$NON-NLS-1$
                + " extraChannel=" + this._extraChannel //$NON-NLS-1$
                + " extraExten=" + this._extraExten; //$NON-NLS-1$
    }

    @Override
    public org.asteriskjava.manager.action.ManagerAction getAJAction()
    {
        final org.asteriskjava.manager.action.RedirectAction action = new org.asteriskjava.manager.action.RedirectAction();
        action.setActionId(this.getActionId());
        action.setChannel(this._channel.getChannelName().toLowerCase());
        action.setContext(this._context);
        action.setExten(this._exten.getFullyQualifiedName());
        action.setPriority(this._priority);
        if (this._extraChannel != null)
            action.setExtraChannel(this._extraChannel.getChannelName().toLowerCase());
        action.setExtraContext(this._extraContext);
        if (this._extraExten != null)
            action.setExtraExten(this._extraExten.getFullyQualifiedName());
        action.setExtraPriority(this._extraPriority);

        return action;
    }

    public void setExtraChannel(final Channel extraChannel)
    {
        this._extraChannel = extraChannel;
    }

    public void setExtraContext(final String extraContext)
    {
        this._extraContext = extraContext;
    }

    public void setExtraExten(final EndPoint extraExten)
    {
        this._extraExten = extraExten;
    }

    public void setExtraPriority(final int priority)
    {
        this._extraPriority = priority;
    }
}

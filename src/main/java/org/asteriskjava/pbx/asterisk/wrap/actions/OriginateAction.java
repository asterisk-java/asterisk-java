package org.asteriskjava.pbx.asterisk.wrap.actions;

import java.util.Map;

import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.internal.asterisk.CallerIDImpl;

public class OriginateAction extends AbstractManagerAction
{

    /**
     * Asterisk uses the term channel but it actually appears to be an endpoint
     * or a channel. Hence we support both. Either of the following may be set
     * but NOT BOTH.
     */
    private EndPoint _endPoint;
    private Channel _channel;

    private String _option;

    private String _context;

    private EndPoint _extension;

    private int _priority;

    private int _callingPres;

    private CallerID _callerID;

    private Map<String, String> _variables;

    private Boolean _async;

    private long _timeout;

    public OriginateAction()
    {
    }

    public String toString()
    {
        return "OriginateAction: endPoint/Channel=" + (this._endPoint == null ? this._channel : this._endPoint) + " context=" //$NON-NLS-1$ //$NON-NLS-2$
                + this._context + " extension=" + this._extension + " callerId=" + this._callerID; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public org.asteriskjava.manager.action.ManagerAction getAJAction()
    {
        final org.asteriskjava.manager.action.OriginateAction action = new org.asteriskjava.manager.action.OriginateAction();
        action.setActionId(getActionId());

        // Was the channel or an end point passed.
        String channel = (this._channel != null ? this._channel.getChannelName() : this._endPoint.getFullyQualifiedName());
        if (this._option != null)
        {
            channel += this._option;
        }
        action.setChannel((channel));
        action.setContext(this._context);
        action.setExten(this._extension.getFullyQualifiedName());
        action.setPriority(this._priority);
        action.setCallingPres(this._callingPres);
        action.setCallerId(((CallerIDImpl) this._callerID).formatted());
        action.setVariables(this._variables);
        action.setAsync(this._async);
        action.setTimeout(this._timeout);

        return action;
    }

    public void setEndPoint(final EndPoint endPoint)
    {
        this._endPoint = endPoint;
        this._channel = null;
    }

    public void setChannel(Channel channel)
    {
        this._channel = channel;
        this._endPoint = null;

    }

    public void setOption(final String option)
    {
        this._option = option;

    }

    public void setContext(final String context)
    {
        this._context = context;

    }

    public void setExten(final EndPoint extension)
    {
        this._extension = extension;

    }

    public void setPriority(final int priority)
    {
        this._priority = priority;

    }

    public void setCallingPres(final int callingPres)
    {
        this._callingPres = callingPres;

    }

    public void setCallerId(final CallerID callerID)
    {
        this._callerID = callerID;

    }

    public void setVariables(final Map<String, String> variables)
    {
        if (_variables == null)
        {
            this._variables = variables;
        }
        else
        {
            this._variables.putAll(variables);
        }

    }

    public void setAsync(final boolean async)
    {
        this._async = async;

    }

    public void setTimeout(final long timeout)
    {
        this._timeout = timeout;

    }
}

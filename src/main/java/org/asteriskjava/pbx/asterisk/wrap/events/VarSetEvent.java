package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class VarSetEvent extends ManagerEvent implements ChannelEvent
{
    private static final long serialVersionUID = 1L;

    /**
     * The name of the channel.
     */
    private final Channel _channel;

    private String _variableName;

    private String _variableValue;

    public VarSetEvent(final org.asteriskjava.manager.event.VarSetEvent event) throws InvalidChannelName
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
        this._channel = pbx.internalRegisterChannel(event.getChannel(), event.getUniqueId());
        this._variableName = event.getVariable();
        this._variableValue = event.getValue();
    }

    @Override
    public Channel getChannel()
    {
        return this._channel;
    }

    public String getVariableName()
    {
        return this._variableName;
    }

    public String getVariableValue()
    {
        return this._variableValue;
    }

}

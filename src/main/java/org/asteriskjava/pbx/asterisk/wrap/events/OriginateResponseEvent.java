package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.TechType;
import org.asteriskjava.pbx.internal.core.ChannelImpl;

public class OriginateResponseEvent extends ResponseEvent implements ChannelEvent
{

    private static final long serialVersionUID = 1L;

    // reflects whether we have a iChannel or an iEndPoint. We can't have both.
    private final boolean isChannel;
    private final Channel channel;
    private final EndPoint endPoint;
    private final String response;
    private final String context;
    private final String exten;
    private final Integer reason;

    public OriginateResponseEvent(final org.asteriskjava.manager.event.OriginateResponseEvent event)
            throws InvalidChannelName
    {
        super(event);

        PBX pbx = PBXFactory.getActivePBX();

        // The doco says this should be a channel but under 1.4 it comes up as
        // an endpoint.
        this.isChannel = pbx.isChannel(event.getChannel());
        if (this.isChannel)
        {
            this.channel = ChannelEventHelper.registerChannel(event.getChannel(),
                    (event.getUniqueId() == null ? ChannelImpl.UNKNOWN_UNIQUE_ID : event.getUniqueId()),
                    event.getCallerIdNum(), event.getCallerIdName());
            this.endPoint = null;
        }
        else
        {
            this.endPoint = pbx.buildEndPoint(TechType.SIP, event.getChannel());
            this.channel = null;
        }

        this.response = event.getResponse();
        this.context = event.getContext();
        this.exten = event.getExten();
        this.reason = event.getReason();

    }

    public String getResponse()
    {
        return this.response;
    }

    public String getContext()
    {
        return this.context;
    }

    public String getExten()
    {
        return this.exten;
    }

    public Integer getReason()
    {
        return this.reason;
    }

    public boolean isChannel()
    {
        return this.isChannel;
    }

    @Override
    public Channel getChannel()
    {
        return this.channel;
    }

    public EndPoint getEndPoint()
    {
        return this.endPoint;
    }

    public boolean isSuccess()
    {
        return "Success".equalsIgnoreCase(this.response); //$NON-NLS-1$
    }

}

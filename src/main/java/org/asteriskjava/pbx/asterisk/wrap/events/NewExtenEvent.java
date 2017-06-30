package org.asteriskjava.pbx.asterisk.wrap.events;

import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;

public class NewExtenEvent extends ManagerEvent implements ChannelEvent
{
    private static final long serialVersionUID = 1L;

    private final String context;
    private final String extension;
    private final String application;
    private final String appData;
    private final Integer priority;
    private final Channel channel;

    public NewExtenEvent(final org.asteriskjava.manager.event.NewExtenEvent event) throws InvalidChannelName
    {
        super(event);
        final AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        this.channel = pbx.internalRegisterChannel(event.getChannel(), event.getUniqueId());
        this.context = event.getContext();
        this.extension = event.getExtension();
        this.application = event.getApplication();
        this.appData = event.getAppData();
        this.priority = event.getPriority();
    }

    public String getContext()
    {
        return this.context;
    }

    public String getExtension()
    {
        return this.extension;
    }

    public String getApplication()
    {
        return this.application;
    }

    public String getAppData()
    {
        return this.appData;
    }

    public Integer getPriority()
    {
        return this.priority;
    }

    @Override
    public Channel getChannel()
    {
        return this.channel;
    }

}

package org.asteriskjava.pbx.asterisk.wrap.events;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.InvalidChannelName;

public class QueueCallerLeaveEvent extends ChannelEventHelper
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(QueueCallerLeaveEvent.class);

    private String queue;

    public QueueCallerLeaveEvent(final org.asteriskjava.manager.event.QueueCallerLeaveEvent event) throws InvalidChannelName
    {
        super(event.getChannel(), event.getUniqueId(), event.getCallerIdNum(), event.getCallerIdName());

        queue = event.getQueue();
    }

    public String getQueueName()
    {
        return queue;
    }

}

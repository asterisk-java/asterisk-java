package org.asteriskjava.fastagi.internal;

import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.reply.AgiReply;
import org.asteriskjava.manager.event.AsyncAgiEvent;

import java.util.Map;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AsyncAgiReader implements AgiReader
{
    private final AgiRequest request;
    private final BlockingQueue<AsyncAgiEvent> asyncAgiEvents;

    public AsyncAgiReader(List<String> environment, BlockingQueue<AsyncAgiEvent> asyncAgiEvents)
    {
        this.request = new AgiRequestImpl(environment);
        this.asyncAgiEvents = asyncAgiEvents;
    }

    public AgiRequest readRequest() throws AgiException
    {
        return request;
    }

    public AgiReply readReply() throws AgiException
    {
        try
        {
            return new AgiReplyImpl(asyncAgiEvents.take().decodeResult());
        }
        catch (InterruptedException e)
        {
            throw new AgiException("Interrupted while waiting for AsyncAgiEvent", e);
        }
    }
}

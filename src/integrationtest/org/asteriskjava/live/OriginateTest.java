/*
 * (c) 2004 Stefan Reuter
 *
 * Created on Oct 28, 2004
 */
package org.asteriskjava.live;

/**
 * @author srt
 * @version $Id$
 */
public class OriginateTest extends AsteriskServerTestCase
{
    private AsteriskChannel channel;
    private Long timeout = 10000L;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        this.channel = null;
    }

    public void XtestOriginate() throws Exception
    {
        AsteriskChannel channel;
        channel = server.originateToExtension("Local/1310@default", "from-local", "1330", 1, timeout);
        System.err.println(channel);
        System.err.println(channel.getVariable("AJ_TRACE_ID"));

        Thread.sleep(20000L);
        System.err.println(channel);
        System.err.println(channel.getVariable("AJ_TRACE_ID"));
    }

    public void testOriginateAsync() throws Exception
    {
        final String source;

        //source = "SIP/1301";
        source = "Local/1313@from-local";
        server.originateToExtensionAsync(source, "from-local", "1399", 1, timeout, 
                new CallerId("AJ Test Call", "08003301000"), null, 
                new OriginateCallback()
        {
            public void onSuccess(AsteriskChannel c)
            {
                channel = c;
                System.err.println("Success: " + c);
                showInfo(c);
            }

            public void onNoAnswer(AsteriskChannel c)
            {
                channel = c;
                System.err.println("No Answer: " + c);
                showInfo(c);
            }

            public void onBusy(AsteriskChannel c)
            {
                channel = c;
                System.err.println("Busy: " + c);
                showInfo(c);
            }

            public void onFailure(LiveException cause)
            {
                System.err.println("Failed: " + cause.getMessage());
            }
        });

        Thread.sleep(20000L);
        System.err.println("final state: " + channel);
        if (channel != null)
        {
            System.err.println("final state linked channels: " + channel.getLinkedChannelHistory());
        }
    }

    void showInfo(AsteriskChannel channel)
    {
        String name;
        String otherName;
        AsteriskChannel otherChannel;

        System.err.println("linkedChannelHistory: " + channel.getLinkedChannelHistory());
        System.err.println("dialedChannelHistory: " + channel.getDialedChannelHistory());

        name = channel.getName();
        if (name.startsWith("Local/"))
        {
            otherName = name.substring(0, name.length() - 1) + "2";
            System.err.println("other name: " + otherName);
            otherChannel = server.getChannelByName(otherName);
            System.err.println("other channel: " + otherChannel);
            System.err.println("other dialedChannel: " + otherChannel.getDialedChannel());
            System.err.println("other linkedChannelHistory: " + otherChannel.getLinkedChannelHistory());
            System.err.println("other dialedChannelHistory: " + otherChannel.getDialedChannelHistory());
        }
    }
}

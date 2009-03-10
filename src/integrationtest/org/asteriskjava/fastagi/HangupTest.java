package org.asteriskjava.fastagi;


import org.apache.log4j.Logger;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.ManagerCommunicationException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

/**
 * @author tobias
 */
public class HangupTest extends BaseAgiScript implements PropertyChangeListener
{
    private AsteriskServer server = null;
    private static Logger logger = Logger.getLogger(HangupTest.class);

    public HangupTest()
    {
        server = new DefaultAsteriskServer("pbx0", "manager", "obelisk");
    }

    public void service(AgiRequest request, AgiChannel channel) throws AgiException
    {
        try
        {
            AsteriskChannel c = server.getChannelById(channel.getUniqueId());
            if (c == null)
            {
                logger.info("Channel is null");
            }
            else
            {
                c.addPropertyChangeListener("state", this);
            }
        }
        catch (ManagerCommunicationException e)
        {
            e.printStackTrace();
        }

        try
        {
            for (int i = 0; i < 5000; i++)
            {
                //logger.info(i + "State: " + channel.getChannelStatus());
                sayDigits(String.valueOf(i));
            }
        }
        catch (Exception e)
        {
            logger.info("Exception caught: " + e.getMessage(), e);
        }

    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        // TODO Auto-generated method stub
        logger.info("Property changed!! : " + evt.toString());

    }

    public static void main(String[] args) throws IOException
    {
        new DefaultAgiServer().startup();
    }
}
package org.asteriskjava.pbx.agi;

import java.util.concurrent.TimeUnit;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.Channel;

public class AgiChannelActivityPlayMessage implements AgiChannelActivityAction
{

    private String file;
    private boolean hangup = false;

    public AgiChannelActivityPlayMessage(String file)
    {
        this.file = file;
    }

    @Override
    public void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException
    {
        if (ichannel == null)
        {
            throw new NullPointerException("ichannel cannot be null");
        }
        String tmp = file;
        if (file.indexOf(".") > 0)
        {
            tmp = file.substring(0, file.indexOf("."));
        }
        channel.exec("Playtones", "beep");
        TimeUnit.MILLISECONDS.sleep(100);
        // file, escape, offset, forward, rewind, pause

        // # to exit, 6 forward, 4 back, 5 pause
        try
        {
            channel.controlStreamFile(tmp, "#", 15000, "6", "4", "5");
        }
        catch (Exception e)
        {
            TimeUnit.MILLISECONDS.sleep(100);
            throw e;
        }

        channel.exec("Playtones", "beep");
        TimeUnit.MILLISECONDS.sleep(100);
        channel.hangup();
        hangup = true;

    }

    @Override
    public boolean isDisconnect()
    {
        return hangup;
    }

    @Override
    public void cancel()
    {
        hangup = true;
    }
}

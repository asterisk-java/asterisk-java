package org.asteriskjava.pbx;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.pbx.agi.ActivityAgi;

public interface AgiChannelActivityAction
{

    void execute(AgiChannel channel, Channel ichannel) throws AgiException, InterruptedException;

    boolean isDisconnect(ActivityAgi activityAgi);

    void cancel();

}

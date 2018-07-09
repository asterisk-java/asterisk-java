package org.asteriskjava.pbx.internal.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.ActivityStatusEnum;
import org.asteriskjava.pbx.AsteriskSettings;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.agi.ActivityAgi;
import org.asteriskjava.pbx.agi.ActivityArrivalListener;
import org.asteriskjava.pbx.asterisk.wrap.actions.OriginateAction;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.pbx.internal.managerAPI.OriginateBaseClass;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class DialLocalToAgiActivity implements Runnable, Activity
{

    private EndPoint from;
    private CallerID fromCallerID;
    private Thread thread;
    private final Log logger = LogFactory.getLog(this.getClass());
    private String originateId;

    CountDownLatch latch = new CountDownLatch(1);
    private List<Channel> channels = new LinkedList<>();
    private ActivityCallback<DialLocalToAgiActivity> callback;
    private Map<String, String> channelVarsToSet;

    public DialLocalToAgiActivity(EndPoint from, CallerID fromCallerID, ActivityCallback<DialLocalToAgiActivity> callback,
            Map<String, String> channelVarsToSet)
    {
        this.from = from;
        this.fromCallerID = fromCallerID;
        this.callback = callback;
        this.channelVarsToSet = channelVarsToSet;

        thread = new Thread(this, "Dial " + from + " to AGI");
        thread.start();
    }
    // Logger logger = LogManager.getLogger();

    @Override
    public void run()
    {
        logger.debug("*******************************************************************************");
        logger.info("***********                    begin dial local to AGI                  ****************");
        logger.debug("***********                                                      ****************");
        logger.debug("*******************************************************************************");
        final AsteriskSettings settings = PBXFactory.getActiveProfile();

        AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();

        final OriginateAction originate = new OriginateAction();
        originate.setEndPoint(from);
        originate.setContext(settings.getManagementContext());
        originate.setExten(pbx.getExtensionAgi());
        originate.setPriority(1);
        originate.setCallerId(fromCallerID);
        originate.setTimeout(30000);
        originateId = originate.getActionId();

        Map<String, String> myVars = new HashMap<>();

        myVars.put("__" + OriginateBaseClass.NJR_ORIGINATE_ID, this.originateId);
        if (channelVarsToSet != null)
        {
            myVars.putAll(channelVarsToSet);
        }

        originate.setVariables(myVars);

        ActivityArrivalListener listener = new ActivityArrivalListener()
        {

            @Override
            public void channelArrived(Channel channel)
            {
                channels.add(channel);
                if (isSuccess())
                {
                    latch.countDown();
                }
            }
        };
        try (AutoCloseable closeable = ActivityAgi.addArrivalListener(originate, listener);)
        {

            ManagerResponse response = pbx.sendAction(originate, 5000);
            if (response.getResponse().compareToIgnoreCase("Success") != 0)
            {
                callback.progress(this, ActivityStatusEnum.FAILURE, ActivityStatusEnum.FAILURE.getDefaultMessage());
            }
            else
            {
                if (latch.await(5, TimeUnit.SECONDS))
                {
                    callback.progress(this, ActivityStatusEnum.SUCCESS, ActivityStatusEnum.SUCCESS.getDefaultMessage());
                }
                else
                {
                    callback.progress(this, ActivityStatusEnum.FAILURE, ActivityStatusEnum.FAILURE.getDefaultMessage());
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            callback.progress(this, ActivityStatusEnum.FAILURE, ActivityStatusEnum.FAILURE.getDefaultMessage());

        }

    }

    public void abort(final String reason)
    {
        logger.warn("Aborting originate ");

        for (Channel channel : channels)
        {
            PBX pbx = PBXFactory.getActivePBX();
            try
            {
                pbx.hangup(channel);
            }
            catch (IllegalArgumentException | IllegalStateException | PBXException e)
            {
                logger.error(e, e);

            }
        }
        latch.countDown();

    }

    @Override
    public Throwable getLastException()
    {
        return null;
    }

    @Override
    public boolean isSuccess()
    {
        return channels.size() == 2;
    }

    public Channel getChannel1()
    {
        return channels.get(0);

    }

    public Channel getChannel2()
    {
        return channels.get(1);

    }
}

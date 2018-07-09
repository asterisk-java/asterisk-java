package org.asteriskjava.pbx.agi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiHangupException;
import org.asteriskjava.pbx.AgiChannelActivityAction;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ChannelHangupListener;
import org.asteriskjava.pbx.InvalidChannelName;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.agi.config.ServiceAgiScriptImpl;
import org.asteriskjava.pbx.asterisk.wrap.actions.OriginateAction;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public abstract class ActivityAgi extends ServiceAgiScriptImpl
{

    public static final String ARRIVAL_KEY = "ActivityAgiArrivalKey";

    private final static Log logger = LogFactory.getLog(ActivityAgi.class);

    private static final Map<String, ActivityArrivalListener> arrivalListeners = new ConcurrentHashMap<>();

    public static AutoCloseable addArrivalListener(OriginateAction originate, ActivityArrivalListener listener)
    {

        final String key = UUID.randomUUID().toString();
        arrivalListeners.put(key, listener);
        if (arrivalListeners.size() > 100)
        {
            // pick one at random to remove
            ActivityArrivalListener leaked = arrivalListeners.remove(arrivalListeners.keySet().iterator().next());
            logger.error("Arrival Listeners are leaking" + leaked.getClass().getCanonicalName());
        }

        Map<String, String> vars = new HashMap<>();
        vars.put("_" + ARRIVAL_KEY, key);
        originate.setVariables(vars);
        return new AutoCloseable()
        {

            @Override
            public void close() throws Exception
            {
                arrivalListeners.remove(key);

            }
        };
    }

    @Override
    public void service() throws AgiException
    {
        Channel channelProxy = null;
        String channelName = channel.getName();

        try
        {
            AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
            String proxyId = getVariable("proxyId");
            if (proxyId != null && proxyId.length() > 0)
            {
                channelProxy = pbx.getProxyById(proxyId);
            }
            if (channelProxy == null)
            {
                logger.info("'proxyId' var not set or proxy doesn't exist anymore, trying to match the channel name... "
                        + channelName);
                channelProxy = pbx.internalRegisterChannel(channel.getName(), channel.getUniqueId());

            }

            logger.info("Channel " + channelName + " arrived in agi");

            channelProxy.setIsInAgi(true);
            channelProxy.addHangupListener(new ChannelHangupListener()
            {

                @Override
                public void channelHangup(Channel channel, Integer cause, String causeText)
                {
                    final AgiChannelActivityAction currentActivityAction = channel.getCurrentActivityAction();
                    if (currentActivityAction != null)
                    {
                        currentActivityAction.cancel();
                    }

                }
            });

            AgiChannelActivityAction action = channelProxy.getCurrentActivityAction();
            if (action == null)
            {
                action = new AgiChannelActivityHold();
            }

            String arrivalKey = channel.getVariable(ARRIVAL_KEY);
            if (arrivalKey != null && arrivalKey.length() > 0)
            {
                ActivityArrivalListener listener = arrivalListeners.get(arrivalKey);
                if (listener != null)
                {
                    listener.channelArrived(channelProxy);
                }
            }

            boolean isAlive = true;
            RateLimiter rateLimiter = new RateLimiter(2);
            while (!action.isDisconnect() && isAlive)
            {

                action.execute(this.channel, channelProxy);

                action = channelProxy.getCurrentActivityAction();
                logger.debug("Action for proxy " + channelProxy + " is " + action.getClass().getSimpleName());
                isAlive = checkChannelIsStillUp();
                rateLimiter.acquire();
            }

        }
        catch (AgiHangupException e)
        {
            logger.warn("Channel hungup " + channelName);
        }
        catch (InvalidChannelName | InterruptedException e)
        {
            logger.error(e, e);
        }

        logger.debug("Channel leaving agi " + channelName);

    }

    private boolean checkChannelIsStillUp()
    {
        try

        {
            this.answer();
            return true;
        }
        catch (Exception e)
        {

        }
        return false;
    }

}

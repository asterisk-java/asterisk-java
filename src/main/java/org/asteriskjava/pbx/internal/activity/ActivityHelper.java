package org.asteriskjava.pbx.internal.activity;

import java.util.HashSet;
import java.util.Set;

import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.ActivityStatusEnum;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.ListenerPriority;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.asterisk.wrap.actions.SetVarAction;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.managerAPI.EventListenerBaseClass;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public abstract class ActivityHelper<T extends Activity> implements Runnable, Activity
{
    private static final Log logger = LogFactory.getLog(ActivityHelper.class);

    /**
     * If the activity fails due to an exception then lastException will contain
     * the final exception that was thrown.
     */
    private PBXException lastException;

    /**
     * Set to true once the activity succeeds.
     */
    private volatile boolean _success = false;

    private final ActivityCallback<T> callback;

    private final String activityName;

    private boolean _sendEvents;

    Exception callSite = new Exception("Invoked from here");

    public ActivityHelper(final String activityName, final ActivityCallback<T> callback)
    {

        this.callback = callback;
        this.activityName = activityName;
    }

    private AutoCloseable getManagerListener()
    {
        if (!_sendEvents)
        {
            return new AutoCloseable()
            {

                @Override
                public void close() throws Exception
                {
                    // do nothing, we never started anything
                }
            };
        }
        EventListenerBaseClass listener = new EventListenerBaseClass(activityName, PBXFactory.getActivePBX())
        {

            @Override
            public Set<Class< ? extends ManagerEvent>> requiredEvents()
            {
                return ActivityHelper.this.requiredEvents();
            }

            @Override
            public void onManagerEvent(ManagerEvent event)
            {
                ActivityHelper.this.onManagerEvent(event);

            }

            @Override
            public ListenerPriority getPriority()
            {
                return ActivityHelper.this.getPriority();
            }
        };
        listener.startListener();
        return listener;

    }

    @SuppressWarnings("unchecked")
    public void startActivity(final boolean sendEvents)
    {
        this._sendEvents = sendEvents;
        if (this.callback != null)
        {
            this.callback.progress((T) this, ActivityStatusEnum.START, ActivityStatusEnum.START.getDefaultMessage());
        }

        final Thread thread = new Thread(this, this.activityName);
        thread.setDaemon(true);
        thread.start();

    }

    @Override
    @SuppressWarnings("unchecked")
    public void run()
    {
        try (AutoCloseable closer = getManagerListener())
        {
            this._success = this.doActivity();
        }
        catch (final PBXException e)
        {
            this.lastException = e;
            ActivityHelper.logger.error(e, e);
            logger.error(callSite, callSite);
        }
        catch (final Throwable e)
        {
            this.lastException = new PBXException(e);
            ActivityHelper.logger.error(callSite, callSite);
            logger.error(e, e);
        }
        finally
        {
            if (this.callback != null)
            {
                if (this._success)
                {
                    this.callback.progress((T) this, ActivityStatusEnum.SUCCESS,
                            ActivityStatusEnum.SUCCESS.getDefaultMessage());
                }
                else
                {
                    // This went badly so make certain we hang everything up
                    this.callback.progress((T) this, ActivityStatusEnum.FAILURE,
                            ActivityStatusEnum.FAILURE.getDefaultMessage());
                }
            }
        }

    }

    abstract protected boolean doActivity() throws PBXException;

    /*
     * Attempt to set a variable on the channel to see if it's up.
     * @param channel the channel which is to be tested.
     */
    public boolean validateChannel(final Channel channel)
    {

        boolean ret = false;
        final SetVarAction var = new SetVarAction(channel, "testState", "1");

        ManagerResponse response = null;
        try
        {
            AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
            response = pbx.sendAction(var, 500);
        }
        catch (final Exception e)
        {
            ActivityHelper.logger.debug(e, e);
            ActivityHelper.logger.error("getVariable: " + e);
        }
        if ((response != null) && (response.getAttribute("Response").compareToIgnoreCase("success") == 0))
        {
            ret = true;
        }

        return ret;

    }

    @Override
    public boolean isSuccess()
    {
        return this._success;
    }

    protected void setLastException(final PBXException e)
    {
        this.lastException = e;
    }

    @Override
    public Throwable getLastException()
    {
        return this.lastException;
    }

    public void progess(final T activity, final String message)
    {
        this.callback.progress(activity, ActivityStatusEnum.PROGRESS, message);

    }

    abstract HashSet<Class< ? extends ManagerEvent>> requiredEvents();

    abstract void onManagerEvent(final ManagerEvent event);

    abstract public ListenerPriority getPriority();

}

package org.asteriskjava.pbx.activities;

import org.apache.log4j.Logger;
import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.PBXException;
import org.asteriskjava.pbx.PBXFactory;
import org.asteriskjava.pbx.internal.asterisk.wrap.actions.SetVarAction;
import org.asteriskjava.pbx.internal.asterisk.wrap.response.ManagerResponse;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.managerAPI.EventListenerBaseClass;

public abstract class ActivityHelper<T extends Activity> extends EventListenerBaseClass implements Runnable, Activity
{
	static private Logger logger = Logger.getLogger(ActivityHelper.class);

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
		super(activityName);
		this.callback = callback;
		this.activityName = activityName;
	}

	@SuppressWarnings("unchecked")
	public void startActivity(final boolean sendEvents)
	{
		this._sendEvents = sendEvents;
		if (this.callback != null)
		{
			this.callback.start((T) this);
		}

		final Thread thread = new Thread(this, this.activityName);
		thread.setDaemon(true);
		thread.start();

	}

	@Override
	@SuppressWarnings("unchecked")
	public void run()
	{
		try (EventListenerBaseClass.AutoClose closer = new EventListenerBaseClass.AutoClose(this, this._sendEvents))
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
					this.callback.completed((T) this, true);
				}
				else
				{
					// This went badly so make certain we hang everything up
					this.callback.completed((T) this, false);
				}
			}
		}

	}

	abstract protected boolean doActivity() throws PBXException;

	/*
	 * Attempt to set a variable on the channel to see if it's up.
	 * 
	 * @param channel the channel which is to be tested.
	 */
	public boolean validateChannel(final Channel channel)
	{

		boolean ret = false;
		final SetVarAction var = new SetVarAction(channel, "testState", "1"); //$NON-NLS-1$ //$NON-NLS-2$

		ManagerResponse response = null;
		try
		{
			AsteriskPBX pbx = (AsteriskPBX) PBXFactory.getActivePBX();
			response = pbx.sendAction(var, 500);
		}
		catch (final Exception e)
		{
			ActivityHelper.logger.debug(e, e);
			ActivityHelper.logger.error("getVariable: " + e); //$NON-NLS-1$
		}
		if ((response != null) && (response.getAttribute("Response").compareToIgnoreCase("success") == 0)) //$NON-NLS-1$ //$NON-NLS-2$
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
		this.callback.progess(activity, message);

	}

}

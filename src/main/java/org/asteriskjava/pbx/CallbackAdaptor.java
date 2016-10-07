package org.asteriskjava.pbx;

public class CallbackAdaptor<T extends Activity> implements ActivityCallback<T>
{
	@Override
	public void start(final T activity)
	{
		// NOOP
	}

	@Override
	public void progess(final T activity, final String message)
	{
		// NOOP

	}

	@Override
	public void completed(final T activity, final boolean success)
	{
		// NOOP

	}
}

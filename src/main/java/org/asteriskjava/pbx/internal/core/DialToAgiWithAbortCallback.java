package org.asteriskjava.pbx.internal.core;

import java.util.concurrent.TimeUnit;

import org.asteriskjava.pbx.ActivityCallback;
import org.asteriskjava.pbx.ActivityStatusEnum;
import org.asteriskjava.pbx.CompletionAdaptor;
import org.asteriskjava.pbx.activities.DialToAgiActivity;
import org.asteriskjava.pbx.internal.activity.DialToAgiActivityImpl;

public class DialToAgiWithAbortCallback
{
    private DialToAgiActivityImpl dialer;
    private CompletionAdaptor<DialToAgiActivity> completion;
    private ActivityCallback<DialToAgiActivity> iCallback;

    public DialToAgiWithAbortCallback(DialToAgiActivityImpl dialer, CompletionAdaptor<DialToAgiActivity> completion,
            ActivityCallback<DialToAgiActivity> iCallback)
    {
        this.dialer = dialer;
        this.completion = completion;
        this.iCallback = iCallback;
    }

    public void asyncStartDial()
    {

        Runnable runner = new Runnable()
        {

            @Override
            public void run()
            {
                dialer.startActivity(false);

                completion.waitForCompletion(3, TimeUnit.MINUTES);

                final ActivityStatusEnum status;

                if (dialer.isSuccess())
                {
                    status = ActivityStatusEnum.SUCCESS;
                }
                else
                {
                    status = ActivityStatusEnum.FAILURE;
                }

                iCallback.progress(dialer, status, status.getDefaultMessage());

            }
        };

        new Thread(runner).start();

    }

    public void abort()
    {
        dialer.abort();
    }
}

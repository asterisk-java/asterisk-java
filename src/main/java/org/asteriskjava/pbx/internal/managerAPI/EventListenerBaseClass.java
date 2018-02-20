package org.asteriskjava.pbx.internal.managerAPI;

import org.asteriskjava.pbx.PBX;
import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.pbx.internal.core.AsteriskPBX;
import org.asteriskjava.pbx.internal.core.FilteredManagerListener;

/*
 * This is the basic abstract event listener class. It implements a thread
 * and queue.
 */
public abstract class EventListenerBaseClass implements FilteredManagerListener<ManagerEvent>, AutoCloseable
{

    private final String name;
    private final PBX pbx;

    protected EventListenerBaseClass(final String descriptiveName, PBX iPBX)
    {
        this.name = descriptiveName;
        this.pbx = iPBX;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    /**
     * we have to take the pbx as an arg here as we sometimes this is called
     * during the creation phase of for the pbx, and the factory can't give out
     * the pbx at that point event though it is initialised.
     */

    public void startListener()
    {

        ((AsteriskPBX) pbx).addListener(this);
    }

    /**
     * Stops the listener.
     */
    @Override
    public void close()
    {
        ((AsteriskPBX) pbx).removeListener(this);
    }

    /**
     * This class exists so we can start and stop the listener using the new
     * try-with-resource of JRE7. Whilst the parent class is Autoclosable we
     * can't always use it directly in a try block (e.g. it is the base class).
     * In those cases you can use this class.
     * 
     * @author bsutton
     */
    public class AutoClose implements java.lang.AutoCloseable
    {
        EventListenerBaseClass listener;

        public AutoClose(final EventListenerBaseClass listener)
        {
            this(listener, true);
        }

        public AutoClose(final EventListenerBaseClass listener, final boolean sendEvents)
        {
            if (listener == null)
            {
                throw new IllegalArgumentException("listener may not be null"); //$NON-NLS-1$
            }

            this.listener = listener;
            if (sendEvents)
            {
                listener.startListener();
            }
        }

        @Override
        public void close()
        {
            this.listener.close();

        }
    }
}

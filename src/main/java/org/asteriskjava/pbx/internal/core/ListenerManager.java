package org.asteriskjava.pbx.internal.core;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.asteriskjava.pbx.asterisk.wrap.events.ManagerEvent;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

public class ListenerManager
{
    private final TreeSet<FilteredManagerListenerWrapper> listeners = new TreeSet<>(new ListenerPriorityComparator());

    Log logger = LogFactory.getLog(ListenerManager.class);

    final class ListenerPriorityComparator implements Comparator<FilteredManagerListenerWrapper>
    {
        @Override
        public int compare(FilteredManagerListenerWrapper lhs, FilteredManagerListenerWrapper rhs)
        {

            int result = lhs._listener.getPriority().compare(rhs._listener.getPriority());

            if (result == 0)
                result = lhs.equalityBuster.compareTo(rhs.equalityBuster);

            return result;
        }
    }

    public void clear()
    {
        listeners.clear();
    }

    public int size()
    {
        return listeners.size();
    }

    public Iterator<FilteredManagerListenerWrapper> iterator()
    {
        return listeners.iterator();
    }

    public void addListener(FilteredManagerListener<ManagerEvent> listener)
    {
        listeners.add(new FilteredManagerListenerWrapper(listener));
    }

    List<FilteredManagerListenerWrapper> getCopyAsList()
    {
        List<FilteredManagerListenerWrapper> list = new LinkedList<>();
        for (FilteredManagerListenerWrapper listener : listeners)
        {
            list.add(listener);
        }

        return list;
    }

    boolean removeListener(FilteredManagerListener<ManagerEvent> toRemove)
    {
        boolean removed = false;
        Iterator<FilteredManagerListenerWrapper> itr = listeners.iterator();
        while (itr.hasNext())
        {
            FilteredManagerListenerWrapper container = itr.next();
            // logger.error("Checking " + container._listener + " " + toRemove);
            if (container._listener == toRemove)
            {
                logger.debug("Removing listener " + toRemove);
                itr.remove();
                removed = true;
                break;
            }
        }
        return removed;
    }

}

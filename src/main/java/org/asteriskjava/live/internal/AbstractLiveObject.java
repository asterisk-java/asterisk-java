package org.asteriskjava.live.internal;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.LiveObject;

/**
 * Abstract base class for all live objects.
 * 
 * @author srt
 * @since 0.3
 */
abstract class AbstractLiveObject implements LiveObject
{
    private final PropertyChangeSupport changes;
    protected final AsteriskServerImpl server;

    AbstractLiveObject(AsteriskServerImpl server)
    {
        this.server = server;
        this.changes = new PropertyChangeSupport(this);
    }

    public AsteriskServer getServer()
    {
        return server;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        changes.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        changes.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        changes.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        changes.removePropertyChangeListener(propertyName, listener);
    }
    
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        if (oldValue != null || newValue != null)
        {
            changes.firePropertyChange(propertyName, oldValue, newValue);
        }
    }
}

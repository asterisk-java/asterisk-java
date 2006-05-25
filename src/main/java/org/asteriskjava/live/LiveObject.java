package org.asteriskjava.live;

import java.beans.PropertyChangeListener;

/**
 * Interface for all live objects.
 * 
 * @author srt
 * @since 0.3
 */
public interface LiveObject
{
    /**
     * Returns the AsteriskServer this live object belongs to.
     * 
     * @return the AsteriskServer this live object belongs to.
     */
    AsteriskServer getServer();
    
    /**
     * Adds a PropertyChangeListener that is notified whenever a property value changes.
     * 
     * @param listener listener to notify
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Adds a PropertyChangeListener that is notified whenever a given property value changes.
     * 
     * @param propertyName property to observe
     * @param listener listener to notify
     * @see #addPropertyChangeListener(PropertyChangeListener)
     */
    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    /**
     * Removes the given PropertyChangeListener that was added by calling
     * {@link #addPropertyChangeListener(PropertyChangeListener)}.
     * 
     * @param listener listener to remove
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes the given PropertyChangeListener that was added by calling
     * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
     * 
     * @param propertyName property that is observed
     * @param listener listener to remove
     */
    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}

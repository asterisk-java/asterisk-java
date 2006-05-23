package org.asteriskjava.live;

import java.beans.PropertyChangeListener;

/**
 * Interface for all live objects that support property change notification
 * 
 * @author srt
 * @since 0.3
 */
public interface LiveObject
{
    /**
     * Adds a PropertyChangeListener that is notified whenever a property value changes.
     * 
     * @param listener listener to notify
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Adds a PropertyChangeListener that is notified whenever a given property value changes.
     * 
     * @param propertyName property to observe
     * @param listener listener to notify
     * @see #addPropertyChangeListener(PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    /**
     * Removes the given PropertyChangeListener that was added by calling
     * {@link #addPropertyChangeListener(PropertyChangeListener)}.
     * 
     * @param listener listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes the given PropertyChangeListener that was added by calling
     * {@link #addPropertyChangeListener(String, PropertyChangeListener)}.
     * 
     * @param propertyName property that is observed
     * @param listener listener to remove
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}

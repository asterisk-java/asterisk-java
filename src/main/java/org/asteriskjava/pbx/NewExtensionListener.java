package org.asteriskjava.pbx;

/**
 * Used to notify interested parties when a new extension has been read from the
 * pbx configuration.
 * 
 * @author bsutton
 * 
 */
public interface NewExtensionListener
{

	void newExtension();

}

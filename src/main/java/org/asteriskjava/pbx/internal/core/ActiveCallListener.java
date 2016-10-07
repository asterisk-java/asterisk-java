package org.asteriskjava.pbx.internal.core;

import org.asteriskjava.pbx.Call;

/**
 * Used to notify a listener that the status of a call has changed. For instance
 * the call may have just been parked.
 * 
 * @author bsutton
 * 
 */
public interface ActiveCallListener
{

	void callStatusChanged(Call call, boolean isHidden);

}

package org.asteriskjava.pbx.asterisk.wrap.actions;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Automatically generates a globally unique action id (based on the systems mac
 * address
 * 
 * @author bsutton
 */
abstract public class AbstractManagerAction implements ManagerAction
{

    private final static AtomicLong _nextActionId = new AtomicLong();

    private final String _actionId = "" + _nextActionId.incrementAndGet();

    public String getActionId()
    {
        return this._actionId;
    }

}

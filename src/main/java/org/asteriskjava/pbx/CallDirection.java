package org.asteriskjava.pbx;

/**
 * Describes the direction of the call.
 * 
 * @author bsutton
 */
public enum CallDirection
{
    /**
     * INBOUND - call
     */
    INBOUND(0),
    /**
     * OUTBOUND - call.
     */
    OUTBOUND(1);
    // /**
    // * JOIN - call was created by joining two existing calls
    // */
    // JOIN(2),
    //
    // /**
    // * Often temporary channels are brought up for actions such as parking a
    // * call. These calls don't really have a direction in their own right.
    // *
    // * TODO: is this really need, should the originating call be the
    // controlling
    // * entity. The problem is that currently we really track 'channels' rather
    // * than Calls.
    // */
    // TRANSIENT(3);

    private final int dbValue;

    CallDirection(final int dbValue)
    {
        this.dbValue = dbValue;
    }

    public static CallDirection valueOf(final int dbValue)
    {
        CallDirection result = null;

        for (final CallDirection direction : CallDirection.values())
        {
            if (direction.dbValue == dbValue)
            {
                result = direction;
                break;
            }
        }
        return result;
    }

}

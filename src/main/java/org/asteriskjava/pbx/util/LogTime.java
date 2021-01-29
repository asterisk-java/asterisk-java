/*
 * Created on 2/03/2005
 *
 */
package org.asteriskjava.pbx.util;

/**
 * @author work shop
 */
public class LogTime
{
    public Long dStartTime = System.currentTimeMillis();

    public long timeTaken()
    {
        // returns the time taken from construction until now in milliseconds

        return System.currentTimeMillis() - this.dStartTime;

    }
}

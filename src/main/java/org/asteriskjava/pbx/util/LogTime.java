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
        // returns the time taken from construction til now in milliseconds
        // Date dStartTime = new Date();

        return System.currentTimeMillis() - this.dStartTime;

    }
}

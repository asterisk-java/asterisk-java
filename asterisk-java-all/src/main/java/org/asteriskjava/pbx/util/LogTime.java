/*
 * Created on 2/03/2005
 *
 */
package org.asteriskjava.pbx.util;

/**
 * @author work shop
 */
public class LogTime {
    private final Long dStartTime = System.currentTimeMillis();

    /**
     * @return time taken from construction until now in milliseconds
     */
    public long timeTaken() {
        return System.currentTimeMillis() - this.dStartTime;
    }
}

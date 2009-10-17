package org.asteriskjava.util.internal;

import java.io.IOException;

/**
 * Interface for tracing network traffic.
 */
public interface Trace
{
    String TRACE_PROPERTY = "org.asteriskjava.trace";

    /**
     * Writes data that has been received from the network to the trace.
     * @param s the String that has been received.
     */
    void received(String s);

    /**
     * Writes data that has been sent to the network to the trace.
     * @param s the String that has been sent.
     */
    void sent(String s);
}

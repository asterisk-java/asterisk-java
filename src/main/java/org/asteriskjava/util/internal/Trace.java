package org.asteriskjava.util.internal;

/**
 * Interface for tracing network traffic.
 */
public interface Trace
{
    /**
     * Name of the system property to enable tracing.<p>
     * To enable tracing add <code>-Dorg.asteriskjava.trace=true</code> to the vm parameters when running Asterisk-Java.
     */
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
    
    
    /**
     * Closes allocated resources by trace implementation.
     */
    void close();
}

package org.asteriskjava.manager.event;

/**
 * Created by plhk on 1/15/15.
 */
public class AsyncAgiExecEvent extends AsyncAgiEvent {
    public AsyncAgiExecEvent (Object source) { super(source); setSubEvent(SUB_EVENT_EXEC); }
}

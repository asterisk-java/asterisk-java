package org.asteriskjava.manager;

import org.asteriskjava.ami.action.response.ManagerActionResponse;
import org.asteriskjava.manager.response.ManagerError;

/**
 * Callback interface to send
 * {@link org.asteriskjava.manager.action.EventGeneratingAction} asynchronously.
 *
 * @see org.asteriskjava.manager.ManagerConnection#sendEventGeneratingAction(org.asteriskjava.manager.action.EventGeneratingAction, SendEventGeneratingActionCallback)
 * <p>
 * Initial response is passed to one of {@link #onResponse(ManagerActionResponse)} or
 * {@link #onErrorResponse(ManagerActionResponse)}. but not both.
 */
public interface SendEventGeneratingActionCallback {
    /**
     * Called to notify that
     * {@link ManagerConnection#sendEventGeneratingAction(org.asteriskjava.manager.action.EventGeneratingAction, SendEventGeneratingActionCallback)}
     * is done.
     * <p>
     * If connection is lost before action is completed then
     * {@link ResponseEvents#getResponse()} can be null or list of events might
     * be lacking some events including end event. The
     * {@link ResponseEvents#getResponse()} can also be {@link ManagerError}
     *
     * @param responseEvents the response at whatever state it was when action ended.
     */
    public void onResponse(ResponseEvents responseEvents);
}

package org.asteriskjava.manager.action;

import org.asteriskjava.ami.action.AbstractManagerAction;

/**
 * Fully restarts all zaptel channels and terminates any calls on Zap
 * interfaces.
 * <p>
 * Available since Asterisk 1.4.
 *
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public class ZapRestartAction extends AbstractManagerAction {
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 5899540386204706397L;

    /**
     * Creates a new ZapRestartAction.
     */
    public ZapRestartAction() {

    }

    @Override
    public String getAction() {
        return "ZapRestart";
    }
}

package org.asteriskjava.manager.action;

/**
 * The QueueMemberRingInUseAction requests to change RingInUse value of queue member.
 */

public class QueueMemberRingInUseAction extends AbstractManagerAction {

    /**
     * Serializable version identifier
     */

    private final static long serialVersionUID = -1554562185640L;

    /**
     * Queue name where member is located;
     */

    private String queue;

    /**
     * RingInUse value.
     */

    private Boolean ringInUse;

    /**
     * Interface where need to change RingInUse value;
     */

    private String iface;

    /**
     * Creates a new empty QueueMemberRingInUseAction.
     */

    public QueueMemberRingInUseAction() {
    }

    /**
     * Creates a new QueueMemberRingInUseAction with mandatory properties queue, ringInUse and iface.
     *
     * @param queue     queueName.
     * @param ringInUse value to set.
     * @param iface     interface where to set ringInUse.
     */

    public QueueMemberRingInUseAction(String queue, Boolean ringInUse, String iface) {
        this.queue = queue;
        this.ringInUse = ringInUse;
        this.iface = iface;
    }

    @Override
    public String getAction() {
        return "QueueMemberRingInUse";
    }

    /**
     * Returns queue name where interface is located.
     *
     * @return queue name;
     */

    public String getQueue() {
        return queue;
    }

    /**
     * Sets queue name where interface is located.
     *
     * @param queue queue name;
     */

    public void setQueue(String queue) {
        this.queue = queue;
    }

    /**
     * Returns RingInUse value of selected interface.
     *
     * @return ringInUse value;
     */

    public Boolean getRingInUse() {
        return ringInUse;
    }

    /**
     * Sets RingInUse to selected interface.
     *
     * @param ringInUse value.
     */

    public void setRingInUse(Boolean ringInUse) {
        this.ringInUse = ringInUse;
    }

    /**
     * Returns interface value eg. SIP/203.
     *
     * @return interface value.
     */

    public String getInterface() {
        return iface;
    }

    /**
     * Sets interface value where RingInUse changes.
     *
     * @param iface name
     */

    public void setInterface(String iface) {
        this.iface = iface;
    }
}

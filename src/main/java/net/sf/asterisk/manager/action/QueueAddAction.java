/*
 * Copyright  2004-2005 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package net.sf.asterisk.manager.action;

/**
 * The QueueAddAction adds a new member to a queue.<br>
 * It is implemented in <code>apps/app_queue.c</code>
 * 
 * @author srt
 * @version $Id: QueueAddAction.java,v 1.6 2005/08/28 09:28:59 srt Exp $
 */
public class QueueAddAction extends AbstractManagerAction
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = -7022129266332219953L;
    private String queue;
    private String iface;
    private Integer penalty;
    private Boolean paused;

    /**
     * Creates a new empty QueueAddAction.
     */
    public QueueAddAction()
    {

    }

    /**
     * Creates a new QueueAddAction that adds a new member on the given
     * interface to the given queue.
     * 
     * @param queue the name of the queue the new member will be added to
     * @param iface Sets the interface to add. To add a specific channel just
     *            use the channel name, e.g. "SIP/1234".
     * @since 0.2
     */
    public QueueAddAction(String queue, String iface)
    {
        this.queue = queue;
        this.iface = iface;
    }

    /**
     * Creates a new QueueAddAction that adds a new member on the given
     * interface to the given queue with the given penalty.
     * 
     * @param queue the name of the queue the new member will be added to
     * @param iface Sets the interface to add. To add a specific channel just
     *            use the channel name, e.g. "SIP/1234".
     * @param penalty the penalty for this member. The penalty must be a
     *            positive integer or 0 for no penalty. When calls are
     *            distributed members with higher penalties are considered last.
     * @since 0.2
     */
    public QueueAddAction(String queue, String iface, Integer penalty)
    {
        this.queue = queue;
        this.iface = iface;
        this.penalty = penalty;
    }

    /**
     * Returns the name of this action, i.e. "QueueAdd".
     */
    public String getAction()
    {
        return "QueueAdd";
    }

    /**
     * Returns the name of the queue the new member will be added to.
     */
    public String getQueue()
    {
        return queue;
    }

    /**
     * Sets the name of the queue the new member will be added to.<br>
     * This property is mandatory.
     */
    public void setQueue(String queue)
    {
        this.queue = queue;
    }

    /**
     * Returns the interface to add.
     */
    public String getInterface()
    {
        return iface;
    }

    /**
     * Sets the interface to add.<br>
     * To add a specific channel just use the channel name, e.g. "SIP/1234".<br>
     * This property is mandatory.
     */
    public void setInterface(String iface)
    {
        this.iface = iface;
    }

    /**
     * Returns the penalty for this member.
     */
    public Integer getPenalty()
    {
        return penalty;
    }

    /**
     * Sets the penalty for this member.<br>
     * The penalty must be a positive integer or 0 for no penalty. If it is
     * not set 0 is assumed.<br>
     * When calls are distributed members with higher penalties are considered
     * last.
     */
    public void setPenalty(Integer penalty)
    {
        this.penalty = penalty;
    }

    /**
     * Returns if the queue member should be paused when added.
     * 
     * @return Boolean.TRUE if the queue member should be paused when added.
     * @since 0.2
     */
    public Boolean getPaused()
    {
        return paused;
    }

    /**
     * Sets if the queue member should be paused when added.
     * 
     * @param paused Boolean.TRUE if the queue member should be paused when
     *            added.
     * @since 0.2
     */
    public void setPaused(Boolean paused)
    {
        this.paused = paused;
    }
}

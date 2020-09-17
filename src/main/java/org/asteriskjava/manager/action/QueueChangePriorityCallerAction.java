package org.asteriskjava.manager.action;

/**
 * The QueueChangePriorityCallerAction requests to change priority of caller.
 * Priority cannot be less than 0.
 */

public class QueueChangePriorityCallerAction extends AbstractManagerAction {

	/**
	 * Serializable version identifier
	 */

	private final static long serialVersionUID = -1554562185648L;

	private String queue;
	private String caller;
	private Integer priority;

	/**
	 * Creates a new empty QueueChangePriorityCallerAction.
	 */

	public QueueChangePriorityCallerAction() {
	}

	/**
	 * Creates a new QueueChangePriorityCAllerAction with all required parameters.
	 * @param queue name of queue.
	 * @param caller caller channel name.
	 * @param priority priority value to set.
	 */

	public QueueChangePriorityCallerAction(String queue, String caller, Integer priority) {
		this.queue = queue;
		this.caller = caller;
		this.priority = priority;
	}

	/**
	 * Returns the name of this action, i.e. "QueueChangePriorityCaller"
	 * @return String, name of action
	 */

	@Override
	public String getAction() {
		return "QueueChangePriorityCaller";
	}

	/**
	 * Returns the queue that caller is inside.
	 *
	 * @return queue name
	 */

	public String getQueue() {
		return queue;
	}

	/**
	 * Sets the queue filter.
	 * @param queue queue name
	 */

	public void setQueue(String queue) {
		this.queue = queue;
	}

	/**
	 * Returns the caller to change priority.
	 * @return String caller name
	 */

	public String getCaller() {
		return caller;
	}

	/**
	 * Sets the caller to change priority. If not sets then nothing changes.
	 * @param caller caller name
	 */

	public void setCaller(String caller) {
		this.caller = caller;
	}

	/**
	 * Returns priority to set to given caller.
	 * @return integer priority value
	 */

	public Integer getPriority() {
		return priority;
	}

	/**
	 * Sets priority to given caller. If nothing sets then nothing changes.
	 * @param priority priority value
	 */

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}

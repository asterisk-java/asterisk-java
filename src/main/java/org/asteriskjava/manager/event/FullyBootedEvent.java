package org.asteriskjava.manager.event;

/**
 * Indicates that Asterisk has loaded all its modules and finished booting.<br>
 * It is handy to have a single event notification for when all Asterisk
 * modules have been loaded - especially for situations like running
 * automated tests. This event will fire 1) immediately upon all modules
 * loading or 2) upon connection to the AMI interface if the modules have
 * already finished loading before the connection was made. This ensures
 * that a user will never miss getting a FullyBooted event. In vary rare
 * circumstances, it might be possible to get two copies of the message
 * if the AMI connection is made right as the modules finish loading.
 * <br>
 * It is implemented in <code>main/asterisk.c</code> and <code>manager.c</code>.<br>
 * Available since Asterisk 1.8
 */
public class FullyBootedEvent extends ManagerEvent
{
    private static final long serialVersionUID = 0L;
    private String status;
    private String lastreload;
    private Integer uptime;

    public FullyBootedEvent(Object source)
    {
        super(source);
    }

    /**
     * Returns the status. Currently this is always "Fully Booted".
     *
     * @return the status.
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the status.
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

	public String getLastreload() {
		return lastreload;
	}

	public void setLastreload(String lastreload) {
		this.lastreload = lastreload;
	}

	public Integer getUptime() {
		return uptime;
	}

	public void setUptime(Integer uptime) {
		this.uptime = uptime;
	}
}

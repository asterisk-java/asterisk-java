package org.asteriskjava.manager.event;

public class AntennaLevelEvent extends AbstractChannelEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7687745700915865011L;

	private String signal;
	
	public AntennaLevelEvent(Object source) {
		super(source);
	}

	public String getSignal() {
		return signal;
	}

	public void setSignal(String signal) {
		this.signal = signal;
	}

}

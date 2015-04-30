package org.asteriskjava.manager.event;

public class PausedEvent extends UserEvent {

	public PausedEvent(Object source) {
		super(source);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7181269530090027203L;

	private String header;
	
	private String extension;
	
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	
}

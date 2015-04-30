package org.asteriskjava.manager.event;

public class UnpausedEvent extends UserEvent {

	public UnpausedEvent(Object source) {
		super(source);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4175034098824101733L;

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

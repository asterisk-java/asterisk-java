package org.asteriskjava.manager.event;

public class DongleNewSMSBase64Event extends ManagerEvent
{
  private static final long serialVersionUID = 3257845467831284784L;
  private String from;
  private String message;
  private String device;

  public DongleNewSMSBase64Event(Object source)
  {
    super(source);
  }

  public String getDevice() {
    return this.device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public String getFrom() {
    return this.from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
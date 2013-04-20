package org.asteriskjava.manager.event;

public class DongleStatusEvent extends ManagerEvent
{
  private static final long serialVersionUID = 3257845467831284784L;
  private String device;
  private String status;

  public DongleStatusEvent(Object source)
  {
    super(source);
  }

  public String getDevice() {
    return this.device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

  

}
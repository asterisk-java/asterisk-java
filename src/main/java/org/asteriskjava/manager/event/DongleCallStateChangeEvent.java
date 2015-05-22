package org.asteriskjava.manager.event;

public class DongleCallStateChangeEvent extends ManagerEvent
{
  private static final long serialVersionUID = 3257845467831284784L;
  private String device;
  private String callidx;
  private String newstate;

  public DongleCallStateChangeEvent(Object source)
  {
    super(source);
  }

  public String getDevice() {
    return this.device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

    public String getCallidx() {
        return callidx;
    }

    public void setCallidx(String callidx) {
        this.callidx = callidx;
    }

    public String getNewstate() {
        return newstate;
    }

    public void setNewstate(String newstate) {
        this.newstate = newstate;
    }

  

}
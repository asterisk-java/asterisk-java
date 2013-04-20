package org.asteriskjava.manager.event;

public class DongleCENDEvent extends ManagerEvent
{
  private static final long serialVersionUID = 3257845467831284784L;
  private String device;
  private String endstatus;
  private String cccause;
  private String duration;
  private String callidx;
  
  

  public DongleCENDEvent(Object source)
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

    public String getCccause() {
        return cccause;
    }

    public void setCccause(String cccause) {
        this.cccause = cccause;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEndstatus() {
        return endstatus;
    }

    public void setEndstatus(String endstatus) {
        this.endstatus = endstatus;
    }

 

  

}
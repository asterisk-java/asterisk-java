package org.asteriskjava.manager.event;

public class DongleNewSMSEvent extends ManagerEvent
{
  private static final long serialVersionUID = 3257845467831284784L;
  private String from;
  private String messageline0;
  private String device;
  private String linecount;
  

  public DongleNewSMSEvent(Object source)
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

    public String getLinecount() {
        return linecount;
    }

    public void setLinecount(String linecount) {
        this.linecount = linecount;
    }

    public String getMessageline0() {
        return messageline0;
    }

    public void setMessageline0(String messageline0) {
        this.messageline0 = messageline0;
    }


}
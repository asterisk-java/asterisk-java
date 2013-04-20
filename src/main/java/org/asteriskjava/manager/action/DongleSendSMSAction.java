package org.asteriskjava.manager.action;

public class DongleSendSMSAction extends AbstractManagerAction
{
  static final long serialVersionUID = 8194597741743334704L;
  private String device;
  private String number;
  private String message;

    @Override
  public String getAction()
  {
    return "DongleSendSMS";
  }

  public String getDevice()
  {
    return this.device;
  }

  public void setDevice(String d)
  {
    this.device = d;
  }

  public String getNumber()
  {
    return this.number;
  }

  public void setNumber(String callerId)
  {
    this.number = callerId;
  }

  public String getMessage()
  {
    return this.message;
  }

  public void setMessage(String m)
  {
    this.message = m;
  }
}
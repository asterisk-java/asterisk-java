package org.asteriskjava.manager.event;

public class DongleNewCMGREvent extends ManagerEvent
{
    private static final long serialVersionUID = 3257845467831284784L;
    private String device;

    public DongleNewCMGREvent(Object source)
    {
        super(source);
    }

    public String getDevice()
    {
        return this.device;
    }

    public void setDevice(String device)
    {
        this.device = device;
    }

}

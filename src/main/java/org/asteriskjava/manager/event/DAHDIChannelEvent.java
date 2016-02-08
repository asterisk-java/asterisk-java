
package org.asteriskjava.manager.event;

/**
 * @author Sebastian
 */
public class DAHDIChannelEvent extends ManagerEvent
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dahdichannel;
    private String dahdispan;
    private String uniqueid;
    private String channel;

    public String getDahdichannel()
    {
        return dahdichannel;
    }

    public void setDahdichannel(String dahdichannel)
    {
        this.dahdichannel = dahdichannel;
    }

    public String getDahdispan()
    {
        return dahdispan;
    }

    public void setDahdispan(String dahdispan)
    {
        this.dahdispan = dahdispan;
    }

    public String getUniqueid()
    {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid)
    {
        this.uniqueid = uniqueid;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public DAHDIChannelEvent(Object source)
    {
        super(source);
    }

}

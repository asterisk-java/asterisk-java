package org.asteriskjava.manager.action.proxy;

/**
 * Disconnects a proxy<->server session.
 * 
 * @author srt
 * @since 0.3
 */
public class DropServerProxyAction extends AbstractManagerProxyAction
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 7080183949750838050L;

    private String server;

    public DropServerProxyAction()
    {
        
    }
    
    public DropServerProxyAction(String server)
    {
        this.server = server;
    }

    @Override
    public String getAction()
    {
        return "DropServer";
    }

    /**
     * Returns the hostname or IP address of the server to remove.
     * 
     * @return the hostname or IP address of the server to remove.
     */
    public String getServer()
    {
        return server;
    }

    /**
     * Sets the hostname or IP address of the server to remove.<br>
     * This should exactly match the entry in your config host= section,
     * or whatever name you used with ProxyAction: AddServer.
     * 
     * @param server the hostname or IP address of the server to remove.
     */
    public void setServer(String server)
    {
        this.server = server;
    }
}

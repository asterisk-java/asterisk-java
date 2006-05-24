package org.asteriskjava.manager.action.proxy;

/**
 * Initiates a proxy connection to a new Asterisk Server; this has the same 
 * effect of including a host entry in your <code>host=</code> section 
 * of the configuration file.
 * 
 * @author srt
 * @since 0.3
 */
public class AddServerProxyAction extends AbstractManagerProxyAction
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 7080183949750838050L;

    private String server;
    private Integer port;
    private String username;
    private String secret;
    private Boolean events;

    public AddServerProxyAction()
    {
        
    }
    
    public AddServerProxyAction(String server, Integer port, String username, String secret, Boolean events)
    {
        this.server = server;
        this.port = port;
        this.username = username;
        this.secret = secret;
        this.events = events;
    }

    @Override
    public String getAction()
    {
        return "AddServer";
    }

    /**
     * Returns the name or IP address of the new Asterisk server to connect to.
     *  
     * @return the name or IP address of the new server to connect to.
     */
    public String getServer()
    {
        return server;
    }

    /**
     * Sets the name or IP address of the new Asterisk server to connect to.
     * 
     * @param server the name or IP address of the new server to connect to.
     */
    public void setServer(String server)
    {
        this.server = server;
    }

    /**
     * Returns the manager port of the server to connect to.
     *  
     * @return the manager port of the server to connect to.
     */
    public Integer getPort()
    {
        return port;
    }

    /**
     * Sets the manager port of the server to connect to.
     * 
     * @param port the manager port of the server to connect to.
     */
    public void setPort(Integer port)
    {
        this.port = port;
    }

    /**
     * Returns the username to use for login.
     * 
     * @return the username to use for login.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the username to use for login.
     * 
     * @param username the username to use for login.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Returns the password to use for login.
     * 
     * @return the password to use for login.
     */
    public String getSecret()
    {
        return secret;
    }

    /**
     * Sets the password to use for login.
     * 
     * @param secret the password to use for login.
     */
    public void setSecret(String secret)
    {
        this.secret = secret;
    }

    public Boolean getEvents()
    {
        return events;
    }

    public void setEvents(Boolean events)
    {
        this.events = events;
    }
}

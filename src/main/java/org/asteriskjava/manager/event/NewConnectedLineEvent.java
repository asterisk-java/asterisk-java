package org.asteriskjava.manager.event;

/**
 * Created by plhk on 1/15/15.
 */
public class NewConnectedLineEvent extends ManagerEvent
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String channel;
    private String uniqueId;
    private String language;
    private String accountCode;
    private String linkedId;

    public NewConnectedLineEvent(Object source)
    {
        super(source);
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getLinkedId()
    {
        return linkedId;
    }

    public void setLinkedId(String linkedId)
    {
        this.linkedId = linkedId;
    }

 
}

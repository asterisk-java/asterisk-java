package org.asteriskjava.manager.event;

public class DialEndEvent extends DialEvent
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String language;
    private String destLanguage;
    private String accountCode;
    private String destAccountCode;
    private String destLinkedId;
    private String linkedId;

    public DialEndEvent(Object source)
    {
        super(source);
        setSubEvent(SUBEVENT_END);
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getDestLanguage()
    {
        return destLanguage;
    }

    public void setDestLanguage(String destLanguage)
    {
        this.destLanguage = destLanguage;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getDestAccountCode()
    {
        return destAccountCode;
    }

    public void setDestAccountCode(String destAccountCode)
    {
        this.destAccountCode = destAccountCode;
    }

    public String getDestLinkedId()
    {
        return destLinkedId;
    }

    public void setDestLinkedId(String destLinkedId)
    {
        this.destLinkedId = destLinkedId;
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

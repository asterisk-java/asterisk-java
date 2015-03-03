package org.asteriskjava.manager.event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class ChallengeResponseFailedEvent extends ManagerEvent
{
    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 1L;

    private String severity;
    private String eventversion;
    private String service;
    private String remoteaddress;
    private String localaddress;

    /**
     * @param source
     */
	public ChallengeResponseFailedEvent(Object source) {
		super(source);
	}

    public String getSeverity()
    {
        return severity;
    }
    public void setSeverity(String severity)
    {
        this.severity = severity;
    }

    public String getEventversion()
    {
        return eventversion;
    }
    public void setEventversion(String eventversion)
    {
        this.eventversion = eventversion;
    }
   
    
    public String getService()
    {
        return service;
    }
    public void setService(String service)
    {
        this.service = service;
    }

    public String getRemoteAddress()
    {
        return remoteaddress;
    }
    public void setRemoteAddress(String remoteaddress)
    {
        this.remoteaddress = remoteaddress;
    }

    public String getLocalAddress()
    {
        return localaddress;
    }
    public void setLocalAddress(String localaddress)
    {
        this.localaddress = localaddress;
    }

    public void setEventtv(String eventv)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
        Long t = 0L;
        try
        {
        	Date date = format.parse(eventv);
        	t = date.getTime();
        }
        catch (ParseException ex){}
        finally
        {
        	this.setTimestamp(t.doubleValue());
        }
    }
}

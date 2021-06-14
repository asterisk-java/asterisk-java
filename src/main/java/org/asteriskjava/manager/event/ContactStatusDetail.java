package org.asteriskjava.manager.event;

/**
 * A ContactStatusDetail event is triggered in response to a
 * {@link org.asteriskjava.manager.action.PJSipShowEndpoint}, and contains
 * information about a PJSIP Contact
 * <p>
 *
 * @author Steve Sether
 * @version $Id$
 * @since 12
 */

public class ContactStatusDetail extends ResponseEvent
{

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 987290433601178780L;
    private String aor;
    private String uri;
    private String userAgent;
    private long regExpire;
    private String viaAddress;
    private String callID;
    private String status;
    // roundtripusec when it contains a value is a long, but when it doesn't
    // asterisk reports "N/A"
    private String roundtripUsec;
    private String endpointName;
    private String id;
    private Boolean authenticateQualify;
    private String outboundProxy;
    private String path;
    private int qualifyFrequency;
    private Float qualifyTimeout;

    public String getAor()
    {
        return aor;
    }

    public void setAor(String aor)
    {
        this.aor = aor;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    public long getRegExpire()
    {
        return regExpire;
    }

    public void setRegExpire(long regExpire)
    {
        this.regExpire = regExpire;
    }

    public String getViaAddress()
    {
        return viaAddress;
    }

    public void setViaAddress(String viaAddress)
    {
        this.viaAddress = viaAddress;
    }

    public String getCallID()
    {
        return callID;
    }

    public void setCallID(String callID)
    {
        this.callID = callID;
    }

    public String getEndpointName()
    {
        return endpointName;
    }

    public void setEndpointName(String endpointName)
    {
        this.endpointName = endpointName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Boolean isAuthenticateQualify()
    {
        return authenticateQualify;
    }

    public void setAuthenticateQualify(Boolean authenticateQualify)
    {
        this.authenticateQualify = authenticateQualify;
    }

    public String getOutboundProxy()
    {
        return outboundProxy;
    }

    public void setOutboundProxy(String outboundProxy)
    {
        this.outboundProxy = outboundProxy;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public int getQualifyFrequency()
    {
        return qualifyFrequency;
    }

    public void setQualifyFrequency(int qualifyFrequency)
    {
        this.qualifyFrequency = qualifyFrequency;
    }

    public Float getQualifyTimeout()
    {
        return qualifyTimeout;
    }

    public void setQualifyTimeout(Float qualifyTimeout)
    {
        this.qualifyTimeout = qualifyTimeout;
    }

    public void setQualifyTimeout(String qualifyTimeout)
    {
        this.qualifyTimeout = Float.parseFloat(qualifyTimeout);
    }

    public ContactStatusDetail(Object source)
    {
        super(source);
    }

    public String getRoundtripUsec()
    {
        return roundtripUsec;
    }

    public void setRoundtripUsec(String roundtripUsec)
    {
        this.roundtripUsec = roundtripUsec;
    }

}

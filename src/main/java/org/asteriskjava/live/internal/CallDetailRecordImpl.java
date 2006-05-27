package org.asteriskjava.live.internal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.asteriskjava.live.AmaFlags;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.CallDetailRecord;
import org.asteriskjava.live.Disposition;
import org.asteriskjava.manager.event.CdrEvent;

public class CallDetailRecordImpl implements CallDetailRecord
{
    private static final Map<String, Disposition> DISPOSITION_MAP;
    private static final Map<String, AmaFlags> AMA_FLAGS_MAP;
    private final AsteriskChannelImpl channel;
    private final AsteriskChannelImpl destinationChannel;

    private final String accountCode;
    private final String destinationContext;
    private final String destinationExtension;
    private final String lastApplication;
    private final String lastData;
    private final Date startDate;
    private final Date answerDate;
    private final Date endDate;
    private final Integer duration;
    private final Integer billableSeconds;
    private final Disposition disposition;
    private final AmaFlags amaFlags;
    private final String userField;

    static
    {
        DISPOSITION_MAP = new HashMap<String, Disposition>();
        DISPOSITION_MAP.put(CdrEvent.DISPOSITION_ANSWERED, Disposition.ANSWERED);
        DISPOSITION_MAP.put(CdrEvent.DISPOSITION_BUSY, Disposition.BUSY);
        DISPOSITION_MAP.put(CdrEvent.DISPOSITION_FAILED, Disposition.FAILED);
        DISPOSITION_MAP.put(CdrEvent.DISPOSITION_NO_ANSWER, Disposition.NO_ANSWER);
        DISPOSITION_MAP.put(CdrEvent.DISPOSITION_UNKNOWN, Disposition.UNKNOWN);

        AMA_FLAGS_MAP = new HashMap<String, AmaFlags>();
        AMA_FLAGS_MAP.put(CdrEvent.AMA_FLAG_BILLING, AmaFlags.BILLING);
        AMA_FLAGS_MAP.put(CdrEvent.AMA_FLAG_DOCUMENTATION, AmaFlags.DOCUMENTATION);
        AMA_FLAGS_MAP.put(CdrEvent.AMA_FLAG_OMIT, AmaFlags.OMIT);
        AMA_FLAGS_MAP.put(CdrEvent.AMA_FLAG_UNKNOWN, AmaFlags.UNKNOWN);
    }

    CallDetailRecordImpl(AsteriskChannelImpl channel, AsteriskChannelImpl destinationChannel, CdrEvent cdrEvent)
    {
        //TODO add timezone to AsteriskServer
        TimeZone tz = TimeZone.getDefault();
        this.channel = channel;
        this.destinationChannel = destinationChannel;

        accountCode = cdrEvent.getAccountCode();
        destinationContext = cdrEvent.getDestinationContext();
        destinationExtension = cdrEvent.getDestination();
        lastApplication = cdrEvent.getLastApplication();
        lastData = cdrEvent.getLastData();
        startDate = cdrEvent.getStartTimeAsDate(tz);
        answerDate = cdrEvent.getAnswerTimeAsDate(tz);
        endDate = cdrEvent.getEndTimeAsDate(tz);
        duration = cdrEvent.getDuration();
        billableSeconds = cdrEvent.getBillableSeconds();
        if (cdrEvent.getAmaFlags() != null)
        {
            amaFlags = AMA_FLAGS_MAP.get(cdrEvent.getAmaFlags());
        }
        else
        {
            amaFlags = null;
        }
        if (cdrEvent.getDisposition() != null)
        {
            disposition = DISPOSITION_MAP.get(cdrEvent.getDisposition());
        }
        else
        {
            disposition = null;
        }
        userField = cdrEvent.getUserField();
    }

    public AsteriskChannel getChannel()
    {
        return channel;
    }

    public AsteriskChannel getDestinationChannel()
    {
        return destinationChannel;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public AmaFlags getAmaFlags()
    {
        return amaFlags;
    }

    public Date getAnswerDate()
    {
        return answerDate;
    }

    public Integer getBillableSeconds()
    {
        return billableSeconds;
    }

    public String getDestinationContext()
    {
        return destinationContext;
    }

    public String getDestinationExtension()
    {
        return destinationExtension;
    }

    public Disposition getDisposition()
    {
        return disposition;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public String getLastApplication()
    {
        return lastApplication;
    }

    public String getLastData()
    {
        return lastData;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public String getUserField()
    {
        return userField;
    }
}

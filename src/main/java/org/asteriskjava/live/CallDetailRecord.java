package org.asteriskjava.live;

import java.util.Date;

/**
 * Represents an Asterisk Call Detail Record (CDR).
 * 
 * @see org.asteriskjava.manager.event.CdrEvent
 * @author srt
 * @version $Id$
 * @since 0.3
 */
public interface CallDetailRecord
{
    AsteriskChannel getChannel();

    AsteriskChannel getDestinationChannel();

    String getAccountCode();

    AmaFlags getAmaFlags();

    Date getAnswerDate();

    Integer getBillableSeconds();

    String getDestinationContext();

    String getDestinationExtension();

    Disposition getDisposition();

    Integer getDuration();

    Date getEndDate();

    String getLastApplication();

    String getLastData();

    Date getStartDate();

    String getUserField();

}

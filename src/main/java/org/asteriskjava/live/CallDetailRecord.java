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

    /**
     * Returns the account number that is usually used to identify the party to bill for the call.<p>
     * Corresponds to CDR field <code>accountcode</code>.
     * 
     * @return the account number.
     */
    String getAccountCode();

    AmaFlags getAmaFlags();

    /**
     * Returns the destination context.<p>
     * Corresponds to CDR field <code>dcontext</code>.
     * 
     * @return the destination context.
     */
    String getDestinationContext();

    /**
     * Returns the destination extension.<p>
     * Corresponds to CDR field <code>dst</code>.
     * 
     * @return the destination extension.
     */
    String getDestinationExtension();

    Disposition getDisposition();

    Date getStartDate();

    Date getAnswerDate();

    Date getEndDate();

    /**
     * Returns the total time (in seconds) the caller spent in the system from dial to hangup.<p>
     * Corresponds to CDR field <code>duration</code>.
     * 
     * @return the total time in system in seconds.
     */
    Integer getDuration();

    /**
     * Returns the total time (in seconds) the call was up from answer to hangup.<p>
     * Corresponds to CDR field <code>billsec</code>.
     * 
     * @return the total time in call in seconds.
     */
    Integer getBillableSeconds();

    /**
     * Returns the last application if appropriate, for example "VoiceMail".<p>
     * Corresponds to CDR field <code>lastapp</code>.
     * 
     * @return the last application or <code>null</code> if not avaialble.
     */
    String getLastApplication();

    /**
     * Returns the last application's data (arguments), for example "s1234".<p>
     * Corresponds to CDR field <code>lastdata</code>.
     * 
     * @return the last application's data or <code>null</code> if not avaialble.
     */
    String getLastAppData();

    /**
     * Returns the user-defined field as set by <code>Set(CDR(userfield)=Value)</code>.<p>
     * Corresponds to CDR field <code>userfield</code>.
     * 
     * @return the user-defined field.
     */
    String getUserField();
}

package org.asteriskjava.pbx;

import java.util.Date;
import java.util.List;

public interface Call
{
    /**
     * Used to identify which channel of a call an operation is to be performed
     * on. This is designed to stop the mis-use of some methods which operate on
     * a specific channel of a call. The Operand allows the caller to specify
     * which channel to operate on without having to explicitly pass in the
     * channel. Without this enum they could potentially pass in a channel which
     * is not owned by the call.
     * 
     * @author bsutton
     */

    enum OperandChannel
    {
        /**
         * this party received (did not originate) the call
         */
        ACCEPTING_PARTY,

        /**
         * this party originated (dialled) the call
         */
        ORIGINATING_PARTY,

        TRANSFER_TARGET_PARTY,

        /**
         * is an end point that is on an external trunk
         */
        REMOTE_PARTY,
        /**
         * a local call is a handset attached directly (via sip) to the pbx
         */
        LOCAL_PARTY
    }

    // Returns true of the given channel is part of this call.
    boolean contains(Channel channel);

    // The caller id of the party that accepted (answered) the call.
    CallerID getAcceptingPartyCallerID();

    // The caller id of the party that dialed the call.
    CallerID getOriginatingPartyCallerID();

    // The caller id of the remote party.
    CallerID getRemotePartyCallerID();

    // The date/time the call started.
    Date getCallStartTime();

    // The channel associated with the party that originated (dialed) the call.
    Channel getOriginatingParty();

    // The channel associated with the part that accepted (answered) the call.
    Channel getAcceptingParty();

    Channel getTransferTargetParty();

    /**
     * retrieves the channel associated with the give Operand.
     * 
     * @param lhs
     * @return
     */
    Channel getOperandChannel(OperandChannel lhs);

    /**
     * This method use the CallDirection to determine which leg of the call is
     * the local call and which is the called/calling party. It then returns the
     * called/calling party.
     * 
     * @return
     */
    Channel getRemoteParty();

    /**
     * This method use the CallDirection to determine which leg of the call is
     * the local call and which is the called/calling party. It then returns the
     * local call .
     * 
     * @return
     */
    Channel getLocalParty();

    /**
     * Returns true of the Call can be split into two (or more) separate calls.
     * @return
     */
    boolean canSplit();

    /**
     * Returns the direction of the call.
     * The call Direction can be a little esoteric as a call can
     * come in and then be transferred out again. So is this an inbound or outbound call?
     * @return
     */
    CallDirection getDirection();

    /**
     * Returns a list of the Channels associated with this call.
     * @return
     */
    List<Channel> getChannels();
}

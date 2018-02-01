package org.asteriskjava.pbx;

import java.util.List;

import org.asteriskjava.pbx.Call.OperandChannel;
import org.asteriskjava.pbx.activities.BlindTransferActivity;
import org.asteriskjava.pbx.activities.DialActivity;
import org.asteriskjava.pbx.activities.HoldActivity;
import org.asteriskjava.pbx.activities.JoinActivity;
import org.asteriskjava.pbx.activities.ParkActivity;
import org.asteriskjava.pbx.activities.RedirectToActivity;
import org.asteriskjava.pbx.activities.SplitActivity;

/**
 * Provides an abstracted interface for communicating with a PBX.
 * 
 * @author bsutton
 */
public interface PBX
{

    /**
     * Call this method when shutting down the PBX interface to allow it to
     * cleanup.
     */
    void shutdown();

    /**
     * Returns true if the bridge function is supported.
     */
    boolean isBridgeSupported();

    /**
     * The BlindTransferActivity is used by the AsteriksPBX to transfer a live
     * channel to an endpoint that may need to be dialed. Note: this action is a
     * long running action which is dependent on a user action to complete. The
     * action will only complete when the call is answered by the tranferTarget.
     * It is recommended that you use the blindTransfer method which takes a
     * callback as there is no way to cancel this action. Until the
     * transferTarget answers the call or the blind transfer times out.
     * 
     * @param channelToTransfer the channel which is to be blind transfered
     * @param transferTarget the endPoint to which the channel is to be
     *            tranferred.
     * @param toCallerID - the callerID to display to the transferTarget during
     *            the transfer.
     * @param autoAnswer - if true then the transferTarget is to be sent an auto
     *            answer header
     * @param timeout - timeout (in seconds) for the blind transfer attempt.
     *            When the timeout is reached the Blind Transfer will be
     *            cancelled.
     * @param ActivityCallback
     */
    BlindTransferActivity blindTransfer(final Call call, OperandChannel channelToTransfer, final EndPoint transferTarget,
            final CallerID toCallerID, boolean autoAnswer, long timeout);

    /**
     * The BlindTransferActivity is used by the AsteriksPBX to transfer a live
     * channel to an endpoint that may need to be dialed. Note: this action is a
     * long running action which is defendant on a user action to complete. The
     * action will only complete when the call is answered by the tranferTarget.
     * You can cancel the action at any time by using the activity object
     * returned from the callback start method.
     * 
     * @param channelToTransfer the channel which is to be blind transfered
     * @param transferTarget the endPoint to which the channel is to be
     *            transferred.
     * @param toCallerID - the callerID to display to the transferTarget during
     *            the transfer.
     * @param autoAnswer - if true then the transferTarget is to be sent an auto
     *            answer header
     * @param timeout - timeout for the blind transfer attempt. When the timeout
     *            is reached the Blind Transfer will be cancelled.
     */
    void blindTransfer(Call call, OperandChannel channelToTransfer, final EndPoint transferTarget, final CallerID toCallerID,
            boolean autoAnswer, long timeout, ActivityCallback<BlindTransferActivity> callback);

    /**
     * Sends a DTMF tone to given channel. Not returning until the tone has been
     * sent.
     * 
     * @param channel
     */
    void conference(Channel channelOne, Channel channelTwo, Channel channelThree);

    /**
     * Sends a DTMF tone to the given channel. This method returns immediately
     * with progress information passed to the given callback. Note: the
     * callback method will be called from a different thread.
     * 
     * @param channel
     * @param callback
     */
    void conference(Channel channelOne, Channel channelTwo, Channel channelThree, ActivityCallback<Activity> callback);

    /**
     * Dials the given phone number using the specified trunk. Not returning
     * until the call has been dialled. The dial will return as soon as the
     * trunk comes up, it does not wait for remote end to answer.
     * 
     * @return Call - the call resulting from dialing the number.
     */
    DialActivity dial(EndPoint from, CallerID fromCallerID, EndPoint to, CallerID toCallerID);

    /**
     * Dials the given phone number using the specified trunk. Not returning
     * until the call has been dialled. The dial will return as soon as the
     * trunk comes up, it does not wait for remote end to answer.
     * 
     * @return Call the call resulting from dialing the number.
     */
    void dial(EndPoint from, CallerID fromCallerID, EndPoint to, CallerID toCallerID,
            ActivityCallback<DialActivity> callback);

    /**
     * Hangs up the given channel. Not returning until the call is hungup.
     * 
     * @param channel
     * @throws PBXException
     */
    void hangup(Channel channel) throws PBXException;

    /**
     * Hangs up the given channel. This method returns immediately with progress
     * information passed to the given callback. Note: the callback method will
     * be called from a different thread.
     * 
     * @param channel
     * @param callback
     */
    void hangup(Channel channel, ActivityCallback<Activity> callback);

    /**
     * Hangup the given call, not returning until the call is hungup.
     * 
     * @param call
     */
    void hangup(Call call) throws PBXException;

    /**
     * Put the given channel on hold.
     * 
     * @param channel
     * @return
     */
    HoldActivity hold(Channel channel);

    /**
     * Returns true if the mute function is supported.
     */
    boolean isMuteSupported();

    /**
     * Parks a call by moving the parkChannel to the parking lot and hanging up
     * the hangupChannel. This method will not return until the park has
     * completed. The park expects two channels which are two legs legs of the
     * same call. The parkChannel will be redirect to the njr-park extension.
     * The hangupChannel (the second leg of the call) will be hung up. The
     * (obvious?) limitation is that we can't park something like a conference
     * call as it has more than two channels. The returned iParkActivty can be
     * used to obtain the extension the call was parked on.
     * 
     * @param the call which is to be parked.
     * @param parkChannel - the channel which is going to be redirect to the
     *            njr-park extension.
     * @param hangupChannel - the channel which is going to be hungup.
     */
    ParkActivity park(Call call, Channel parkChannel);

    /**
     * Parks a call by moving the parkChannel to the parking lot and hanging up
     * the hangupChannel. This method returns almost immediately call progress
     * can be tracked via the callback. The park expects two channels which are
     * two legs legs of the same call. The parkChannel will be redirect to the
     * njr-park extension. The hangupChannel (the second leg of the call) will
     * be hung up. The (obvious?) limitation is that we can't park something
     * like a conference call as it has more than two channels. The returned
     * iParkActivty can be used to obtain the extension the call was parked on.
     * 
     * @param the call which is to be parked.
     * @param parkChannel - the channel which is going to be redirect to the
     *            njr-park extension.
     */
    void park(Call call, Channel parkChannel, ActivityCallback<ParkActivity> callback);

    /**
     * Sends a DTMF tone to given channel. Not returning until the tone has been
     * sent.
     * 
     * @param channel
     * @throws PBXException
     */
    void sendDTMF(Channel channel, DTMFTone tone) throws PBXException;

    /**
     * Sends a DTMF tone to the given channel. This method returns immediately
     * with progress information passed to the given callback. Note: the
     * callback method will be called from a different thread.
     * 
     * @param channel
     * @param callback
     */
    void sendDTMF(Channel channel, DTMFTone tone, ActivityCallback<Activity> callback);

    /**
     * Splits a call (with two channels) This call returns once the split action
     * has completed.
     */

    SplitActivity split(final Call callToSplit, final ActivityCallback<SplitActivity> listener) throws PBXException;

    RedirectToActivity redirectToActivity(final Channel channel, final ActivityCallback<RedirectToActivity> listener);

    void split(final Call callToSplit) throws PBXException;

    /**
     * Joins two calls not returning until the join completes. Each call must
     * only have one active channel
     */
    JoinActivity join(Call lhs, OperandChannel originatingOperand, Call rhs, OperandChannel acceptingOperand,
            CallDirection direction);

    void join(Call lhs, OperandChannel originatingOperand, Call rhs, OperandChannel acceptingOperand,
            CallDirection direction, ActivityCallback<JoinActivity> listener);

    /**
     * Returns the channel currently attached to the given end point, if one
     * exists. If no channel is attached to the given end point then null is
     * returned.
     * 
     * @return the channel attached to the end point or null if the endpoint is
     *         not currently in a call.
     */
    Channel getChannelByEndPoint(EndPoint endPoint);

    // /**
    // * Converts a String representation of an extension into an extension
    // object.
    // *
    // * The conversion is PBX specific.
    // *
    // * @param extension
    // * @return
    // */
    // iExtension getExtension(String extension);

    /**
     * Builds an end point from a fully qualified end point name. A fully
     * qualified name includes the tech. How the tech is encoded is pbx
     * specific.
     * 
     * @param fullyQualifiedEndPointName
     * @return
     */
    EndPoint buildEndPoint(String fullyQualifiedEndPointName);

    /**
     * Builds an iEndPoint from an end point name. If the tech isn't passed in
     * the endPointName the tech is set to the defaultTech The encoding for an
     * end point name is pbx specific.
     * 
     * @param endPointName
     * @return
     */
    EndPoint buildEndPoint(TechType defaultTech, String endPointName);

    EndPoint buildEndPoint(TechType tech, Trunk trunk, String endPointName);

    void transferToMusicOnHold(Channel channel) throws PBXException;

    CallerID buildCallerID(String number, String name);

    boolean isChannel(String channel);

    boolean waitForChannelsToQuiescent(List<Channel> channels, long timeout);

    EndPoint getExtensionAgi();

    boolean waitForChannelToQuiescent(Channel channel, int timeout);

    Trunk buildTrunk(String string);

    void performPostCreationTasks();

}

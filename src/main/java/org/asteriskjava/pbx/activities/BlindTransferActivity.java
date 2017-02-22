package org.asteriskjava.pbx.activities;

import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.Call;
import org.asteriskjava.pbx.CallerID;
import org.asteriskjava.pbx.Channel;
import org.asteriskjava.pbx.EndPoint;
import org.asteriskjava.pbx.PBXException;

public interface BlindTransferActivity extends Activity
{
	/**
	 * A bridged call is a successful transfer all other options are a failed
	 * transfer.
	 * 
	 * @author bsutton
	 * 
	 */
	enum CompletionCause
	{
		// The call was bridged.
		BRIDGED("Connected")
		// The call hungup
		, HANGUP("Transfer Failed, Caller Hungup or Destination Busy")
		// A timeout occur during the transfer
		// which essentially means the transferTarget didn't answer the phone
		, TIMEOUT("Transfer Failed, Timeout")
		// The cancel method was called.
		, CANCELLED("Transfer Cancelled");

		String message;
		
		CompletionCause(String message)
		{
			this.message = message;
		}
		
		public String getMessage()
		{
			return message;
		}
	}

	Channel getChannelToTransfer();

	CallerID getTransferTargetCallerID();

	EndPoint getTransferTarget();

	Channel getTransferTargetChannel();

	CompletionCause getCompletionCause();

	void cancel();

	/**
	 * The call created as a result of the blind transfer being answered.
	 * 
	 * @return
	 * @throws PBXException 
	 */
	Call getNewCall() throws PBXException;

}

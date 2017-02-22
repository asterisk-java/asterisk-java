package org.asteriskjava.pbx.activities;

import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.Call;

public interface SplitActivity extends Activity
{

	/**
	 * After a call has been split we get a new calls. The call created as a
	 * result of the lhsOperandChannel being split can be retrieved by calling
	 * this method.
	 * 
	 * @return the call which holds the call associated with the
	 *         lhsOperandChannel
	 */
	Call getLHSCall();

	/**
	 * After a call has been split we get a new calls. The call created as a
	 * result of the rhsOperandChannel being split can be retrieved by calling
	 * this method.
	 * 
	 * This method will return now if split was called with only a single
	 * operand channel.
	 * 
	 * @return the call which holds the call associated with the
	 *         lhsOperandChannel
	 */
	Call getRHSCall();

}

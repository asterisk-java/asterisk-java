package org.asteriskjava.pbx.activities;

import org.asteriskjava.pbx.Activity;
import org.asteriskjava.pbx.Call;

public interface RedirectToActivity extends Activity
{

    /**
     * After a call has been split we get a new calls. The call created as a
     * result of the lhsOperandChannel being split can be retrieved by calling
     * this method.
     * 
     * @return the call which holds the call associated with the
     *         lhsOperandChannel
     */
    Call getCall();

}

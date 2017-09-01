package org.asteriskjava.pbx;

import org.asteriskjava.pbx.CallImpl.TransferType;
import org.asteriskjava.pbx.activities.BlindTransferActivity;

public class CallStateDataTransfer extends CallStateData
{

    private TransferType _transferType;
    /**
     * the end point we are attempting to transfer the call to.
     */
    private EndPoint _transferTarget;

    private CallerID _transferTargetCallerID;

    private BlindTransferActivity _transferActivity;

    public CallStateDataTransfer(TransferType transferType, EndPoint transferTarget, CallerID transferTargetCallerID,
            BlindTransferActivity transferActivity)
    {
        this._transferType = transferType;
        this._transferTarget = transferTarget;
        this._transferTargetCallerID = transferTargetCallerID;
        this._transferActivity = transferActivity;
    }

    public EndPoint getTransferTarget()
    {
        return this._transferTarget;
    }

    public CallerID getTransferTargetCallerID()
    {
        return this._transferTargetCallerID;
    }

    public TransferType getTransferType()
    {
        return this._transferType;
    }

    public BlindTransferActivity getActivity()
    {
        return this._transferActivity;
    }
}

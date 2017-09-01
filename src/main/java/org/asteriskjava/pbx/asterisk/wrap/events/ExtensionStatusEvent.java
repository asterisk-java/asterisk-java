package org.asteriskjava.pbx.asterisk.wrap.events;

public class ExtensionStatusEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;

    enum Status
    {

        /**
         * No device INUSE or BUSY.
         */
        NOT_INUSE(org.asteriskjava.manager.event.ExtensionStatusEvent.NOT_INUSE),

        /**
         * One or more devices INUSE.
         */
        INUSE(org.asteriskjava.manager.event.ExtensionStatusEvent.INUSE),

        /**
         * All devices BUSY.
         */
        BUSY(org.asteriskjava.manager.event.ExtensionStatusEvent.BUSY),

        /**
         * All devices UNAVAILABLE/UNREGISTERED.
         */
        UNAVAILABLE(org.asteriskjava.manager.event.ExtensionStatusEvent.UNAVAILABLE),

        /**
         * One or more devices RINGING.
         */
        RINGING(org.asteriskjava.manager.event.ExtensionStatusEvent.RINGING);

        int _status;

        Status(int status)
        {
            this._status = status;
        }

        static Status valueOf(Integer status)
        {
            Status theStatus = null;
            for (Status aStatus : Status.values())
            {
                if (aStatus._status == status)
                {
                    theStatus = aStatus;
                    break;
                }
            }
            return theStatus;
        }
    }

    private final String exten;
    private final String context;
    private final String hint;
    private final Status status;

    public ExtensionStatusEvent(final org.asteriskjava.manager.event.ExtensionStatusEvent event)
    {
        super(event);
        this.exten = event.getExten();
        this.context = event.getContext();
        this.hint = event.getHint();
        this.status = Status.valueOf(event.getStatus());
    }

    public String getExten()
    {
        return this.exten;
    }

    public String getContext()
    {
        return this.context;
    }

    public String getHint()
    {
        return this.hint;
    }

    public Status getStatus()
    {
        return this.status;
    }

}

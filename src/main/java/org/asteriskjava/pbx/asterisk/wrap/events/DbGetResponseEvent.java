package org.asteriskjava.pbx.asterisk.wrap.events;

public class DbGetResponseEvent extends ManagerEvent
{
    private static final long serialVersionUID = 1L;

    private final String family;
    private final String key;
    private final String val;

    DbGetResponseEvent(final org.asteriskjava.manager.event.DbGetResponseEvent event)
    {
        super(event);
        this.family = event.getFamily();
        this.key = event.getKey();
        this.val = event.getVal();
    }

    public String getVal()
    {
        return this.val;
    }

    public String getFamily()
    {
        return this.family;
    }

    public String getKey()
    {
        return this.key;
    }

}

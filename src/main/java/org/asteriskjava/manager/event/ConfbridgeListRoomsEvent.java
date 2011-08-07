package org.asteriskjava.manager.event;

public class ConfbridgeListRoomsEvent extends ResponseEvent
{
    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 1L;

    private String conference;
    private Integer parties;
    private Integer marked;
    private Boolean locked;

    public ConfbridgeListRoomsEvent(Object source)
    {
        super(source);
    }

    /**
     * Sets the id of the conference to be listed.
     */
    public void setConference(String conference)
    {
        this.conference = conference;
    }

    /**
     * Returns the id of the conference to be listed.
     */
    public String getConference()
    {
        return conference;
    }

    /**
     * Sets the number of participants in this conference.
     *
     * @param parties the number of participants in this conference.
     */
    public void setParties(Integer parties)
    {
        this.parties = parties;
    }

    /**
     * Returns the number of participants in this conference.
     *
     * @return the number of participants in this conference.
     */
    public Integer getParties()
    {
        return parties;
    }

    /**
     * Sets the number of marked participants in this conference.
     *
     * @param marked the number of marked participants in this conference.
     */
    public void setMarked(Integer marked)
    {
        this.marked = marked;
    }

    /**
     * Returns the number of marked participants in this conference.
     *
     * @return the number of marked participants in this conference.
     */
    public Integer getMarked()
    {
        return marked;
    }

    public void setLocked(Boolean locked)
    {
        this.locked = locked;
    }

    public Boolean getLocked()
    {
        return locked;
    }
}

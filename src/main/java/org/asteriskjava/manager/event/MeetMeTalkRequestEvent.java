package org.asteriskjava.manager.event;

/**
 * A MeetMeTalkRequestEvent is triggered when a muted user requests talking in
 * a MeetMe conference.
 */
public class MeetMeTalkRequestEvent extends AbstractMeetMeEvent {
    private static final long serialVersionUID = 0L;

    private Boolean status;
    private Integer duration;

    /**
     * Constructs a MeetMeTalkRequestEvent.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MeetMeTalkRequestEvent(Object source) {
        super(source);
    }

    /**
     * The length of time (in seconds) that the MeetMe user has been in the
     * conference at the time of this event.
     *
     * @return the number of seconds this user has been in the conference.
     */
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Returns whether the user has started or stopped requesting talking.
     *
     * @return {@code true} if the user has started requesting talking,
     * {@code false} if the user has stopped requesting talking.
     */
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

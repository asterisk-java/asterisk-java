package org.asteriskjava.pbx.internal.asterisk;

public interface RoomOwner
{
    /**
     * implementations of this method should NOT take locks and should return
     * quickly. This is used as an indication that the room (even though empty)
     * is still required by the owner
     * 
     * @return true if the meetme room is still required
     */
    boolean isRoomStillRequired();

    /**
     * this can be usefull to null the reference to the Owner when the room is
     * nolong owned
     * 
     * @param meetmeRoom
     */
    void setRoom(MeetmeRoom meetmeRoom);
}

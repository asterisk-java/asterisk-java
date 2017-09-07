package org.asteriskjava.pbx.internal.asterisk;

import java.util.concurrent.TimeUnit;

public class DurationRoomOwner implements RoomOwner
{

    long end = System.currentTimeMillis();

    public DurationRoomOwner(long duration, TimeUnit units)
    {
        end = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(duration, units);
    }

    @Override
    public boolean isRoomStillRequired()
    {
        return System.currentTimeMillis() < end;
    }

    @Override
    public void setRoom(MeetmeRoom meetmeRoom)
    {

    }

}

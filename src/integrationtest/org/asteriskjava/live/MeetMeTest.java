package org.asteriskjava.live;

import java.util.Collection;

public class MeetMeTest extends AsteriskServerTestCase
{
    public void testMeetMeRoom() throws Exception
    {
        Collection<MeetMeRoom> rooms;
        
        server.initialize();
        //Thread.sleep(10000);
        server.getMeetMeRoom("1000");
        
        rooms = server.getMeetMeRooms();
        for (MeetMeRoom room : rooms)
        {
            System.out.println(room);
            for (MeetMeUser user : room.getUsers())
            {
                System.out.println("  " + user);
            }
        }
    }
}

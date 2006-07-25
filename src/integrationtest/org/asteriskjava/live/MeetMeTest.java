package org.asteriskjava.live;

import java.util.Collection;

public class MeetMeTest extends AsteriskServerTestCase
{
    public void testMeetMeRoom() throws Exception
    {
        server.getMeetMeRoom("1000");
        printRooms();
        System.out.println("waiting...");
        Thread.sleep(20000);
        printRooms();
    }
    
    private void printRooms() throws Exception
    {
        Collection<MeetMeRoom> rooms;
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

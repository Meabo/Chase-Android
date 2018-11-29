package project.chasemvp.Model.Events.Socket.Room;

/**
 * Created by Mehdi on 13/07/2017.
 */

public class SocketEventRoomJoin
{
    boolean joinedroom;

    public SocketEventRoomJoin(boolean joinedroom) {
        this.joinedroom = joinedroom;
    }

    public boolean isJoinedroom() {
        return joinedroom;
    }
}

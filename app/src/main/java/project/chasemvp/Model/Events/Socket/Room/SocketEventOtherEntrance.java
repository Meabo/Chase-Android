package project.chasemvp.Model.Events.Socket.Room;

/**
 * Created by Mehdi on 13/07/2017.
 */

public class SocketEventOtherEntrance
{
    boolean entered;

    public SocketEventOtherEntrance(boolean entered) {
        this.entered = entered;
    }

    public boolean isEntered() {
        return entered;
    }
}

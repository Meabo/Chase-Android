package project.chasemvp.Model.Events.Socket.Room;

/**
 * Created by Mehdi on 04/07/2017.
 */

public class SocketEventReady {
    boolean ready;
    String id;

    public SocketEventReady(boolean ready, String id) {
        this.ready = ready;
        this.id = id;
    }

    public boolean isReady() {
        return ready;
    }

    public String getId() {
        return id;
    }
}

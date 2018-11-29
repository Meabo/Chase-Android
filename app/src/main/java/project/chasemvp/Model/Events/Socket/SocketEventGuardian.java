package project.chasemvp.Model.Events.Socket;

/**
 * Created by Mehdi on 15/06/2017.
 */

public class SocketEventGuardian
{
   boolean got_object;

    public SocketEventGuardian(boolean got_object) {
        this.got_object = got_object;
    }

    public boolean isGot_object() {
        return got_object;
    }
}

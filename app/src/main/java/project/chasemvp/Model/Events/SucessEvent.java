package project.chasemvp.Model.Events;

/**
 * Created by Mehdi on 19/06/2017.
 */

public class SucessEvent
{
    boolean success;

    public SucessEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}

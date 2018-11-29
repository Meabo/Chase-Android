package project.chasemvp.Model.Events.GPS;

/**
 * Created by Mehdi on 02/08/2017.
 */

public class EventGPSPlay
{
    boolean play;

    public EventGPSPlay(boolean play) {
        this.play = play;
    }

    public boolean isPlay() {
        return play;
    }
}

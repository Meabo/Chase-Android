package project.chasemvp.Model.Events.Socket;

/**
 * Created by Mehdi on 15/06/2017.
 */

public class SocketEventSteal
{
    int score;
    boolean solo;


    public SocketEventSteal(int score, boolean solo) {
        this.score = score;
        this.solo = solo;
    }

    public int getScore() {
        return score;
    }
    public boolean isSolo()
    {
        return solo;
    }
}

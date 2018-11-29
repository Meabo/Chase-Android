package project.chasemvp.Model.Events.Socket;

/**
 * Created by Mehdi on 15/06/2017.
 */

public class SocketEventResult
{
    int score;
    double distance;
    double speed_avg;

    public SocketEventResult(int score, double distance, double speed_avg) {
        this.score = score;
        this.distance = distance;
        this.speed_avg = speed_avg;
    }

    public int getScore() {
        return score;
    }

    public double getDistance() {
        return distance;
    }

    public double getSpeed_avg() {
        return speed_avg;
    }
}

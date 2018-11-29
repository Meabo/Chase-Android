package project.chasemvp.Model.POJO.GoogleMapResult;

/**
 * Created by Mehdi on 12/04/2017.
 */

public class Result
{
    public Result(int sc, double dist, double spd_avg)
    {
        score = sc;
        distance = dist;
        speed_avg = spd_avg;
    }

    private int score;
    private double distance;
    private double speed_avg;

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

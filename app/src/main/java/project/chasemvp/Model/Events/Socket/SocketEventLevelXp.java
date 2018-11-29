package project.chasemvp.Model.Events.Socket;

/**
 * Created by Mehdi on 06/07/2017.
 */

public class SocketEventLevelXp
{
    int level;
    int xp;
    int xp_max;


    public SocketEventLevelXp(int level, int xp, int xp_max) {
        this.level = level;
        this.xp = xp;
        this.xp_max = xp_max;
    }

    public int getXp_max() {
        return xp_max;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }
}

package project.chasemvp.Model.Events.Socket.Room;

import java.util.List;

import project.chasemvp.Model.POJO.Chaser;

/**
 * Created by Mehdi on 13/07/2017.
 */

public class SocketEventPlayersInRoom
{
    List<Chaser> players;

    public SocketEventPlayersInRoom(List<Chaser> players) {
        this.players = players;
    }

    public List<Chaser> getPlayers() {
        return players;
    }
}

package project.chasemvp.Model.Events;

import java.util.List;

import project.chasemvp.Model.POJO.Chase;

/**
 * Created by Mehdi on 11/06/2017.
 */

public class ChasesEvent
{
    List<Chase> chases;

    public List<Chase> getChases() {
        return chases;
    }

    public ChasesEvent(List<Chase> chases) {
        this.chases = chases;
    }
}

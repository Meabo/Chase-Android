package project.chasemvp.Model.Events.Socket;

/**
 * Created by Mehdi on 15/06/2017.
 */

public class SocketEventSkill
{
    int skill;
    int duration;
    String pseudo;
    boolean skill_used;

    public SocketEventSkill (boolean skill_used, int skill, String pseudo, int duration)
    {
        this.skill = skill;
        this.duration = duration;
        this.pseudo = pseudo;
        this.skill_used = skill_used;
    }

    public SocketEventSkill(boolean skill_used) {
        this.skill_used = skill_used;
    }

    public boolean isSkill_used() {
        return skill_used;
    }

    public int getSkill() {
        return skill;
    }

    public int getDuration() {
        return duration;
    }

    public String getPseudo() {
        return pseudo;
    }
}

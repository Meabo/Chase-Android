package project.chasemvp.Model.Events;

/**
 * Created by Mehdi on 16/06/2017.
 */

public class PlayerEvent
{
    String pseudo;
    int level;
    int xp;
    String url_image;
   String email;
   String id;
  Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public String getUrl_image() {
        return url_image;
    }

    public String getEmail() {
        return email;
    }



    public PlayerEvent(String pseudo, int level, int xp, String id, boolean success) {

        this.pseudo = pseudo;
        this.level = level;
        this.xp = xp;
        this.id = id;
        this.success = success;
    }
    public PlayerEvent(boolean success)
    {
        this.success = success;
    }
}

package project.chasemvp.Model.POJO;

/**
 * Created by Mehdi on 11/06/2017.
 */

public class Chaser
{
    private String pseudo;
    private int level;

    public int getXp() {
        return xp;
    }

    private int xp;
    private String id;
    private String url_image;


    public String getUrl_image() {
        return url_image;
    }





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public Chaser(String pseudo, int level,int xp, String email, String id, String url_image) {
        this.pseudo = pseudo;
        this.level = level;
        this.xp = xp;
        this.email = email;
        this.id = id;
        this.url_image = url_image;
    }

    public Chaser(String pseudo, int level, int xp, String email, String id) {
        this.pseudo = pseudo;
        this.level = level;
        this.xp = xp;
        this.email = email;
        this.id = id;
        this.url_image = url_image;
    }

    public Chaser()
    {
    }


}

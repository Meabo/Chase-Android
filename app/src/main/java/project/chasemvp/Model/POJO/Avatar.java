package project.chasemvp.Model.POJO;

/**
 * Created by Mehdi on 03/07/2017.
 */

public class Avatar
{


    String id;
    String title;
    String url;



    public Avatar(String title, String url_image, String id) {

        this.title = title;
        this.url = url_image;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl_image() {
        return url;
    }

    public void setUrl_image(String url_image) {
        this.url = url_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

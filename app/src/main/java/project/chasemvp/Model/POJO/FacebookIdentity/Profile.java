
package project.chasemvp.Model.POJO.FacebookIdentity;

import java.util.List;

public class Profile
{
    private String provider;

    private String id;

    private String displayName;

    private Name name;

    private String gender;

    private List<Email> emails = null;

    private List<Photo> photos = null;

    private String raw;

    private Json json;

    public String getDisplayName() {
        return displayName;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
}

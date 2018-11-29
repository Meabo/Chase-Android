
package project.chasemvp.Model.POJO.FacebookIdentity;

public class ChaserIdentity  {


    private String provider;
    private String authScheme;
    private String externalId;
    private Profile profile;
    private Credentials credentials;
    private String created;
    private String modified;
    private String userId;

    public String getProvider() {
        return provider;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getUserId() {
        return userId;
    }
}

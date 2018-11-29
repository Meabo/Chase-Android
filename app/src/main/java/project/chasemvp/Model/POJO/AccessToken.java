package project.chasemvp.Model.POJO;

/**
 * Created by Mehdi on 12/06/2017.
 */

public class AccessToken
{
    String userId;
    String access_token;

    public AccessToken(String userId, String access_token) {
        this.userId = userId;
        this.access_token = access_token;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccess_token() {
        return access_token;
    }
}

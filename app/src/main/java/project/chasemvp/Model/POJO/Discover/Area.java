
package project.chasemvp.Model.POJO.Discover;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Area {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("center")
    @Expose
    private Center center;
    @SerializedName("locationarea")
    @Expose
    private List<Locationarea> locationarea = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("url_image")
    @Expose
    private String urlImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public List<Locationarea> getLocationarea() {
        return locationarea;
    }

    public void setLocationarea(List<Locationarea> locationarea) {
        this.locationarea = locationarea;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

}

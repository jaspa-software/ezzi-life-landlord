package za.co.ezzilyf.partner.models;

import java.io.Serializable;

public class Photo implements Serializable {

    private String photoId;

    private String propertyId;

    private String url;


    public Photo() {

    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }
}

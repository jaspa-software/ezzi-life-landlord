package za.co.ezzilyf.partner.models;

import java.io.Serializable;

public class NearBy implements Serializable {

    private String institution;

    private String campus;

    private String propertyName;

    private String propertyCoverImage;

    private String propertyId;

    private String emblem;

    public NearBy() {

    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyCoverImage() {
        return propertyCoverImage;
    }

    public void setPropertyCoverImage(String propertyCoverImage) {
        this.propertyCoverImage = propertyCoverImage;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getEmblem() {
        return emblem;
    }

    public void setEmblem(String emblem) {
        this.emblem = emblem;
    }
}

package za.co.ezzilyf.partner.models;

import java.io.Serializable;

public class Property implements Serializable {

    private String propertyRefNumber;

    private String propertyName;

    private String propertyType;

    private String properyAddress;

    private String propertyOwnerName;

    private String properyOwnerUid;

    // amenities
    private String wifi;

    private String studyArea;

    private String wardrobes;

    private String parking;

    private String cookingArea;

    private String showers;

    private String lounge;

    private String laundry;

    // property status
    private String propertyStatus;

    public Property() {

        //super();

    }

    public String getPropertyRefNumber() {
        return propertyRefNumber;
    }

    public void setPropertyRefNumber(String propertyRefNumber) {
        this.propertyRefNumber = propertyRefNumber;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getProperyAddress() {
        return properyAddress;
    }

    public void setProperyAddress(String properyAddress) {
        this.properyAddress = properyAddress;
    }

    public String getPropertyOwnerName() {
        return propertyOwnerName;
    }

    public void setPropertyOwnerName(String propertyOwnerName) {
        this.propertyOwnerName = propertyOwnerName;
    }

    public String getProperyOwnerUid() {
        return properyOwnerUid;
    }

    public void setProperyOwnerUid(String properyOwnerUid) {
        this.properyOwnerUid = properyOwnerUid;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getStudyArea() {
        return studyArea;
    }

    public void setStudyArea(String studyArea) {
        this.studyArea = studyArea;
    }

    public String getWardrobes() {
        return wardrobes;
    }

    public void setWardrobes(String wardrobes) {
        this.wardrobes = wardrobes;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getCookingArea() {
        return cookingArea;
    }

    public void setCookingArea(String cookingArea) {
        this.cookingArea = cookingArea;
    }

    public String getShowers() {
        return showers;
    }

    public void setShowers(String showers) {
        this.showers = showers;
    }

    public String getLounge() {
        return lounge;
    }

    public void setLounge(String lounge) {
        this.lounge = lounge;
    }

    public String getLaundry() {
        return laundry;
    }

    public void setLaundry(String laundry) {
        this.laundry = laundry;
    }

    public String getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(String propertyStatus) {
        this.propertyStatus = propertyStatus;
    }
}

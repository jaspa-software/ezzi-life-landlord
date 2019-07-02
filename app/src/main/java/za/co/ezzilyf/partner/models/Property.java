package za.co.ezzilyf.partner.models;

import java.io.Serializable;

public class Property implements Serializable {

    private String propertyId;

    private String propertyType;

    private String propertyLocation;

    private String propertyDescription;

    private String propertyListerUid;

    private String listingStatus;

    private String isKitchenAvailable;

    private String isWifiAvailable;

    private String isLoungeAvailable;

    private String isStudyAreaAvailable;

    private int numberOfBedrooms;

    private int numberOfBathrooms;

    private int numberOfToilets;

    private String photoOneUrl;

    private String photoTwoUrl;

    private String photoThreeUrl;

    public Property() {

    }


    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyLocation() {
        return propertyLocation;
    }

    public void setPropertyLocation(String propertyLocation) {
        this.propertyLocation = propertyLocation;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public String getPropertyListerUid() {
        return propertyListerUid;
    }

    public void setPropertyListerUid(String propertyListerUid) {
        this.propertyListerUid = propertyListerUid;
    }

    public String getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(String listingStatus) {
        this.listingStatus = listingStatus;
    }

    public String getIsKitchenAvailable() {
        return isKitchenAvailable;
    }

    public void setIsKitchenAvailable(String isKitchenAvailable) {
        this.isKitchenAvailable = isKitchenAvailable;
    }

    public String getIsWifiAvailable() {
        return isWifiAvailable;
    }

    public void setIsWifiAvailable(String isWifiAvailable) {
        this.isWifiAvailable = isWifiAvailable;
    }

    public String getIsLoungeAvailable() {
        return isLoungeAvailable;
    }

    public void setIsLoungeAvailable(String isLoungeAvailable) {
        this.isLoungeAvailable = isLoungeAvailable;
    }

    public String getIsStudyAreaAvailable() {
        return isStudyAreaAvailable;
    }

    public void setIsStudyAreaAvailable(String isStudyAreaAvailable) {
        this.isStudyAreaAvailable = isStudyAreaAvailable;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public int getNumberOfToilets() {
        return numberOfToilets;
    }

    public void setNumberOfToilets(int numberOfToilets) {
        this.numberOfToilets = numberOfToilets;
    }

    public String getPhotoOneUrl() {
        return photoOneUrl;
    }

    public void setPhotoOneUrl(String photoOneUrl) {
        this.photoOneUrl = photoOneUrl;
    }

    public String getPhotoTwoUrl() {
        return photoTwoUrl;
    }

    public void setPhotoTwoUrl(String photoTwoUrl) {
        this.photoTwoUrl = photoTwoUrl;
    }

    public String getPhotoThreeUrl() {
        return photoThreeUrl;
    }

    public void setPhotoThreeUrl(String photoThreeUrl) {
        this.photoThreeUrl = photoThreeUrl;
    }
}

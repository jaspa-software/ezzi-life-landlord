package za.co.ezzilyf.partner.models;

import java.io.Serializable;

public class Room implements Serializable {

    private String roomId;

    private String roomType;

    private int occupants;

    private String roomDescription;

    private String propertyId;

    private String propertyOwnerUid;

    private double monthlyRental;

    private String roomStatus;

    private String typeOfOccupants;

    private String roomImageOne;

    private String roomImageTwo;

    private String roomImageThree;

    private String roomLocation;

    private String roomName;

    private String propertyName;

    public Room() {

    }


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getOccupants() {
        return occupants;
    }

    public void setOccupants(int occupants) {
        this.occupants = occupants;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public double getMonthlyRental() {
        return monthlyRental;
    }

    public void setMonthlyRental(double monthlyRental) {
        this.monthlyRental = monthlyRental;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getTypeOfOccupants() {
        return typeOfOccupants;
    }

    public void setTypeOfOccupants(String typeOfOccupants) {
        this.typeOfOccupants = typeOfOccupants;
    }

    public String getRoomImageOne() {
        return roomImageOne;
    }

    public void setRoomImageOne(String roomImageOne) {
        this.roomImageOne = roomImageOne;
    }

    public String getRoomImageTwo() {
        return roomImageTwo;
    }

    public void setRoomImageTwo(String roomImageTwo) {
        this.roomImageTwo = roomImageTwo;
    }

    public String getRoomImageThree() {
        return roomImageThree;
    }

    public void setRoomImageThree(String roomImageThree) {
        this.roomImageThree = roomImageThree;
    }

    public String getPropertyOwnerUid() {
        return propertyOwnerUid;
    }

    public void setPropertyOwnerUid(String propertyOwnerUid) {
        this.propertyOwnerUid = propertyOwnerUid;
    }

    public String getRoomLocation() {
        return roomLocation;
    }

    public void setRoomLocation(String roomLocation) {
        this.roomLocation = roomLocation;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}

package za.co.ezzilyf.partner.models;

import java.io.Serializable;

public class Room implements Serializable {

    private String propertyId;

    private String roomNumber;

    private String roomId;

    private int totalTenants;

    private int currentTenants;

    private double rental;

    private String roomType;

    private String tenantType;


    public Room() {

    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getTotalTenants() {
        return totalTenants;
    }

    public void setTotalTenants(int totalTenants) {
        this.totalTenants = totalTenants;
    }

    public int getCurrentTenants() {
        return currentTenants;
    }

    public void setCurrentTenants(int currentTenants) {
        this.currentTenants = currentTenants;
    }

    public double getRental() {
        return rental;
    }

    public void setRental(double rental) {
        this.rental = rental;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getTenantType() {
        return tenantType;
    }

    public void setTenantType(String tenantType) {
        this.tenantType = tenantType;
    }
}

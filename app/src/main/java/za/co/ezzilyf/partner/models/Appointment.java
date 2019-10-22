package za.co.ezzilyf.partner.models;

import java.io.Serializable;

public class Appointment implements Serializable {

    private String date;

    private String inspectorName;

    private String landlordName;

    private String landlordId;

    private String message;

    private String propertyRef;

    private String time;

    private String status;

    public Appointment() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public String getLandlordName() {
        return landlordName;
    }

    public void setLandlordName(String landlordName) {
        this.landlordName = landlordName;
    }

    public String getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(String landlordId) {
        this.landlordId = landlordId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPropertyRef() {
        return propertyRef;
    }

    public void setPropertyRef(String propertyRef) {
        this.propertyRef = propertyRef;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

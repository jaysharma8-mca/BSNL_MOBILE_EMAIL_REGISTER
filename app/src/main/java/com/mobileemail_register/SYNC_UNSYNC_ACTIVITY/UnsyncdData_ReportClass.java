package com.mobileemail_register.SYNC_UNSYNC_ACTIVITY;

public class UnsyncdData_ReportClass {

    String phoneNo;
    String emailId;
    String mobileNo;
    String goGreenStatus;
    String updatedBy;
    String emailIdValid;
    String mobileNoValid;
    String fileUploaded;
    String latitude;
    String longitude;
    String docType;
    String updateType;
    byte[] image;
    private int Sync_status;
    String  dateTime;

    public UnsyncdData_ReportClass(String phoneNo, String emailId, String mobileNo, String goGreenStatus, String updatedBy, String emailIdValid, String mobileNoValid, String fileUploaded, String latitude, String longitude, String docType, String updateType, byte[] image, int sync_status, String dateTime) {
        this.phoneNo = phoneNo;
        this.emailId = emailId;
        this.mobileNo = mobileNo;
        this.goGreenStatus = goGreenStatus;
        this.updatedBy = updatedBy;
        this.emailIdValid = emailIdValid;
        this.mobileNoValid = mobileNoValid;
        this.fileUploaded = fileUploaded;
        this.latitude = latitude;
        this.longitude = longitude;
        this.docType = docType;
        this.updateType = updateType;
        this.image = image;
        Sync_status = sync_status;
        this.dateTime = dateTime;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getGoGreenStatus() {
        return goGreenStatus;
    }

    public void setGoGreenStatus(String goGreenStatus) {
        this.goGreenStatus = goGreenStatus;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getEmailIdValid() {
        return emailIdValid;
    }

    public void setEmailIdValid(String emailIdValid) {
        this.emailIdValid = emailIdValid;
    }

    public String getMobileNoValid() {
        return mobileNoValid;
    }

    public void setMobileNoValid(String mobileNoValid) {
        this.mobileNoValid = mobileNoValid;
    }

    public String getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(String fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getSync_status() {
        return Sync_status;
    }

    public void setSync_status(int sync_status) {
        Sync_status = sync_status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}

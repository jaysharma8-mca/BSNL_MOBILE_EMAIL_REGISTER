package com.mobileemail_register.LINEMAN_WL_ACTIVITY;

class LinemanWorklist_ReportClass {

    private String sdeCode;
    private String address;
    private String lmCode;
    private String mobile;

    private String emailId;
    private String phoneNo;
    private String customerName;
    private String stdCode;

    private String jtoCode;
    private String osAmount;
    private String areaCode;
    private String serviceOperStatus;

    private String customerCategory;
    private String exchangeCode;
    private String goGreenEmail;
    private String randomString;

    public LinemanWorklist_ReportClass(String sdeCode, String address, String lmCode, String mobile, String emailId, String phoneNo, String customerName, String stdCode, String jtoCode, String osAmount, String areaCode, String serviceOperStatus, String customerCategory, String exchangeCode, String goGreenEmail, String randomString) {
        this.sdeCode = sdeCode;
        this.address = address;
        this.lmCode = lmCode;
        this.mobile = mobile;
        this.emailId = emailId;
        this.phoneNo = phoneNo;
        this.customerName = customerName;
        this.stdCode = stdCode;
        this.jtoCode = jtoCode;
        this.osAmount = osAmount;
        this.areaCode = areaCode;
        this.serviceOperStatus = serviceOperStatus;
        this.customerCategory = customerCategory;
        this.exchangeCode = exchangeCode;
        this.goGreenEmail = goGreenEmail;
        this.randomString = randomString;
    }

    public LinemanWorklist_ReportClass(String lmCode) {

    }


    public String getSdeCode() {
        return sdeCode;
    }

    public void setSdeCode(String sdeCode) {
        this.sdeCode = sdeCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLmCode() {
        return lmCode;
    }

    public void setLmCode(String lmCode) {
        this.lmCode = lmCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStdCode() {
        return stdCode;
    }

    public void setStdCode(String stdCode) {
        this.stdCode = stdCode;
    }

    public String getJtoCode() {
        return jtoCode;
    }

    public void setJtoCode(String jtoCode) {
        this.jtoCode = jtoCode;
    }

    public String getOsAmount() {
        return osAmount;
    }

    public void setOsAmount(String osAmount) {
        this.osAmount = osAmount;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getServiceOperStatus() {
        return serviceOperStatus;
    }

    public void setServiceOperStatus(String serviceOperStatus) {
        this.serviceOperStatus = serviceOperStatus;
    }

    public String getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public String getGoGreenEmail() {
        return goGreenEmail;
    }

    public void setGoGreenEmail(String goGreenEmail) {
        this.goGreenEmail = goGreenEmail;
    }

    public String getRandomString() {
        return randomString;
    }

    public void setRandomString(String randomString) {
        this.randomString = randomString;
    }
}
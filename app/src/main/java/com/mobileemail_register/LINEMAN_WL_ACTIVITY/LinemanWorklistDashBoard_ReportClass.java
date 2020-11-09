package com.mobileemail_register.LINEMAN_WL_ACTIVITY;

public class LinemanWorklistDashBoard_ReportClass {

    String ASSIGNED_LM;
    String ASSIGNED_LM_COUNT;
    String AREA_CODE;
    String EXCHANGE_CODE;
    String CUSTOMER_CATEGORY;

    public LinemanWorklistDashBoard_ReportClass(String ASSIGNED_LM, String ASSIGNED_LM_COUNT, String AREA_CODE, String EXCHANGE_CODE, String CUSTOMER_CATEGORY) {
        this.ASSIGNED_LM = ASSIGNED_LM;
        this.ASSIGNED_LM_COUNT = ASSIGNED_LM_COUNT;
        this.AREA_CODE = AREA_CODE;
        this.EXCHANGE_CODE = EXCHANGE_CODE;
        this.CUSTOMER_CATEGORY = CUSTOMER_CATEGORY;
    }

    public String getASSIGNED_LM() {
        return ASSIGNED_LM;
    }

    public void setASSIGNED_LM(String ASSIGNED_LM) {
        this.ASSIGNED_LM = ASSIGNED_LM;
    }

    public String getASSIGNED_LM_COUNT() {
        return ASSIGNED_LM_COUNT;
    }

    public void setASSIGNED_LM_COUNT(String ASSIGNED_LM_COUNT) {
        this.ASSIGNED_LM_COUNT = ASSIGNED_LM_COUNT;
    }

    public String getAREA_CODE() {
        return AREA_CODE;
    }

    public void setAREA_CODE(String AREA_CODE) {
        this.AREA_CODE = AREA_CODE;
    }

    public String getEXCHANGE_CODE() {
        return EXCHANGE_CODE;
    }

    public void setEXCHANGE_CODE(String EXCHANGE_CODE) {
        this.EXCHANGE_CODE = EXCHANGE_CODE;
    }

    public String getCUSTOMER_CATEGORY() {
        return CUSTOMER_CATEGORY;
    }

    public void setCUSTOMER_CATEGORY(String CUSTOMER_CATEGORY) {
        this.CUSTOMER_CATEGORY = CUSTOMER_CATEGORY;
    }
}

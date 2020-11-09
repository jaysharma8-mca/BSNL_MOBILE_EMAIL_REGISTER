package com.mobileemail_register.SDEFIELDLIST_ASSIGNED_ACTIVITY;

public class SdeConfiguredLm_ReportClass {

    String PRIMARY_LM;
    //String ASSIGNED_DATE;
    String PER_NO;
    String ASSIGNED_LM;


    public SdeConfiguredLm_ReportClass(String PRIMARY_LM, String ASSIGNED_LM, String PER_NO) {
        this.PRIMARY_LM = PRIMARY_LM;
        //this.ASSIGNED_DATE = ASSIGNED_DATE;
        this.PER_NO = PER_NO;
        this.ASSIGNED_LM = ASSIGNED_LM;
    }

    public String getPRIMARY_LM() {
        return PRIMARY_LM;
    }

    public void setPRIMARY_LM(String PRIMARY_LM) {
        this.PRIMARY_LM = PRIMARY_LM;
    }

   /* public String getASSIGNED_DATE() {
        return ASSIGNED_DATE;
    }

    public void setASSIGNED_DATE(String ASSIGNED_DATE) {
        this.ASSIGNED_DATE = ASSIGNED_DATE;
    }*/

    public String getPER_NO() {
        return PER_NO;
    }

    public void setPER_NO(String PER_NO) {
        this.PER_NO = PER_NO;
    }

    public String getASSIGNED_LM() {
        return ASSIGNED_LM;
    }

    public void setASSIGNED_LM(String ASSIGNED_LM) {
        this.ASSIGNED_LM = ASSIGNED_LM;
    }
}

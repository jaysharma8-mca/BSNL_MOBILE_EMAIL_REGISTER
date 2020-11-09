package com.mobileemail_register.SDEFIELDLIST_ACTIVITY;

public class SdeFieldListConfigureLm_ReportClass {

    String USER_PERNO;
    String LM_CODE;
    String LM_PERNO;
    String TTL_CON;
    String TGT;
    String LMC_ASSIGNED;
    String LMC_ASSIGNED_TO_TEXT;
    String ASSIGNED_FLAG;
    String DEASSIGNED_FLAG;
    int FLAG;

    public SdeFieldListConfigureLm_ReportClass(String USER_PERNO, String LM_CODE, String LM_PERNO, String TTL_CON, String TGT, String LMC_ASSIGNED, String LMC_ASSIGNED_TO_TEXT, String ASSIGNED_FLAG, String DEASSIGNED_FLAG, int FLAG) {
        this.USER_PERNO = USER_PERNO;
        this.LM_CODE = LM_CODE;
        this.LM_PERNO = LM_PERNO;
        this.TTL_CON = TTL_CON;
        this.TGT = TGT;
        this.LMC_ASSIGNED = LMC_ASSIGNED;
        this.LMC_ASSIGNED_TO_TEXT = LMC_ASSIGNED_TO_TEXT;
        this.ASSIGNED_FLAG = ASSIGNED_FLAG;
        this.DEASSIGNED_FLAG = DEASSIGNED_FLAG;
        this.FLAG = FLAG;
    }

    public String getUSER_PERNO() {
        return USER_PERNO;
    }

    public void setUSER_PERNO(String USER_PERNO) {
        this.USER_PERNO = USER_PERNO;
    }

    public String getLM_CODE() {
        return LM_CODE;
    }

    public void setLM_CODE(String LM_CODE) {
        this.LM_CODE = LM_CODE;
    }

    public String getLM_PERNO() {
        return LM_PERNO;
    }

    public void setLM_PERNO(String LM_PERNO) {
        this.LM_PERNO = LM_PERNO;
    }

    public String getTTL_CON() {
        return TTL_CON;
    }

    public void setTTL_CON(String TTL_CON) {
        this.TTL_CON = TTL_CON;
    }

    public String getTGT() {
        return TGT;
    }

    public void setTGT(String TGT) {
        this.TGT = TGT;
    }

    public String getLMC_ASSIGNED() {
        return LMC_ASSIGNED;
    }

    public void setLMC_ASSIGNED(String LMC_ASSIGNED) {
        this.LMC_ASSIGNED = LMC_ASSIGNED;
    }

    public String getLMC_ASSIGNED_TO_TEXT() {
        return LMC_ASSIGNED_TO_TEXT;
    }

    public void setLMC_ASSIGNED_TO_TEXT(String LMC_ASSIGNED_TO_TEXT) {
        this.LMC_ASSIGNED_TO_TEXT = LMC_ASSIGNED_TO_TEXT;
    }

    public String getASSIGNED_FLAG() {
        return ASSIGNED_FLAG;
    }

    public void setASSIGNED_FLAG(String ASSIGNED_FLAG) {
        this.ASSIGNED_FLAG = ASSIGNED_FLAG;
    }

    public String getDEASSIGNED_FLAG() {
        return DEASSIGNED_FLAG;
    }

    public void setDEASSIGNED_FLAG(String DEASSIGNED_FLAG) {
        this.DEASSIGNED_FLAG = DEASSIGNED_FLAG;
    }

    public int getFLAG() {
        return FLAG;
    }

    public void setFLAG(int FLAG) {
        this.FLAG = FLAG;
    }
}

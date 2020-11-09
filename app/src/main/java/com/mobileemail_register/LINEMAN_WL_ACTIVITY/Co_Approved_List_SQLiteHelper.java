package com.mobileemail_register.LINEMAN_WL_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Co_Approved_List_SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = Co_Approved_List_SQLiteHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CO_APPROVED_LIST = "TABLE_CO_APPROVED_LIST";

    public static final String SSA = "SSA";
    public static final String CUST_ACCNT_NO = "CUST_ACCNT_NO";
    public static final String GO_GREEN = "GO_GREEN";
    public static final String STATUS_FLAG = "STATUS_FLAG";

    public static final String PHONE_NO = "PHONE_NO";
    public static final String BILL_ACCNT_NO = "BILL_ACCNT_NO";
    public static final String UPDATED_ON = "UPDATED_ON";
    public static final String CIRCLE = "CIRCLE";

    public static final String CO_APPROVED_DATE = "CO_APPROVED_DATE";
    public static final String MOBILE_NO = "MOBILE_NO";
    public static final String UPDATED_BY = "UPDATED_BY";
    public static final String UPDATE_TYPE = "UPDATE_TYPE";

    public static final String EXCHANGE_CODE = "EXCHANGE_CODE";
    public static final String EMAIL_ID = "EMAIL_ID";
    public static final String SUBMIT_STAT = "SUBMIT_STAT";
    public static final String APPROVED_BY = "APPROVED_BY";

    public static final String CO_REMARKS = "CO_REMARKS";


    public Co_Approved_List_SQLiteHelper(Context context) {
        super(context, TABLE_CO_APPROVED_LIST, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_CO_APPROVED_LIST + "("
                + SSA + " TEXT,"
                + CUST_ACCNT_NO + " TEXT,"
                + GO_GREEN + " TEXT,"
                + STATUS_FLAG + " TEXT,"
                + PHONE_NO + " TEXT,"
                + BILL_ACCNT_NO + " TEXT,"
                + UPDATED_ON + " TEXT,"
                + CIRCLE + " TEXT,"
                + CO_APPROVED_DATE + " TEXT,"
                + MOBILE_NO + " TEXT,"
                + UPDATED_BY + " TEXT,"
                + UPDATE_TYPE + " TEXT,"
                + EXCHANGE_CODE + " TEXT,"
                + EMAIL_ID + " TEXT,"
                + SUBMIT_STAT + " TEXT,"
                + APPROVED_BY + " TEXT,"
                + CO_REMARKS + " TEXT )";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database table created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CO_APPROVED_LIST);

        // Create tables again
        onCreate(db);
    }

    void open() throws SQLException {

        this.getWritableDatabase();
    }


    void addUser(String ssa, String cust_accnt_no, String go_green, String status_falg,
                 String phone_no, String bill_accnt_no, String updated_on, String circle,
                 String co_approved_date, String mobile_no, String updated_by, String update_type,
                 String exchange_code, String email_id, String submit_stat, String approved_by,
                 String co_remarks) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SSA, ssa);
        values.put(CUST_ACCNT_NO, cust_accnt_no);
        values.put(GO_GREEN, go_green);
        values.put(STATUS_FLAG, status_falg);

        values.put(PHONE_NO, phone_no);
        values.put(BILL_ACCNT_NO, bill_accnt_no);
        values.put(UPDATED_ON, updated_on);
        values.put(CIRCLE, circle);

        values.put(CO_APPROVED_DATE, co_approved_date);
        values.put(MOBILE_NO, mobile_no);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATE_TYPE, update_type);

        values.put(EXCHANGE_CODE, exchange_code);
        values.put(EMAIL_ID, email_id);
        values.put(SUBMIT_STAT, submit_stat);
        values.put(APPROVED_BY, approved_by);

        values.put(CO_REMARKS, co_remarks);

        long id = db.insert(TABLE_CO_APPROVED_LIST, null, values);
        db.close();

        Log.d(TAG, "New data inserted into sqlite: " + id);
    }


    String[] SelectAllTableData() {
        // TODO Auto-generated method stub

        try {
            String[] arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            /*String strSQL = "SELECT  DISTINCT  areaCode FROM " + TABLE_LM_WORKLIST;*/
            //String strSQL = ("SELECT * FROM " + TABLE_NAME + " where " + PEOPLE_RATION_NUMBER + "='" + rationNumber + "'", null);
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CO_APPROVED_LIST, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());

                }
            }
            assert cursor != null;
            cursor.close();

            return arrData;

        } catch (Exception e) {
            return null;
        }

    }


    void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CO_APPROVED_LIST, null, null);
        db.close();

        Log.d(TAG, "Deleted all data info from sqlite");
    }


    @SuppressLint("Recycle")
    Cursor readFromLocalDatabase(SQLiteDatabase database) {

        return database.rawQuery("SELECT * FROM  TABLE_CO_APPROVED_LIST", null);
    }


}
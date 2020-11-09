package com.mobileemail_register.LINEMAN_WL_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Linemanworklist_SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = Linemanworklist_SQLiteHelper.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_LM_WORKLIST = "TABLE_LM_WORKLIST";

    private Context context;

    public static final String sdeCode = "sdeCode";
    public static final String address = "address";
    public static final String lmCode = "lmCode";

    public static final String mobile = "mobile";
    public static final String emailId = "emailId";
    public static final String phoneNo = "phoneNo";
    public static final String customerName = "customerName";

    public static final String stdCode = "stdCode";
    public static final String jtoCode = "jtoCode";
    public static final String osAmount = "osAmount";
    public static final String areaCode = "areaCode";

    public static final String serviceOperStatus = "serviceOperStatus";
    public static final String goGreenEmail = "goGreenEmail";
    public static final String customerCategory = "customerCategory";
    public static final String exchangeCode = "exchangeCode";

    public static final String RANDOM_STRING = "RANDOM_STRING";

    private static final String SP_MEREG = "SP_MEREG";


    public Linemanworklist_SQLiteHelper(Context context) {
        super(context, TABLE_LM_WORKLIST, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LM_WORKLIST + "("
                + sdeCode + " TEXT,"
                + address + " TEXT,"
                + lmCode + " TEXT,"
                + mobile + " TEXT,"
                + emailId + " TEXT,"
                + phoneNo + " TEXT,"
                + customerName + " TEXT,"
                + stdCode + " TEXT,"
                + jtoCode + " TEXT,"
                + osAmount + " TEXT,"
                + areaCode + " TEXT,"
                + serviceOperStatus + " TEXT,"
                + customerCategory + " TEXT,"
                + exchangeCode + " TEXT,"
                + goGreenEmail + " TEXT,"
                + RANDOM_STRING + " TEXT )";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database table created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LM_WORKLIST);

        // Create tables again
        onCreate(db);
    }




    void addUser(String SDECODE, String ADDRESS, String LMCODE, String MOBILE,
                 String EMAILID, String PHONENO, String CUSTOMERNAME, String STDCODE,
                 String JTOCODE, String OSAMOUNT,String AREACODE, String SERVICEOPERSTATUS, String CUSTOMERCATEGORY,
                 String EXCHANGECODE, String GOGREENEMAIL,String random_string) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(sdeCode,SDECODE);
        values.put(address, ADDRESS);
        values.put(lmCode, LMCODE);
        values.put(mobile,MOBILE);

        values.put(emailId,EMAILID);
        values.put(phoneNo,PHONENO);
        values.put(customerName, CUSTOMERNAME);
        values.put(stdCode, STDCODE);

        values.put(jtoCode,JTOCODE);
        values.put(osAmount,OSAMOUNT);
        values.put(areaCode,AREACODE);
        values.put(serviceOperStatus, SERVICEOPERSTATUS);

        values.put(customerCategory, CUSTOMERCATEGORY);
        values.put(exchangeCode, EXCHANGECODE);
        values.put(goGreenEmail,GOGREENEMAIL);

        values.put(RANDOM_STRING,random_string);

        //values.put(KEY_MOBILE, mobile);

        long id = db.insert(TABLE_LM_WORKLIST, null, values);
        db.close();

        Log.d(TAG, "New data inserted into sqlite: " + id);
    }


    void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LM_WORKLIST, null, null);
        db.close();

        Log.d(TAG, "Deleted all data info from sqlite");
    }

    @SuppressLint("Recycle")
    Cursor readFromLocalDatabase(SQLiteDatabase database)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String ASSIGNED_LM_CODE = "ASSIGNED_LM_CODE";
        final String assigned_lm_code = sharedPreferences.getString(ASSIGNED_LM_CODE, null);

        //Toast.makeText(context,assigned_lm_code,Toast.LENGTH_LONG).show();

        return database.rawQuery("SELECT * FROM  TABLE_LM_WORKLIST where lmCode = '" + assigned_lm_code + "' ORDER BY cast(osAmount as REAL) DESC" , null);
    }


   @SuppressLint("Recycle")
    Cursor readFromLocalDatabasePillarwiseData(SQLiteDatabase database)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String PILLAR_CODE = "PILLAR_CODE";
        final String ASSIGNED_LM_CODE = "ASSIGNED_LM_CODE";
        final String pillar_code = sharedPreferences.getString(PILLAR_CODE, null);
        final String assigned_lm_code = sharedPreferences.getString(ASSIGNED_LM_CODE, null);

        //Toast.makeText(context,pillar_code,Toast.LENGTH_LONG).show();

        return database.rawQuery("SELECT * FROM  TABLE_LM_WORKLIST where lmCode = '" + assigned_lm_code + "' and areaCode = '" + pillar_code + "' ORDER BY cast(osAmount as REAL) DESC" , null);
    }


    @SuppressLint("Recycle")
    Cursor readFromLocalDatabasePhoneStatus(SQLiteDatabase database)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String PHONE_STATUS = "PHONE_STATUS";
        final String ASSIGNED_LM_CODE = "ASSIGNED_LM_CODE";
        String phone_status = sharedPreferences.getString(PHONE_STATUS, null);
        final String assigned_lm_code = sharedPreferences.getString(ASSIGNED_LM_CODE, null);


        //Toast.makeText(context,phone_status,Toast.LENGTH_LONG).show();

        return database.rawQuery("SELECT * FROM  TABLE_LM_WORKLIST where lmCode = '" + assigned_lm_code + "' and serviceOperStatus = '" + phone_status + "' ORDER BY cast(osAmount as REAL) DESC" , null);
    }


    void open() throws SQLException {

        this.getWritableDatabase();
    }

    String[] SelectAllTableData() {
        // TODO Auto-generated method stub

        try {
            String[] arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            /*String strSQL = "SELECT  DISTINCT  areaCode FROM " + TABLE_LM_WORKLIST;*/
            //String strSQL = ("SELECT * FROM " + TABLE_NAME + " where " + PEOPLE_RATION_NUMBER + "='" + rationNumber + "'", null);
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LM_WORKLIST , null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i= 0;
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

    String[] SelectAllData() {
        // TODO Auto-generated method stub

        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String ASSIGNED_LM_CODE = "ASSIGNED_LM_CODE";
        final String assigned_lm_code = sharedPreferences.getString(ASSIGNED_LM_CODE, null);

        try {
            String[] arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            /*String strSQL = "SELECT  DISTINCT  areaCode FROM " + TABLE_LM_WORKLIST;*/
            //String strSQL = ("SELECT * FROM " + TABLE_NAME + " where " + PEOPLE_RATION_NUMBER + "='" + rationNumber + "'", null);
            Cursor cursor = db.rawQuery("SELECT DISTINCT  areaCode FROM " + TABLE_LM_WORKLIST + " where " + lmCode + "='" + assigned_lm_code + "'", null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i= 0;
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

    String[] SelectAllDataPhoneStatus() {
        // TODO Auto-generated method stub
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String ASSIGNED_LM_CODE = "ASSIGNED_LM_CODE";
        final String assigned_lm_code = sharedPreferences.getString(ASSIGNED_LM_CODE, null);

        try {
            String[] arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            //String strSQL = "SELECT  DISTINCT  serviceOperStatus FROM " + TABLE_LM_WORKLIST ;
            Cursor cursor = db.rawQuery("SELECT DISTINCT  serviceOperStatus FROM " + TABLE_LM_WORKLIST + " where " + lmCode + "='" + assigned_lm_code + "'", null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];

                    int i= 0;
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


    long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_LM_WORKLIST);
        db.close();
        return count;
    }


    public void deleteRowLMWorkList(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LM_WORKLIST, RANDOM_STRING + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }


    @SuppressLint("Recycle")
    Cursor readFromLocalDatabaseLMWL(SQLiteDatabase database)
    {
        return database.rawQuery("SELECT lmCode,count(phoneNo) phoneNo, areaCode, exchangeCode, customerCategory FROM TABLE_LM_WORKLIST GROUP BY lmCode",null);

    }

}

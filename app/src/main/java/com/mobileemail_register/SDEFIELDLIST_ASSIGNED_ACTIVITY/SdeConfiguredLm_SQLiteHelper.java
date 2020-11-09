package com.mobileemail_register.SDEFIELDLIST_ASSIGNED_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SdeConfiguredLm_SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = SdeConfiguredLm_SQLiteHelper.class.getSimpleName();

    private Context context;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_SDE_CONFIGURED_LM_LIST = "TABLE_SDE_CONFIGURED_LM_LIST";

    public static final String PRIMARY_LM = "PRIMARY_LM";
    public static final String ASSIGNED_DATE = "ASSIGNED_DATE";
    public static final String PER_NO = "PER_NO";
    public static final String ASSIGNED_LM = "ASSIGNED_LM";
    public static final String DEASSIGNMENT_FLAG = "DEASSIGNMENT_FLAG";

    private static final String SP_MEREG = "SP_MEREG";

    public SdeConfiguredLm_SQLiteHelper(Context context) {
        super(context, TABLE_SDE_CONFIGURED_LM_LIST, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_SDE_CONFIGURED_LM_LIST + "("
                + PRIMARY_LM + " TEXT,"
                + ASSIGNED_DATE + " TEXT,"
                + PER_NO + " TEXT,"
                + ASSIGNED_LM + " TEXT,"
                + DEASSIGNMENT_FLAG + " TEXT)";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database table created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_SDE_CONFIGURED_LM_LIST + "'");

        // Create tables again
        onCreate(db);

        Log.d(TAG, "Table Dropped");
    }


    public void addUser(String primary_lm_code, String assigned_date, String per_no, String assigned_lm, String deassignment_flag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRIMARY_LM, primary_lm_code);
        values.put(ASSIGNED_DATE, assigned_date);
        values.put(PER_NO, per_no);
        values.put(ASSIGNED_LM, assigned_lm);
        values.put(DEASSIGNMENT_FLAG, deassignment_flag);



        //values.put(KEY_MOBILE, mobile);

        long id = db.insert(TABLE_SDE_CONFIGURED_LM_LIST, null, values);
        db.close();

        Log.d(TAG, "New data inserted into sqlite: " + id);
    }


    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SDE_CONFIGURED_LM_LIST);
        db.execSQL("VACUUM");
        db.close();

        Log.d(TAG, "Deleted all data info from sqlite");
    }


    public void open() throws SQLException {

        this.getWritableDatabase();
    }


    String[] SelectAllData() {
        // TODO Auto-generated method stub

        String assignment = "ASSIGNED";

        try {
            String[] arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data


            //String strSQL = "SELECT LM_CODE FROM " + TABLE_SDE_FIELD_LIST + " WHERE " + LM_CONFIG + "!='" + assignment + "'";
            String strSQL = "SELECT LM_CODE FROM " + TABLE_SDE_CONFIGURED_LM_LIST + " WHERE " + ASSIGNED_LM + " NOT LIKE '%" + assignment + "%'";

            Cursor cursor = db.rawQuery(strSQL, null);

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


    public String[] SelectAllTableData() {
        // TODO Auto-generated method stub

        try {
            String[] arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            /*String strSQL = "SELECT  DISTINCT  areaCode FROM " + TABLE_LM_WORKLIST;*/
            //String strSQL = ("SELECT * FROM " + TABLE_NAME + " where " + PEOPLE_RATION_NUMBER + "='" + rationNumber + "'", null);
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SDE_CONFIGURED_LM_LIST , null);

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



    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_SDE_CONFIGURED_LM_LIST);
        db.close();
        return count;
    }

    public void deleteRowLMWorkList(String lm_perno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SDE_CONFIGURED_LM_LIST, ASSIGNED_LM + " = ?",
                new String[]{String.valueOf(lm_perno)});
        db.close();
    }


    @SuppressLint("Recycle")
    Cursor readFromLocalDatabase(SQLiteDatabase database)
    {
        return database.rawQuery("SELECT PRIMARY_LM,PER_NO,count(ASSIGNED_LM) ASSIGNED_LM FROM TABLE_SDE_CONFIGURED_LM_LIST GROUP BY PRIMARY_LM",null);

    }




    @SuppressLint("Recycle")
    public Cursor readFromLocalDatabaseSdeLM(SQLiteDatabase database) {

        //return (database.query(TABLE_SDE_FIELD_LIST, tableColumns, whereClause, whereArgs, null, null, null));
        String[] projection = {PRIMARY_LM, ASSIGNED_DATE, PER_NO, ASSIGNED_LM,DEASSIGNMENT_FLAG};
        return (database.query(TABLE_SDE_CONFIGURED_LM_LIST, projection, null, null, null, null, null, null));

    }

    public void reassignData(String Assigned_Lm_Code,String deassigned_flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEASSIGNMENT_FLAG, deassigned_flag);
        db.update(TABLE_SDE_CONFIGURED_LM_LIST, contentValues, "ASSIGNED_LM = ?", new String[]{Assigned_Lm_Code});
    }

    /*public void removeDFlag(String Assigned_Lm_Code,String deassigned_flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEASSIGNMENT_FLAG, deassigned_flag);
        db.update(TABLE_SDE_CONFIGURED_LM_LIST, contentValues, "ASSIGNED_LM = ?", new String[]{Assigned_Lm_Code});
    }*/


    public void updateLocalDBRemoveDFlag(String flag,String  updatedFlag) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEASSIGNMENT_FLAG, updatedFlag);
        db.update(TABLE_SDE_CONFIGURED_LM_LIST, contentValues, "DEASSIGNMENT_FLAG = ?", new String[]{flag});


    }

}
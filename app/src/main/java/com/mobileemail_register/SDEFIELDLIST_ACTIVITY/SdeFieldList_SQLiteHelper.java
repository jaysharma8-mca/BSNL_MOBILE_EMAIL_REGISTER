package com.mobileemail_register.SDEFIELDLIST_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SdeFieldList_SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = SdeFieldList_SQLiteHelper.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_SDE_FIELD_LIST = "TABLE_SDE_FIELD_LIST";

    public static final String USER_PERNO = "USER_PERNO";
    public static final String LM_CODE = "LM_CODE";
    public static final String LM_PERNO = "LM_PERNO";
    public static final String TTL_CON = "TTL_CON";
    public static final String TGT = "TGT";
    public static final String LMC_ASSIGNED_TO = "LMC_ASSIGNED_TO";
    public static final String LMC_ASSIGNED_TO_TEXT = "LMC_ASSIGNED_TO_TEXT";
    public static final String ASSIGNED_FLAG = "ASSIGNED_FLAG";
    public static final String DEASSIGNED_FLAG = "DEASSIGNED_FLAG";
    public static final String SYNC_STATUS_FLAG = "SYNC_STATUS_FLAG";


    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_SDE_FIELD_LIST + "("
            + USER_PERNO + " TEXT,"
            + LM_CODE + " TEXT,"
            + LM_PERNO + " TEXT,"
            + TTL_CON + " TEXT,"
            + TGT + " TEXT,"
            + LMC_ASSIGNED_TO + " TEXT,"
            + LMC_ASSIGNED_TO_TEXT + " TEXT,"
            + ASSIGNED_FLAG + " TEXT,"
            + DEASSIGNED_FLAG + " TEXT,"
            + SYNC_STATUS_FLAG + " TEXT)";


    private static final String DROP_TABLE = "drop table if exists "+ TABLE_SDE_FIELD_LIST;

    public SdeFieldList_SQLiteHelper(Context context) {
        super(context, TABLE_SDE_FIELD_LIST, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE);
        onCreate(db);
    }


    void addUser(String user_perno, String lm_code, String lm_perno, String ttl_con, String tgt,
                 String lmc_assigned, String lm_assigned_to_text,String assigned_flag,String deassigned_flag,
                 String sync_status_flag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_PERNO, user_perno);
        values.put(LM_CODE, lm_code);
        values.put(LM_PERNO, lm_perno);
        values.put(TTL_CON, ttl_con);
        values.put(TGT, tgt);
        values.put(LMC_ASSIGNED_TO, lmc_assigned);
        values.put(LMC_ASSIGNED_TO_TEXT, lm_assigned_to_text);
        values.put(ASSIGNED_FLAG, assigned_flag);
        values.put(DEASSIGNED_FLAG, deassigned_flag);
        values.put(SYNC_STATUS_FLAG, sync_status_flag);


        //values.put(KEY_MOBILE, mobile);

        long id = db.insert(TABLE_SDE_FIELD_LIST, null, values);
        db.close();

        Log.d(TAG, "New data inserted into sqlite: " + id);
    }


    void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SDE_FIELD_LIST);
        db.execSQL("VACUUM");
        db.close();

        Log.d(TAG, "Deleted all data info from sqlite");
    }


    void open() throws SQLException {

        this.getWritableDatabase();
    }


    public String[] SelectAllData() {
        // TODO Auto-generated method stub

        String assignment = "ASSIGNED";

        try {
            String[] arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data


            //String strSQL = "SELECT LM_CODE FROM " + TABLE_SDE_FIELD_LIST + " WHERE " + LM_CONFIG + "!='" + assignment + "'";
            String strSQL = "SELECT LM_CODE FROM " + TABLE_SDE_FIELD_LIST + " WHERE " + LMC_ASSIGNED_TO_TEXT + " NOT LIKE '%" + assignment + "%'";

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
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SDE_FIELD_LIST , null);

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
        long count = DatabaseUtils.queryNumEntries(db, TABLE_SDE_FIELD_LIST);
        db.close();
        return count;
    }

    public void deleteRowLMWorkList(String lm_perno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SDE_FIELD_LIST, LM_PERNO + " = ?",
                new String[]{String.valueOf(lm_perno)});
        db.close();
    }

    public void updateData(String selectedItemText, String lm_code, String lm_config,
                           String assigned_deassigned_flag,String sync_status_flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LMC_ASSIGNED_TO, lm_code);
        contentValues.put(LMC_ASSIGNED_TO_TEXT, lm_config);
        contentValues.put(ASSIGNED_FLAG, assigned_deassigned_flag);
        contentValues.put(SYNC_STATUS_FLAG, sync_status_flag);
        db.update(TABLE_SDE_FIELD_LIST, contentValues, "LM_CODE = ?", new String[]{selectedItemText});
    }

    public void updateAssignedData(String selectedItemText, String lm_code, String lm_config,
                                   String assigned_deassigned_flag,String sync_status_flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LMC_ASSIGNED_TO, lm_code);
        contentValues.put(LMC_ASSIGNED_TO_TEXT, lm_config);
        contentValues.put(ASSIGNED_FLAG, assigned_deassigned_flag);
        contentValues.put(SYNC_STATUS_FLAG, sync_status_flag);
        db.update(TABLE_SDE_FIELD_LIST, contentValues, "LMC_ASSIGNED_TO = ?", new String[]{selectedItemText});
    }

    public void reassignData(String lm_code, String lmc_assigned_to_text,
                             String deassigned_flag,String sync_status_flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LMC_ASSIGNED_TO_TEXT, lmc_assigned_to_text);
        contentValues.put(DEASSIGNED_FLAG, deassigned_flag);
        contentValues.put(SYNC_STATUS_FLAG, sync_status_flag);
        db.update(TABLE_SDE_FIELD_LIST, contentValues, "LM_CODE = ?", new String[]{lm_code});
    }


    public int getTaskCount(String selectedItemText) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM " + TABLE_SDE_FIELD_LIST + " WHERE " + LMC_ASSIGNED_TO + "=?",
                new String[]{String.valueOf(selectedItemText)});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public void updateLocalDatabase(String lm_code,String sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS_FLAG, sync_status);
        String selection = LM_CODE + " LIKE ?";
        String[] selection_args = {lm_code};
        db.update(TABLE_SDE_FIELD_LIST, contentValues, selection, selection_args);
    }

    public void updateLocalDatabaseDeassignment(String lm_code,String lmc_assigned_to,String lmc_assigned_to_text,String deassigned_flag,String sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LMC_ASSIGNED_TO, lmc_assigned_to);
        contentValues.put(LMC_ASSIGNED_TO_TEXT, lmc_assigned_to_text);
        contentValues.put(DEASSIGNED_FLAG, deassigned_flag);
        contentValues.put(SYNC_STATUS_FLAG, sync_status);
        String selection = LM_CODE + " LIKE ?";
        String[] selection_args = {lm_code};
        db.update(TABLE_SDE_FIELD_LIST, contentValues, selection, selection_args);
    }

    @SuppressLint("Recycle")
    public Cursor readFromLocalDatabase(SQLiteDatabase database) {
        String[] projection = {USER_PERNO, LM_CODE, LM_PERNO, TTL_CON, TGT, LMC_ASSIGNED_TO, LMC_ASSIGNED_TO_TEXT, ASSIGNED_FLAG,DEASSIGNED_FLAG,SYNC_STATUS_FLAG};

        return (database.query(TABLE_SDE_FIELD_LIST, projection, null, null, null, null, null, null));

        //return database.rawQuery("SELECT * FROM TABLE_SDE_FIELD_LIST ORDER BY cast(TTL_CON as REAL) DESC",null);
    }

    @SuppressLint("Recycle")
    public Cursor readFromLocalDatabaseSdeLM(SQLiteDatabase database) {

        String flag = "1";
        String whereClause = SYNC_STATUS_FLAG + " = ?";
        String[] whereArgs = new String[] {flag};

         //return (database.query(TABLE_SDE_FIELD_LIST, tableColumns, whereClause, whereArgs, null, null, null));
        String[] projection = {USER_PERNO, LM_CODE, LM_PERNO, TTL_CON, TGT, LMC_ASSIGNED_TO, LMC_ASSIGNED_TO_TEXT, ASSIGNED_FLAG,DEASSIGNED_FLAG,SYNC_STATUS_FLAG};
        return (database.query(TABLE_SDE_FIELD_LIST, projection, whereClause, whereArgs, null, null, null, null));

    }

    public void clearAssignedData(String ASSLMCODE,String lmc_assigned_to, String lmc_assigned_text, String assigned_flag,
                           String sync_status_flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LMC_ASSIGNED_TO, lmc_assigned_to);
        contentValues.put(LMC_ASSIGNED_TO_TEXT, lmc_assigned_text);
        contentValues.put(ASSIGNED_FLAG, assigned_flag);
        contentValues.put(SYNC_STATUS_FLAG, sync_status_flag);
        db.update(TABLE_SDE_FIELD_LIST, contentValues, "LM_CODE = ?", new String[]{ASSLMCODE});
    }

    public String[] SelectAllDataLMCODE() {
        // TODO Auto-generated method stub


        try {
            String[] arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data


            /*String strSQL = "SELECT  DISTINCT  areaCode FROM " + TABLE_LM_WORKLIST;*/
            //String strSQL = ("SELECT * FROM " + TABLE_NAME + " where " + PEOPLE_RATION_NUMBER + "='" + rationNumber + "'", null);
            Cursor cursor = db.rawQuery("SELECT DISTINCT LM_PERNO FROM " + TABLE_SDE_FIELD_LIST , null);

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
}
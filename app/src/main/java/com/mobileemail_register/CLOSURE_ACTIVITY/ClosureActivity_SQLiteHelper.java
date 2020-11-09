package com.mobileemail_register.CLOSURE_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mobileemail_register.MISC.constants;


public class ClosureActivity_SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = ClosureActivity_SQLiteHelper.class.getSimpleName();

    public ClosureActivity_SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "create table "+ constants.TABLE_NAME+ "("
            + "id integer primary key autoincrement,"
            + constants.phoneNo+" integer,"
            + constants.emailId+" text,"
            + constants.mobileNo+" text,"
            + constants.goGreenStatus+" text,"
            + constants.updatedBy+" text,"
            + constants.emailIdValid+" text,"
            + constants.mobileNoValid+" text,"
            + constants.fileUploaded+" text,"
            + constants.latitude+" text,"
            + constants.longitude+" text,"
            + constants.docType+" text,"
            + constants.updateType+" text,"
            + constants.image+" blob,"
            + constants.sync_status+ " text,"
            + constants.dateTime+ " text) ;";

    private static final String DROP_TABLE = "drop table if exists "+ constants.TABLE_NAME;
    private SQLiteDatabase mDb;

    public ClosureActivity_SQLiteHelper(Context context)
    {
        super(context, constants.DATABASE_NAME,null,DATABASE_VERSION);
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

    public void saveToLocalDatabase(String phoneNo, String emailId,
                                    String mobileNo, String goGreenStatus,
                                    String updatedBy, String emailIdValid,
                                    String mobileNoValid, String fileUploaded,
                                    String latitude, String longitude, String docType,
                                    String updateType, byte[] image, String sync_status, String dateTime, SQLiteDatabase database)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(constants.phoneNo,phoneNo);
        contentValues.put(constants.emailId,emailId);
        contentValues.put(constants.mobileNo,mobileNo);
        contentValues.put(constants.goGreenStatus,goGreenStatus);

        contentValues.put(constants.updatedBy,updatedBy);
        contentValues.put(constants.emailIdValid,emailIdValid);
        contentValues.put(constants.mobileNoValid,mobileNoValid);
        contentValues.put(constants.fileUploaded,fileUploaded);

        contentValues.put(constants.latitude,latitude);
        contentValues.put(constants.longitude,longitude);
        contentValues.put(constants.docType,docType);
        contentValues.put(constants.updateType,updateType);

        contentValues.put(constants.image,image);
        contentValues.put(constants.sync_status,sync_status);
        contentValues.put(constants.dateTime,dateTime);

        database.insert(constants.TABLE_NAME,null,contentValues);
    }

    @SuppressLint("Recycle")
    public Cursor readFromLocalDatabase(SQLiteDatabase database)
    {
        String[] projection = {constants.phoneNo, constants.emailId, constants.mobileNo,
                                constants.goGreenStatus, constants.updatedBy, constants.emailIdValid,
                                constants.mobileNoValid, constants.fileUploaded, constants.latitude,
                                constants.longitude, constants.docType, constants.updateType,
                                constants.image, constants.sync_status, constants.dateTime};

        return (database.query(constants.TABLE_NAME,projection,null,null,null,null,null,null));
    }

    public void updateLocalDatabase(String phoneNo, String emailId,
                                    String mobileNo, String goGreenStatus,
                                    String updatedBy, String emailIdValid,
                                    String mobileNoValid, String fileUploaded,
                                    String latitude, String longitude, String docType,
                                    String updateType, byte[] image, String sync_status, String dateTime, SQLiteDatabase database)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(constants.sync_status,sync_status);
        String selection = constants.phoneNo+" LIKE ?";
        String[] selection_args = {phoneNo};
        database.update(constants.TABLE_NAME,contentValues,selection,selection_args);
    }

    String[] SelectAllData() {
        // TODO Auto-generated method stub

        try {
            String[] arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            //String strSQL = "SELECT  FROM " + Constants.TABLE_NAME;
            String strSQL = "SELECT  DISTINCT phoneNo FROM " + constants.TABLE_NAME;
            //String strSQL = "SELECT DISTINCT TASK_NAME FROM " + Constants.TABLE_NAME;
            Cursor cursor = db.rawQuery(strSQL, null);

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
            if (cursor != null) {
                cursor.close();
            }

            return arrData;

        } catch (Exception e) {
            return null;
        }

    }



    String[] SelectAllDataTaskName() {
        // TODO Auto-generated method stub

        try {
            String[] arrData = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            //String strSQL = "SELECT  FROM " + Constants.TABLE_NAME;
            String strSQL = "SELECT DISTINCT phoneNo FROM " + constants.TABLE_NAME;
            Cursor cursor = db.rawQuery(strSQL, null);

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
            if (cursor != null) {
                cursor.close();
            }

            return arrData;

        } catch (Exception e) {
            return null;
        }

    }



    void open() throws SQLException {

        this.getWritableDatabase();
    }

    void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(constants.TABLE_NAME, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


    /*void addUser(String TASK_NAME, String TASK_DESCRIPTION, String TASK_PRIORITY, String TASK_PERTAINS_TO, String SYNC_STATUS, String RANDOM_STRING) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TASK_NAME,TASK_NAME);
        values.put(Constants.TASK_DESCRIPTION, TASK_DESCRIPTION);
        values.put(Constants.TASK_PRIORITY, TASK_PRIORITY);
        values.put(Constants.TASK_PERTAINS_TO,TASK_PERTAINS_TO);
        values.put(Constants.SYNC_STATUS,SYNC_STATUS);
        values.put(Constants.RANDOM_STRING,RANDOM_STRING);
        //values.put(KEY_MOBILE, mobile);

        long id = db.insert(Constants.TABLE_NAME, null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }*/

    /*public void deleteRow(String RANDOM_STRING)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.RANDOM_STRING+"="+RANDOM_STRING, null);
        db.close();
    }*/

    public void deleteRow(String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(constants.TABLE_NAME, constants.dateTime + " = ?",
                new String[] { String.valueOf(dateTime) });
        db.close();
    }


    public void deleteRowSyncd(int SyncStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(constants.TABLE_NAME, constants.sync_status + "=\" " + SyncStatus+"\"", null);
        db.close();
    }

    public  void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(constants.TABLE_NAME,null,null);
        db.close();
    }



    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, constants.TABLE_NAME);
        db.close();
        return count;
    }


    public byte[] retreiveImageFromDB(SQLiteDatabase database) {
        Cursor cur = database.query(false, constants.TABLE_NAME, new String[]{constants.phoneNo, constants.image},
                null, null, null, null,
                constants.phoneNo  , "1");
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex(constants.image));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }


}

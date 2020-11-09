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

public class UpdMultipleRows_SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = UpdMultipleRows_SQLiteHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_SDE_FIELD_LIST_UPD_ROWS = "TABLE_SDE_FIELD_LIST_UPD_ROWS";


    public static final String LM_CODE = "LM_CODE";



    public UpdMultipleRows_SQLiteHelper(Context context) {
        super(context, TABLE_SDE_FIELD_LIST_UPD_ROWS, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_SDE_FIELD_LIST_UPD_ROWS + "("
                + LM_CODE + " TEXT)";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database table created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_SDE_FIELD_LIST_UPD_ROWS + "'");

        // Create tables again
        onCreate(db);

        Log.d(TAG, "Table Dropped");
    }


    public void addUser(String lm_code) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LM_CODE, lm_code);

        long id = db.insert(TABLE_SDE_FIELD_LIST_UPD_ROWS, null, values);
        db.close();

        Log.d(TAG, "New data inserted into sqlite: " + id);
    }


    @SuppressLint("Recycle")
    public Cursor readFromLocalDatabase(SQLiteDatabase database) {
        String[] projection = {LM_CODE};

        return (database.query(TABLE_SDE_FIELD_LIST_UPD_ROWS, projection, null, null, null, null, null, null));

        //return database.rawQuery("SELECT * FROM TABLE_SDE_FIELD_LIST ORDER BY cast(TTL_CON as REAL) DESC",null);
    }


    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SDE_FIELD_LIST_UPD_ROWS);
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
            String strSQL = "SELECT LM_CODE FROM " + TABLE_SDE_FIELD_LIST_UPD_ROWS + " WHERE " + LM_CODE + " NOT LIKE '%" + assignment + "%'";

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

    /*public String[] getAllSpinnerContent(){

        SQLiteDatabase db;
        db = this.getReadableDatabase();

        String query = "Select LM_CODE from TABLE_SDE_FIELD_LIST";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> spinnerContent = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                String word = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                spinnerContent.add(word);
            }while(cursor.moveToNext());
        }
        cursor.close();

        String[] allSpinner = new String[spinnerContent.size()];
        allSpinner = spinnerContent.toArray(allSpinner);

        return allSpinner;
}*/

    long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_SDE_FIELD_LIST_UPD_ROWS);
        db.close();
        return count;
    }

    public void deleteRowLMWorkList(String lm_perno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SDE_FIELD_LIST_UPD_ROWS, LM_CODE + " = ?",
                new String[]{String.valueOf(lm_perno)});
        db.close();
    }


}
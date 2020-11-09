package com.mobileemail_register.SYNC_UNSYNC_ACTIVITY;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mobileemail_register.CLOSURE_ACTIVITY.ClosureActivity_SQLiteHelper;
import com.mobileemail_register.CLOSURE_ACTIVITY.VolleyMultipartRequest;
import com.mobileemail_register.DASHBOARD_ACTIVITY.DashBoard_Activity;
import com.mobileemail_register.R;
import com.mobileemail_register.MISC.constants;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



import static android.content.ContentValues.TAG;

@SuppressWarnings({"Convert2Lambda"})
public class UnsyncdData_Activity extends AppCompatActivity implements UnsyncdData_Adapter.ContactsAdapterListener, View.OnClickListener {


    private ArrayList<UnsyncdData_ReportClass> unsyncdDataReportClasses = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static UnsyncdData_Adapter mAdapter;
    private TextView tv;

    ClosureActivity_SQLiteHelper sqlite_obj;
    public static ClosureActivity_SQLiteHelper sqLiteHelper;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsyncd_data);

        AppCenter.start(getApplication(), "15ee0d52-7eb0-4aa2-9ec5-40b1491add0e",
                Analytics.class, Crashes.class);

        sqlite_obj = new ClosureActivity_SQLiteHelper(getApplicationContext());

        sqLiteHelper = new ClosureActivity_SQLiteHelper(this, "UPLOAD_LM_ABOVE_LM_WORKLIST_DATA.db", null, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BHARAT SANCHAR NIGAM LTD.");
        setSupportActionBar(toolbar);


        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        toolbar1.setTitle("LM UNSYNCD DATA");
        setSupportActionBar(toolbar1);

        recyclerView = findViewById(R.id.recycler_view);
        unsyncdDataReportClasses = new ArrayList<>();
        mAdapter = new UnsyncdData_Adapter(this, unsyncdDataReportClasses, this);
        tv = new TextView(this);


        //sharedpreference = getSharedPreferences(MySharedPREFS, Context.MODE_PRIVATE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);


        readFromLoacalStorage();
    }

    private void readFromLoacalStorage() {
        unsyncdDataReportClasses.clear();
        ClosureActivity_SQLiteHelper closureActivity_sqLiteHelper = new ClosureActivity_SQLiteHelper(this);
        SQLiteDatabase database = closureActivity_sqLiteHelper.getReadableDatabase();

        Cursor cursor = closureActivity_sqLiteHelper.readFromLocalDatabase(database);

        while (cursor.moveToNext()) {
            String phoneNo = cursor.getString(cursor.getColumnIndex(constants.phoneNo));
            String emailId = cursor.getString(cursor.getColumnIndex(constants.emailId));
            String mobileNo = cursor.getString(cursor.getColumnIndex(constants.mobileNo));
            String goGreenStatus = cursor.getString(cursor.getColumnIndex(constants.goGreenStatus));

            String updatedBy = cursor.getString(cursor.getColumnIndex(constants.updatedBy));
            String emailIdValid = cursor.getString(cursor.getColumnIndex(constants.emailIdValid));
            String mobileNoValid = cursor.getString(cursor.getColumnIndex(constants.mobileNoValid));
            String fileUploaded = cursor.getString(cursor.getColumnIndex(constants.fileUploaded));

            String latitude = cursor.getString(cursor.getColumnIndex(constants.latitude));
            String longitude = cursor.getString(cursor.getColumnIndex(constants.longitude));
            String docType = cursor.getString(cursor.getColumnIndex(constants.docType));
            String updateType = cursor.getString(cursor.getColumnIndex(constants.updateType));

            byte[] image = cursor.getBlob(cursor.getColumnIndex(constants.image));
            int sync_status = cursor.getInt(cursor.getColumnIndex(constants.sync_status));
            String dateTime = cursor.getString(cursor.getColumnIndex(constants.dateTime));

            unsyncdDataReportClasses.add(new UnsyncdData_ReportClass(
                    phoneNo, emailId, mobileNo, goGreenStatus, updatedBy,
                    emailIdValid, mobileNoValid, fileUploaded,
                    latitude, longitude, docType, updateType, image, sync_status, dateTime));
        }

        mAdapter.notifyDataSetChanged();
        cursor.close();
        closureActivity_sqLiteHelper.close();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.offline_data_main_menu, menu);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
        }
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        int profile_counts = (int) sqlite_obj.getProfilesCount();
        sqlite_obj.close();

        if(profile_counts>0)
        {
            tv.setText("" + profile_counts);
            tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
            tv.setOnClickListener(UnsyncdData_Activity.this);
            tv.setPadding(5, 0, 5, 0);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextSize(18);
            menu.add(0, 0, 1, "").setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:

                break;

            /*case R.id.delete_data:


                final ME_UPD_LM_SQLITE_HELPER me_upd_lm_sqlite_helper = new ME_UPD_LM_SQLITE_HELPER(ME_UPD_LM_WORKLIST_OFFLINE_DATA_ACTIVITY.this);
                final SQLiteDatabase database = me_upd_lm_sqlite_helper.getWritableDatabase();



                Cursor cursor = me_upd_lm_sqlite_helper.readFromLocalDatabase(database);

                while (cursor.moveToNext()) {
                    database.execSQL("DELETE FROM "+ constants.TABLE_NAME + " WHERE " + constants.sync_status + " = "+constants.SYNC_STATUS_OK+"");
                    readFromLoacalStorage();

                    int profile_counts = (int) sqlite_obj.getProfilesCount();
                    sqlite_obj.close();


                    tv.setText("" + profile_counts);
                    tv.setTextColor(getResources().getColor(R.color.colorErrorText));
                    //tv.setOnClickListener(PendingFaultOrdersActivity.this);
                    tv.setPadding(5, 0, 5, 0);
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setTextSize(18);

                }

                break;*/

            case R.id.action_refresh:

                new OfflineDataAsyncTask().execute();

                //syncOfflineData();

            default:
                break;
        }

        return true;
    }


    @Override
    public void onContactSelected(UnsyncdData_ReportClass me_upd_lm_worklist_offline_data_report) {


        /*byte[] kycDocImage = me_upd_lm_worklist_offline_data_report.getImage();
        Intent intent = new Intent(ME_UPD_LM_WORKLIST_OFFLINE_DATA_ACTIVITY.this, ME_UPD_LM_WORKLIST_OFFLINE_DATA_DISPLAY_ACTIVITY.class);
        intent.putExtra("GETPHONE", me_upd_lm_worklist_offline_data_report.getPhoneNo());
        intent.putExtra("GETEMAIL", me_upd_lm_worklist_offline_data_report.getEmailId());
        intent.putExtra("GETMOBILE", me_upd_lm_worklist_offline_data_report.getMobileNo());
        intent.putExtra("GETGOGREEN", me_upd_lm_worklist_offline_data_report.getGoGreenStatus());
        intent.putExtra("GETDOCTYPE", me_upd_lm_worklist_offline_data_report.getDocType());
        intent.putExtra("GETUPDATETYPE", me_upd_lm_worklist_offline_data_report.getUpdateType());
        intent.putExtra("GETIMAGE", kycDocImage);
        startActivity(intent);
        finishAffinity();*/


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "Total Counts Of Pending Offline Data...", Toast.LENGTH_LONG).show();
    }



    @SuppressLint("StaticFieldLeak")
    class OfflineDataAsyncTask extends AsyncTask<Void, Integer, String> {

        protected void onPreExecute() {

            syncOfflineData();

            super.onPreExecute();

        }

        protected String doInBackground(Void... arg0) {
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {
            super.onProgressUpdate(a);

        }

        protected void onPostExecute(String result) {
            //fetchPersonalTaskData();

            super.onPostExecute(result);

        }
    }

    public void syncOfflineData() {
        if (checkInternetConnection()) {

            final ClosureActivity_SQLiteHelper closureActivity_sqLiteHelper = new ClosureActivity_SQLiteHelper(UnsyncdData_Activity.this);
            final SQLiteDatabase database = closureActivity_sqLiteHelper.getWritableDatabase();

            Cursor cursor = closureActivity_sqLiteHelper.readFromLocalDatabase(database);

            while (cursor.moveToNext())
            {
                int sync_status = cursor.getInt(cursor.getColumnIndex(constants.sync_status));

                if(sync_status==constants.SYNC_STATUS_FAILED) {
                    String phoneNo = cursor.getString(cursor.getColumnIndex(constants.phoneNo));
                    String emailId = cursor.getString(cursor.getColumnIndex(constants.emailId));
                    String mobileNo = cursor.getString(cursor.getColumnIndex(constants.mobileNo));
                    String goGreenStatus = cursor.getString(cursor.getColumnIndex(constants.goGreenStatus));

                    String updatedBy = cursor.getString(cursor.getColumnIndex(constants.updatedBy));
                    String emailIdValid = cursor.getString(cursor.getColumnIndex(constants.emailIdValid));
                    String mobileNoValid = cursor.getString(cursor.getColumnIndex(constants.mobileNoValid));
                    String fileUploaded = cursor.getString(cursor.getColumnIndex(constants.fileUploaded));

                    String latitude = cursor.getString(cursor.getColumnIndex(constants.latitude));
                    String longitude = cursor.getString(cursor.getColumnIndex(constants.longitude));
                    String docType = cursor.getString(cursor.getColumnIndex(constants.docType));
                    String updateType = cursor.getString(cursor.getColumnIndex(constants.updateType));
                    String dateTime = cursor.getString(cursor.getColumnIndex(constants.dateTime));

                    byte[] kycDoc = cursor.getBlob(cursor.getColumnIndex(constants.image));

                    String uploadXML =
                            "<FileUploadModel>" +
                                    "<phoneNo>" + phoneNo + "</phoneNo>" +
                                    "<emailId>" + emailId + "</emailId>" +
                                    "<mobileNo>" + mobileNo + "</mobileNo>" +
                                    "<goGreenStatus>" + goGreenStatus + "</goGreenStatus>" +
                                    "<updatedBy>" + updatedBy + "</updatedBy>" +
                                    "<emailIdValid>" + emailIdValid + "</emailIdValid>" +
                                    "<mobileNoValid>" + mobileNoValid + "</mobileNoValid>" +
                                    "<fileUploaded>" + fileUploaded + "</fileUploaded>" +
                                    "<latitude>" + latitude + "</latitude>" +
                                    "<longitude>" + longitude + "</longitude>" +
                                    "<docType>" + docType + "</docType>" +
                                    "<updateType>" + updateType + "</updateType>" +
                                    "</FileUploadModel>";


                    LayoutInflater inflater = getLayoutInflater();
                    @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
                    AlertDialog.Builder alert = new AlertDialog.Builder(UnsyncdData_Activity.this);
                    alert.setView(alertLayout);
                    alert.setCancelable(false);
                    final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
                    textViewAlertDialog.setText(R.string.sync_offline_data);
                    AlertDialog dialog = alert.create();
                    dialog.show();

                    Log.d(TAG, "uploadXML" + uploadXML);

                    if (fileUploaded != null && fileUploaded.equals("N")) {

                        Log.d(TAG, "W/O DOC EXECUTED");


                        String URL = "https://fms.bsnl.in/fmswebservices/rest/update/emailmobilereq";
                        RequestQueue queue = Volley.newRequestQueue(UnsyncdData_Activity.this);

                        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                                new Response.Listener<NetworkResponse>() {
                                    @Override
                                    public void onResponse(NetworkResponse response) {
                                        queue.getCache().clear();

                                        if (response.statusCode == 200) {

                                            try {

                                                queue.getCache().clear();
                                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                                //Toast.makeText(getApplicationContext(), jsonObject.getString("REMARKS"), Toast.LENGTH_SHORT).show();
                                                if (jsonObject.getString("STATUS").contentEquals("SUCCESS")) {
                                                    JSONArray Jarray = jsonObject.getJSONArray("ROWSET");


                                                    for (int i = 0; i < Jarray.length(); i++) {
                                                        //jsonObject = Jarray.getJSONObject(i);

                                                        //String REMARKS = jsonObject.getString("REMARKS");


                                                        //showAlertSuccess(REMARKS);

                                                        closureActivity_sqLiteHelper.updateLocalDatabase(phoneNo,
                                                                emailId, mobileNo,
                                                                goGreenStatus, updatedBy,
                                                                emailIdValid, mobileNoValid, fileUploaded, latitude,
                                                                longitude, docType, updateType, kycDoc, String.valueOf(constants.SYNC_STATUS_OK), dateTime, database);



                                                    }

                                                }

                                                dialog.dismiss();
                                                readFromLoacalStorage();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {


                                        NetworkResponse response = error.networkResponse;

                                        if (response != null && response.statusCode == 200) {

                                            try {
                                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                                //Toast.makeText(getApplicationContext(), jsonObject.getString("REMARKS"), Toast.LENGTH_SHORT).show();
                                                if (jsonObject.getString("STATUS").contentEquals("FAILED")) {
                                                    JSONArray Jarray = jsonObject.getJSONArray("ROWSET");
                                                    for (int i = 0; i < Jarray.length(); i++) {
                                                        //jsonObject = Jarray.getJSONObject(i);
                                                        //Toast.makeText(getApplicationContext(),"Response"+response,Toast.LENGTH_LONG).show();

                                                        //String REMARKS = jsonObject.getString("REMARKS");

                                                        //showAlertSuccess(REMARKS);


                                                    }
                                                }


                                                //Toast.makeText(getApplicationContext(), obj + "Response", Toast.LENGTH_SHORT).show();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            dialog.dismiss();
                                        }
                                    }
                                }) {


                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("uploadXML", uploadXML);
                                return params;
                            }

                        };


                        Volley.newRequestQueue(this).add(volleyMultipartRequest);

                        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

                    } else {

                        Log.d(TAG, "WITH DOC EXECUTED");






                        String URL = "https://fms.bsnl.in/fmswebservices/rest/update/emailmobilereq";

                        RequestQueue queue = Volley.newRequestQueue(UnsyncdData_Activity.this);

                        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                                new Response.Listener<NetworkResponse>() {
                                    @Override
                                    public void onResponse(NetworkResponse response) {

                                        if(response.statusCode == 200)
                                        {

                                            try {

                                                queue.getCache().clear();
                                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                                //Toast.makeText(getApplicationContext(), jsonObject.getString("REMARKS"), Toast.LENGTH_SHORT).show();
                                                if (jsonObject.getString("STATUS").contentEquals("SUCCESS")) {
                                                    JSONArray Jarray = jsonObject.getJSONArray("ROWSET");


                                                    for (int i = 0; i < Jarray.length(); i++) {
                                                        //jsonObject = Jarray.getJSONObject(i);

                                                        //String REMARKS = jsonObject.getString("REMARKS");

                                                        closureActivity_sqLiteHelper.updateLocalDatabase(phoneNo,
                                                                emailId, mobileNo,
                                                                goGreenStatus, updatedBy,
                                                                emailIdValid, mobileNoValid, fileUploaded, latitude,
                                                                longitude, docType, updateType, kycDoc, String.valueOf(constants.SYNC_STATUS_OK), dateTime, database);


                                                        //showAlertSuccess(REMARKS);

                                                    }

                                                }

                                                dialog.dismiss();
                                                readFromLoacalStorage();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                                        NetworkResponse response = error.networkResponse;

                                        if(response != null && response.statusCode == 200)
                                        {

                                            try {
                                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                                //Toast.makeText(getApplicationContext(), jsonObject.getString("REMARKS"), Toast.LENGTH_SHORT).show();
                                                if(jsonObject.getString("STATUS").contentEquals("FAILED")){
                                                    JSONArray Jarray = jsonObject.getJSONArray("ROWSET");
                                                    for (int i = 0; i < Jarray.length(); i++) {
                                                        //jsonObject = Jarray.getJSONObject(i);
                                                        //Toast.makeText(getApplicationContext(),"Response"+response,Toast.LENGTH_LONG).show();

                                                        //String REMARKS = jsonObject.getString("REMARKS");

                                                        //showAlertSuccess(REMARKS);


                                                    }
                                                }


                                                //Toast.makeText(getApplicationContext(), obj + "Response", Toast.LENGTH_SHORT).show();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            dialog.dismiss();


                                        }

                                    }
                                }) {

                            /*
                             * If you want to add more parameters with the image
                             * you can do it here
                             * here we have only one parameter with the image
                             * which is uploadXML
                             * */
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("uploadXML", uploadXML);
                                return params;
                            }

                            /*
                             * Here we are passing image by renaming it with a unique name
                             * */
                            @Override
                            protected Map<String, DataPart> getByteData() {
                                Map<String, DataPart> params = new HashMap<>();
                                params.put("file", new DataPart(phoneNo + ".jpg", kycDoc));
                                return params;
                            }


                        };




                        //adding the request to volley
                        Volley.newRequestQueue(this).add(volleyMultipartRequest);

                        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));


                    }
                }
            }
        }

        else
        {
            //Toast.makeText(context, "Internet Not Connected", Toast.LENGTH_LONG).show();
            Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
        }


    }

    private boolean checkInternetConnection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UnsyncdData_Activity.this, DashBoard_Activity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finishAffinity();
        super.onBackPressed();
    }

}

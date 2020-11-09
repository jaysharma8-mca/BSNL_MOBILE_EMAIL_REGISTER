package com.mobileemail_register.SDEFIELDLIST_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mobileemail_register.DASHBOARD_ACTIVITY.DashBoard_Activity;
import com.mobileemail_register.MISC.RequestHandler;
import com.mobileemail_register.R;
import com.mobileemail_register.SDEFIELDLIST_ASSIGNED_ACTIVITY.SdeConfiguredLm_SQLiteHelper;
import com.mobileemail_register.SDEFIELDLIST_ASSIGNED_ACTIVITY.SdeFieldListGetAssignedReport_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;



public class SdeFieldList_Activity extends AppCompatActivity {

    TextView textViewTTLCon;
    TextView textViewTGT;
    TextView textViewPMSData;
    TextView textViewAssignedCount;

    private static final String SP_MEREG = "SP_MEREG";
    private static final String TGT = "TGT";
    private static final String TTL_CON = "TTL_CON";
    private AlertDialog alertDialog;

    private SdeFieldList_SQLiteHelper sdeFieldList_sqLiteHelper;
    private List<String> user_perno;
    private List<String> lm_code;
    private List<String> lm_perno;
    private List<String> ttl_con;
    private List<String> tgt;
    private List<String> lmc_assigned;
    private List<String> lmc_assigned_to_text;
    private List<String> assigned_flag;
    private List<String> deassigned_flag;
    private List<String> sync_status_flag;

    private SdeConfiguredLm_SQLiteHelper sdeConfiguredLm_sqLiteHelper;
    private List<String> PRIMARY_LM;
    private List<String> ASSIGNED_DATE;
    private List<String> PER_NO;
    private List<String> ASSIGNED_LM;
    private List<String> DEASSIGNMENT_FLAG;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sde_field_list);

        AppCenter.start(getApplication(), "f7271b3f-27aa-4985-8654-37f1347f4bf4",
                Analytics.class, Crashes.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SDO FIELD LIST");
        setSupportActionBar(toolbar);


        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);



        sdeFieldList_sqLiteHelper = new SdeFieldList_SQLiteHelper(getApplicationContext());
        sdeConfiguredLm_sqLiteHelper = new SdeConfiguredLm_SQLiteHelper(getApplicationContext());
        SdeConfiguredLm_SQLiteHelper sqlite_obj = new SdeConfiguredLm_SQLiteHelper(getApplicationContext());

        final String[] myData = sqlite_obj.SelectAllTableData();
        final String[] allData = sdeFieldList_sqLiteHelper.SelectAllTableData();

        if (myData == null || allData ==  null) {
            new AsyncTaskRunner().execute();
        }


        TextView textViewName = findViewById(R.id.textViewName);
        textViewPMSData = findViewById(R.id.textViewPMSData);
        TextView textViewMonth = findViewById(R.id.textViewMonth);
        textViewTTLCon = findViewById(R.id.textViewTTLCon);
        textViewTGT = findViewById(R.id.textViewTGT);
        textViewAssignedCount = findViewById(R.id.textViewAssignedCount);

        getSumTGT();
        getSumTTLCON();

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String TGT = "TGT";
        final String TTL_CON = "TTL_CON";
        final String NAME = "NAME";
        final String target = sharedPreferences.getString(TGT, null);
        final String ttl_con = sharedPreferences.getString(TTL_CON, null);
        final String name = sharedPreferences.getString(NAME, null);



        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month = month_date.format(calendar.getTime());
        int year = calendar.get(Calendar.YEAR);

        textViewMonth.setText(""+month+" "+year);

        if(target != null && !target.equals("0") && ttl_con != null && !ttl_con.equals("0"))
        {

            textViewTTLCon.setText(ttl_con);
            textViewTGT.setText(target);

            int tgt = Integer.parseInt(target);
            int ttlcon = Integer.parseInt(ttl_con);
            double cCom = ((double)tgt/ttlcon) * 100;
            cCom =Double.parseDouble(new DecimalFormat("##.##").format(cCom));

            textViewPMSData.setText(""+cCom+"%");

        }

        else
        {
            textViewPMSData.setText("");
        }


        if(name != null)
        {
            textViewName.setText(name);
        }

        int profile_counts = (int) sqlite_obj.getProfilesCount();
        String counts = String.valueOf(profile_counts);


        if(counts != null)
        {
            textViewAssignedCount.setText(""+profile_counts);
        }
        sqlite_obj.close();

    }

    public void configureLinemanActivity(View view) {

        Intent intent = new Intent(SdeFieldList_Activity.this, SdeFieldListConfigureLm_Activity.class);
        startActivity(intent);
        finish();
    }

    public void assignTaskToLmActivity(View view) {

        Intent intent = new Intent(SdeFieldList_Activity.this, SdeFieldListGetAssignedReport_Activity.class);
        startActivity(intent);
        finish();
    }

    public void getSumTGT(){

        SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreference.edit();

        int sum;
        SdeFieldList_SQLiteHelper sdeFieldList_sqLiteHelper = new SdeFieldList_SQLiteHelper(this);
        SQLiteDatabase database = sdeFieldList_sqLiteHelper.getReadableDatabase();


        String sumQuery=String.format("SELECT SUM(%s) as Total FROM %s",SdeFieldList_SQLiteHelper.TGT,SdeFieldList_SQLiteHelper.TABLE_SDE_FIELD_LIST);
        Cursor cursor=database.rawQuery(sumQuery,null);
        if(cursor != null && cursor.moveToFirst())
        {
            sum= cursor.getInt(cursor.getColumnIndex("Total"));
            editor.putString(TGT, String.valueOf(sum));
            editor.apply();


        }


        if (cursor != null) {
            cursor.close();
        }
        sdeFieldList_sqLiteHelper.close();
    }

    @SuppressLint("SetTextI18n")
    private void setData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String TGT = "TGT";
        final String TTL_CON = "TTL_CON";
        final String target = sharedPreferences.getString(TGT, null);
        final String ttl_con = sharedPreferences.getString(TTL_CON, null);

        if(target != null && !target.equals("0") && ttl_con != null && !ttl_con.equals("0"))
        {

            textViewTTLCon.setText(ttl_con);
            textViewTGT.setText(target);

            int tgt = Integer.parseInt(target);
            int ttlcon = Integer.parseInt(ttl_con);
            double cCom = ((double)tgt/ttlcon) * 100;
            cCom =Double.parseDouble(new DecimalFormat("##.##").format(cCom));

            textViewPMSData.setText(""+cCom+"%");

        }
    }

    public void getSumTTLCON(){

        SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreference.edit();

        int sum;
        SdeFieldList_SQLiteHelper sdeFieldList_sqLiteHelper = new SdeFieldList_SQLiteHelper(this);
        SQLiteDatabase database = sdeFieldList_sqLiteHelper.getReadableDatabase();


        String sumQuery=String.format("SELECT SUM(%s) as Total FROM %s",SdeFieldList_SQLiteHelper.TTL_CON,SdeFieldList_SQLiteHelper.TABLE_SDE_FIELD_LIST);
        Cursor cursor=database.rawQuery(sumQuery,null);
        if(cursor != null && cursor.moveToFirst())
        {
            sum= cursor.getInt(cursor.getColumnIndex("Total"));
            editor.putString(TTL_CON, String.valueOf(sum));
            editor.apply();

        }


        if (cursor != null) {
            cursor.close();
        }
        sdeFieldList_sqLiteHelper.close();
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            retrieveDetails();
        }



        @Override
        protected String doInBackground(String... params) {

            getData();
            getAssignedData();



            return  null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

        }





        @Override
        protected void onProgressUpdate(String... text) {


        }
    }


    public void getData() {

        if (checkInternetConnection()) {
            SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
            final String PERSONAL_NUMBER = "PERSONAL_NUMBER";
            final String personal_number = sharedPreferences.getString(PERSONAL_NUMBER, null);

            if(personal_number != null)
            {

                String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/sdolmcount/username/" + personal_number + "";
                URL = URL.replaceAll(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        URL,
                        response -> {

                            if (response.contains("ORA-")) {
                                alertDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(SdeFieldList_Activity.this);

                                builder.setTitle("Fetching Failed");
                                builder.setMessage("FMS Database Connectivity Error... Please Try again After Sometime...");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                                builder.show();
                            } else {

                                try {
                                    //sdeFieldList_sqLiteHelper.deleteUsers();
                                    JSONObject json = new JSONObject(response);

                                    //now get your  json array like this
                                    JSONArray jsonArray = json.getJSONArray("ROWSET");


                                    user_perno = new ArrayList<>();
                                    lm_code = new ArrayList<>();
                                    lm_perno = new ArrayList<>();
                                    ttl_con = new ArrayList<>();
                                    tgt = new ArrayList<>();
                                    lmc_assigned = new ArrayList<>();
                                    lmc_assigned_to_text = new ArrayList<>();
                                    assigned_flag = new ArrayList<>();
                                    deassigned_flag = new ArrayList<>();
                                    sync_status_flag = new ArrayList<>();


                                    if (jsonArray.length() > 0) {

                                        for (int countItem = 0; countItem < jsonArray.length(); countItem++) {

                                            JSONObject jsonObject = jsonArray.getJSONObject(countItem);

                                            user_perno.add(personal_number);
                                            lm_code.add(jsonObject.isNull("LM_CODE") ? "" : jsonObject.optString("LM_CODE"));
                                            lm_perno.add(jsonObject.isNull("LM_PER_NO") ? "" : jsonObject.optString("LM_PER_NO"));
                                            ttl_con.add(jsonObject.isNull("TTL_CON") ? "" : jsonObject.optString("TTL_CON"));
                                            tgt.add(jsonObject.isNull("TGT") ? "" : jsonObject.optString("TGT"));
                                            lmc_assigned.add(" ");
                                            lmc_assigned_to_text.add(" ");
                                            assigned_flag.add(" ");
                                            deassigned_flag.add(" ");
                                            sync_status_flag.add("2");

                                            //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                                        }

                                        sqlite();


                                        sdeFieldList_sqLiteHelper.close();
                                        getSumTTLCON();
                                        getSumTGT();
                                        setData();

                                        if(alertDialog != null)
                                        {
                                            alertDialog.dismiss();
                                        }


                                    } else {
                                        if(alertDialog != null)
                                        {
                                            alertDialog.dismiss();
                                        }
                                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            //Toast.makeText(getApplicationContext(), "Respnse :" + response, Toast.LENGTH_LONG).show();
                        },
                        error -> {

                            Toast.makeText(SdeFieldList_Activity.this, "Database Connectivity Error!!! Check Your Network Connection And Try Again...", Toast.LENGTH_LONG).show();
                            if(alertDialog != null)
                            {
                                alertDialog.dismiss();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {


                        return new HashMap<>();
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("EKEY", "b28272183c64fcb45b11d9098a7dd97df51f89bc1bae9448e4126258fd9446d1");
                        return params;
                    }
                };


                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }

            else
            {
               this.runOnUiThread(() -> {
                   Snackbar.make(findViewById(android.R.id.content), "No PER NO Found... Kindly Reregister...", Snackbar.LENGTH_LONG).show();
                   if(alertDialog != null)
                   {
                       alertDialog.dismiss();
                   }
               });

                //Toast.makeText(getApplicationContext(),"No PER NO Found... Kindly Reregister...",Toast.LENGTH_LONG).show();
            }


        } else {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }

            Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
        }

    }

    public void getAssignedData() {

        if (checkInternetConnection()) {
            SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
            final String PERSONAL_NUMBER = "PERSONAL_NUMBER";
            final String personal_number = sharedPreferences.getString(PERSONAL_NUMBER, null);


            if (personal_number != null) {
                String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/getassignedlm/perno/" + personal_number + "";
                URL = URL.replaceAll(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        URL,
                        response -> {

                            if (response.contains("ORA-")) {
                                alertDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(SdeFieldList_Activity.this);

                                builder.setTitle("Fetching Failed");
                                builder.setMessage("FMS Database Connectivity Error... Please Try again After Sometime...");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                                builder.show();
                            } else {

                                try {
                                    //getting the whole json object from the response
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("STATUS").contentEquals("SUCCESS")) {

                                        JSONArray jsonArray = jsonObject.getJSONArray("ROWSET");


                                        PRIMARY_LM = new ArrayList<>();
                                        ASSIGNED_DATE = new ArrayList<>();
                                        PER_NO = new ArrayList<>();
                                        ASSIGNED_LM = new ArrayList<>();
                                        DEASSIGNMENT_FLAG = new ArrayList<>();


                                        if (jsonArray.length() > 0) {
                                            for (int countItem = 0; countItem < jsonArray.length(); countItem++) {

                                                jsonObject = jsonArray.getJSONObject(countItem);


                                                PRIMARY_LM.add(jsonObject.isNull("PRIMARY_LM") ? "" : jsonObject.optString("PRIMARY_LM"));
                                                ASSIGNED_DATE.add(jsonObject.isNull("ASSIGNED_DATE") ? "" : jsonObject.optString("ASSIGNED_DATE"));
                                                PER_NO.add(jsonObject.isNull("PER_NO") ? "" : jsonObject.optString("PER_NO"));
                                                ASSIGNED_LM.add(jsonObject.isNull("ASSIGNED_LM") ? "" : jsonObject.optString("ASSIGNED_LM"));
                                                DEASSIGNMENT_FLAG.add(" ");


                                                //Toast.makeText(getApplicationContext(), "DATA UPDATED SUCCESSFULLY !!!", Toast.LENGTH_LONG).show();

                                            }

                                            Assignedsqlite();
                                            if (alertDialog != null) {
                                                alertDialog.dismiss();
                                            }



                                            int profile_counts = (int) sdeConfiguredLm_sqLiteHelper.getProfilesCount();
                                            String counts = String.valueOf(profile_counts);


                                            if(counts != null)
                                            {
                                                textViewAssignedCount.setText(""+profile_counts);
                                            }
                                            sdeConfiguredLm_sqLiteHelper.close();


                                        }


                                    } else if (jsonObject.getString("STATUS").contentEquals("FAILED")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("ROWSET");
                                        if (jsonArray.length() > 0) {

                                            for (int countItem = 0; countItem < jsonArray.length(); countItem++) {

                                                jsonObject = jsonArray.getJSONObject(countItem);

                                                //String STATUS = jsonObject.isNull("STATUS") ? "" : jsonObject.optString("STATUS");
                                                String REMARKS = jsonObject.isNull("REMARKS") ? "" : jsonObject.optString("REMARKS");

                                                Toast.makeText(getApplicationContext(), REMARKS, Toast.LENGTH_LONG).show();
                                                //showAlertSuccess(REMARKS);
                                                sdeConfiguredLm_sqLiteHelper.deleteUsers();
                                                if (alertDialog != null) {
                                                    alertDialog.dismiss();
                                                }

                                            }

                                        }

                                        //onRefresh();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            //Toast.makeText(getApplicationContext(), "Respnse :" + response, Toast.LENGTH_LONG).show();
                        },
                        error -> {

                            Toast.makeText(SdeFieldList_Activity.this, "Database Connectivity Error!!! Check Your Network Connection And Try Again...", Toast.LENGTH_LONG).show();
                            if (alertDialog != null) {
                                alertDialog.dismiss();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {


                        return new HashMap<>();
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("EKEY", "b28272183c64fcb45b11d9098a7dd97df51f89bc1bae9448e4126258fd9446d1");
                        return params;
                    }
                };


                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            } else {
                this.runOnUiThread(() -> {
                    Snackbar.make(findViewById(android.R.id.content), "No PER NO Found... Kindly Reregister...", Snackbar.LENGTH_LONG).show();
                    if(alertDialog != null)
                    {
                        alertDialog.dismiss();
                    }
                });
            }


        } else {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }

            Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
        }

    }


    private void sqlite() {

        sdeFieldList_sqLiteHelper.open();

        sdeFieldList_sqLiteHelper.deleteUsers();

        if (user_perno != null && lm_perno != null &&
                ttl_con != null && tgt != null && lmc_assigned != null &&
                lmc_assigned_to_text != null && assigned_flag != null &&
                deassigned_flag != null && sync_status_flag != null) {
            for (int i = 0; i < lm_code.size(); i++) {

                sdeFieldList_sqLiteHelper.addUser(user_perno.get(i), lm_code.get(i),lm_perno.get(i),
                        ttl_con.get(i),tgt.get(i),lmc_assigned.get(i),lmc_assigned_to_text.get(i),
                        deassigned_flag.get(i),assigned_flag.get(i),
                        sync_status_flag.get(i));

                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Data Synchronized!!!", Snackbar.LENGTH_LONG)
                        /*.setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))*/
                        .show();
            }
        } else {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "No Data To Display!!!", Snackbar.LENGTH_LONG).show();
        }


        sdeFieldList_sqLiteHelper.close();
    }


    private void Assignedsqlite() {

        sdeConfiguredLm_sqLiteHelper.open();

        sdeConfiguredLm_sqLiteHelper.deleteUsers();

        if (PRIMARY_LM != null && ASSIGNED_DATE != null && PER_NO != null &&
                ASSIGNED_LM != null && DEASSIGNMENT_FLAG != null) {
            for (int i = 0; i < PRIMARY_LM.size(); i++) {

                sdeConfiguredLm_sqLiteHelper.addUser(PRIMARY_LM.get(i), ASSIGNED_DATE.get(i), PER_NO.get(i),
                        ASSIGNED_LM.get(i),DEASSIGNMENT_FLAG.get(i));

                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Data Synchronized!!!", Snackbar.LENGTH_LONG)
                        /*.setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))*/
                        .show();
            }
        } else {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "No Data To Display!!!", Snackbar.LENGTH_LONG).show();
        }


        sdeConfiguredLm_sqLiteHelper.close();
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void retrieveDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(SdeFieldList_Activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.data_fetching);
        alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SdeFieldList_Activity.this, DashBoard_Activity.class);
        startActivity(intent);
        finishAffinity();
    }

}

package com.mobileemail_register.SDEFIELDLIST_ASSIGNED_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mobileemail_register.MISC.RequestHandler;
import com.mobileemail_register.R;
import com.mobileemail_register.SDEFIELDLIST_ACTIVITY.SdeFieldList_Activity;
import com.mobileemail_register.SDEFIELDLIST_ACTIVITY.SdeFieldList_SQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@SuppressWarnings({"Convert2Lambda"})
public class SdeFieldListGetAssignedReport_Activity extends AppCompatActivity implements SdeConfiguredLm_Adapter.ContactsAdapterListener, View.OnClickListener {

    private ArrayList<SdeConfiguredLm_ReportClass> sdeConfiguredLm_reportClasses = new ArrayList<>();

    private static final String SP_MEREG = "SP_MEREG";
    private static final String PRIMARY_LM_CODE = "PRIMARY_LM_CODE";




    @SuppressLint("StaticFieldLeak")
    public static SdeConfiguredLm_Adapter mAdapter;

    private AlertDialog alertDialog;

    private SdeConfiguredLm_SQLiteHelper sdeConfiguredLm_sqLiteHelper;
    private List<String> PRIMARY_LM;
    private List<String> ASSIGNED_DATE;
    private List<String> PER_NO;
    private List<String> ASSIGNED_LM;
    private List<String> DEASSIGNMENT_FLAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sde_field_list_assign_task);

        AppCenter.start(getApplication(), "15ee0d52-7eb0-4aa2-9ec5-40b1491add0e",
                Analytics.class, Crashes.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BHARAT SANCHAR NIGAM LTD.");
        setSupportActionBar(toolbar);


        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        toolbar1.setTitle("ASSIGNED LM LIST");
        setSupportActionBar(toolbar1);

        //displayData = findViewById(R.id.displayData);

        sdeConfiguredLm_sqLiteHelper = new SdeConfiguredLm_SQLiteHelper(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        sdeConfiguredLm_reportClasses = new ArrayList<>();
        mAdapter = new SdeConfiguredLm_Adapter(this, sdeConfiguredLm_reportClasses, this);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        readFromLocalStorage();
    }

    private void sqlite() {

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

    public void readFromLocalStorage() {
        sdeConfiguredLm_reportClasses.clear();
        SdeConfiguredLm_SQLiteHelper sdeConfiguredLm_sqLiteHelper = new SdeConfiguredLm_SQLiteHelper(this);
        SQLiteDatabase database = sdeConfiguredLm_sqLiteHelper.getReadableDatabase();

        Cursor cursor = sdeConfiguredLm_sqLiteHelper.readFromLocalDatabase(database);

        while (cursor.moveToNext()) {


            /*list.add(cursor.getString(cursor.getColumnIndex(SdeConfiguredLm_SQLiteHelper.ASSIGNED_LM)));

            displayData.setText(String.valueOf(list));*/

            String PRIMARY_LM = cursor.getString(cursor.getColumnIndex(SdeConfiguredLm_SQLiteHelper.PRIMARY_LM));
            //String ASSIGNED_DATE = cursor.getString(cursor.getColumnIndex(SdeConfiguredLm_SQLiteHelper.ASSIGNED_DATE));
            String PER_NO = cursor.getString(cursor.getColumnIndex(SdeConfiguredLm_SQLiteHelper.PER_NO));
            String ASSIGNED_LM = cursor.getString(cursor.getColumnIndex(SdeConfiguredLm_SQLiteHelper.ASSIGNED_LM));


            sdeConfiguredLm_reportClasses.add(new SdeConfiguredLm_ReportClass(PRIMARY_LM, PER_NO, ASSIGNED_LM));

        }

        mAdapter.notifyDataSetChanged();
        cursor.close();
        sdeConfiguredLm_sqLiteHelper.close();
    }

    public void getData() {

        if (checkInternetConnection()) {
            SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
            final String PERSONAL_NUMBER = "PERSONAL_NUMBER";
            final String personal_number = sharedPreferences.getString(PERSONAL_NUMBER, null);


            if (personal_number != null) {
                retrieveDetails();
                String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/getassignedlm/perno/" + personal_number + "";
                URL = URL.replaceAll(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        URL,
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(String response) {

                                if (response.contains("ORA-")) {
                                    alertDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SdeFieldListGetAssignedReport_Activity.this);

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

                                                sqlite();
                                                if (alertDialog != null) {
                                                    alertDialog.dismiss();
                                                }

                                                readFromLocalStorage();
                                                sdeConfiguredLm_sqLiteHelper.close();
                                                //onRefresh();


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
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(SdeFieldListGetAssignedReport_Activity.this, "Database Connectivity Error!!! Check Your Network Connection And Try Again...", Toast.LENGTH_LONG).show();
                                if (alertDialog != null) {
                                    alertDialog.dismiss();
                                }
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
                Toast.makeText(getApplicationContext(), "No PER NO Found... Kindly Reregister...", Toast.LENGTH_LONG).show();
            }


        } else {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }

            Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sdefieldlist_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            if (checkInternetConnection()) {

                getData();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
            }

                /*case R.id.action_upload:



                break;*/
        }

        return true;
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onContactSelected(SdeConfiguredLm_ReportClass sdeFieldListConfigureLm_reportClass) {

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRIMARY_LM_CODE, sdeFieldListConfigureLm_reportClass.getPRIMARY_LM());
        editor.apply();

        readFromLocalStorageAssignedDataReport();

        /*Intent intent = new Intent(SdeFieldListGetAssignedReport_Activity.this, SdeFieldList_FetchReport_Activity.class);
        //intent.putExtra("primary_lm_code", sdeFieldListConfigureLm_reportClass.getPRIMARY_LM());
        startActivity(intent);
        finishAffinity();*/

    }

    @SuppressLint("SetTextI18n")
    public void readFromLocalStorageAssignedDataReport() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.custom_table_layout, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ImageView imageViewDeAssign = dialogLayout.findViewById(R.id.imageViewDeAssign);
        //ImageView imageViewDeleteDeAssign = dialogLayout.findViewById(R.id.imageViewDeleteDeAssign);
        TableLayout tableLayout = dialogLayout.findViewById(R.id.tableLayout);
        TableRow tableRow = new TableRow(SdeFieldListGetAssignedReport_Activity.this);
        tableRow.setBackground(getResources().getDrawable(R.drawable.tablerow_design));

        final TextView tv10 = new TextView(SdeFieldListGetAssignedReport_Activity.this);
        tv10.setText(" ASSIGNED LMC ");
        tv10.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv10.setGravity(Gravity.CENTER_HORIZONTAL);
        tv10.setTypeface(null, Typeface.BOLD);
        tv10.setTextColor(Color.parseColor("#263237"));
        tv10.setPadding(20, 40, 40, 40);
        tv10.setBackground(getResources().getDrawable(R.drawable.tablerow_design));
        tableRow.addView(tv10);

        final TextView tv11 = new TextView(SdeFieldListGetAssignedReport_Activity.this);
        tv11.setText(" ASSIGNMENT DATE ");
        tv11.setTextColor(Color.parseColor("#263237"));
        tv11.setGravity(Gravity.CENTER_HORIZONTAL);
        tv11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv11.setTypeface(null, Typeface.BOLD);
        tv11.setPadding(0, 40, 0, 40);
        tv11.setBackground(getResources().getDrawable(R.drawable.tablerow_design));
        tableRow.addView(tv11);

        tableLayout.addView(tableRow);

        SdeConfiguredLm_SQLiteHelper sdeConfiguredLm_sqLiteHelper = new SdeConfiguredLm_SQLiteHelper(this);
        SQLiteDatabase database = sdeConfiguredLm_sqLiteHelper.getReadableDatabase();

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String PRIMARY_LM_CODE = "PRIMARY_LM_CODE";
        final String primary_lm_code = sharedPreferences.getString(PRIMARY_LM_CODE, null);

        Cursor cursor = database.rawQuery("SELECT * FROM TABLE_SDE_CONFIGURED_LM_LIST Where PRIMARY_LM='" + primary_lm_code + "'", null);

        while (cursor.moveToNext()) {


            String assigned_date = cursor.getString(cursor.getColumnIndex(SdeConfiguredLm_SQLiteHelper.ASSIGNED_DATE));
            String assigned_lm = cursor.getString(cursor.getColumnIndex(SdeConfiguredLm_SQLiteHelper.ASSIGNED_LM));


            TableRow tbrow0 = new TableRow(SdeFieldListGetAssignedReport_Activity.this);
            tbrow0.setBackground(getResources().getDrawable(R.drawable.tablecelldesign));

            TextView tv0 = new TextView(SdeFieldListGetAssignedReport_Activity.this);
            tv0.setText(assigned_lm);
            tv0.setTextColor(Color.parseColor("#263237"));
            tv0.setGravity(Gravity.CENTER_HORIZONTAL);
            tv0.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv0.setTypeface(null, Typeface.BOLD);
            tv0.setPadding(0, 40, 0, 40);
            tv0.setBackground(getResources().getDrawable(R.drawable.tablecelldesign));
            tbrow0.addView(tv0);

            TextView tv1 = new TextView(SdeFieldListGetAssignedReport_Activity.this);
            tbrow0.setBackground(getResources().getDrawable(R.drawable.tablecelldesign));
            tv1.setText(assigned_date);
            tv1.setTextColor(Color.parseColor("#263237"));
            tv1.setGravity(Gravity.CENTER_HORIZONTAL);
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv1.setTypeface(null, Typeface.BOLD);
            tv1.setPadding(0, 40, 0, 40);
            tv1.setBackground(getResources().getDrawable(R.drawable.tablecelldesign));
            tbrow0.addView(tv1);
            tableLayout.addView(tbrow0);


            tbrow0.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    TableRow tableRow1 = (TableRow) view;
                    TextView assigned_lm_code = (TextView) tableRow1.getChildAt(0);

                    String Assigned_Lm_Code = assigned_lm_code.getText().toString().trim();

                    String deassigned_flag = "D";
                        sdeConfiguredLm_sqLiteHelper.reassignData(Assigned_Lm_Code,deassigned_flag);
                        //Toast.makeText(getApplicationContext(), Assigned_Lm_Code+deassigned_flag, Toast.LENGTH_LONG).show();


                    imageViewDeAssign.setVisibility(View.VISIBLE);
                    //imageViewDeleteDeAssign.setVisibility(View.VISIBLE);

                    imageViewDeAssign.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if (checkInternetConnection()) {

                                submitDetails();

                                SdeConfiguredLm_SQLiteHelper sdeConfiguredLm_sqLiteHelper = new SdeConfiguredLm_SQLiteHelper(SdeFieldListGetAssignedReport_Activity.this);
                                SQLiteDatabase database = sdeConfiguredLm_sqLiteHelper.getReadableDatabase();

                                Cursor cursor = sdeConfiguredLm_sqLiteHelper.readFromLocalDatabaseSdeLM(database);


                                if (cursor.getCount() > 0) {

                                    while (cursor.moveToNext()) {

                                        String PRILMCODE = cursor.getString(cursor.getColumnIndex(SdeConfiguredLm_SQLiteHelper.PRIMARY_LM));
                                        String DEASSIGNMENT_FLAG = cursor.getString(cursor.getColumnIndex(SdeConfiguredLm_SQLiteHelper.DEASSIGNMENT_FLAG));

                                        if (DEASSIGNMENT_FLAG.equals("D") && PRILMCODE != null) {
                                            //lmperno = LM_PERNO.substring(1);

                                            String USER_PERNO = cursor.getString(cursor.getColumnIndex(SdeConfiguredLm_SQLiteHelper.PER_NO));
                                            String ASSLMCODE = cursor.getString(cursor.getColumnIndex(SdeConfiguredLm_SQLiteHelper.ASSIGNED_LM));

                                            //String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/assignlm/perno/00302854/prilmcode/PUN234401/asslmcode/PUN235501/flag/I
                                            String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/assignlm/perno/" + USER_PERNO + "/prilmcode/" + PRILMCODE + "/asslmcode/" + ASSLMCODE + "/flag/" + DEASSIGNMENT_FLAG + "";
                                            URL = URL.replaceAll(" ", "%20");


                                            //Toast.makeText(getApplicationContext(), URL, Toast.LENGTH_LONG).show();

                                            StringRequest sr = new StringRequest(Request.Method.POST, URL,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            if (response.contains("ORA-")) {
                                                                alertDialog.dismiss();


                                                                Toast.makeText(getApplicationContext(), "FMS Database Connectivity Error... Please Try again After Sometime...", Toast.LENGTH_LONG).show();
                                                            } else
                                                                try {


                                                                    JSONObject json = new JSONObject(response);

                                                                    //now get your  json array like this
                                                                    JSONArray jsonArray = json.getJSONArray("ROWSET");


                                                                    if (jsonArray.length() > 0) {

                                                                        for (int countItem = 0; countItem < jsonArray.length(); countItem++) {

                                                                            //JSONObject jsonObject = jsonArray.getJSONObject(countItem);

                                                                            //String STATUS = jsonObject.isNull("STATUS") ? "" : jsonObject.optString("STATUS");
                                                                            //String REMARKS = jsonObject.isNull("REMARKS") ? "" : jsonObject.optString("REMARKS");

                                                                            //Toast.makeText(getApplicationContext(),"Data Updated... Kindly Sync Again To Get Updated Data...", Toast.LENGTH_LONG).show();
                                                                            //showAlertSuccess(REMARKS);

                                                                            sdeConfiguredLm_sqLiteHelper.deleteRowLMWorkList(ASSLMCODE);
                                                                            SdeFieldList_SQLiteHelper sdeFieldList_sqLiteHelper = new SdeFieldList_SQLiteHelper(SdeFieldListGetAssignedReport_Activity.this);

                                                                            String lmc_assigned_to = " ";
                                                                            String lmc_assigned_text = " ";
                                                                            String assigned_flag = " ";
                                                                            String sync_status_flag = "2";

                                                                            sdeFieldList_sqLiteHelper.clearAssignedData(ASSLMCODE,lmc_assigned_to,lmc_assigned_text,assigned_flag,sync_status_flag);
                                                                            readFromLocalStorage();
                                                                            if (dialog.isShowing()) {
                                                                                dialog.dismiss();
                                                                            }

                                                                            if (alertDialog != null) {
                                                                                alertDialog.dismiss();
                                                                            }
                                                                        }

                                                                        //getData();

                                                                    } else {

                                                                        if (alertDialog != null) {
                                                                            alertDialog.dismiss();
                                                                        }

                                                                        Toast.makeText(getApplicationContext(), "Error Processing Request...", Toast.LENGTH_LONG).show();


                                                                    }

                                                                } catch (Throwable e) {
                                                                    e.printStackTrace();
                                                                }


                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {

                                                            //Toast.makeText(getApplicationContext(),String.valueOf(mStatusCode),Toast.LENGTH_LONG).show();
                                                            Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                                            if (alertDialog != null) {
                                                                alertDialog.dismiss();
                                                            }

                                                        }
                                                    }) {
                                                @Override
                                                public Map<String, String> getHeaders() {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("EKEY", "b28272183c64fcb45b11d9098a7dd97df51f89bc1bae9448e4126258fd9446d1");
                                                    return params;
                                                }

                                            };


                                            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(sr);
                                        }


                                    }
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), "No Pending Data To Sync With Server...", Snackbar.LENGTH_LONG).show();
                                    if (alertDialog != null) {
                                        alertDialog.dismiss();
                                    }
                                }


                            } else {
                                if (alertDialog != null) {
                                    alertDialog.dismiss();
                                }
                                Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
                            }


                        }
                    });

                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.test));

                    return false;
                }
            });



            /*imageViewDeleteDeAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String flag = "D";
                    String updatedFlag = " ";

                    SdeConfiguredLm_SQLiteHelper dbBackend = new SdeConfiguredLm_SQLiteHelper(SdeFieldListGetAssignedReport_Activity.this);
                    dbBackend.updateLocalDBRemoveDFlag(flag,updatedFlag);
                    Toast.makeText(getApplicationContext(),"Deassignment Cancelled !!!",Toast.LENGTH_LONG).show();

                }
            });*/

        }

        dialog.show();
        cursor.close();
        sdeConfiguredLm_sqLiteHelper.close();
    }





    /*private void submitDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(SdeFieldListGetAssignedReport_Activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.data_submit_registration_page);
        alertDialog = alert.create();
        alertDialog.show();
    }*/

    private void submitDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(SdeFieldListGetAssignedReport_Activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.data_deassignment);
        alertDialog = alert.create();
        alertDialog.show();
    }


    private void retrieveDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(SdeFieldListGetAssignedReport_Activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.data_fetching);
        alertDialog = alert.create();
        alertDialog.show();
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /*private void onRefresh() {
        Intent intent = new Intent(SdeFieldListGetAssignedReport_Activity.this, SdeFieldListGetAssignedReport_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 0);
        overridePendingTransition(0, 0);
        finishAffinity();
    }*/

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(SdeFieldListGetAssignedReport_Activity.this, SdeFieldList_Activity.class);
        startActivity(intent);
        finishAffinity();
    }
}

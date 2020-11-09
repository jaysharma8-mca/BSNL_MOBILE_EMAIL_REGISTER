package com.mobileemail_register.LINEMAN_WL_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mobileemail_register.MISC.RequestHandler;
import com.mobileemail_register.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Co_Approved_List_activity extends AppCompatActivity implements Co_List_Adapter.ContactsAdapterListener, View.OnClickListener {

    private ArrayList<Co_List_ReportClass> co_list_reportClasses = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    public static Co_List_Adapter mAdapter;

    private AlertDialog alertDialog;

    private Co_Approved_List_SQLiteHelper co_approved_list_sqLiteHelper;

    private List<String> list1;
    private List<String> list2;
    private List<String> list3;
    private List<String> list4;
    private List<String> list5;
    private List<String> list6;
    private List<String> list7;
    private List<String> list8;
    private List<String> list9;
    private List<String> list10;
    private List<String> list11;
    private List<String> list12;
    private List<String> list13;
    private List<String> list14;
    private List<String> list15;
    private List<String> list16;
    private List<String> list17;

    private static final String SP_MEREG = "SP_MEREG";

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_approved_list_activity);

        AppCenter.start(getApplication(), "15ee0d52-7eb0-4aa2-9ec5-40b1491add0e",
                Analytics.class, Crashes.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BHARAT SANCHAR NIGAM LTD.");
        setSupportActionBar(toolbar);


        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        toolbar1.setTitle("APPROVED WL");
        setSupportActionBar(toolbar1);

        tv = new TextView(this);

        co_approved_list_sqLiteHelper = new Co_Approved_List_SQLiteHelper(getApplicationContext());
        final String[] myData = co_approved_list_sqLiteHelper.SelectAllTableData();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        co_list_reportClasses = new ArrayList<>();
        mAdapter = new Co_List_Adapter(this, co_list_reportClasses, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if (myData == null) {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Kindly Synchronize To Populate The Data!!!", Snackbar.LENGTH_LONG).show();
        }

        else
        {
            readFromLocalStorage();
        }
    }

    private void sqlite() {


        co_approved_list_sqLiteHelper.deleteUsers();

        if (list1 != null && list2 != null && list3 != null && list4 != null &&
                list5 != null && list6 != null && list7 != null && list8 != null &&
                list9 != null && list10 != null && list11 != null && list12 != null &&
                list13 != null && list14 != null && list15 != null && list16 != null
                && list17 != null) {
            for (int i = 0; i < list15.size(); i++) {

                co_approved_list_sqLiteHelper.addUser(list1.get(i), list2.get(i), list3.get(i), list4.get(i), list5.get(i),
                        list6.get(i), list7.get(i), list8.get(i), list9.get(i), list10.get(i), list11.get(i),
                        list12.get(i), list13.get(i), list14.get(i), list15.get(i), list16.get(i), list17.get(i));

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


        co_approved_list_sqLiteHelper.close();
    }

    @SuppressLint("SetTextI18n")
    public void readFromLocalStorage() {

        co_approved_list_sqLiteHelper.open();

        co_list_reportClasses.clear();
        Co_Approved_List_SQLiteHelper co_approved_list_sqLiteHelper = new Co_Approved_List_SQLiteHelper(this);
        SQLiteDatabase database = co_approved_list_sqLiteHelper.getReadableDatabase();

        Cursor cursor = co_approved_list_sqLiteHelper.readFromLocalDatabase(database);

        while (cursor.moveToNext()) {


            String SSA = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.SSA));
            String CUST_ACCNT_NO = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.CUST_ACCNT_NO));
            String GO_GREEN = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.GO_GREEN));
            String STATUS_FLAG = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.STATUS_FLAG));

            String PHONE_NO = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.PHONE_NO));
            String BILL_ACCNT_NO = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.BILL_ACCNT_NO));
            String UPDATED_ON = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.UPDATED_ON));
            String CIRCLE = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.CIRCLE));

            String CO_APPROVED_DATE = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.CO_APPROVED_DATE));
            String MOBILE_NO = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.MOBILE_NO));
            String UPDATED_BY = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.UPDATED_BY));
            String UPDATE_TYPE = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.UPDATE_TYPE));

            String EXCHANGE_CODE = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.EXCHANGE_CODE));
            String EMAIL_ID = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.EMAIL_ID));
            String SUBMIT_STAT = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.SUBMIT_STAT));
            String APPROVED_BY = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.APPROVED_BY));

            String CO_REMARKS = cursor.getString(cursor.getColumnIndex(Co_Pending_List_SQLiteHelper.CO_REMARKS));



            co_list_reportClasses.add(new Co_List_ReportClass(SSA, CUST_ACCNT_NO,GO_GREEN,STATUS_FLAG,PHONE_NO,
                    BILL_ACCNT_NO,UPDATED_ON,CIRCLE,CO_APPROVED_DATE,MOBILE_NO,UPDATED_BY,UPDATE_TYPE,EXCHANGE_CODE,
                    EMAIL_ID,SUBMIT_STAT,APPROVED_BY,CO_REMARKS));

            int count;
            if (mAdapter != null) {
                count = mAdapter.getItemCount();

                tv.setText("" + count);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
                tv.setOnClickListener(Co_Approved_List_activity.this);
                tv.setPadding(5, 0, 5, 0);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setTextSize(18);
            }


        }

        mAdapter.notifyDataSetChanged();
        cursor.close();
        co_approved_list_sqLiteHelper.close();
    }

    private void fetchPendingList() {

        SharedPreferences sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);

        final String PERSONAL_NUMBER = "PERSONAL_NUMBER";
        final String PERNOLM = "PERNOLM";
        final String SDELMWL = "SDELMWL";
        final String personal_number = sharedpreferences.getString(PERSONAL_NUMBER, null);
        final String pernolm = sharedpreferences.getString(PERNOLM, null);
        final String sdewl = sharedpreferences.getString(SDELMWL, null);

//Toast.makeText(getApplicationContext(),"Else Part",Toast.LENGTH_LONG).show();
        if(checkInternetConnection()) {
            if (sdewl != null) {
                retrieveDetails();

                String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/costatuslist/updatedby/" + pernolm + "/flag/Y";
                URL = URL.replaceAll(" ", "%20");

                //Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_LONG).show();

                StringRequest sr = new StringRequest(Request.Method.GET, URL,
                        response -> {

                            try {

                                JSONObject json = new JSONObject(response);
                                if (json.getString("STATUS").contentEquals("SUCCESS")) {
                                    JSONArray jsonArray = json.getJSONArray("ROWSET");

                                    list1 = new ArrayList<>();
                                    list2 = new ArrayList<>();
                                    list3 = new ArrayList<>();
                                    list4 = new ArrayList<>();
                                    list5 = new ArrayList<>();
                                    list6 = new ArrayList<>();
                                    list7 = new ArrayList<>();
                                    list8 = new ArrayList<>();
                                    list9 = new ArrayList<>();
                                    list10 = new ArrayList<>();
                                    list11 = new ArrayList<>();
                                    list12 = new ArrayList<>();
                                    list13 = new ArrayList<>();
                                    list14 = new ArrayList<>();
                                    list15 = new ArrayList<>();
                                    list16 = new ArrayList<>();
                                    list17 = new ArrayList<>();


                                    if (jsonArray.length() > 0) {

                                        for (int countItem = 0; countItem < jsonArray.length(); countItem++) {

                                            JSONObject jsonObject = jsonArray.getJSONObject(countItem);


                                            list1.add(jsonObject.isNull("SSA") ? "" : jsonObject.optString("SSA"));
                                            list2.add(jsonObject.isNull("CUST_ACCNT_NO") ? "" : jsonObject.optString("CUST_ACCNT_NO"));
                                            list3.add(jsonObject.isNull("GO_GREEN") ? "" : jsonObject.optString("GO_GREEN"));
                                            list4.add(jsonObject.isNull("STATUS_FLAG") ? "" : jsonObject.optString("STATUS_FLAG"));

                                            list5.add(jsonObject.isNull("PHONE_NO") ? "" : jsonObject.optString("PHONE_NO"));
                                            list6.add(jsonObject.isNull("BILL_ACCNT_NO") ? "" : jsonObject.optString("BILL_ACCNT_NO"));
                                            list7.add(jsonObject.isNull("UPDATED_ON") ? "" : jsonObject.optString("UPDATED_ON"));
                                            list8.add(jsonObject.isNull("CIRCLE") ? "" : jsonObject.optString("CIRCLE"));

                                            list9.add(jsonObject.isNull("CO_APPROVED_DATE") ? "" : jsonObject.optString("CO_APPROVED_DATE"));
                                            list10.add(jsonObject.isNull("MOBILE_NO") ? "" : jsonObject.optString("MOBILE_NO"));
                                            list11.add(jsonObject.isNull("UPDATED_BY") ? "" : jsonObject.optString("UPDATED_BY"));
                                            list12.add(jsonObject.isNull("UPDATE_TYPE") ? "" : jsonObject.optString("UPDATE_TYPE"));

                                            list13.add(jsonObject.isNull("EXCHANGE_CODE") ? "" : jsonObject.optString("EXCHANGE_CODE"));
                                            list14.add(jsonObject.isNull("EMAIL_ID") ? "" : jsonObject.optString("EMAIL_ID"));
                                            list15.add(jsonObject.isNull("SUBMIT_STAT") ? "" : jsonObject.optString("SUBMIT_STAT"));
                                            list16.add(jsonObject.isNull("APPROVED_BY") ? "" : jsonObject.optString("APPROVED_BY"));

                                            list17.add(jsonObject.isNull("CO_REMARKS") ? "" : jsonObject.optString("CO_REMARKS"));

                                            sqlite();
                                            if (alertDialog != null) {
                                                alertDialog.dismiss();
                                            }


                                            readFromLocalStorage();
                                            co_approved_list_sqLiteHelper.close();

                                        }

                                    }

                                } else if (json.getString("STATUS").contentEquals("FAILED")) {
                                    if (alertDialog != null) {
                                        alertDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "NO DATA FOUND !!!", Toast.LENGTH_LONG).show();
                                    co_approved_list_sqLiteHelper.deleteUsers();
                                    co_approved_list_sqLiteHelper.close();
                                    readFromLocalStorage();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {

                            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            if (alertDialog != null) {
                                alertDialog.dismiss();
                            }


                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("EKEY", "b28272183c64fcb45b11d9098a7dd97df51f89bc1bae9448e4126258fd9446d1");

                        return params;
                    }
                };

                sr.setRetryPolicy(new DefaultRetryPolicy(
                        50000,
                        0,
                        0));
                RequestHandler.getInstance(this).addToRequestQueue(sr);
            } else {
                retrieveDetails();

                String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/costatuslist/updatedby/" + personal_number + "/flag/Y";
                URL = URL.replaceAll(" ", "%20");

                //Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_LONG).show();

                StringRequest sr = new StringRequest(Request.Method.GET, URL,
                        response -> {

                            try {

                                JSONObject json = new JSONObject(response);
                                if (json.getString("STATUS").contentEquals("SUCCESS")) {
                                    JSONArray jsonArray = json.getJSONArray("ROWSET");

                                    list1 = new ArrayList<>();
                                    list2 = new ArrayList<>();
                                    list3 = new ArrayList<>();
                                    list4 = new ArrayList<>();
                                    list5 = new ArrayList<>();
                                    list6 = new ArrayList<>();
                                    list7 = new ArrayList<>();
                                    list8 = new ArrayList<>();
                                    list9 = new ArrayList<>();
                                    list10 = new ArrayList<>();
                                    list11 = new ArrayList<>();
                                    list12 = new ArrayList<>();
                                    list13 = new ArrayList<>();
                                    list14 = new ArrayList<>();
                                    list15 = new ArrayList<>();
                                    list16 = new ArrayList<>();
                                    list17 = new ArrayList<>();


                                    if (jsonArray.length() > 0) {

                                        for (int countItem = 0; countItem < jsonArray.length(); countItem++) {

                                            JSONObject jsonObject = jsonArray.getJSONObject(countItem);


                                            list1.add(jsonObject.isNull("SSA") ? "" : jsonObject.optString("SSA"));
                                            list2.add(jsonObject.isNull("CUST_ACCNT_NO") ? "" : jsonObject.optString("CUST_ACCNT_NO"));
                                            list3.add(jsonObject.isNull("GO_GREEN") ? "" : jsonObject.optString("GO_GREEN"));
                                            list4.add(jsonObject.isNull("STATUS_FLAG") ? "" : jsonObject.optString("STATUS_FLAG"));

                                            list5.add(jsonObject.isNull("PHONE_NO") ? "" : jsonObject.optString("PHONE_NO"));
                                            list6.add(jsonObject.isNull("BILL_ACCNT_NO") ? "" : jsonObject.optString("BILL_ACCNT_NO"));
                                            list7.add(jsonObject.isNull("UPDATED_ON") ? "" : jsonObject.optString("UPDATED_ON"));
                                            list8.add(jsonObject.isNull("CIRCLE") ? "" : jsonObject.optString("CIRCLE"));

                                            list9.add(jsonObject.isNull("CO_APPROVED_DATE") ? "" : jsonObject.optString("CO_APPROVED_DATE"));
                                            list10.add(jsonObject.isNull("MOBILE_NO") ? "" : jsonObject.optString("MOBILE_NO"));
                                            list11.add(jsonObject.isNull("UPDATED_BY") ? "" : jsonObject.optString("UPDATED_BY"));
                                            list12.add(jsonObject.isNull("UPDATE_TYPE") ? "" : jsonObject.optString("UPDATE_TYPE"));

                                            list13.add(jsonObject.isNull("EXCHANGE_CODE") ? "" : jsonObject.optString("EXCHANGE_CODE"));
                                            list14.add(jsonObject.isNull("EMAIL_ID") ? "" : jsonObject.optString("EMAIL_ID"));
                                            list15.add(jsonObject.isNull("SUBMIT_STAT") ? "" : jsonObject.optString("SUBMIT_STAT"));
                                            list16.add(jsonObject.isNull("APPROVED_BY") ? "" : jsonObject.optString("APPROVED_BY"));

                                            list17.add(jsonObject.isNull("CO_REMARKS") ? "" : jsonObject.optString("CO_REMARKS"));

                                            sqlite();
                                            if (alertDialog != null) {
                                                alertDialog.dismiss();
                                            }


                                            readFromLocalStorage();
                                            co_approved_list_sqLiteHelper.close();

                                        }

                                    }

                                } else if (json.getString("STATUS").contentEquals("FAILED")) {
                                    if (alertDialog != null) {
                                        alertDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "NO DATA FOUND !!!", Toast.LENGTH_LONG).show();
                                    co_approved_list_sqLiteHelper.deleteUsers();
                                    co_approved_list_sqLiteHelper.close();
                                    readFromLocalStorage();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {

                            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            if (alertDialog != null) {
                                alertDialog.dismiss();
                            }


                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("EKEY", "b28272183c64fcb45b11d9098a7dd97df51f89bc1bae9448e4126258fd9446d1");

                        return params;
                    }
                };

                sr.setRetryPolicy(new DefaultRetryPolicy(
                        50000,
                        0,
                        0));
                RequestHandler.getInstance(this).addToRequestQueue(sr);
            }

        }

        else
        {
            Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
            if(alertDialog != null)
            {
                alertDialog.dismiss();
            }
        }
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void retrieveDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(Co_Approved_List_activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.data_fetching);
        alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public void onContactSelected(Co_List_ReportClass co_list_reportClass) {


    }

    @Override
    public void onClick(View view) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sdefieldlist_menu, menu);

        menu.add(0, 0, 1,"").setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {

            fetchPendingList();

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Co_Approved_List_activity.this,Co_List_Activity.class);
        startActivity(intent);
        finishAffinity();
    }
}

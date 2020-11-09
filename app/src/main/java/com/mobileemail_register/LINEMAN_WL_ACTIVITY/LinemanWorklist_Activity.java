package com.mobileemail_register.LINEMAN_WL_ACTIVITY;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mobileemail_register.R;
import com.mobileemail_register.SEARCH_SUBSCRIBER_ACTIVITY.SearchSubscriber_Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;


@SuppressWarnings({"Convert2Lambda"})
public class LinemanWorklist_Activity extends AppCompatActivity implements LinemanWorklist_Adapter.ContactsAdapterListener, View.OnClickListener {

    private ArrayList<LinemanWorklist_ReportClass> linemanWorklist_reportClasses = new ArrayList<>();


    private LinemanWorklist_Adapter mAdapter;
    RecyclerView recyclerView;

    private AlertDialog alert;

    private TextView tv;
    private TextView Sorting_Status;

    private static final String SP_MEREG = "SP_MEREG";
    private static final String DATE_TIME_SYNC = "DATE_TIME_SYNC";
    private static final String LINEMAN_WL = "LINEMAN_WL";
    private static final String CUSTOMER_RANDOM_STRING = "CUSTOMER_RANDOM_STRING";
    private static final String PILLAR_CODE = "PILLAR_CODE";
    private static final String PHONE_STATUS = "PHONE_STATUS";
    private SharedPreferences sharedpreferences;

    private final String[] spinnerValues = {"SELECT PILLAR CODE"};
    private final String[] spinnerValuesPhoneStatus = {"SELECT PHONE STATUS CODE"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineman_worklist);

        AppCenter.start(getApplication(), "f7271b3f-27aa-4985-8654-37f1347f4bf4",
                Analytics.class, Crashes.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BHARAT SANCHAR NIGAM LTD.");
        setSupportActionBar(toolbar);


        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        toolbar1.setTitle("LM PENDING WL");
        setSupportActionBar(toolbar1);


        recyclerView = findViewById(R.id.recycler_view);
        linemanWorklist_reportClasses = new ArrayList<>();
        mAdapter = new LinemanWorklist_Adapter(linemanWorklist_reportClasses, this);
        tv = new TextView(this);
        Sorting_Status = new TextView(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        final Linemanworklist_SQLiteHelper dbHelper = new Linemanworklist_SQLiteHelper(this);
        final String[] myData = dbHelper.SelectAllData();

        sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String SDELMWL = "SDELMWL";
        final String PILLAR_CODE = "PILLAR_CODE";
        final String PHONE_STATUS = "PHONE_STATUS";
        final String sdelmwl = sharedpreferences.getString(SDELMWL, null);
        final String pillar_code = sharedpreferences.getString(PILLAR_CODE, null);
        final String phone_status = sharedpreferences.getString(PHONE_STATUS, null);


            if (myData == null) {
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Kindly Synchronize To Populate The Data!!!", Snackbar.LENGTH_LONG).show();
            }

            else
            {
                readFromLoacalStorage();
            }



        if(pillar_code != null)
        {
            readFromLoacalStoragePillarCodeData();
            Sorting_Status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
            Sorting_Status.setOnClickListener(LinemanWorklist_Activity.this);
            Sorting_Status.setPadding(5, 0, 5, 0);
            Sorting_Status.setTypeface(null, Typeface.BOLD);
            Sorting_Status.setTextSize(18);
            Sorting_Status.setText(pillar_code);
        }

        else if(phone_status != null)
        {
            readFromLocalDatabasePhoneStatus();
            Sorting_Status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
            Sorting_Status.setOnClickListener(LinemanWorklist_Activity.this);
            Sorting_Status.setPadding(15, 0, 5, 0);
            Sorting_Status.setTypeface(null, Typeface.BOLD);
            Sorting_Status.setTextSize(18);
            Sorting_Status.setText(phone_status);
        }

        else
        {
            readFromLoacalStorage();
            Sorting_Status.setText("");
        }


    }

    /*private void sqlite() {

        sqlite_obj.open();

        sqlite_obj.deleteUsers();

        if (list1 != null && list2 != null && list3 != null && list4 != null &&
                list5 != null && list6 != null && list7 != null && list8 != null &&
                list9 != null && list10 != null && list11 != null && list12 != null &&
                list13 != null && list14 != null && list15 != null) {
            for (int i = 0; i < list15.size(); i++) {

                sqlite_obj.addUser(list1.get(i), list2.get(i), list3.get(i), list4.get(i), list5.get(i), list6.get(i), list7.get(i), list8.get(i), list9.get(i), list10.get(i), list11.get(i), list12.get(i), list13.get(i), list14.get(i), list15.get(i), list16.get(i));

                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Data Synchronized!!!", Snackbar.LENGTH_LONG)
                        *//*.setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))*//*
                        .show();
            }
        } else {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "No Data To Display!!!", Snackbar.LENGTH_LONG).show();
        }


        sqlite_obj.close();
    }*/

    @SuppressLint("SetTextI18n")
    private void readFromLoacalStorage() {
        linemanWorklist_reportClasses.clear();
        Linemanworklist_SQLiteHelper dbHelper = new Linemanworklist_SQLiteHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = dbHelper.readFromLocalDatabase(database);

        while (cursor.moveToNext()) {


            String sdeCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.sdeCode));
            String address = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.address));
            String lmCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.lmCode));
            String mobile = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.mobile));

            String emailId = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.emailId));
            String phoneNo = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.phoneNo));
            String customerName = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.customerName));
            String stdCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.stdCode));

            String jtoCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.jtoCode));
            String osAmount = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.osAmount));
            String areaCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.areaCode));
            String serviceOperStatus = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.serviceOperStatus));

            String customerCategory = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.customerCategory));
            String exchangeCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.exchangeCode));
            String goGreenEmail = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.goGreenEmail));
            String randomString = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.RANDOM_STRING));


            linemanWorklist_reportClasses.add(new LinemanWorklist_ReportClass(sdeCode, address, lmCode, mobile,
                    emailId, phoneNo, customerName, stdCode,
                    jtoCode, osAmount, areaCode, serviceOperStatus, customerCategory,
                    exchangeCode, goGreenEmail, randomString));

            int count;
            if (mAdapter != null) {
                count = mAdapter.getItemCount();

                tv.setText("" + count);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
                tv.setOnClickListener(LinemanWorklist_Activity.this);
                tv.setPadding(5, 0, 5, 0);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setTextSize(18);

                Sorting_Status.setText("");
            }

        }

        mAdapter.notifyDataSetChanged();
        cursor.close();
        dbHelper.close();



    }




    /*private void LM_WROKLIST_FETCH() {

        sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);

        final String PERSONAL_NUMBER = "PERSONAL_NUMBER";
        final String PERNOLM = "PERNOLM";
        final String SDELMWL = "SDELMWL";
        final String personal_number = sharedpreferences.getString(PERSONAL_NUMBER, null);
        final String pernolm = sharedpreferences.getString(PERNOLM, null);
        final String sdewl = sharedpreferences.getString(SDELMWL, null);


        if(sdewl != null)
        {
            //Toast.makeText(getApplicationContext(),"Else Part",Toast.LENGTH_LONG).show();
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(LinemanWorklist_Activity.this);
            alert.setView(alertLayout);
            alert.setCancelable(false);
            final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
            textViewAlertDialog.setText(R.string.loading_data_please_wait);
            AlertDialog dialog = alert.create();
            dialog.show();

            String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/pendingworklist/username/"+pernolm+"";
            URL = URL.replaceAll(" ", "%20");

            //Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_LONG).show();

            StringRequest sr = new StringRequest(Request.Method.GET, URL,
                    response -> {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("STATUS").contentEquals("SUCCESS")) {
                                JSONArray Jarray = jsonObject.getJSONArray("ROWSET");
                                JSONObject jo;


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


                                for (int i = 0; i < Jarray.length(); i++) {

                                    jo = Jarray.getJSONObject(i);

                                    list1.add(jo.getString("sdeCode"));


                                    list2.add(jo.getString("address"));


                                    list3.add(jo.getString("lmCode"));


                                    list4.add(jo.getString("mobile"));


                                    list5.add(jo.getString("emailId"));


                                    list6.add(jo.getString("phoneNo"));


                                    list7.add(jo.getString("customerName"));


                                    list8.add(jo.getString("stdCode"));


                                    list9.add(jo.getString("jtoCode"));


                                    list10.add(jo.getString("osAmount"));


                                    list11.add(jo.getString("areaCode"));


                                    list12.add(jo.getString("serviceOperStatus"));


                                    list13.add(jo.getString("customerCategory"));


                                    list14.add(jo.getString("exchangeCode"));


                                    list15.add(jo.getString("goGreenEmail"));

                                    list16.add(String.valueOf(StrictMath.abs(random() % 50000)));


                                    //onRefresh();


                                }
                                sqlite();
                                dialog.dismiss();
                                readFromLoacalStorage();
                                sqlite_obj.close();


                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
                                final String currentDateandTime = simpleDateFormat.format(new Date());

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString(DATE_TIME_SYNC, currentDateandTime);
                                editor.apply();


                                int profile_counts = (int) sqlite_obj.getProfilesCount();
                                String total_counts = String.valueOf(profile_counts);

                                tv.setText(total_counts);
                                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
                                tv.setOnClickListener(LinemanWorklist_Activity.this);
                                tv.setPadding(5, 0, 5, 0);
                                tv.setTypeface(null, Typeface.BOLD);
                                tv.setTextSize(18);


                            } else {


                                sqlite();
                                readFromLoacalStorage();
                                dialog.dismiss();
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(DATE_TIME_SYNC, "Data Not Synchronized!!!");
                                editor.apply();

                                Toast.makeText(getApplicationContext(), "No Data Found !!!", Toast.LENGTH_SHORT).show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(DATE_TIME_SYNC, "Data Not Synchronized!!!");
                        editor.apply();

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


        else
        {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(LinemanWorklist_Activity.this);
            alert.setView(alertLayout);
            alert.setCancelable(false);
            final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
            textViewAlertDialog.setText(R.string.loading_data_please_wait);
            AlertDialog dialog = alert.create();
            dialog.show();

            String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/pendingworklist/username/"+personal_number+"";
            URL = URL.replaceAll(" ", "%20");

            //Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_LONG).show();

            StringRequest sr = new StringRequest(Request.Method.GET, URL,
                    response -> {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("STATUS").contentEquals("SUCCESS")) {
                                JSONArray Jarray = jsonObject.getJSONArray("ROWSET");
                                JSONObject jo;


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


                                for (int i = 0; i < Jarray.length(); i++) {

                                    jo = Jarray.getJSONObject(i);

                                    list1.add(jo.getString("sdeCode"));


                                    list2.add(jo.getString("address"));


                                    list3.add(jo.getString("lmCode"));


                                    list4.add(jo.getString("mobile"));


                                    list5.add(jo.getString("emailId"));


                                    list6.add(jo.getString("phoneNo"));


                                    list7.add(jo.getString("customerName"));


                                    list8.add(jo.getString("stdCode"));


                                    list9.add(jo.getString("jtoCode"));


                                    list10.add(jo.getString("osAmount"));


                                    list11.add(jo.getString("areaCode"));


                                    list12.add(jo.getString("serviceOperStatus"));


                                    list13.add(jo.getString("customerCategory"));


                                    list14.add(jo.getString("exchangeCode"));


                                    list15.add(jo.getString("goGreenEmail"));

                                    list16.add(String.valueOf(StrictMath.abs(random() % 50000)));


                                    //onRefresh();


                                }
                                sqlite();
                                dialog.dismiss();
                                readFromLoacalStorage();
                                sqlite_obj.close();


                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
                                final String currentDateandTime = simpleDateFormat.format(new Date());

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString(DATE_TIME_SYNC, currentDateandTime);
                                editor.apply();


                                int profile_counts = (int) sqlite_obj.getProfilesCount();
                                String total_counts = String.valueOf(profile_counts);

                                tv.setText(total_counts);
                                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
                                tv.setOnClickListener(LinemanWorklist_Activity.this);
                                tv.setPadding(5, 0, 5, 0);
                                tv.setTypeface(null, Typeface.BOLD);
                                tv.setTextSize(18);


                            } else {


                                sqlite();
                                readFromLoacalStorage();
                                dialog.dismiss();
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(DATE_TIME_SYNC, "Data Not Synchronized!!!");
                                editor.apply();

                                Toast.makeText(getApplicationContext(), "No Data Found !!!", Toast.LENGTH_SHORT).show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(DATE_TIME_SYNC, "Data Not Synchronized!!!");
                        editor.apply();

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

    }*/

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


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

        menu.add(0, 0, 1,"").setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 0, 1,"").setActionView(Sorting_Status).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        /*int profile_counts = (int) sqlite_obj.getProfilesCount();
        String counts = String.valueOf(profile_counts);


        if(counts != null)
        {
            tv.setText("" + profile_counts);
            tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
            tv.setOnClickListener(LinemanWorklist_Activity.this);
            tv.setPadding(5, 0, 5, 0);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextSize(18);
            menu.add(0, 0, 1,"").setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        sqlite_obj.close();*/



        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                tv.setVisibility(View.INVISIBLE);

                break;


            case R.id.action_add:

                readFromLoacalStorage();
                sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove("PHONE_STATUS");
                editor.remove("PILLAR_CODE");
                editor.apply();

                break;

            /*case R.id.action_refresh:

                if(checkInternetConnection()){



                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Syncing Data... Please Wait!!!", Snackbar.LENGTH_LONG).show();

                    LM_WROKLIST_FETCH();
                }

                else
                {
                    Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
                }


                break;*/


            case R.id.sort_data:

                final AlertDialog.Builder build = new AlertDialog.Builder(LinemanWorklist_Activity.this);

                build.setTitle("SORTING OPTION MENU");
                build.setIcon(R.drawable.bsnl_me_reg_icon);


                final String[] siren_options = new String[]{
                        "SORT USING PHONE STATUS",
                        "SORT DATA PILLARWISE"
                };

                // Item click listener
                build.setSingleChoiceItems(siren_options, // Items list
                        -1, // Index of checked item (-1 = no selection)
                        (dialogInterface, i) -> {
                            // Get the alert dialog selected item's text
                            String selectedItem = Arrays.asList(siren_options).get(i);

                            if (selectedItem.equals("SORT USING PHONE STATUS")) {

                                alert.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                AlertDialog dialog = builder.create();
                                LayoutInflater inflater = getLayoutInflater();
                                @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.custom_dialog_sorting, null);
                                dialog.setView(dialogLayout);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                Spinner spinner_sort_pillar_wise = dialogLayout.findViewById(R.id.spinner_sort_pillar_wise);

                                Linemanworklist_SQLiteHelper dbBackend = new Linemanworklist_SQLiteHelper(LinemanWorklist_Activity.this);

                                String [] spinnerLists = dbBackend.SelectAllDataPhoneStatus();

                                if (spinnerLists != null) {
                                    String[] both = Stream.concat(Arrays.stream(spinnerValuesPhoneStatus), Arrays.stream(spinnerLists)).toArray(String[]::new);

                                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                            getApplicationContext(), R.layout.custome_spinner_configurelm, both) {
                                        @Override
                                        public boolean isEnabled(int position) {
                                            return position != 0;
                                        }

                                        @Override
                                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                            View view = super.getDropDownView(position, convertView, parent);
                                            TextView tv = (TextView) view;

                                            if (position == 0) {
                                                tv.setTextColor(Color.GRAY);

                                            } else {
                                                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorText));
                                            }
                                            return view;
                                        }
                                    };
                                    spinnerArrayAdapter.setDropDownViewResource(R.layout.custome_spinner_configurelm);


                                    spinner_sort_pillar_wise.setAdapter(spinnerArrayAdapter);
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), "Kindly Sync to load the data...", Snackbar.LENGTH_LONG).show();
                                }


                                    spinner_sort_pillar_wise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            if (position > 0) {
                                                String selectedItemText = (String) parent.getItemAtPosition(position);


                                                    sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                                                    final SharedPreferences.Editor editor = sharedpreferences.edit();
                                                    editor.putString(PHONE_STATUS,selectedItemText);
                                                    editor.remove("PILLAR_CODE");

                                                    editor.apply();

                                                    //Toast.makeText(getApplicationContext(),selectedItemText,Toast.LENGTH_LONG).show();

                                                    readFromLocalDatabasePhoneStatus();


                                                //Toast.makeText(getApplicationContext(),selectedItemText,Toast.LENGTH_LONG).show();


                                                dialog.dismiss();

                                            }

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                dialog.show();



                            } else if (selectedItem.equals("SORT DATA PILLARWISE")) {

                                alert.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                AlertDialog dialog = builder.create();
                                LayoutInflater inflater = getLayoutInflater();
                                @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.custom_dialog_sorting, null);
                                dialog.setView(dialogLayout);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                Spinner spinner_sort_pillar_wise = dialogLayout.findViewById(R.id.spinner_sort_pillar_wise);

                                Linemanworklist_SQLiteHelper dbBackend = new Linemanworklist_SQLiteHelper(LinemanWorklist_Activity.this);

                                String [] spinnerLists = dbBackend.SelectAllData();

                                if (spinnerLists != null) {
                                    String[] both = Stream.concat(Arrays.stream(spinnerValues), Arrays.stream(spinnerLists)).toArray(String[]::new);

                                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                            getApplicationContext(), R.layout.custome_spinner_configurelm, both) {
                                        @Override
                                        public boolean isEnabled(int position) {
                                            return position != 0;
                                        }

                                        @Override
                                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                            View view = super.getDropDownView(position, convertView, parent);
                                            TextView tv = (TextView) view;

                                            if (position == 0) {
                                                tv.setTextColor(Color.GRAY);

                                            } else {
                                                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorText));
                                            }
                                            return view;
                                        }
                                    };
                                    spinnerArrayAdapter.setDropDownViewResource(R.layout.custome_spinner_configurelm);


                                    spinner_sort_pillar_wise.setAdapter(spinnerArrayAdapter);
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), "Kindly Sync to load the data...", Snackbar.LENGTH_LONG).show();
                                }

                                    spinner_sort_pillar_wise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            if (position > 0) {
                                                String selectedItemText = (String) parent.getItemAtPosition(position);

                                                    sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                                                    final SharedPreferences.Editor editor = sharedpreferences.edit();
                                                    editor.putString(PILLAR_CODE,selectedItemText);
                                                    //editor.remove("PHONE_STATUS");
                                                    editor.apply();

                                                    readFromLoacalStoragePillarCodeData();


                                                //Toast.makeText(getApplicationContext(),selectedItemText,Toast.LENGTH_LONG).show();


                                                dialog.dismiss();

                                            }

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                dialog.show();

                            }
                        });

                build.setNegativeButton("Cancel", (dialogInterface, i) -> {
                    alert.dismiss();
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                });


                alert = build.create();
                alert.show();

                /**/


                break;


            default:
                break;
        }

        return true;
    }

    @SuppressLint("SetTextI18n")
    private void readFromLoacalStoragePillarCodeData() {

        sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String SDELMWL = "SDELMWL";
        final String PILLAR_CODE = "PILLAR_CODE";
        final String pillar_code = sharedpreferences.getString(PILLAR_CODE, null);

        linemanWorklist_reportClasses.clear();
        Linemanworklist_SQLiteHelper dbHelper = new Linemanworklist_SQLiteHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = dbHelper.readFromLocalDatabasePillarwiseData(database);

        if(cursor != null) {
            while (cursor.moveToNext()) {


                String sdeCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.sdeCode));
                String address = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.address));
                String lmCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.lmCode));
                String mobile = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.mobile));

                String emailId = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.emailId));
                String phoneNo = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.phoneNo));
                String customerName = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.customerName));
                String stdCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.stdCode));

                String jtoCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.jtoCode));
                String osAmount = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.osAmount));
                String areaCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.areaCode));
                String serviceOperStatus = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.serviceOperStatus));

                String customerCategory = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.customerCategory));
                String exchangeCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.exchangeCode));
                String goGreenEmail = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.goGreenEmail));
                String randomString = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.RANDOM_STRING));


                linemanWorklist_reportClasses.add(new LinemanWorklist_ReportClass(sdeCode, address, lmCode, mobile,
                        emailId, phoneNo, customerName, stdCode,
                        jtoCode, osAmount, areaCode, serviceOperStatus, customerCategory,
                        exchangeCode, goGreenEmail, randomString));

                int count;
                if (mAdapter != null) {
                    count = mAdapter.getItemCount();

                    tv.setText("" + count);
                    tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
                    tv.setOnClickListener(LinemanWorklist_Activity.this);
                    tv.setPadding(5, 0, 5, 0);
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setTextSize(18);

                    Sorting_Status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
                    Sorting_Status.setOnClickListener(LinemanWorklist_Activity.this);
                    Sorting_Status.setPadding(15, 0, 5, 0);
                    Sorting_Status.setTypeface(null, Typeface.BOLD);
                    Sorting_Status.setTextSize(18);
                    Sorting_Status.setText(pillar_code);
                }

            }

            mAdapter.notifyDataSetChanged();
            cursor.close();
            dbHelper.close();

        }

    }


    @SuppressLint("SetTextI18n")
    private void readFromLocalDatabasePhoneStatus() {

        sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String SDELMWL = "SDELMWL";
        final String PILLAR_CODE = "PILLAR_CODE";
        final String PHONE_STATUS = "PHONE_STATUS";
        final String phone_status = sharedpreferences.getString(PHONE_STATUS, null);

        linemanWorklist_reportClasses.clear();
        Linemanworklist_SQLiteHelper dbHelper = new Linemanworklist_SQLiteHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = dbHelper.readFromLocalDatabasePhoneStatus(database);

        if(cursor != null) {
            while (cursor.moveToNext()) {


                String sdeCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.sdeCode));
                String address = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.address));
                String lmCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.lmCode));
                String mobile = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.mobile));

                String emailId = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.emailId));
                String phoneNo = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.phoneNo));
                String customerName = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.customerName));
                String stdCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.stdCode));

                String jtoCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.jtoCode));
                String osAmount = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.osAmount));
                String areaCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.areaCode));
                String serviceOperStatus = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.serviceOperStatus));

                String customerCategory = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.customerCategory));
                String exchangeCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.exchangeCode));
                String goGreenEmail = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.goGreenEmail));
                String randomString = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.RANDOM_STRING));


                linemanWorklist_reportClasses.add(new LinemanWorklist_ReportClass(sdeCode, address, lmCode, mobile,
                        emailId, phoneNo, customerName, stdCode,
                        jtoCode, osAmount, areaCode, serviceOperStatus, customerCategory,
                        exchangeCode, goGreenEmail, randomString));

                int count;
                if (mAdapter != null) {
                    count = mAdapter.getItemCount();

                    tv.setText("" + count);
                    tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
                    tv.setOnClickListener(LinemanWorklist_Activity.this);
                    tv.setPadding(5, 0, 5, 0);
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setTextSize(18);

                    Sorting_Status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
                    Sorting_Status.setOnClickListener(LinemanWorklist_Activity.this);
                    Sorting_Status.setPadding(15, 0, 5, 0);
                    Sorting_Status.setTypeface(null, Typeface.BOLD);
                    Sorting_Status.setTextSize(18);
                    Sorting_Status.setText(phone_status);
                }

            }

            mAdapter.notifyDataSetChanged();
            cursor.close();
            dbHelper.close();

        }

    }



    @Override
    public void onContactSelected(LinemanWorklist_ReportClass lineman_worklist_report) {

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LINEMAN_WL,"LINEMAN_WL");
        editor.putString(CUSTOMER_RANDOM_STRING,lineman_worklist_report.getRandomString());
        editor.remove("CUSTOMER_MOBILENUMBER");
        editor.apply();


        if(!lineman_worklist_report.getEmailId().equals(" ") && !lineman_worklist_report.getEmailId().equals("NA"))
        {
            String[] separated = lineman_worklist_report.getEmailId().split("@");
            String mailId = separated[0].trim();
            mailId = mailId.toLowerCase();
            String domainName = separated[1].trim();
            domainName = domainName.toLowerCase();

            Intent intent = new Intent(LinemanWorklist_Activity.this, SearchSubscriber_Activity.class);
            intent.putExtra("areaCode", lineman_worklist_report.getStdCode());
            intent.putExtra("phoneNo", lineman_worklist_report.getPhoneNo());
            intent.putExtra("customerName", lineman_worklist_report.getCustomerName());
            intent.putExtra("address", lineman_worklist_report.getAddress());
            intent.putExtra("mailId", mailId);
            intent.putExtra("domainName", domainName);
            intent.putExtra("mobile", lineman_worklist_report.getMobile());
            startActivity(intent);
            finishAffinity();
        }


        else
        {
            Intent intent = new Intent(LinemanWorklist_Activity.this, SearchSubscriber_Activity.class);
            intent.putExtra("areaCode", lineman_worklist_report.getStdCode());
            intent.putExtra("phoneNo", lineman_worklist_report.getPhoneNo());
            intent.putExtra("customerName", lineman_worklist_report.getCustomerName());
            intent.putExtra("address", lineman_worklist_report.getAddress());
            intent.putExtra("mailId", "");
            intent.putExtra("domainName", "");
            intent.putExtra("mobile", lineman_worklist_report.getMobile());
            startActivity(intent);
            finishAffinity();
        }

    }






    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "Total Counts Of Data", Toast.LENGTH_LONG).show();
    }


    private boolean checkInternetConnection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return (networkInfo != null && networkInfo.isConnected());
    }



    @Override
    public void onBackPressed() {


            Intent intent = new Intent(LinemanWorklist_Activity.this, LinemanWorklistDashBoard_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finishAffinity();


    }

}

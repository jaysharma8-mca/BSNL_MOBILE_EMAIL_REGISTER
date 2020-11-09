package com.mobileemail_register.LINEMAN_WL_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.mobileemail_register.DASHBOARD_ACTIVITY.DashBoard_Activity;
import com.mobileemail_register.MISC.RequestHandler;
import com.mobileemail_register.R;
import com.mobileemail_register.SDEFIELDLIST_ACTIVITY.SdeFieldListConfigureLm_Activity;
import com.mobileemail_register.SDEFIELDLIST_ACTIVITY.SdeFieldList_SQLiteHelper;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static java.lang.Math.random;

public class LinemanWorklistDashBoard_Activity extends AppCompatActivity implements LinemanWorklistDashBoard_Adapter.ContactsAdapterListener, View.OnClickListener {

private ArrayList<LinemanWorklistDashBoard_ReportClass> linemanWorklistDashBoard_reportClassArrayList = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    public static LinemanWorklistDashBoard_Adapter mAdapter;

    private ImageView imageViewSync,imageViewMore;

    private TextView textViewClearAlert;
    Animation textViewAnimation;

    private String[] spinnerValues = {"SELECT HRMS NUMBER"};

    private Linemanworklist_SQLiteHelper sqlite_obj;
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


    private static final String SP_MEREG = "SP_MEREG";
    private static final String DATE_TIME_SYNC = "DATE_TIME_SYNC";
    private static final String ASSIGNED_LM_CODE = "ASSIGNED_LM_CODE";
    private static final String SYNC_BUTTON_ANIMATION_END = "SYNC_BUTTON_ANIMATION_END";
    private static final String PERNOLM = "PERNOLM";
    private SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineman_worklist_dash_board_);

        AppCenter.start(getApplication(), "15ee0d52-7eb0-4aa2-9ec5-40b1491add0e",
                Analytics.class, Crashes.class);

        sqlite_obj = new Linemanworklist_SQLiteHelper(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        linemanWorklistDashBoard_reportClassArrayList = new ArrayList<>();
        mAdapter = new LinemanWorklistDashBoard_Adapter(this, linemanWorklistDashBoard_reportClassArrayList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        imageViewSync = findViewById(R.id.imageViewSync);
        textViewClearAlert = findViewById(R.id.textViewClearAlert);
        imageViewMore = findViewById(R.id.imageViewMore);

        final Linemanworklist_SQLiteHelper dbHelper = new Linemanworklist_SQLiteHelper(this);
        final String[] myData = dbHelper.SelectAllTableData();

        sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String SYNC_BUTTON_ANIMATION_END = "SYNC_BUTTON_ANIMATION_END";
        final String sync_button_animation_end = sharedpreferences.getString(SYNC_BUTTON_ANIMATION_END, null);

        final Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);


        if(sync_button_animation_end != null)
        {

        }

        else
        {
            textViewAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_slide_in_left);
            imageViewSync.startAnimation(anim);

            textViewClearAlert.setVisibility(View.VISIBLE);
            textViewClearAlert.startAnimation(textViewAnimation);
        }



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


        sqlite_obj.close();
    }

    public void readFromLocalStorage() {
        linemanWorklistDashBoard_reportClassArrayList.clear();
        Linemanworklist_SQLiteHelper linemanworklist_sqLiteHelper = new Linemanworklist_SQLiteHelper(this);
        SQLiteDatabase database = linemanworklist_sqLiteHelper.getReadableDatabase();

        Cursor cursor = linemanworklist_sqLiteHelper.readFromLocalDatabaseLMWL(database);

        while (cursor.moveToNext()) {


            String lmCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.lmCode));
            String assigned_count = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.phoneNo));

            String areaCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.areaCode));
            String exchangeCode = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.exchangeCode));
            String customerCategory = cursor.getString(cursor.getColumnIndex(Linemanworklist_SQLiteHelper.customerCategory));



            linemanWorklistDashBoard_reportClassArrayList.add(new LinemanWorklistDashBoard_ReportClass(lmCode, assigned_count,areaCode,exchangeCode,customerCategory));

        }

        mAdapter.notifyDataSetChanged();
        cursor.close();
        linemanworklist_sqLiteHelper.close();
    }

    public void getDataFromServer(View view) {

        sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(SYNC_BUTTON_ANIMATION_END, "SYNC_BUTTON_ANIMATION_END");
        editor.apply();


        LM_WROKLIST_FETCH();

    }

    private void LM_WROKLIST_FETCH() {

        sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);

        final String PERSONAL_NUMBER = "PERSONAL_NUMBER";
        final String PERNOLM = "PERNOLM";
        final String SDELMWL = "SDELMWL";
        final String SYNC_BUTTON_ANIMATION_END = "SYNC_BUTTON_ANIMATION_END";
        final String personal_number = sharedpreferences.getString(PERSONAL_NUMBER, null);
        final String pernolm = sharedpreferences.getString(PERNOLM, null);
        final String sdewl = sharedpreferences.getString(SDELMWL, null);
        final String sync_button_animation_end = sharedpreferences.getString(SYNC_BUTTON_ANIMATION_END, null);


        if(sdewl != null)
        {
            if(sync_button_animation_end != null)
            {
                textViewClearAlert.clearAnimation();
                textViewClearAlert.setVisibility(View.GONE);
            }


            //Toast.makeText(getApplicationContext(),"Else Part",Toast.LENGTH_LONG).show();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation_animation);
            imageViewSync.startAnimation(animation);
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(LinemanWorklistDashBoard_Activity.this);
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
                                readFromLocalStorage();
                                sqlite_obj.close();
                                imageViewSync.clearAnimation();

                                //textViewAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_slide_out_right);


                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
                                final String currentDateandTime = simpleDateFormat.format(new Date());

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString(DATE_TIME_SYNC, currentDateandTime);
                                editor.apply();




                            } else {


                                sqlite();
                                readFromLocalStorage();
                                dialog.dismiss();
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(DATE_TIME_SYNC, "Data Not Synchronized!!!");
                                editor.apply();

                                Toast.makeText(getApplicationContext(), "No Data Found !!!", Toast.LENGTH_SHORT).show();

                                imageViewSync.clearAnimation();
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
                        imageViewSync.clearAnimation();
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
            if(sync_button_animation_end != null)
            {
                textViewClearAlert.clearAnimation();
                textViewClearAlert.setVisibility(View.GONE);

            }

            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(LinemanWorklistDashBoard_Activity.this);
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
                                readFromLocalStorage();
                                sqlite_obj.close();
                                imageViewSync.clearAnimation();
                                //textViewAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_out_right);


                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
                                final String currentDateandTime = simpleDateFormat.format(new Date());

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString(DATE_TIME_SYNC, currentDateandTime);
                                editor.apply();




                            } else {


                                sqlite();
                                readFromLocalStorage();
                                dialog.dismiss();
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(DATE_TIME_SYNC, "Data Not Synchronized!!!");
                                editor.apply();

                                Toast.makeText(getApplicationContext(), "No Data Found !!!", Toast.LENGTH_SHORT).show();

                                imageViewSync.clearAnimation();

                                if(sync_button_animation_end != null)
                                {
                                    textViewClearAlert.clearAnimation();
                                    textViewClearAlert.setVisibility(View.GONE);
                                    imageViewMore.setVisibility(View.GONE);
                                }
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
                        imageViewSync.clearAnimation();

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

    public void goToCoRejectionList(View view) {

        sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String SDELMWL = "SDELMWL";
        final String sdelmwl = sharedpreferences.getString(SDELMWL, null);

        if(sdelmwl != null && sdelmwl.equals("SDELMWL"))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = builder.create();
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.custom_dialog_co_list, null);
            dialog.setView(dialogLayout);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Spinner spinner_sort_pillar_wise = dialogLayout.findViewById(R.id.spinner_sort_pillar_wise);

            SdeFieldList_SQLiteHelper dbBackend = new SdeFieldList_SQLiteHelper(LinemanWorklistDashBoard_Activity.this);

            String [] spinnerLists = dbBackend.SelectAllDataLMCODE();


            List<String> stringArrayList = new ArrayList<>();
            Collections.addAll(stringArrayList,spinnerValues);

            Collections.addAll(stringArrayList,spinnerLists);
            String[] finalArray = stringArrayList.toArray(new String[0]);

            if (finalArray != null) {
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                        this, R.layout.custome_spinner_configurelm, finalArray) {
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
                            tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackground));
                        }
                        return view;
                    }
                };
                spinnerArrayAdapter.setDropDownViewResource(R.layout.custome_spinner_configurelm);


                spinner_sort_pillar_wise.setAdapter(spinnerArrayAdapter);

                spinner_sort_pillar_wise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position > 0) {
                            String selectedItemText = (String) parent.getItemAtPosition(position);

                            selectedItemText = selectedItemText.substring(1);

                            //Toast.makeText(getApplicationContext(),selectedItemText,Toast.LENGTH_LONG).show();

                            sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                            final SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString(PERNOLM,selectedItemText);
                            editor.apply();

                            Intent intent = new Intent(LinemanWorklistDashBoard_Activity.this,Co_List_Activity.class);
                            startActivity(intent);
                            finishAffinity();


                            dialog.dismiss();

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            dialog.show();
        }

        else
        {
            Intent intent = new Intent(LinemanWorklistDashBoard_Activity.this,Co_List_Activity.class);
            startActivity(intent);
            finishAffinity();
        }


    }




    @Override
    public void onContactSelected(LinemanWorklistDashBoard_ReportClass linemanWorklistDashBoard_reportClass) {

        String assigned_lm_code = linemanWorklistDashBoard_reportClass.getASSIGNED_LM();

        sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(ASSIGNED_LM_CODE,assigned_lm_code);
        editor.apply();

        Intent intent = new Intent(LinemanWorklistDashBoard_Activity.this, LinemanWorklist_Activity.class);
        startActivity(intent);
        finishAffinity();

    }

    @Override
    public void onClick(View view) {

    }



    @Override
    public void onBackPressed() {

        sharedpreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String SDELMWL = "SDELMWL";
        final String sdelmwl = sharedpreferences.getString(SDELMWL, null);

        if(sdelmwl != null && sdelmwl.equals("SDELMWL"))
        {
            Intent intent = new Intent(LinemanWorklistDashBoard_Activity.this, SdeFieldListConfigureLm_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finishAffinity();
        }

        else
        {
            Intent intent = new Intent(LinemanWorklistDashBoard_Activity.this, DashBoard_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finishAffinity();
        }
    }

}

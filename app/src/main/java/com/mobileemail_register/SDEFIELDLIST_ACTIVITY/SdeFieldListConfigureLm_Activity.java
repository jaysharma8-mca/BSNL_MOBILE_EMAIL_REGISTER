package com.mobileemail_register.SDEFIELDLIST_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.mobileemail_register.MISC.RequestHandler;
import com.mobileemail_register.MISC.constants;
import com.mobileemail_register.R;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;



@SuppressWarnings({"Convert2Lambda"})
public class SdeFieldListConfigureLm_Activity extends AppCompatActivity implements SdeFieldListConfigureLm_Adapter.ContactsAdapterListener, View.OnClickListener {

    private ArrayList<SdeFieldListConfigureLm_ReportClass> sdeFieldListConfigureLmReportClass = new ArrayList<>();


    @SuppressLint("StaticFieldLeak")
    public static SdeFieldListConfigureLm_Adapter mAdapter;

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


    private AlertDialog alertDialog;

    private static final String SP_MEREG = "SP_MEREG";

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sde_field_list_configure_lm);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BHARAT SANCHAR NIGAM LTD.");
        setSupportActionBar(toolbar);*/


        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);

        tv = new TextView(this);

        sdeFieldList_sqLiteHelper = new SdeFieldList_SQLiteHelper(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        sdeFieldListConfigureLmReportClass = new ArrayList<>();
        mAdapter = new SdeFieldListConfigureLm_Adapter(this, sdeFieldListConfigureLmReportClass, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        final String[] myData = sdeFieldList_sqLiteHelper.SelectAllData();

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

    @SuppressLint("SetTextI18n")
    public void readFromLocalStorage() {
        sdeFieldListConfigureLmReportClass.clear();
        SdeFieldList_SQLiteHelper sdeFieldList_sqLiteHelper = new SdeFieldList_SQLiteHelper(this);
        SQLiteDatabase database = sdeFieldList_sqLiteHelper.getReadableDatabase();

        Cursor cursor = sdeFieldList_sqLiteHelper.readFromLocalDatabase(database);

        while (cursor.moveToNext()) {


            String USER_PERNO = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.USER_PERNO));
            String LM_CODE = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.LM_CODE));
            String LM_PERNO = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.LM_PERNO));
            String TTL_CON = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.TTL_CON));
            String TGT = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.TGT));
            String LMC_ASSIGNED = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.LMC_ASSIGNED_TO));
            String LMC_ASSIGNED_TO_TEXT = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.LMC_ASSIGNED_TO_TEXT));
            String  ASSIGNED_FLAG = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.ASSIGNED_FLAG));
            String  DEASSIGNED_FLAG = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.DEASSIGNED_FLAG));
            int  FLAG = cursor.getInt(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.SYNC_STATUS_FLAG));

            sdeFieldListConfigureLmReportClass.add(new SdeFieldListConfigureLm_ReportClass(USER_PERNO, LM_CODE, LM_PERNO,
                    TTL_CON, TGT, LMC_ASSIGNED, LMC_ASSIGNED_TO_TEXT,ASSIGNED_FLAG,DEASSIGNED_FLAG,FLAG));

            int count;
            if (mAdapter != null) {
                count = mAdapter.getItemCount();

                tv.setText("" + count);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorErrorText));
                tv.setOnClickListener(SdeFieldListConfigureLm_Activity.this);
                tv.setPadding(5, 0, 5, 0);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setTextSize(18);
            }

        }

        mAdapter.notifyDataSetChanged();
        cursor.close();
        sdeFieldList_sqLiteHelper.close();

    }


    public void getData() {

        if (checkInternetConnection()) {
            SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
            final String PERSONAL_NUMBER = "PERSONAL_NUMBER";
            final String personal_number = sharedPreferences.getString(PERSONAL_NUMBER, null);

            if(personal_number != null)
            {
                retrieveDetails();
                String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/sdolmcount/username/" + personal_number + "";
                URL = URL.replaceAll(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        URL,
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(String response) {

                                if (response.contains("ORA-")) {
                                    alertDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SdeFieldListConfigureLm_Activity.this);

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
                                            if(alertDialog != null)
                                            {
                                                alertDialog.dismiss();
                                            }

                                            onRefresh();
                                            readFromLocalStorage();
                                            sdeFieldList_sqLiteHelper.close();



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
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(SdeFieldListConfigureLm_Activity.this, "Database Connectivity Error!!! Check Your Network Connection And Try Again...", Toast.LENGTH_LONG).show();
                                if(alertDialog != null)
                                {
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
            }

            else
            {
                Toast.makeText(getApplicationContext(),"No PER NO Found... Kindly Reregister...",Toast.LENGTH_LONG).show();
            }


        } else {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }

            Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
        }

    }

    public void updateSdeLmData(View view) {
        if(checkInternetConnection())
        {
            submitDetails();


            final SdeFieldList_SQLiteHelper sdeFieldList_sqLiteHelper = new SdeFieldList_SQLiteHelper(SdeFieldListConfigureLm_Activity.this);
            final SQLiteDatabase database = sdeFieldList_sqLiteHelper.getWritableDatabase();

            Cursor cursor = sdeFieldList_sqLiteHelper.readFromLocalDatabaseSdeLM(database);


            if((cursor != null) && (cursor.getCount() > 0))
            {

                while (cursor.moveToNext())
                {

                    String  sync_status = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.SYNC_STATUS_FLAG));
                    String ASSIGNED_DEASSIGNED_FLAG = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.ASSIGNED_FLAG));

                    if(sync_status.equalsIgnoreCase(String.valueOf(constants.SYNC_STATUS_FAILED)) && ASSIGNED_DEASSIGNED_FLAG.equals("I"))
                    {
                        //lmperno = LM_PERNO.substring(1);

                        String USER_PERNO = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.USER_PERNO));
                        String PRILMCODE = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.LMC_ASSIGNED_TO));
                        String ASSLMCODE = cursor.getString(cursor.getColumnIndex(SdeFieldList_SQLiteHelper.LM_CODE));



                        //String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/assignlm/perno/00302854/prilmcode/PUN234401/asslmcode/PUN235501/flag/I
                        String URL = "https://fms.bsnl.in/fmswebservices/rest/gogreen/assignlm/perno/"+USER_PERNO+"/prilmcode/"+PRILMCODE+"/asslmcode/"+ASSLMCODE+"/flag/"+ASSIGNED_DEASSIGNED_FLAG+"";
                        URL = URL.replaceAll(" ", "%20");


                        //Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_LONG).show();

                        StringRequest sr = new StringRequest(Request.Method.POST, URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.contains("ORA-")) {
                                            alertDialog.dismiss();

                                            Toast.makeText(getApplicationContext(),"FMS Database Connectivity Error... Please Try again After Sometime...",Toast.LENGTH_LONG).show();
                                        }

                                        else
                                            try {

                                                JSONObject json = new JSONObject(response);

                                                JSONArray jsonArray = json.getJSONArray("ROWSET");


                                                if(jsonArray.length ()> 0){

                                                    for(int countItem = 0;countItem<jsonArray.length();countItem++){

                                                        JSONObject jsonObject = jsonArray.getJSONObject(countItem);

                                                        //String STATUS = jsonObject.isNull("STATUS")?"":jsonObject.optString("STATUS");
                                                        String REMARKS = jsonObject.isNull("REMARKS")?"":jsonObject.optString("REMARKS");

                                                        Toast.makeText(getApplicationContext(),REMARKS,Toast.LENGTH_LONG).show();
                                                        //showAlertSuccess(REMARKS);
                                                        alertDialog.dismiss();

                                                        sdeFieldList_sqLiteHelper.updateLocalDatabase(ASSLMCODE,String.valueOf(constants.SYNC_STATUS_OK));
                                                        onRefresh();
                                                    }

                                                }

                                                else {

                                                    if(alertDialog != null)
                                                    {
                                                        alertDialog.dismiss();
                                                    }

                                                    Toast.makeText(getApplicationContext(),"Error Processing Request...",Toast.LENGTH_LONG).show();


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
                                        if(alertDialog !=null)
                                        { alertDialog.dismiss();}

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
            }

            else
            {
                Snackbar.make(findViewById(android.R.id.content), "No Pending Data To Sync With Server...", Snackbar.LENGTH_LONG).show();
                if(alertDialog != null)
                {
                    alertDialog.dismiss();
                }
            }


            if (cursor != null) {
                cursor.close();
            }
            sdeFieldList_sqLiteHelper.close();
        }

        else
        {
            if(alertDialog != null)
            {
                alertDialog.dismiss();
            }
            Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
        }
    }

    private void retrieveDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(SdeFieldListConfigureLm_Activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.data_fetching);
        alertDialog = alert.create();
        alertDialog.show();
    }

    private void submitDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(SdeFieldListConfigureLm_Activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.data_submit_registration_page);
        alertDialog = alert.create();
        alertDialog.show();
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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
            if (checkInternetConnection()) {

                getData();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
            }

        }

        return true;
    }



    private void onRefresh() {
        Intent intent = new Intent(SdeFieldListConfigureLm_Activity.this, SdeFieldListConfigureLm_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 0);
        overridePendingTransition(0, 0);
        finishAffinity();
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onContactSelected(SdeFieldListConfigureLm_ReportClass sdeFieldListConfigureLm_reportClass) {




        /*String spinnerValue = sdeFieldListConfigureLm_reportClass.getLMC_ASSIGNED();

        //Toast.makeText(getApplicationContext(),spinnerValue,Toast.LENGTH_LONG).show();

        if (spinnerValue.equals(" ")) {
            Toast.makeText(getApplicationContext(), "No LINEMAN WORKLIST To Deassign !!!", Toast.LENGTH_LONG).show();
            onRefresh();
        }

        else
        {
            imageViewDeAssign.setVisibility(View.VISIBLE);

            deleteData = new ArrayList<>();
            String lm_code = sdeFieldListConfigureLm_reportClass.getLM_CODE();

            deleteData.add(lm_code);

            sqliteUpd();

            imageViewDeAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //final SdeFieldListConfigureLm_ReportClass sdeFieldListConfigureLm_reportClass = sdeFieldListConfigureLmReportClass.get(pos);


                        AlertDialog.Builder builder = new AlertDialog.Builder(SdeFieldListConfigureLm_Activity.this);

                        builder.setTitle("Deassign WL");
                        builder.setMessage("Dear User, \n\nThis Will Deassign The Assigned \nLINEMAN WORKLIST...");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Toast.makeText(getApplicationContext(), "No LINEMAN WORKLIST To Deassign !!!", Toast.LENGTH_LONG).show();

                                updRows();
                                onRefresh();

                            }
                        });

                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                        builder.show();
                    }
            });
        }*/




    }



    @Override
    public void onBackPressed() {

            Intent intent = new Intent(SdeFieldListConfigureLm_Activity.this, SdeFieldList_Activity.class);
            startActivity(intent);
            finishAffinity();


    }

}
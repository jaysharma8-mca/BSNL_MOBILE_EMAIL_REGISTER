package com.mobileemail_register.DASHBOARD_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mobileemail_register.CLOSURE_ACTIVITY.ClosureActivity_SQLiteHelper;
import com.mobileemail_register.LINEMAN_WL_ACTIVITY.LinemanWorklistDashBoard_Activity;
import com.mobileemail_register.LOGIN_ACTIVITY.LoginPage_Activity;
import com.mobileemail_register.MISC.constants;
import com.mobileemail_register.R;
import com.mobileemail_register.SDEFIELDLIST_ACTIVITY.SdeFieldList_Activity;
import com.mobileemail_register.SEARCH_SUBSCRIBER_ACTIVITY.SearchSubscriber_Activity;
import com.mobileemail_register.SYNC_UNSYNC_ACTIVITY.UnsyncdData_Activity;

import java.io.File;

public class DashBoard_Activity extends AppCompatActivity {

    private static final String SP_MEREG = "SP_MEREG";
    private TextView textViewClearDbAlert;

    Animation textViewAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        AppCenter.start(getApplication(), "15ee0d52-7eb0-4aa2-9ec5-40b1491add0e",
                Analytics.class, Crashes.class);

        textViewClearDbAlert = findViewById(R.id.textViewClearDbAlert);


        textViewAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_slide_in_left);

        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        //long gigabyte = megabyte * 1024;
        //long terabyte = gigabyte * 1024;

        long DB_SIZE = megabyte*2;

        File f = getDatabasePath("BSNL_ME_REG_CUSTOMER_DATA.db");
        long dbSize = f.length();


        if ((dbSize >= DB_SIZE)) {
            //String  digitGroups =  (dbSize / megabyte) + " MB";

            textViewClearDbAlert.setVisibility(View.VISIBLE);
            textViewClearDbAlert.startAnimation(textViewAnimation);


        }

    }


    public void searchSubscriberTask(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("LINEMAN_WL");
        editor.remove("CUSTOMER_STDCODE");
        editor.remove("CUSTOMER_LLNO");
        editor.remove("CUSTOMER_NAME");
        editor.remove("CUSTOMER_ADDRESS");
        editor.remove("CUSTOMER_EMAILID");
        editor.remove("CUSTOMER_EMAILID_DOMAIN");
        editor.remove("CUSTOMER_MOBILENUMBER");
        editor.apply();

        Intent intent = new Intent(DashBoard_Activity.this, SearchSubscriber_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }

    public void fieldList(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String DESIGNATION = "DESIGNATION";
        final String designation = sharedPreferences.getString(DESIGNATION, null);

        if(designation != null)
        {
            if(designation.equals("LM"))
            {
                Intent intent = new Intent(DashBoard_Activity.this, LinemanWorklistDashBoard_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }

            else if(designation.equals("JE") || designation.equals("JTO") || designation.equals("SDE"))
            {
                Intent intent = new Intent(DashBoard_Activity.this, SdeFieldList_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }

            else
            {
                Toast.makeText(getApplicationContext(),"No WorkList Found",Toast.LENGTH_LONG).show();
            }
        }

        else
        {
            Toast.makeText(getApplicationContext(),"No Designation Defined... Kindly Register...",Toast.LENGTH_LONG).show();
        }


    }

    public void unSyncdDataTask(View view) {

        Intent intent = new Intent(DashBoard_Activity.this, UnsyncdData_Activity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void clearUploadedData(View view) {

        clearSyncdData();
    }

    private void clearSyncdData() {

        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;

        File f = getDatabasePath("BSNL_ME_REG_CUSTOMER_DATA.db");
        long dbSize = f.length();


        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DashBoard_Activity.this);
        builder1.setView(alertLayout);
        builder1.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.data_deletion_from_db);
        AlertDialog alertDialog = builder1.create();




        if (dbSize >= megabyte /*|| dbSize >= DB_SIZE*/) {
            //String  digitGroups =  (dbSize / megabyte) + " MB";
            alertDialog.show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("BSNL ME REG");
            builder.setIcon(R.drawable.ic_folder);
            builder.setMessage("Dear User, \nThis Will Delete All SYNCD & UNSYNCD Data From ME REG APP...");
            builder.setPositiveButton("OK", (dialog, which) -> {

                dialog.dismiss();
                Runnable progressRunnable = () -> {

                    /*final ME_UPD_LM_SQLITE_HELPER me_upd_lm_sqlite_helper = new ME_UPD_LM_SQLITE_HELPER(LANDING_PAGE_ACTIVITY.this);
                    final SQLiteDatabase database = me_upd_lm_sqlite_helper.getWritableDatabase();

                    database.delete(constants.TABLE_NAME, null,null);*/

                    final ClosureActivity_SQLiteHelper closureActivity_sqLiteHelper = new ClosureActivity_SQLiteHelper(DashBoard_Activity.this);
                    final SQLiteDatabase database = closureActivity_sqLiteHelper.getWritableDatabase();



                    Cursor cursor = closureActivity_sqLiteHelper.readFromLocalDatabase(database);

                    while (cursor.moveToNext()) {
                        database.execSQL("DELETE FROM "+ constants.TABLE_NAME + " WHERE " + constants.sync_status + " = "+constants.SYNC_STATUS_OK+"");

                    }

                    textViewAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.anim_slide_out_right);

                    textViewClearDbAlert.setVisibility(View.GONE);
                    textViewClearDbAlert.startAnimation(textViewAnimation);
                    alertDialog.dismiss();
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 3000);





            });
            builder.setNegativeButton("CANCEL", (dialog, which) -> {
                //if user select "No", just cancel this dialog and continue with app
                alertDialog.cancel();
            });
            AlertDialog alert = builder.create();
            alert.show();


        }

        else
        {
            Snackbar.make(findViewById(android.R.id.content), "Data Deletion Not Required...", Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DashBoard_Activity.this, LoginPage_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }

}

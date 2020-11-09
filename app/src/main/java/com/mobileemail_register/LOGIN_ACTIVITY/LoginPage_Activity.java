package com.mobileemail_register.LOGIN_ACTIVITY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mobileemail_register.DASHBOARD_ACTIVITY.DashBoard_Activity;
import com.mobileemail_register.R;
import com.mobileemail_register.REGISTRATION_ACTIVITY.RegistrationPage_Activity;


import java.util.ArrayList;
import java.util.List;



@SuppressWarnings({"Convert2Lambda"})
public class LoginPage_Activity extends AppCompatActivity {

    private EditText editTextHrmsNo;
     private boolean doubleBackToExitPressedOnce = false;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS= 1;
    private static final String SP_MEREG = "SP_MEREG";
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_);

        AppCenter.start(getApplication(), "15ee0d52-7eb0-4aa2-9ec5-40b1491add0e",
                Analytics.class, Crashes.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(null);
        }

        editTextHrmsNo = findViewById(R.id.editTextHrmsNo);

        checkAndRequestPermissions();

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String PERSONAL_NUMBER = "PERSONAL_NUMBER";
        final String personal_number = sharedPreferences.getString(PERSONAL_NUMBER, null);

        if(personal_number != null)
        {
            editTextHrmsNo.setText(personal_number);
            editTextHrmsNo.setEnabled(false);
        }

    }

    public void loginActivity(View view) {

        if(!editTextHrmsNo.getText().toString().trim().equals(""))
        {
            submitDetails();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(LoginPage_Activity.this, DashBoard_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finishAffinity();
                    alertDialog.dismiss();
                }
            }, 1500);

        }

        else
        {
            Toast.makeText(getApplicationContext(),"Kindly register to login into the App...",Toast.LENGTH_LONG).show();
            //alertDialog.dismiss();
        }


    }


    public void logoutFromApp(View view) {

        showAlertDialogLogout();

    }

    public void registrationActivity(View view) {

        Intent intent = new Intent(LoginPage_Activity.this, RegistrationPage_Activity.class);
        startActivity(intent);
        finishAffinity();
    }


    private void checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);

        int writeExternalStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);


        int accessFineLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        int accessCoarseLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (writeExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(accessFineLocation != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(accessCoarseLocation != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[0]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    private void submitDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginPage_Activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.login_dialog_box);
        alertDialog = alert.create();
        alertDialog.show();
    }

    private void showAlertDialogLogout()
    {
        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setMessage("This will remove your saved username and password...")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.clear().apply();

                        onRefresh();


                        //System.exit(0);
                    }

                    private void close() {
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.d("MainActivity", "Aborting mission...");
                    }
                })
                .show();
    }


    private void onRefresh() {
        Intent intent = new Intent(LoginPage_Activity.this, LoginPage_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 0);
        overridePendingTransition(0, 0);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to close BSNL ME REG", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 4000);

    }

}

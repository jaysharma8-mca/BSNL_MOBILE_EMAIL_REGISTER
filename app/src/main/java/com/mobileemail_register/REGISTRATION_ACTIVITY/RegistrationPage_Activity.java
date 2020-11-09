package com.mobileemail_register.REGISTRATION_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mobileemail_register.LOGIN_ACTIVITY.LoginPage_Activity;
import com.mobileemail_register.MISC.RequestHandler;
import com.mobileemail_register.R;
import com.google.android.material.snackbar.Snackbar;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings({"Convert2Lambda"})
public class RegistrationPage_Activity extends AppCompatActivity {

    private SmsVerifyCatcher smsVerifyCatcher;
    private EditText editTextUserName, editTextName, editTextMobileNo, editTextVerificationCode;
    private ImageView imageViewSendSmsCode;
    private AlertDialog alertDialog;


    ProgressBar mProgressBar, mProgressBar1;
    private TextView textViewShowTime;
    private CountDownTimer countDownTimer;
    private long totalTimeCountInMilliseconds;



    private static final String SP_MEREG = "SP_MEREG";
    private static final String NAME = "NAME";
    private static final String MOBILE_NO = "MOBILE_NO";
    private static final String DESIGNATION = "DESIGNATION";
    private static final String SSA_CODE = "SSA_CODE";
    private static final String PERSONAL_NUMBER = "PERSONAL_NUMBER";
    private static final String VERIFICATION_CODE = "VERIFICATION_CODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        AppCenter.start(getApplication(), "15ee0d52-7eb0-4aa2-9ec5-40b1491add0e",
                Analytics.class, Crashes.class);

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextName = findViewById(R.id.editTextName);
        editTextMobileNo = findViewById(R.id.editTextMobileNo);
        editTextVerificationCode = findViewById(R.id.editTextVerificationCode);
        imageViewSendSmsCode = findViewById(R.id.imageViewSendSmsCode);
        textViewShowTime = findViewById(R.id.textViewShowTime);

        mProgressBar = findViewById(R.id.progressbar_timerview);
        mProgressBar1 = findViewById(R.id.progressbar1_timerview);

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                editTextVerificationCode.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });

        verifyreceivedSmsCode();
        editTextUserNameTextChange();


    }


    public void submitPersonalNumber(View view) {


        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String PERSONAL_NUMBER = "PERSONAL_NUMBER";
        final String personal_number = sharedPreferences.getString(PERSONAL_NUMBER, null);

        String  per_no = editTextUserName.getText().toString().trim();

        if(personal_number != null && personal_number.equals(per_no))
        {
            resendPersonalNumberDataToServer();
        }

        else
        {
            sendPersonalDataToServer();
        }


    }



    private void setTimer() {
        int time = Integer.parseInt("180");
        totalTimeCountInMilliseconds = time * 1000;
        mProgressBar1.setMax( time * 1000);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                mProgressBar1.setProgress((int) (leftTimeInMilliseconds));

                textViewShowTime.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));
            }
            @Override
            public void onFinish() {
                textViewShowTime.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                mProgressBar1.setVisibility(View.GONE);

            }
        }.start();
    }


    private void countDownTimerStart()
    {
        setTimer();
        mProgressBar.setVisibility(View.INVISIBLE);
        startTimer();
        mProgressBar1.setVisibility(View.VISIBLE);
        textViewShowTime.setVisibility(View.VISIBLE);
    }

    private void countDownTimerStop()
    {
        if(countDownTimer != null)
        {
            countDownTimer.cancel();
            countDownTimer.onFinish();
            mProgressBar1.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.INVISIBLE);
            textViewShowTime.setVisibility(View.INVISIBLE);

        }

    }


    private void vibrationService() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            Objects.requireNonNull(vibrator).vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            Objects.requireNonNull(vibrator).vibrate(200);
        }
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }



    private void hideSoftInput() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void progressDialogSendDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(RegistrationPage_Activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.data_submit_registration_page);
        alertDialog = alert.create();
        alertDialog.show();
    }

    private void submitDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(RegistrationPage_Activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.data_verify_registration_page);
        alertDialog = alert.create();
        alertDialog.show();
    }


    private void verifySmsCode()
    {
        SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreference.edit();
        final String VERIFICATION_CODE = "VERIFICATION_CODE";
        final String sms_verification_code = sharedPreference.getString(VERIFICATION_CODE, null);

        String personal_number = editTextUserName.getText().toString().trim();

        String verification_code = editTextVerificationCode.getText().toString().trim();

        if(sms_verification_code != null && sms_verification_code.equals(verification_code))
        {

            submitDetails();
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    if(countDownTimer != null)
                    {
                        countDownTimer.cancel();
                    }
                    editor.putString(PERSONAL_NUMBER, personal_number);
                    editor.apply();
                    Intent intent = new Intent(RegistrationPage_Activity.this, LoginPage_Activity.class);
                    startActivity(intent);
                    finishAffinity();
                    if(alertDialog != null)
                    {
                        alertDialog.dismiss();
                    }
                }
            }, 1000);

        }

        else
        {
            editTextVerificationCode.setError("OTP not verified please try again !!!");
            if(alertDialog != null)
            {
                alertDialog.dismiss();
            }
        }
    }


    private void verifyreceivedSmsCode()
    {
        editTextVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(editTextVerificationCode.getText().toString().trim().length() == 4)
                {
                    verifySmsCode();
                }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void editTextUserNameTextChange()
    {
        editTextUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(editTextUserName.getText().toString().trim().length() == 8)
                {
                    imageViewSendSmsCode.setVisibility(View.VISIBLE);
                }

                else
                {
                    imageViewSendSmsCode.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }





    private void sendPersonalDataToServer()
    {
        final String personal_number = editTextUserName.getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        if (editTextUserName.getText().toString().trim().equals("")) {
            editTextUserName.setError("Invalid Personal Number");
            editTextUserName.requestFocus();
            vibrationService();
        } else if (editTextUserName.getText().length() < 8) {
            editTextUserName.setError("Invalid Personal Number");
            editTextUserName.requestFocus();
            vibrationService();
        } else if (editTextUserName.getText().length() > 8) {
            editTextUserName.setError("Invalid Personal Number");
            editTextUserName.requestFocus();
            vibrationService();
        }

        else
            {

                if (checkInternetConnection())
                {
                    if(countDownTimer != null)
                    {
                        countDownTimerStop();
                    }

                    SharedPreferences preferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = preferences.edit();
                    editor1.clear().apply();

                    hideSoftInput();

                Random random = new Random();
                @SuppressLint("DefaultLocale") final String random_otp = String.format("%04d", random.nextInt(10000));
                String MESSAGE = ("Dear User, your  BSNL verification code for APP registration is" + " " + random_otp);

                progressDialogSendDetails();

                String URL = "https://fms.bsnl.in/fmswebservices/rest/user/getmobile/username/" + personal_number + "/otpmsg/" + MESSAGE + "";
                URL = URL.replaceAll(" ", "%20");
                //Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_LONG).show();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(String response) {

                                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                                if (response.contains("ORA-")) {
                                    alertDialog.dismiss();
                                    countDownTimerStop();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationPage_Activity.this);

                                    builder.setTitle("Registration Failed");
                                    builder.setMessage("FMS Database Connectivity Error... Please Try again After Sometime...");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });

                                    builder.show();


                                } else {
                                    try {


                                        JSONObject json = new JSONObject(response);

                                        //now get your  json array like this
                                        JSONArray jsonArray = json.getJSONArray("ROWSET");


                                        if(jsonArray.length ()> 0){

                                            for(int countItem = 0;countItem<jsonArray.length();countItem++){

                                                JSONObject jsonObject = jsonArray.getJSONObject(countItem);

                                                String name = jsonObject.isNull("NAME")?"":jsonObject.optString("NAME");
                                                String mobile_number = jsonObject.isNull("MOBILE_NO")?"":jsonObject.optString("MOBILE_NO");
                                                String designation = jsonObject.isNull("DESIGNATION")?"":jsonObject.optString("DESIGNATION");
                                                String ssa_code = jsonObject.isNull("SSA_CODE")?"":jsonObject.optString("SSA_CODE");

                                                if (ssa_code.equals("NA")) {
                                                    alertDialog.dismiss();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationPage_Activity.this);

                                                    builder.setTitle("Registration Failed");
                                                    builder.setMessage("Dear User, You Cannot Register As You Are Not Mapped To Any SSA In ERP");
                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                        }
                                                    });

                                                    builder.show();
                                                    countDownTimerStop();


                                                }

                                                else {

                                                    countDownTimerStart();
                                                    editor.putString(NAME, name);
                                                    editor.putString(DESIGNATION, designation);
                                                    editor.putString(MOBILE_NO, mobile_number);
                                                    editor.putString(PERSONAL_NUMBER, personal_number);
                                                    editor.putString(SSA_CODE, ssa_code);
                                                    editor.putString(VERIFICATION_CODE, random_otp);
                                                    editor.apply();

                                                    editTextName.setText(name);
                                                    editTextMobileNo.setText(mobile_number);
                                                    if(alertDialog != null)
                                                    {
                                                        alertDialog.dismiss();
                                                    }



                                                }



                                            }

                                        }

                                        else {

                                            if(alertDialog != null)
                                            {
                                                alertDialog.dismiss();
                                            }
                                            countDownTimerStop();

                                            Toast.makeText(getApplicationContext(),"Error Processing Request...",Toast.LENGTH_LONG).show();


                                        }

                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(RegistrationPage_Activity.this, "Database Connectivity Error!!! Check Your Network Connection And Try Again...", Toast.LENGTH_LONG).show();
                                if(alertDialog != null)
                                {
                                    alertDialog.dismiss();
                                }
                                countDownTimerStop();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("EKEY", "b28272183c64fcb45b11d9098a7dd97df51f89bc1bae9448e4126258fd9446d1");
                        return params;
                    }
                };


                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            } else {

                    if(alertDialog != null)
                    {
                        alertDialog.dismiss();
                    }

                Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
                    countDownTimerStop();
            }
        }
    }

    private void resendPersonalNumberDataToServer()
    {
        final String personal_number = editTextUserName.getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String VERIFICATION_CODE = "VERIFICATION_CODE";
        final String verification_code = sharedPreferences.getString(VERIFICATION_CODE, null);

        if (editTextUserName.getText().toString().trim().equals("")) {
            editTextUserName.setError("Invalid Personal Number");
            editTextUserName.requestFocus();
            vibrationService();
        } else if (editTextUserName.getText().length() < 8) {
            editTextUserName.setError("Invalid Personal Number");
            editTextUserName.requestFocus();
            vibrationService();
        } else if (editTextUserName.getText().length() > 8) {
            editTextUserName.setError("Invalid Personal Number");
            editTextUserName.requestFocus();
            vibrationService();
        }

        else {

            if (checkInternetConnection())
            {

                if(countDownTimer != null)
                {
                   countDownTimerStop();
                }

                hideSoftInput();

                String MESSAGE = ("Dear User, your  BSNL verification code for APP registration is" + " " + verification_code);

                progressDialogSendDetails();


                String URL = "https://fms.bsnl.in/fmswebservices/rest/user/getmobile/username/" + personal_number + "/otpmsg/" + MESSAGE + "";
                URL = URL.replaceAll(" ", "%20");
                //Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_LONG).show();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(String response) {

                                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                                if (response.contains("ORA-")) {
                                    alertDialog.dismiss();
                                    countDownTimerStop();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationPage_Activity.this);

                                    builder.setTitle("Registration Failed");
                                    builder.setMessage("FMS Database Connectivity Error... Please Try again After Sometime...");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });

                                    builder.show();


                                } else {
                                    try {

                                        JSONObject json = new JSONObject(response);

                                        //now get your  json array like this
                                        JSONArray jsonArray = json.getJSONArray("ROWSET");

                                        if(jsonArray.length ()> 0){

                                            for(int countItem = 0;countItem<jsonArray.length();countItem++){

                                                JSONObject jsonObject = jsonArray.getJSONObject(countItem);

                                                String name = jsonObject.isNull("NAME")?"":jsonObject.optString("NAME");
                                                String mobile_number = jsonObject.isNull("MOBILE_NO")?"":jsonObject.optString("MOBILE_NO");
                                                String designation = jsonObject.isNull("DESIGNATION")?"":jsonObject.optString("DESIGNATION");
                                                String ssa_code = jsonObject.isNull("SSA_CODE")?"":jsonObject.optString("SSA_CODE");

                                                if (ssa_code.equals("NA")) {
                                                    if(alertDialog != null)
                                                    {
                                                        alertDialog.dismiss();
                                                    }
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationPage_Activity.this);
                                                    builder.setTitle("Registration Failed");
                                                    builder.setMessage("Dear User, You Cannot Register As You Are Not Mapped To Any SSA In ERP");
                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                        }
                                                    });

                                                    builder.show();
                                                    countDownTimerStop();
                                                } else {

                                                    SharedPreferences preferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor1 = preferences.edit();
                                                    editor1.clear().apply();

                                                    countDownTimerStart();
                                                    editor.putString(NAME, name);
                                                    editor.putString(DESIGNATION, designation);
                                                    editor.putString(MOBILE_NO, mobile_number);
                                                    editor.putString(PERSONAL_NUMBER, personal_number);
                                                    editor.putString(SSA_CODE, ssa_code);
                                                    editor.putString(VERIFICATION_CODE, verification_code);
                                                    editor.apply();

                                                    editTextName.setText(name);
                                                    editTextMobileNo.setText(mobile_number);
                                                    if(alertDialog != null)
                                                    {
                                                        alertDialog.dismiss();
                                                    }

                                                }

                                            }

                                        }

                                        else
                                        {
                                            if(alertDialog != null)
                                            {
                                                alertDialog.dismiss();
                                            }
                                            countDownTimerStop();
                                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(RegistrationPage_Activity.this, "Database Connectivity Error!!! Check Your Network Connection And Try Again...", Toast.LENGTH_LONG).show();
                                if(alertDialog != null)
                                {
                                    alertDialog.dismiss();
                                }
                                countDownTimerStop();
                            }
                        }) {
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
                    if(alertDialog != null)
                    {
                        alertDialog.dismiss();
                    }

                Snackbar.make(findViewById(android.R.id.content), "Check Your Internet Connection & Try Again...", Snackbar.LENGTH_LONG).show();
                    countDownTimerStop();

            }
        }
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(countDownTimer != null)
        {
            countDownTimerStop();
        }

        Intent intent = new Intent(RegistrationPage_Activity.this,LoginPage_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
        finish();
    }

}

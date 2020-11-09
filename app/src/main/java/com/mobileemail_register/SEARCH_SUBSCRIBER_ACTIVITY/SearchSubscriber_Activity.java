package com.mobileemail_register.SEARCH_SUBSCRIBER_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mobileemail_register.DASHBOARD_ACTIVITY.DashBoard_Activity;
import com.mobileemail_register.INFO_ACTIVITY.INFO_ACTIVITY;
import com.mobileemail_register.LINEMAN_WL_ACTIVITY.LinemanWorklist_Activity;
import com.mobileemail_register.MISC.RequestHandler;
import com.mobileemail_register.R;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



@SuppressWarnings({"Convert2Lambda"})
public class SearchSubscriber_Activity extends AppCompatActivity {

    private TextInputEditText editTextAreaCode,editTextLLNo,editTextName,editTextAddress,editTextMailId,editTextMobileNo;
    private Spinner spinner_search_subscriber;
    private CheckBox checkBoxGoGreen;
    private ImageView imageViewSearch;

    private AutoCompleteTextView editTextMailDomains;

    private AlertDialog alertDialog;

    private Animation anim;


    private final String[] mail_domains = {"gmail.com", "yahoo.com", "hotmail.com", "aol.com", "hotmail.co.uk", "hotmail.fr", "msn.com", "yahoo.fr",
            "wanadoo.fr", "orange.fr", "comcast.net", "yahoo.co.uk", "yahoo.com.br", "yahoo.co.in", "live.com", "rediffmail.com",
            "free.fr", "gmx.de", "web.de", "yandex.ru", "ymail.com", "libero.it", "outlook.com", "uol.com.br", "bol.com.br",
            "mail.ru", "cox.net", "hotmail.it", "sbcglobal.net", "sfr.fr", "live.fr", "verizon.net", "live.co.uk", "googlemail.com",
            "yahoo.es", "ig.com.br", "live.nl", "bigpond.com", "terra.com.br", "yahoo.it", "neuf.fr", "yahoo.de", "alice.it", "rocketmail.com",
            "att.net", "laposte.net", "facebook.com", "bellsouth.net", "yahoo.in", "hotmail.es", "charter.net", "yahoo.ca", "yahoo.com.au",
            "yahoo.co.in", "rambler.ru", "hotmail.de", "tiscali.it", "shaw.ca", "yahoo.co.jp", "sky.com", "earthlink.net", "optonline.net",
            "freenet.de", "t-online.de", "aliceadsl.fr", "virgilio.it", "home.nl", "qq.com", "telenet.be", "me.com", "yahoo.com.ar", "tiscali.co.uk",
            "yahoo.com.mx", "voila.fr", "gmx.net", "mail.com", "planet.nl", "tin.it", "live.it", "ntlworld.com", "arcor.de", "yahoo.co.id", "frontiernet.net",
            "hetnet.nl", "live.com.au", "yahoo.com.sg", "zonnet.nl", "club-internet.fr", "juno.com", "optusnet.com.au", "blueyonder.co.uk",
            "bluewin.ch", "skynet.be", "sympatico.ca", "windstream.net", "mac.com", "centurytel.net", "chello.nl", "live.ca", "aim.com",
            "bigpond.net.au","tcs.com","bsnl.co.in"};



    private final String[] spinnerValues = {"SELECT SERVICE TO UPDATE",
            "MOBILE NUMBER",
            "EMAIL ID",
            "MOBILE AND EMAIL",
            "GO GREEN"};


    private final int[] total_images = {R.drawable.service_update,
            R.drawable.ic_mobile_app,
            R.drawable.ic_email_icon,
            R.drawable.ic_mobile_email_icon,
            R.drawable.ic_go_green_icon};

    private static final String SP_MEREG = "SP_MEREG";
    private static final String CUSTOMER_STDCODE = "CUSTOMER_STDCODE";
    private static final String CUSTOMER_LLNO = "CUSTOMER_LLNO";
    private static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    private static final String CUSTOMER_ADDRESS = "CUSTOMER_ADDRESS";
    private static final String CUSTOMER_EMAILID = "CUSTOMER_EMAILID";
    private static final String CUSTOMER_EMAILID_DOMAIN = "CUSTOMER_EMAILID_DOMAIN";
    private static final String CUSTOMER_MOBILENUMBER = "CUSTOMER_MOBILENUMBER";
    private static final String CUSTOMER_UPDATE_TYPE = "CUSTOMER_UPDATE_TYPE";
    private static final String CUSTOMER_GO_GREEN = "CUSTOMER_GO_GREEN";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_subscriber);

        AppCenter.start(getApplication(), "f7271b3f-27aa-4985-8654-37f1347f4bf4",
                Analytics.class, Crashes.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(null);
        }

        editTextAreaCode = findViewById(R.id.editTextAreaCode);
        editTextLLNo = findViewById(R.id.editTextLLNo);
        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextMailId = findViewById(R.id.editTextMailId);
        editTextMailDomains = findViewById(R.id.editTextMailDomains);
        editTextMobileNo = findViewById(R.id.editTextMobileNo);

        imageViewSearch = findViewById(R.id.imageViewSearch);

        checkBoxGoGreen = findViewById(R.id.checkBoxGoGreen);

        spinner_search_subscriber = findViewById(R.id.spinner_search_subscriber);
        spinner_search_subscriber.setAdapter(new MyAdapter(this, R.layout.custom_spinner, spinnerValues));




        //editTextMailDomains = (AutoCompleteTextView) new EditText(getApplicationContext());
        editTextMailDomains.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1300); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.select_dialog_item, mail_domains);
        //Getting the instance of AutoCompleteTextView
        editTextMailDomains.setThreshold(1);//will start working from first character
        editTextMailDomains.setAdapter(adapter);

        spinnerActivity();

        setDataToDisplay();

        getDataToDisplay();

        checkBoxGoGreen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
            {
                checkBoxGoGreen.clearAnimation();
                anim.cancel();
                anim.reset();
            }
        });

        String editTextStdCode = Objects.requireNonNull(editTextAreaCode.getText()).toString().trim();
        String editTextLandLine = Objects.requireNonNull(editTextLLNo.getText()).toString().trim();


        spinner_search_subscriber.setEnabled(!editTextStdCode.trim().equals("") && !editTextLandLine.trim().equals(""));

    }


    public void searchSubscriberTask(View view) {

        searchSubscribedCustomer();

    }

    public void NavigateToClosureActivity(View view) {

        navigateToClosureActivity();
    }


    private void searchSubscribedCustomer()
    {
        String area_code = Objects.requireNonNull(editTextAreaCode.getText()).toString().trim();
        String landlines_no = Objects.requireNonNull(editTextLLNo.getText()).toString().trim();
        String LANDLINE_NO = area_code+"-"+landlines_no;

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String SSA_CODE = "SSA_CODE";
        final String ssa_code_user = sharedPreferences.getString(SSA_CODE, null);

        if (editTextAreaCode.getText().toString().trim().equals(""))
        {
            editTextAreaCode.setError("Invalid STD Code !!!");
            editTextAreaCode.requestFocus();
            vibrationService();

        }

        else if(editTextLLNo.getText().toString().trim().equals(""))
        {
            editTextLLNo.setError("Invalid Landline Number !!!");
            editTextLLNo.requestFocus();
            vibrationService();
        }

        else
        {
            if(checkInternetConnection())
            {
                hideSoftInput();
                progressDialogSendDetails();
                imageViewSearch.setEnabled(false);

                String URL = "https://fms.bsnl.in/fmswebservices/rest/customer/custdetails/phone/" + LANDLINE_NO + "";
                URL = URL.replaceAll(" ", "%20");
                //Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_LONG).show();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(String response) {

                                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                                if (response.contains("ORA-")) {
                                    if(alertDialog != null)
                                    {
                                        alertDialog.dismiss();
                                    }

                                    imageViewSearch.setEnabled(true);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchSubscriber_Activity.this);

                                    builder.setTitle("Searching Failed");
                                    builder.setMessage("FMS Database Connectivity Error... Please Try again After Sometime...");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });

                                    builder.show();


                                } else {
                                    try {

                                        JSONObject jsonObject = new JSONObject(response);
                                        //Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                                        if (jsonObject.getString("STATUS").contentEquals("SUCCESS")) {
                                            JSONArray Jarray = jsonObject.getJSONArray("ROWSET");

                                            for (int i = 0; i < Jarray.length(); i++) {
                                                JSONObject Jobj = Jarray.getJSONObject(i);


                                                String std_code = Jobj.getString("STD_CODE");
                                                String phone_no = Jobj.getString("PHONE_NO");
                                                String name = Jobj.getString("CUSTOMER_NAME");
                                                String address = Jobj.getString("ADDRESS");
                                                String email_id = Jobj.getString("EMAIL_ID");
                                                String mobile_no = Jobj.getString("MOBILE_NO");
                                                String ssa_code = Jobj.getString("SSA_CODE");


                                                if(!ssa_code.equals("NA") && !Objects.requireNonNull(ssa_code_user).equals("NA") && ssa_code.equals(ssa_code_user))
                                                {
                                                    editTextAreaCode.setText(std_code);
                                                    editor.putString(CUSTOMER_STDCODE, std_code);

                                                    editTextLLNo.setText(phone_no);
                                                    editor.putString(CUSTOMER_LLNO, phone_no);

                                                    editor.putString(CUSTOMER_NAME,name);
                                                    editor.putString(CUSTOMER_ADDRESS,address);

                                                    editTextName.setText(name);
                                                    editTextAddress.setText(address);

                                                    if(email_id.equals("NA"))
                                                    {
                                                        editTextMailId.setText("");
                                                        editTextMailDomains.setText("");
                                                    }

                                                    else
                                                    {
                                                        String[] separated = email_id.split("@");
                                                        String MAIL_ID = separated[0].trim();
                                                        MAIL_ID = MAIL_ID.toLowerCase();
                                                        editTextMailId.setText(MAIL_ID);
                                                        String DOMAIN_NAME = separated[1].trim();
                                                        DOMAIN_NAME = DOMAIN_NAME.toLowerCase();
                                                        editTextMailDomains.setText(DOMAIN_NAME);

                                                        editor.putString(CUSTOMER_EMAILID, MAIL_ID);
                                                        editor.putString(CUSTOMER_EMAILID_DOMAIN,DOMAIN_NAME);
                                                        editor.apply();
                                                    }

                                                    if(!mobile_no.equals("NA"))
                                                    {
                                                        editTextMobileNo.setText(mobile_no);
                                                        editor.putString(CUSTOMER_MOBILENUMBER, mobile_no);
                                                        editor.apply();
                                                    }

                                                    else
                                                    {
                                                        editTextMobileNo.setText("");
                                                    }

                                                    editor.apply();
                                                    if(alertDialog != null)
                                                    {
                                                        alertDialog.dismiss();
                                                    }
                                                    spinner_search_subscriber.setEnabled(true);
                                                    imageViewSearch.setEnabled(false);
                                                }

                                                else
                                                {
                                                    showAlertDialogAuthorizationFailed();
                                                }

                                                //Toast.makeText(getApplicationContext(),"RESPONSE PART"+Jarray.toString(),Toast.LENGTH_LONG).show();


                                                }

                                        } else {


                                            if(alertDialog != null)
                                            {
                                                alertDialog.dismiss();
                                            }
                                            imageViewSearch.setEnabled(true);
                                            Toast.makeText(getApplicationContext(),"No Data Found !!!",Toast.LENGTH_LONG).show();


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

                                Toast.makeText(SearchSubscriber_Activity.this, "Database Connectivity Error!!! Check Your Network Connection And Try Again...", Toast.LENGTH_LONG).show();
                                if(alertDialog != null)
                                {
                                    alertDialog.dismiss();
                                }
                                imageViewSearch.setEnabled(true);
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
                imageViewSearch.setEnabled(true);
            }
        }

    }

    private void spinnerActivity()
    {

        spinner_search_subscriber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                if(selectedItem.equals("SELECT SERVICE TO UPDATE"))
                {
                    editTextMailId.setFocusable(false);
                    editTextMailDomains.setFocusable(false);
                    editTextMobileNo.setFocusable(false);
                }

                if(selectedItem.equals("MOBILE NUMBER"))
                {
                    if (Objects.requireNonNull(editTextAreaCode.getText()).toString().trim().equals(""))
                    {
                        editTextAreaCode.setError("Inavalid STD Code !!!");
                        editTextAreaCode.requestFocus();
                        vibrationService();

                    }

                    else if(Objects.requireNonNull(editTextLLNo.getText()).toString().trim().equals(""))
                    {
                        editTextLLNo.setError("Invalid Landline Number !!!");
                        editTextLLNo.requestFocus();
                        vibrationService();
                    }


                    else
                    {


                        final String EMAIL_ID = "CUSTOMER_EMAILID";
                        final String EMAIL_ID_DOMAIN = "CUSTOMER_EMAILID_DOMAIN";
                        final String customer_email_id = sharedPreferences.getString(EMAIL_ID, null);
                        final String customer_email_id_domain = sharedPreferences.getString(EMAIL_ID_DOMAIN, null);


                        if(!editTextMobileNo.hasFocus())
                        {
                            editTextMobileNo.setFocusableInTouchMode(true);
                            editTextMobileNo.setFocusable(true);
                            editTextMobileNo.requestFocus();
                            Objects.requireNonNull(editTextMobileNo.getText()).clear();
                        }




                        editTextMailId.setFocusableInTouchMode(false);
                        editTextMailId.setFocusable(false);


                        editTextMailDomains.setFocusableInTouchMode(false);
                        editTextMailDomains.setFocusable(false);


                        if (customer_email_id != null) {
                            editTextMailId.setText(customer_email_id);

                        } else {
                            editTextMailId.setText("");

                        }

                        if (customer_email_id_domain != null) {
                            editTextMailDomains.setText(customer_email_id_domain);

                        } else {
                            editTextMailDomains.setText("");

                        }

                    }

                }

                if(selectedItem.equals("EMAIL ID"))
                {

                    if (Objects.requireNonNull(editTextAreaCode.getText()).toString().trim().equals(""))
                    {
                        editTextAreaCode.setError("Invalid STD Code !!!");
                        editTextAreaCode.requestFocus();
                        vibrationService();

                    }

                    else if(Objects.requireNonNull(editTextLLNo.getText()).toString().trim().equals(""))
                    {
                        editTextLLNo.setError("Invalid Landline Number !!!");
                        editTextLLNo.requestFocus();
                        vibrationService();
                    }

                    else
                    {

                        final String MOBILE_NUMBER = "CUSTOMER_MOBILENUMBER";
                        final String customer_mobile_number = sharedPreferences.getString(MOBILE_NUMBER, null);

                        if(!editTextMailId.hasFocus())
                        {
                            editTextMailId.setFocusableInTouchMode(true);
                            editTextMailId.setFocusable(true);
                            editTextMailId.requestFocus();
                            Objects.requireNonNull(editTextMailId.getText()).clear();

                        }


                        if(!editTextMailDomains.hasFocus())
                        {
                            editTextMailDomains.setFocusableInTouchMode(true);
                            editTextMailDomains.setFocusable(true);
                            editTextMailDomains.getText().clear();

                        }



                        editTextMobileNo.setFocusableInTouchMode(false);
                        editTextMobileNo.setFocusable(false);


                        if (customer_mobile_number != null) {
                            editTextMobileNo.setText(customer_mobile_number);

                        } else {
                            editTextMobileNo.setText("");

                        }

                    }


                }


                if(selectedItem.equals("GO GREEN")) {

                    if (Objects.requireNonNull(editTextAreaCode.getText()).toString().trim().equals(""))
                    {
                        editTextAreaCode.setError("Invalid STD Code !!!");
                        editTextAreaCode.requestFocus();
                        vibrationService();

                    }

                    else if(Objects.requireNonNull(editTextLLNo.getText()).toString().trim().equals(""))
                    {
                        editTextLLNo.setError("Invalid Landline Number !!!");
                        editTextLLNo.requestFocus();
                        vibrationService();
                    }

                    else
                    {


                        final String EMAIL_ID = "CUSTOMER_EMAILID";
                        final String EMAIL_ID_DOMAIN = "CUSTOMER_EMAILID_DOMAIN";
                        final String MOBILE_NUMBER = "CUSTOMER_MOBILENUMBER";
                        final String customer_email_id = sharedPreferences.getString(EMAIL_ID, null);
                        final String customer_email_id_domain = sharedPreferences.getString(EMAIL_ID_DOMAIN, null);
                        final String customer_mobile_number = sharedPreferences.getString(MOBILE_NUMBER, null);


                        editTextMailId.setFocusableInTouchMode(false);
                        editTextMailId.setFocusable(false);



                        editTextMailDomains.setFocusableInTouchMode(false);
                        editTextMailDomains.setFocusable(false);



                        editTextMobileNo.setFocusableInTouchMode(false);
                        editTextMobileNo.setFocusable(false);



                        if (customer_email_id != null) {
                            editTextMailId.setText(customer_email_id);
                        } else {
                            editTextMailId.setText("");
                        }

                        if (customer_email_id_domain != null) {
                            editTextMailDomains.setText(customer_email_id_domain);
                        } else {
                            editTextMailDomains.setText("");
                        }


                        if (customer_mobile_number != null) {
                            editTextMobileNo.setText(customer_mobile_number);
                        } else {
                            editTextMobileNo.setText("");
                        }
                    }


                }
                if(selectedItem.equals("MOBILE AND EMAIL"))
                {


                    if (Objects.requireNonNull(editTextAreaCode.getText()).toString().trim().equals(""))
                    {
                        editTextAreaCode.setError("Invalid STD Code !!!");
                        editTextAreaCode.requestFocus();
                        vibrationService();

                    }

                    else if(Objects.requireNonNull(editTextLLNo.getText()).toString().trim().equals(""))
                    {
                        editTextLLNo.setError("Invalid Landline Number !!!");
                        editTextLLNo.requestFocus();
                        vibrationService();
                    }

                    else
                    {
                        if(!editTextMailId.hasFocus())
                        {
                            editTextMailId.setFocusableInTouchMode(true);
                            editTextMailId.setFocusable(true);
                            editTextMailId.requestFocus();
                            Objects.requireNonNull(editTextMailId.getText()).clear();

                        }

                        if(!editTextMailDomains.hasFocus())
                        {
                            editTextMailDomains.setFocusableInTouchMode(true);
                            editTextMailDomains.setFocusable(true);
                            editTextMailDomains.getText().clear();

                        }


                        if(!editTextMobileNo.hasFocus())
                        {
                            editTextMobileNo.setFocusableInTouchMode(true);
                            editTextMobileNo.setFocusable(true);
                            Objects.requireNonNull(editTextMobileNo.getText()).clear();

                        }

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void navigateToClosureActivity() {

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (Objects.requireNonNull(editTextAreaCode.getText()).toString().trim().equals(""))
        {
            vibrationService();
            editTextAreaCode.setError("Invalid STD CODE !!!");
            editTextAreaCode.requestFocus();
        }
        else if (Objects.requireNonNull(editTextLLNo.getText()).toString().trim().equals(""))
        {
            vibrationService();
            editTextLLNo.setError("Invalid Landline Number !!!");
            editTextLLNo.requestFocus();
        }
        else if (spinner_search_subscriber.getSelectedItem().toString().trim().equals("SELECT SERVICE TO UPDATE"))
        {
            Snackbar.make(findViewById(android.R.id.content), "Atleast Select One Service To Update...", Snackbar.LENGTH_LONG).show();
            vibrationService();
        }
        else if (spinner_search_subscriber.getSelectedItem().toString().trim().equals("MOBILE NUMBER") && Objects.requireNonNull(editTextMobileNo.getText()).toString().trim().equals(""))
        {
            vibrationService();
            editTextMobileNo.setError("Invalid Mobile Number !!!");
            editTextMobileNo.requestFocus();
        }
        else if (spinner_search_subscriber.getSelectedItem().toString().trim().equals("MOBILE AND EMAIL") && Objects.requireNonNull(editTextMobileNo.getText()).toString().trim().equals(""))
        {
            vibrationService();
            editTextMobileNo.setError("Invalid Mobile Number !!!");
            editTextMobileNo.requestFocus();
        }
        else if (spinner_search_subscriber.getSelectedItem().toString().trim().equals("MOBILE AND EMAIL") && Objects.requireNonNull(editTextMailId.getText()).toString().trim().equals(""))
        {
            vibrationService();
            editTextMailId.setError("Invalid MailId !!!");
            editTextMailId.requestFocus();
        }
        else if (spinner_search_subscriber.getSelectedItem().toString().trim().equals("MOBILE AND EMAIL") && editTextMailDomains.getText().toString().trim().equals(""))
        {
            vibrationService();
            editTextMailDomains.setError("Invalid MailId Domain !!!");
            editTextMailDomains.requestFocus();
        }


        else if (spinner_search_subscriber.getSelectedItem().toString().trim().equals("EMAIL ID") && Objects.requireNonNull(editTextMailId.getText()).toString().trim().equals(""))
        {
            vibrationService();
            editTextMailId.setError("Invalid MailId !!!");
            editTextMailId.requestFocus();
        }
        else if (spinner_search_subscriber.getSelectedItem().toString().trim().equals("EMAIL ID") && editTextMailDomains.getText().toString().trim().equals(""))
        {
            vibrationService();
            editTextMailDomains.setError("Invalid MailId Domain !!!");
            editTextMailDomains.requestFocus();
        }


        else if (spinner_search_subscriber.getSelectedItem().toString().trim().equals("GO GREEN") && !checkBoxGoGreen.isChecked())
        {
            Snackbar.make(findViewById(android.R.id.content), "Please Check Go Green Box...", Snackbar.LENGTH_LONG).show();
            vibrationService();
            checkBoxGoGreen.requestFocus();
            checkBoxGoGreen.startAnimation(anim);
        }

        else if(Objects.requireNonNull(editTextMobileNo.getText()).toString().trim().length()<10)
        {
            vibrationService();
            editTextMobileNo.setError("Invalid Mobile Number !!!");
            editTextMobileNo.requestFocus();
        }


        else
        {

            String STDCODE = editTextAreaCode.getText().toString().trim();
            String LANDLINENO = editTextLLNo.getText().toString().trim();
            String EMAILID = Objects.requireNonNull(editTextMailId.getText()).toString().trim();
            String DOMAINNAME = editTextMailDomains.getText().toString().trim();
            String mobileno = editTextMobileNo.getText().toString().trim();
            final String UPDATE_TYPE = String.valueOf(spinner_search_subscriber.getSelectedItem()).trim();

            String gogreen;

            if (checkBoxGoGreen.isChecked()) {
                gogreen = "Y";
            } else {
                gogreen = "N";
            }



            editor.putString(CUSTOMER_STDCODE,STDCODE);
            editor.putString(CUSTOMER_LLNO,LANDLINENO);
            editor.putString(CUSTOMER_MOBILENUMBER,mobileno);
            editor.putString(CUSTOMER_EMAILID,EMAILID);
            editor.putString(CUSTOMER_EMAILID_DOMAIN,DOMAINNAME);
            editor.putString(CUSTOMER_GO_GREEN,gogreen);
            editor.putString(CUSTOMER_UPDATE_TYPE,UPDATE_TYPE);


            editor.remove("CUSTOMER_MOBILEVERIFIED");
            editor.apply();

            Intent intent = new Intent(SearchSubscriber_Activity.this, INFO_ACTIVITY.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finishAffinity();
        }

    }


    class MyAdapter extends ArrayAdapter<String> {
        MyAdapter(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);


        }

        @Override
        public View getDropDownView(int position, View cnvtView,  ViewGroup prnt) {
            return getCustomView(position, prnt);

        }


        @Override
        public View getView(int pos, View cnvtView,  ViewGroup prnt) {
            return getCustomView(pos, prnt);


        }

        View getCustomView(int position, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.custom_spinner, parent, false);
            TextView main_text = mySpinner.findViewById(R.id.text_main_seen);
            main_text.setText(spinnerValues[position]);
            ImageView left_icon = mySpinner.findViewById(R.id.left_pic);
            left_icon.setImageResource(total_images[position]);
            return mySpinner;


        }

        public boolean isEnabled(int position) {
            return position != 0;
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


    private void progressDialogSendDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_search_subscriber_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(SearchSubscriber_Activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alertDialog = alert.create();
        alertDialog.show();
    }

    private void hideSoftInput() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showAlertDialogAuthorizationFailed()
    {
        if(alertDialog != null)
        {
            alertDialog.dismiss();
        }
        new AlertDialog.Builder(this)
                .setTitle("Authorization Failed !!!")
                .setMessage("Dear User, You Are Not Authorized To Modify Details OF Other SSA's Phone Number")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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

    private void getDataToDisplay()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String CUSTOMER_STDCODE = "CUSTOMER_STDCODE";
        final String CUSTOMER_LLNO = "CUSTOMER_LLNO";
        final String  CUSTOMER_NAME = "CUSTOMER_NAME";
        final String  CUSTOMER_ADDRESS = "CUSTOMER_ADDRESS";
        final String  CUSTOMER_EMAILID = "CUSTOMER_EMAILID";
        final String  CUSTOMER_EMAILID_DOMAIN = "CUSTOMER_EMAILID_DOMAIN";
        final String  CUSTOMER_MOBILENUMBER = "CUSTOMER_MOBILENUMBER";

        final String customer_stdcode = sharedPreferences.getString(CUSTOMER_STDCODE, null);
        final String customer_llno = sharedPreferences.getString(CUSTOMER_LLNO, null);
        final String customer_name = sharedPreferences.getString(CUSTOMER_NAME, null);
        final String customer_address = sharedPreferences.getString(CUSTOMER_ADDRESS, null);
        final String cstomer_emailid = sharedPreferences.getString(CUSTOMER_EMAILID, null);
        final String customer_emailid_domain = sharedPreferences.getString(CUSTOMER_EMAILID_DOMAIN, null);
        final String customer_mobilenumber = sharedPreferences.getString(CUSTOMER_MOBILENUMBER, null);

        editTextAreaCode.setText(customer_stdcode);
        editTextLLNo.setText(customer_llno);
        editTextName.setText(customer_name);
        editTextAddress.setText(customer_address);
        editTextMailId.setText(cstomer_emailid);
        editTextMailDomains.setText(customer_emailid_domain);
        editTextMobileNo.setText(customer_mobilenumber);

    }

    private void setDataToDisplay()
    {

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        editTextAreaCode.setText(getIntent().getStringExtra("areaCode"));
        editTextLLNo.setText(getIntent().getStringExtra("phoneNo"));
        editTextName.setText(getIntent().getStringExtra("customerName"));
        editTextAddress.setText(getIntent().getStringExtra("address"));
        String mailId = getIntent().getStringExtra("mailId");
        String domainName = getIntent().getStringExtra("domainName");
        String mobile = getIntent().getStringExtra("mobile");

        editor.putString(CUSTOMER_STDCODE,getIntent().getStringExtra("areaCode"));
        editor.putString(CUSTOMER_LLNO,getIntent().getStringExtra("phoneNo"));
        editor.putString(CUSTOMER_NAME,getIntent().getStringExtra("customerName"));
        editor.putString(CUSTOMER_ADDRESS,getIntent().getStringExtra("address"));


        if(getIntent().getStringExtra("areaCode") != null && getIntent().getStringExtra("phoneNo") != null)
        {
            spinner_search_subscriber.setEnabled(true);
        }

        if (mailId != null  || domainName != null) {

            editTextMailId.setText(mailId);
            editTextMailDomains.setText(domainName);

            editor.putString(CUSTOMER_EMAILID,mailId);
            editor.putString(CUSTOMER_EMAILID_DOMAIN,domainName);
            editor.apply();
        }

        else
        {
            editTextMailId.setText(mailId);
            editTextMailDomains.setText(domainName);
        }

        if (mobile != null) {
            if (mobile.equals("NA") ) {

                editTextMobileNo.setText("");

            }

            else
            {
                editTextMobileNo.setText(mobile);
                editor.putString(CUSTOMER_MOBILENUMBER,mobile);
                editor.apply();
            }

            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String LINEMAN_WL = "LINEMAN_WL";
        final String lineman_wl = sharedPreferences.getString(LINEMAN_WL, null);

        if(lineman_wl != null && lineman_wl.equals("LINEMAN_WL"))
        {
            Intent intent = new Intent(SearchSubscriber_Activity.this, LinemanWorklist_Activity.class);
            startActivity(intent);
            finishAffinity();
        }

        else
        {
            Intent intent = new Intent(SearchSubscriber_Activity.this, DashBoard_Activity.class);
            startActivity(intent);
            finishAffinity();
        }

    }
}

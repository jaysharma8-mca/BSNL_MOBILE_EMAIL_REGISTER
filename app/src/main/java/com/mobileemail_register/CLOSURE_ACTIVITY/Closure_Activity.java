package com.mobileemail_register.CLOSURE_ACTIVITY;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.NetworkError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mobileemail_register.DASHBOARD_ACTIVITY.DashBoard_Activity;
import com.mobileemail_register.LINEMAN_WL_ACTIVITY.LinemanWorklist_Activity;
import com.mobileemail_register.LINEMAN_WL_ACTIVITY.Linemanworklist_SQLiteHelper;
import com.mobileemail_register.MISC.NetworkMonitor;
import com.mobileemail_register.MISC.constants;
import com.mobileemail_register.R;
import com.mobileemail_register.SEARCH_SUBSCRIBER_ACTIVITY.SearchSubscriber_Activity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


@SuppressWarnings({"Convert2Lambda"})
public class Closure_Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private static final String TAG = "Closure_Activity";
    private GoogleApiClient mGoogleApiClient;
    //private com.google.android.gms.location.LocationListener listener;


    private EditText editTextVerificationCode;

    private Button buttonOk;
    private Button buttonCancel;

    private ImageView imageViewSendCode;
    private ImageView imageViewResendCode;
    private ImageView imageViewCodeVerified;
    private ImageView imageViewDKYCDocument;
    private ImageView imageViewDeleteButton;
    private ImageView imageView;



    private ProgressBar uploadProgressBar;
    private TextView textViewImageSize;
    private TextView textViewUploadProgress;
    private TextView textViewCodeVerified;
    private TextView textViewMessage;

    private Spinner spinnerDKYCDocumentsList;

    private CheckBox checkBoxKyc;

    private static final String SP_MEREG = "SP_MEREG";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String CUSTOMER_SMSVERIFICATION_CODE = "CUSTOMER_SMSVERIFICATION_CODE";
    private static final String IMAGE_URI = "IMAGE_URI";
    private static final String CUSTOMER_FILEUPLOADED = "CUSTOMER_FILEUPLOADED";
    private static final String CUSTOMER_DOCUPLOADED = "CUSTOMER_DOCUPLOADED";
    private static final String CUSTOMER_MOBILEVERIFIED = "CUSTOMER_MOBILEVERIFIED";
    private static final String CUSTOMER_DOCTYPE = "CUSTOMER_DOCTYPE";

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private SharedPreferences sharedPreferences;

    private final String[] spinnerValues = {"SELECT KYC DOCUMENT",
            "AADHAR CARD",
            "DRIVING LICENCE",
            "INDIAN PASSPORT",
            "PAN CARD",
            "VOTER ID",
            "AUTHORIZED COMPANY LETTER",
            "SHOP ACT LICENCE",
            "GRAMPANCHAYAT NOC",
            "RENT AGREEMENT"};

    private final int[] total_images = {R.drawable.ic_kyc_image,
            R.drawable.ic_aadhar_card_image,
            R.drawable.ic_drivers_licence_image,
            R.drawable.ic_indian_passport_image,
            R.drawable.ic_pan_card_image,
            R.drawable.ic_voter_id_image,
            R.drawable.ic_company_letterhead_image,
            R.drawable.ic_shopact_licence_image,
            R.drawable.ic_noc_grampanchayat_image,
            R.drawable.ic_rent_agreement_image};


    private AlertDialog alertDialog,alert;
    private Dialog dialog, progressDialog;

    private Uri mCropImageUri;
    private Bitmap bitmap;
    byte[] image;

    private int i = 0;
    private final Handler handler = new Handler();

    final Animation anim = new AlphaAnimation(0.0f, 1.0f);

    private PendingIntent pendingIntent;


    Linemanworklist_SQLiteHelper linemanworklist_sqLiteHelper;


    ProgressBar mProgressBar, mProgressBar1;
    private TextView textViewShowTime;
    private CountDownTimer countDownTimer;
    private long totalTimeCountInMilliseconds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closure);

        AppCenter.start(getApplication(), "f7271b3f-27aa-4985-8654-37f1347f4bf4",
                Analytics.class, Crashes.class);

        //mLatitudeTextView = (TextView) findViewById((R.id.latitude_textview));
        //mLongitudeTextView = (TextView) findViewById((R.id.longitude_textview));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //LocationManager mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone

        EditText editTextLLNO = findViewById(R.id.editTextLLNO);
        EditText editTextMobileNo = findViewById(R.id.editTextMobileNo);
        EditText editTextMailId = findViewById(R.id.editTextMailId);
        EditText editTextGoGreen = findViewById(R.id.editTextGoGreen);
        EditText editTextUpdateType = findViewById(R.id.editTextUpdateType);

        editTextVerificationCode = findViewById(R.id.editTextVerificationCode);

        imageViewSendCode = findViewById(R.id.imageViewSendCode);
        imageViewResendCode = findViewById(R.id.imageViewResendCode);
        imageViewDKYCDocument = findViewById(R.id.imageViewDKYCDocument);
        imageViewDeleteButton = findViewById(R.id.imageViewDeleteButton);
        textViewImageSize = findViewById(R.id.textViewImageSize);
        textViewCodeVerified = findViewById(R.id.textViewCodeVerified);
        textViewMessage = findViewById(R.id.textViewMessage);

        mProgressBar = findViewById(R.id.progressbar_timerview);
        mProgressBar1 = findViewById(R.id.progressbar1_timerview);
        textViewShowTime = findViewById(R.id.textViewShowTime);
        imageViewCodeVerified= findViewById(R.id.imageViewOtpVerified);

        spinnerDKYCDocumentsList = findViewById(R.id.spinnerDKYCDocumentsList);
        checkBoxKyc = findViewById(R.id.checkBoxKyc);


        sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String CUSTOMER_STDCODE = "CUSTOMER_STDCODE";
        final String CUSTOMER_LLNO = "CUSTOMER_LLNO";
        final String CUSTOMER_EMAILID = "CUSTOMER_EMAILID";
        final String CUSTOMER_EMAILID_DOMAIN = "CUSTOMER_EMAILID_DOMAIN";
        final String CUSTOMER_MOBILENUMBER = "CUSTOMER_MOBILENUMBER";
        final String CUSTOMER_GO_GREEN = "CUSTOMER_GO_GREEN";
        final String CUSTOMER_UPDATE_TYPE = "CUSTOMER_UPDATE_TYPE";
        final String customer_std_code = sharedPreferences.getString(CUSTOMER_STDCODE, null);
        final String customer_landline_no = sharedPreferences.getString(CUSTOMER_LLNO, null);
        final String customer_mobile_number = sharedPreferences.getString(CUSTOMER_MOBILENUMBER, null);
        final String customer_email_id = sharedPreferences.getString(CUSTOMER_EMAILID, null);
        final String customer_email_id_domain = sharedPreferences.getString(CUSTOMER_EMAILID_DOMAIN, null);
        final String customer_go_green = sharedPreferences.getString(CUSTOMER_GO_GREEN, null);
        final String customer_update_type = sharedPreferences.getString(CUSTOMER_UPDATE_TYPE, null);

        if(customer_std_code != null && customer_landline_no != null && customer_mobile_number != null)
        {
            String customerLandLineNumber = customer_std_code+"-"+customer_landline_no;
            editTextLLNO.setText(customerLandLineNumber);
            editTextMobileNo.setText(customer_mobile_number);
        }

        else
        {
            editTextLLNO.setText("");
            editTextMobileNo.setText("");
        }


        String customerEmailId = customer_email_id+"@"+customer_email_id_domain;
        editTextMailId.setText(customerEmailId);
        editTextGoGreen.setText(customer_go_green);
        editTextUpdateType.setText(customer_update_type);

        spinnerDKYCDocumentsList.setAdapter(new MyAdapter(this, R.layout.custom_spinner, spinnerValues));

        checkAndRequestPermissions();

        //getLocation();

        verifyreceivedSmsCode();

        dialogDeclaration();

        flagUpdation();

        spinnerActivity();

        anim.setDuration(1300); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        Intent alarmIntent = new Intent(Closure_Activity.this, NetworkMonitor.class);
        pendingIntent = PendingIntent.getBroadcast(Closure_Activity.this, 0, alarmIntent, 0);

        start();

        linemanworklist_sqLiteHelper = new Linemanworklist_SQLiteHelper(getApplicationContext());



    }

    public void sendSmsVerificationCode(View view) {
        sendSmsToCustomer();
    }

    public void resendSmsVerificationCode(View view) {

        resendSmsCodeToCustomer();
    }




    public void deleteKYDDocument(View view) {

        imageViewDKYCDocument.setImageBitmap(null);

        if (!textViewImageSize.getText().toString().equals("")) {
            textViewImageSize.setText("");
        }
    }

    public void validationKYCCheckbox(View view) {

        if (checkBoxKyc.isChecked()) {
            checkBoxKyc.clearAnimation();
            anim.cancel();
            anim.reset();
        }
    }


    public void callCustomerActivity(View view) {
        callCustomerNumber();
    }

    public void submitDetailsToAppServer(View view) {

        saveToAppServerLocalStorage();
    }



    public void selectImageFromMobile(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("IMAGE_URI");
        editor.apply();

        final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {

            onSelectImageClick();

        }

    }


    private void onSelectImageClick() {
        CropImage.startPickImageActivity(this);

    }

    @Override
    @SuppressLint({"NewApi", "SetTextI18n"})
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);

            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mCropImageUri = result.getUri();

                dialog.show();


                imageView.setImageURI(mCropImageUri);

                //imageViewDKYCDocument.setImageURI(mCropImageUri);

                /*try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),mCropImageUri);
                    imageViewDKYCDocument.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/


                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new ImageRendering().execute();

                    }
                });


                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(findViewById(android.R.id.content), "IMAGE Rendering Cancelled...", Snackbar.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });


                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(String.valueOf(mCropImageUri)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(this, "Cropping successful" + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed...", Toast.LENGTH_LONG).show();


            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } /*else {
            //Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }*/
    }


    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }




    @SuppressLint("StaticFieldLeak")
    class ImageRendering extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            uploadProgressBar.setProgress(0);
            Log.d(TAG + " PreExceute","On pre Exceute......");

            i = uploadProgressBar.getProgress();
            new Thread(new Runnable() {
                public void run() {
                    while (i < 100) {
                        i += 1;

                        handler.post(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            public void run() {
                                uploadProgressBar.setProgress(i);
                                textViewUploadProgress.setText(i+"/"+uploadProgressBar.getMax()+"%");
                            }
                        });
                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//Compression quality, here 100 means no compression, the storage of compressed data to baos
            int options = 90;

            while (baos.toByteArray().length / 1024 > 400) {
                //Loop if compressed picture is greater than 400kb, than to compression

                baos.reset();//Reset baos is empty baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//The compression options%, storing the compressed data to the baos
                options -= 10;
                //Every time reduced by 10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//The storage of compressed data in the baos to ByteArrayInputStream
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//The ByteArrayInputStream data generation
            image = baos.toByteArray();
            //imgString = Base64.encodeToString(image, Base64.NO_WRAP);




            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, null, null);

            sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(IMAGE_URI, path);
            editor.apply();


            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer...a) {
            super.onProgressUpdate(a);
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG + " onPostExecute", "" + result);
            final String IMAGE_URI = "IMAGE_URI";
            final String image_uri = sharedPreferences.getString(IMAGE_URI, null);

            mCropImageUri = Uri.parse(image_uri);
            String filePath;
            filePath = PATH_UTIL.getPath(getApplicationContext(), mCropImageUri);


            File img = null;
            if (filePath != null) {
                img = new File(filePath);
            }
            long length = 0;
            if (img != null) {
                length = img.length() / 1024;
            }
            textViewImageSize.setText("File Size :"+" "+length+" "+"KB");


            //Bitmap bitmap = BitmapFactory.decodeByteArray(kycDocImage, 0, kycDocImage.length);
            //holder.imageViewKYCdoc.setImageBitmap(bitmap);

            Glide.with(getApplicationContext()).load(mCropImageUri).into(imageViewDKYCDocument);


            //imageViewDKYCDocument.setImageURI(mCropImageUri);

            dialog.dismiss();
            progressDialog.dismiss();
        }
    }


    class MyAdapter extends ArrayAdapter<String> {
        MyAdapter(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);


        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, prnt);

        }


        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
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


    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void progressDialogSendDetails() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.progress_bar_alert_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(Closure_Activity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final TextView textViewAlertDialog = alertLayout.findViewById(R.id.textViewAlertDialog);
        textViewAlertDialog.setText(R.string.send_sms_code);
        alertDialog = alert.create();
        alertDialog.show();
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


    private void verifySmsCode()
    {
        SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String CUSTOMER_SMSVERIFICATION_CODE = "CUSTOMER_SMSVERIFICATION_CODE";
        final String sms_verification_code = sharedPreference.getString(CUSTOMER_SMSVERIFICATION_CODE, null);

        String verification_code = editTextVerificationCode.getText().toString().trim();

        if(sms_verification_code != null && sms_verification_code.equals(verification_code))
        {
            imageViewCodeVerified.setVisibility(View.VISIBLE);
            editTextVerificationCode.setEnabled(false);
            countDownTimerStop();
            imageViewSendCode.setVisibility(View.INVISIBLE);
            imageViewResendCode.setVisibility(View.INVISIBLE);
            textViewCodeVerified.setVisibility(View.VISIBLE);

            editor.putString(CUSTOMER_MOBILEVERIFIED,"Y");
            editor.apply();
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

    private void callCustomerNumber()
    {
        sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);

        final String std_code = "CUSTOMER_STDCODE";
        final String ll_no = "CUSTOMER_LLNO";
        final String mobileNo = "CUSTOMER_MOBILENUMBER";
        final String STD_CODE = sharedPreferences.getString(std_code, null);
        final String LL_NO = sharedPreferences.getString(ll_no, null);
        final String customer_mobile_closure_data = sharedPreferences.getString(mobileNo, null);

        String customer_landline_number_closure_data = STD_CODE+"-"+LL_NO;

        final AlertDialog.Builder build = new AlertDialog.Builder(Closure_Activity.this);

        build.setTitle("CALLING OPTIONS");
        build.setIcon(R.drawable.phone_receiver);


        final String[] call_options = new String[]{
                "LANDLINE NO" + " " + "-" + customer_landline_number_closure_data,
                "MOBILE NO" + " " + "-" + customer_mobile_closure_data
        };

        // Item click listener

        build.setSingleChoiceItems(call_options, // Items list
                -1, // Index of checked item (-1 = no selection)
                (dialogInterface, i) -> {
                    // Get the alert dialog selected item's text
                    String selectedItem = Arrays.asList(call_options).get(i);

                    if (selectedItem.equals("LANDLINE NO" + " " + "-" + customer_landline_number_closure_data)) {


                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        String p = "tel:" + customer_landline_number_closure_data;
                        intent.setData(Uri.parse(p));
                        Closure_Activity.this.startActivity(intent);
                        alert.dismiss();


                    } else if (selectedItem.equals("MOBILE NO" + " " + "-" + customer_mobile_closure_data)) {

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        String p = "tel:" + customer_mobile_closure_data;
                        intent.setData(Uri.parse(p));
                        Closure_Activity.this.startActivity(intent);
                        alert.dismiss();

                    }
                });

        build.setNegativeButton("Cancel", (dialogInterface, i) -> alert.dismiss());

        // Create the alert dialog
        alert = build.create();

        // Finally, display the alert dialog
        alert.show();
    }

    @SuppressLint("SetTextI18n")
    private void flagUpdation()
    {

        sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String CUSTOMER_UPDATE_TYPE = "CUSTOMER_UPDATE_TYPE";
        final String type_of_updation = sharedPreferences.getString(CUSTOMER_UPDATE_TYPE, null);

        if (type_of_updation != null && type_of_updation.equals("GO GREEN")) {
            imageViewDKYCDocument.setEnabled(false);
            textViewMessage.setText("DKYC NOT REQUIRED...");
            imageViewDeleteButton.setEnabled(false);
            spinnerDKYCDocumentsList.setEnabled(false);

            editor.putString(CUSTOMER_FILEUPLOADED, "N");
            editor.putString(CUSTOMER_DOCUPLOADED, "NA");
            editor.apply();
            imageViewDKYCDocument.getBackground().setColorFilter(null);
            spinnerDKYCDocumentsList.getBackground().setColorFilter(null);



        } else if (type_of_updation != null && type_of_updation.equals("EMAIL ID")) {
            imageViewDKYCDocument.setEnabled(false);
            textViewMessage.setText("DKYC NOT REQUIRED...");
            imageViewDeleteButton.setEnabled(false);
            spinnerDKYCDocumentsList.setEnabled(false);

            editor.putString(CUSTOMER_FILEUPLOADED, "N");
            editor.putString(CUSTOMER_DOCUPLOADED, "NA");
            editor.apply();

            imageViewDKYCDocument.getBackground().setColorFilter(null);
            spinnerDKYCDocumentsList.getBackground().setColorFilter(null);

        } else {
            editor.putString(CUSTOMER_FILEUPLOADED, "Y");
            editor.apply();
        }
    }

    private void spinnerActivity()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        spinnerDKYCDocumentsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                editor.putString(CUSTOMER_DOCTYPE, selectedItem);
                editor.apply();
                //Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendSmsToCustomer()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String CUSTOMER_MOBILENUMBER = "CUSTOMER_MOBILENUMBER";
        final String customer_mobile_number = sharedPreferences.getString(CUSTOMER_MOBILENUMBER, null);

        if (checkInternetConnection())
        {

            if(countDownTimer != null)
            {
                countDownTimer.cancel();
            }

            Random random = new Random();
            @SuppressLint("DefaultLocale") String random_verification_code = String.format("%04d", random.nextInt(10000));
            String MESSAGE = ("Dear Customer"+" "+random_verification_code+" "+"is the BSNL Verification code for Go Green. Receive Bills through E-Mail and Mobile and get discount of Rs. 10 on each Bill Subject to maximum of Rs. 100/- Thank you for availing BSNL Services.");

            progressDialogSendDetails();



            RequestQueue queue = Volley.newRequestQueue(Closure_Activity.this);
            //String URL = "https://fms.bsnl.in/fmswebservices/rest/user/sendsms/mobile/"+customer_mobile_number+"/otpmsg/"+MESSAGE+"";
            String URL = "https://fms.bsnl.in/fmswebservices/rest/user/sendsms/mobile/" + customer_mobile_number + "/otpmsg/" + MESSAGE + "";
            URL = URL.replaceAll(" ", "%20");
            StringRequest sr = new StringRequest(Request.Method.GET, URL,
                    response -> {
                        if(response.contains("ORA-"))
                        {
                            if(alertDialog != null)
                            {
                                alertDialog.dismiss();
                            }
                            countDownTimerStop();


                            AlertDialog.Builder builder = new AlertDialog.Builder(Closure_Activity.this);

                            builder.setTitle("Code Sent Failed");
                            builder.setMessage("FMS Database Connectivity Error... Please Try again After Sometime...");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                            builder.show();

                        }

                        else
                        {
                            try {
                                //getting the whole json object from the response
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("STATUS").contentEquals("SUCCESS")) {
                                    JSONArray Jarray = jsonObject.getJSONArray("ROWSET");


                                    for (int i = 0; i < Jarray.length(); i++) {


                                        Toast.makeText(getApplicationContext(), "Verification Code Has Been Sent To Customer...", Toast.LENGTH_LONG).show();
                                        if(alertDialog != null)
                                        {
                                            alertDialog.dismiss();
                                        }
                                        countDownTimerStart();
                                        editor.putString(CUSTOMER_SMSVERIFICATION_CODE,random_verification_code);
                                        editor.apply();

                                    }


                                } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                    if(alertDialog != null)
                                    {
                                        alertDialog.dismiss();
                                    }
                                    countDownTimerStop();


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(Closure_Activity.this, "Database Connectivity Error!!! Check Your Network Connection And Try Again...", Toast.LENGTH_LONG).show();
                            if(alertDialog != null)
                            {
                                alertDialog.dismiss();
                            }
                            countDownTimerStop();


                        }

                    }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("EKEY", "b28272183c64fcb45b11d9098a7dd97df51f89bc1bae9448e4126258fd9446d1");
                    return headers;
                }


            };

            sr.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

            queue.add(sr);
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


    private void resendSmsCodeToCustomer() {
        if (checkInternetConnection())
        {
            if(countDownTimer != null)
            {
                countDownTimer.cancel();
            }

            sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
            final String CUSTOMER_MOBILENUMBER = "CUSTOMER_MOBILENUMBER";
            final String customer_mobile_number = sharedPreferences.getString(CUSTOMER_MOBILENUMBER, null);

            final String CUSTOMER_SMSVERIFICATION_CODE = "CUSTOMER_SMSVERIFICATION_CODE";
            final String customer_verification_code = sharedPreferences.getString(CUSTOMER_SMSVERIFICATION_CODE, null);

            progressDialogSendDetails();

            String MESSAGE = ("Dear Customer"+" "+customer_verification_code+" "+"is the BSNL Verification code for Go Green. Receive Bills through E-Mail and Mobile and get discount of Rs. 10 on each Bill Subject to maximum of Rs. 100/- Thank you for availing BSNL Services.");

            RequestQueue queue = Volley.newRequestQueue(Closure_Activity.this);
            String URL = "https://fms.bsnl.in/fmswebservices/rest/user/sendsms/mobile/" + customer_mobile_number + "/otpmsg/" + MESSAGE + "";
            URL = URL.replaceAll(" ", "%20");
            StringRequest sr = new StringRequest(Request.Method.GET, URL,
                    response -> {

                        if (response.contains("ORA-")) {
                            if(alertDialog != null)
                            {
                                alertDialog.dismiss();
                            }
                            countDownTimerStop();


                            AlertDialog.Builder builder = new AlertDialog.Builder(Closure_Activity.this);

                            builder.setTitle("Code Sent Failed");
                            builder.setMessage("FMS Database Connectivity Error... Please Try again After Sometime...");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                            builder.show();

                        } else
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("STATUS").contentEquals("SUCCESS")) {
                                    JSONArray Jarray = jsonObject.getJSONArray("ROWSET");


                                    for (int i = 0; i < Jarray.length(); i++) {

                                        Toast.makeText(getApplicationContext(), "Verification Code Has Been Sent To Customer's" + " " + ":-" + " " + customer_mobile_number + " " + "Mobile Number !!!", Toast.LENGTH_LONG).show();
                                        if(alertDialog != null)
                                        {
                                            alertDialog.dismiss();
                                        }
                                        countDownTimerStart();


                                    }


                                } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                    if(alertDialog != null)
                                    {
                                        alertDialog.dismiss();
                                    }
                                    countDownTimerStop();


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(Closure_Activity.this, "Database Connectivity Error!!! Check Your Network Connection And Try Again...", Toast.LENGTH_LONG).show();
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

            sr.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
            queue.add(sr);
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


    private void dialogDeclaration()
    {
        dialog = new Dialog(Closure_Activity.this);
        dialog.setContentView(R.layout.display_captured_image);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.setCancelable(false);

        progressDialog = new Dialog(Closure_Activity.this);
        progressDialog.setContentView(R.layout.alert_dialog_render_image_progress_bar);
        progressDialog.setCanceledOnTouchOutside(false);


        imageView = dialog.findViewById(R.id.imageView);
        buttonCancel = dialog.findViewById(R.id.buttonCancel);
        buttonOk = dialog.findViewById(R.id.buttonOk);
        uploadProgressBar = progressDialog.findViewById(R.id.uploadProgressBar);
        textViewUploadProgress = progressDialog.findViewById(R.id.textViewUploadProgress);
    }


    private void saveToAppServerLocalStorage() {

        sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);

        final String CUSTOMER_STDCODE = "CUSTOMER_STDCODE";
        final String CUSTOMER_LLNO = "CUSTOMER_LLNO";
        final String CUSTOMER_EMAILID = "CUSTOMER_EMAILID";
        final String CUSTOMER_EMAILID_DOMAIN = "CUSTOMER_EMAILID_DOMAIN";

        final String CUSTOMER_MOBILENUMBER = "CUSTOMER_MOBILENUMBER";
        final String CUSTOMER_GO_GREEN = "CUSTOMER_GO_GREEN";
        final String PERSONAL_NUMBER = "PERSONAL_NUMBER";
        final String CUSTOMER_MOBILEVERIFIED = "CUSTOMER_MOBILEVERIFIED";

        final String CUSTOMER_FILEUPLOADED = "CUSTOMER_FILEUPLOADED";
        final String LATITUDE = "LATITUDE";
        final String LONGITUDE = "LONGITUDE";
        String customer_emailid_validated = "N";

        final String CUSTOMER_DOCTYPE = "CUSTOMER_DOCTYPE";
        final String CUSTOMER_UPDATE_TYPE = "CUSTOMER_UPDATE_TYPE";


        final String customer_std_code = sharedPreferences.getString(CUSTOMER_STDCODE, null);
        final String customer_landline_number = sharedPreferences.getString(CUSTOMER_LLNO, null);

        String customer_land_line_number = customer_std_code+"-"+customer_landline_number;

        final String customer_emailid = sharedPreferences.getString(CUSTOMER_EMAILID, null);
        final String customer_emailid_domain = sharedPreferences.getString(CUSTOMER_EMAILID_DOMAIN, null);

        String customer_email_id = customer_emailid+"@"+customer_emailid_domain;

        final String customer_mobilenumber = sharedPreferences.getString(CUSTOMER_MOBILENUMBER, null);
        final String customer_gogreen = sharedPreferences.getString(CUSTOMER_GO_GREEN, null);
        final String user_personalnumber = sharedPreferences.getString(PERSONAL_NUMBER, null);
        String customer_mobileverified = sharedPreferences.getString(CUSTOMER_MOBILEVERIFIED, null);

        final String customer_fileuploaded = sharedPreferences.getString(CUSTOMER_FILEUPLOADED, null);
        final String latitude = sharedPreferences.getString(LATITUDE, null);
        final String longitude = sharedPreferences.getString(LONGITUDE,null);
        final String customer_doctype = sharedPreferences.getString(CUSTOMER_DOCTYPE, null);

        final String customer_updatetype = sharedPreferences.getString(CUSTOMER_UPDATE_TYPE, null);

        if(customer_mobileverified == null)
        {
            customer_mobileverified = "N";
        }


        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
        String dateTime = simpleDateFormat.format(new Date());


        //Toast.makeText(getApplicationContext(),DOCTYPE,Toast.LENGTH_LONG).show();



        if(customer_updatetype != null && customer_updatetype.equals("GO GREEN") || customer_updatetype != null && customer_updatetype.equals("EMAIL ID")) {

            sharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove("IMAGE_URI");
            edit.apply();

            image = null;

           /* if (editTextVerificationCode.isEnabled()) {
                Snackbar.make(findViewById(android.R.id.content), "Kindly Verify Customer's MOBILE Number...", Snackbar.LENGTH_LONG).show();
            } else if (editTextVerificationCode.getText().toString().trim().equals("")) {
                Snackbar.make(findViewById(android.R.id.content), "Kindly Verify Customer's MOBILE Number...", Snackbar.LENGTH_LONG).show();
            } else */if (!checkBoxKyc.isChecked()) {
                Toast.makeText(getApplicationContext(), "Kindly check the KYC Checkbox Validation !!!", Toast.LENGTH_LONG).show();
                checkBoxKyc.startAnimation(anim);
            } else {

                uploadDataToServer(customer_land_line_number, customer_email_id,
                        customer_mobilenumber, customer_gogreen,
                        user_personalnumber, customer_emailid_validated, customer_mobileverified,
                        customer_fileuploaded, latitude, longitude, customer_doctype,
                        customer_updatetype, image, dateTime);

            }

        }
        else{

            final String IMAGE_URI = "IMAGE_URI";
            final String image_uri = sharedPreferences.getString(IMAGE_URI, null);

            if(image_uri != null)

            {
                try {

                    //Toast.makeText(getApplicationContext(),image_uri,Toast.LENGTH_LONG).show();

                    mCropImageUri = Uri.parse(image_uri);


                    InputStream iStream = getContentResolver().openInputStream(mCropImageUri);
                    if (iStream != null) {
                        image = Utils.getBytes(iStream);
                    }


                } catch (IOException ioe) {
                    //Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());


                }
            }

            /*if (editTextVerificationCode.isEnabled())
            {
                Snackbar.make(findViewById(android.R.id.content), "Kindly Verify Customer's MOBILE Number...", Snackbar.LENGTH_LONG).show();
            }

            else if (editTextVerificationCode.getText().toString().trim().equals(""))
            {
                Snackbar.make(findViewById(android.R.id.content), "Kindly Verify Customer's MOBILE Number...", Snackbar.LENGTH_LONG).show();
            }

            else */if (imageViewDKYCDocument.getDrawable() == null)
            {
                Snackbar.make(findViewById(android.R.id.content), "Please Select KYC Document...", Snackbar.LENGTH_LONG).show();
            }

            else if (spinnerDKYCDocumentsList.getSelectedItem().toString().trim().equals("SELECT KYC DOCUMENT"))
            {
                Snackbar.make(findViewById(android.R.id.content), "Please Select Type Of KYC Document From DropDown List...", Snackbar.LENGTH_LONG).show();
            }

            else if (!checkBoxKyc.isChecked())
            {

                Snackbar.make(findViewById(android.R.id.content), "Kindly check the KYC Checkbox Validation...", Snackbar.LENGTH_LONG).show();
                checkBoxKyc.startAnimation(anim);
            }

            else
            {

                uploadDataToServer(customer_land_line_number, customer_email_id,
                        customer_mobilenumber, customer_gogreen,
                        user_personalnumber, customer_emailid_validated, customer_mobileverified,
                        customer_fileuploaded, latitude, longitude, customer_doctype,
                        customer_updatetype, image, dateTime);

                  /*saveToAppServerMobileEmail(PHONENO,EMAILID,MOBILENO,GOGREENSTATUS,
                          UPDATEDBY,emailidvalid,MOBILENOVALID,
                          FILEUPLOADED,LATITUDE,LONGITUDE,doctype,
                          UPDATETYPE,image,dateTime);*/
            }

        }
    }


    private void uploadDataToServer(String customer_land_line_number, String customer_email_id, String customer_mobilenumber,
                                    String customer_gogreen, String user_personalnumber, String customer_emailid_validated,
                                    String cuatomer_mobileverified, String customer_fileuploaded, String latitude, String longitude,
                                    String customer_doctype, String customer_updatetype, byte[] image, String dateTime)
    {

        SharedPreferences getSharedPreferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        final String CUSTOMER_RANDOM_STRING = "CUSTOMER_RANDOM_STRING";
        final String customer_random_string = getSharedPreferences.getString(CUSTOMER_RANDOM_STRING, null);

        if(checkInternetConnection())
        {
            String uploadXML =
                    "<FileUploadModel>\n" +
                            "<phoneNo>" + customer_land_line_number + "</phoneNo>\n" +
                            "<emailId>" + customer_email_id + "</emailId>\n" +
                            "<mobileNo>" + customer_mobilenumber + "</mobileNo>\n" +
                            "<goGreenStatus>" + customer_gogreen + "</goGreenStatus>\n" +
                            "<updatedBy>" + user_personalnumber + "</updatedBy>\n" +
                            "<emailIdValid>" + customer_emailid_validated + "</emailIdValid>\n" +
                            "<mobileNoValid>" + cuatomer_mobileverified + "</mobileNoValid>\n" +
                            "<fileUploaded>" + customer_fileuploaded + "</fileUploaded>\n" +
                            "<latitude>" + latitude + "</latitude>\n" +
                            "<longitude>" + longitude + "</longitude>\n" +
                            "<docType>" + customer_doctype + "</docType>\n" +
                            "<updateType>" + customer_updatetype + "</updateType>\n" +
                            "</FileUploadModel>";

            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View alertLayout = inflater.inflate(R.layout.alert_dialog_upload_progress_bar, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(Closure_Activity.this);
            final ProgressBar uploadProgressBar = alertLayout.findViewById(R.id.uploadProgressBar);
            final TextView textViewUploadProgress = alertLayout.findViewById(R.id.textViewUploadProgress);
            alert.setView(alertLayout);
            alert.setCancelable(false);
            AlertDialog dialog = alert.create();
            dialog.show();

            //Toast.makeText(getApplicationContext(),uploadXML,Toast.LENGTH_LONG).show();

            uploadProgressBar.setProgress(0);

            if(customer_fileuploaded != null && customer_fileuploaded.contains("N"))
            {
                Log.d(TAG, "W/O DOC EXECUTED");
                String URL = "https://fms.bsnl.in/fmswebservices/rest/update/emailmobilereq";

                RequestQueue queue = Volley.newRequestQueue(Closure_Activity.this);

                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {

                                if (response.toString().contains("ORA-")) {
                                    alertDialog.dismiss();
                                    countDownTimerStop();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Closure_Activity.this);

                                    builder.setTitle("Registration Failed");
                                    builder.setMessage("FMS Database Connectivity Error... Please Try again After Sometime...");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });

                                    builder.show();


                                }

                                else if(response.statusCode == 200)
                                {

                                    try {

                                        queue.getCache().clear();
                                        JSONObject jsonObject = new JSONObject(new String(response.data));
                                        //Toast.makeText(getApplicationContext(), jsonObject.getString("REMARKS"), Toast.LENGTH_SHORT).show();
                                        if (jsonObject.getString("STATUS").contentEquals("SUCCESS")) {
                                            JSONArray Jarray = jsonObject.getJSONArray("ROWSET");


                                            for (int i = 0; i < Jarray.length(); i++) {
                                                jsonObject = Jarray.getJSONObject(i);

                                                String REMARKS = jsonObject.getString("REMARKS");

                                                SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                                                final String TRANSITION_PAGE = "LINEMAN_WL";
                                                String transition_page = sharedPreference.getString(TRANSITION_PAGE, null);


                                                if(transition_page != null)
                                                {
                                                    if(transition_page.equals("LINEMAN_WL"))

                                                    linemanworklist_sqLiteHelper.deleteRowLMWorkList(customer_random_string);
                                                    linemanworklist_sqLiteHelper.close();
                                                }

                                                //Toast.makeText(getApplicationContext(), REMARKS, Toast.LENGTH_SHORT).show();
                                                showAlertSuccess(REMARKS);

                                                dialog.dismiss();

                                            }

                                        }

                                        else if(jsonObject.getString("STATUS").contentEquals("FAILED"))
                                        {
                                            JSONArray Jarray = jsonObject.getJSONArray("ROWSET");


                                            for (int i = 0; i < Jarray.length(); i++) {

                                                String REMARKS = "Dear User, Data Updation Failed... \n Saving To Local DB...";

                                                saveToLocalStorage(customer_land_line_number, customer_email_id,
                                                        customer_mobilenumber, customer_gogreen,
                                                        user_personalnumber, customer_emailid_validated,
                                                        cuatomer_mobileverified, customer_fileuploaded,
                                                        latitude, longitude, customer_doctype,
                                                        customer_updatetype, image, String.valueOf(constants.SYNC_STATUS_FAILED), dateTime);

                                                SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                                                final String TRANSITION_PAGE = "LINEMAN_WL";
                                                String transition_page = sharedPreference.getString(TRANSITION_PAGE, null);


                                                if(transition_page != null)
                                                {
                                                    if(transition_page.equals("LINEMAN_WL"))

                                                        linemanworklist_sqLiteHelper.deleteRowLMWorkList(customer_random_string);
                                                    linemanworklist_sqLiteHelper.close();
                                                }


                                                showAlertSuccess(REMARKS);
                                                dialog.dismiss();

                                            }
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                                NetworkResponse response = error.networkResponse;

                                if (error instanceof TimeoutError || error instanceof NetworkError) {
                                    Toast.makeText(getApplicationContext(),"Network Timed Out... Please Try Again Later...",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }

                                else
                                {


                                    if(response != null && response.statusCode == 500)
                                    {

                                        try {
                                            JSONObject jsonObject = new JSONObject(new String(response.data));
                                            //Toast.makeText(getApplicationContext(), jsonObject.getString("REMARKS"), Toast.LENGTH_SHORT).show();
                                            if(jsonObject.getString("STATUS").contentEquals("FAILED")){
                                                JSONArray Jarray = jsonObject.getJSONArray("ROWSET");
                                                for (int i = 0; i < Jarray.length(); i++) {

                                                    //Toast.makeText(getApplicationContext(),"Response"+response,Toast.LENGTH_LONG).show();

                                                    String FAILURE_REMARKS = "Dear User, Data Updation Failed... \n Saving To Local DB...";

                                                    saveToLocalStorage(customer_land_line_number, customer_email_id,
                                                            customer_mobilenumber, customer_gogreen,
                                                            user_personalnumber, customer_emailid_validated,
                                                            cuatomer_mobileverified, customer_fileuploaded,
                                                            latitude, longitude, customer_doctype,
                                                            customer_updatetype, image, String.valueOf(constants.SYNC_STATUS_FAILED), dateTime);

                                                    SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                                                    final String TRANSITION_PAGE = "LINEMAN_WL";
                                                    String transition_page = sharedPreference.getString(TRANSITION_PAGE, null);


                                                    if(transition_page != null)
                                                    {
                                                        if(transition_page.equals("LINEMAN_WL"))

                                                            linemanworklist_sqLiteHelper.deleteRowLMWorkList(customer_random_string);
                                                        linemanworklist_sqLiteHelper.close();
                                                    }

                                                    showAlertFailureResponse(FAILURE_REMARKS);

                                                    dialog.dismiss();


                                                }
                                            }


                                            //Toast.makeText(getApplicationContext(), obj + "Response", Toast.LENGTH_SHORT).show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                    }
                                }



                            }
                        }) {

                    /*
                     * If you want to add more parameters with the image
                     * you can do it here
                     * here we have only one parameter with the image
                     * which is uploadXML
                     * */
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("uploadXML", uploadXML);
                        return params;
                    }


                };


                volleyMultipartRequest.setOnProgressListener(new Response.ProgressListener() {
                    int latestPercentDone, percentDone;
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onProgress(long transferredBytes, long totalSize) {
                        latestPercentDone = (int) ((transferredBytes / (float) totalSize) * 100);
                        if (percentDone != latestPercentDone) {
                            percentDone = latestPercentDone;
                            //Log.e(TAG, "percentDone: " + percentDone);

                            try
                            {
                                uploadProgressBar.setProgress(percentDone);
                                textViewUploadProgress.setText(percentDone+"/"+"100%");
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }
                });

                //adding the request to volley
                //volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000,0,0));
                Log.e(TAG, "Retry Policy Volley" + volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000,0,0)));
                Volley.newRequestQueue(this).add(volleyMultipartRequest);
            }

            else
            {
                Log.d(TAG, "WITH DOC EXECUTED");
                String URL = "https://fms.bsnl.in/fmswebservices/rest/update/emailmobilereq";

                RequestQueue queue = Volley.newRequestQueue(Closure_Activity.this);

                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {

                                if(response.statusCode == 200)
                                {

                                    try {

                                        queue.getCache().clear();
                                        JSONObject jsonObject = new JSONObject(new String(response.data));
                                        //Toast.makeText(getApplicationContext(), jsonObject.getString("REMARKS"), Toast.LENGTH_SHORT).show();
                                        if (jsonObject.getString("STATUS").contentEquals("SUCCESS")) {
                                            JSONArray Jarray = jsonObject.getJSONArray("ROWSET");


                                            for (int i = 0; i < Jarray.length(); i++) {
                                                jsonObject = Jarray.getJSONObject(i);


                                                String REMARKS = jsonObject.getString("REMARKS");

                                                SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                                                final String TRANSITION_PAGE = "LINEMAN_WL";
                                                String transition_page = sharedPreference.getString(TRANSITION_PAGE, null);


                                                if(transition_page != null)
                                                {
                                                    if(transition_page.equals("LINEMAN_WL"))

                                                        linemanworklist_sqLiteHelper.deleteRowLMWorkList(customer_random_string);
                                                    linemanworklist_sqLiteHelper.close();
                                                }

                                                showAlertSuccess(REMARKS);
                                                //Toast.makeText(getApplicationContext(), REMARKS, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();

                                            }

                                        }

                                        else if(jsonObject.getString("STATUS").contentEquals("FAILED"))
                                        {
                                            JSONArray Jarray = jsonObject.getJSONArray("ROWSET");


                                            for (int i = 0; i < Jarray.length(); i++) {


                                                String REMARKS = "Dear User, Data Updation Failed... \n Saving To Local DB...";

                                                saveToLocalStorage(customer_land_line_number, customer_email_id,
                                                        customer_mobilenumber, customer_gogreen,
                                                        user_personalnumber, customer_emailid_validated,
                                                        cuatomer_mobileverified, customer_fileuploaded,
                                                        latitude, longitude, customer_doctype,
                                                        customer_updatetype, image, String.valueOf(constants.SYNC_STATUS_FAILED), dateTime);

                                                SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                                                final String TRANSITION_PAGE = "LINEMAN_WL";
                                                String transition_page = sharedPreference.getString(TRANSITION_PAGE, null);


                                                if(transition_page != null)
                                                {
                                                    if(transition_page.equals("LINEMAN_WL"))

                                                        linemanworklist_sqLiteHelper.deleteRowLMWorkList(customer_random_string);
                                                    linemanworklist_sqLiteHelper.close();
                                                }

                                                showAlertSuccess(REMARKS);
                                                dialog.dismiss();

                                            }
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                                NetworkResponse response = error.networkResponse;


                                if (error instanceof TimeoutError || error instanceof NetworkError) {
                                    Toast.makeText(getApplicationContext(),"Network Timed Out... Please Try Again Later...",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }

                                else
                                {
                                    if(response != null && response.statusCode == 500)
                                    {

                                        try {
                                            JSONObject jsonObject = new JSONObject(new String(response.data));
                                            //Toast.makeText(getApplicationContext(), jsonObject.getString("REMARKS"), Toast.LENGTH_SHORT).show();
                                            if(jsonObject.getString("STATUS").contentEquals("FAILED")){
                                                JSONArray Jarray = jsonObject.getJSONArray("ROWSET");
                                                for (int i = 0; i < Jarray.length(); i++) {

                                                    //Toast.makeText(getApplicationContext(),"Response"+response,Toast.LENGTH_LONG).show();

                                                    String FAILURE_REMARKS = "Dear User, Data Updation Failed... \n Saving To Local DB...";

                                                    saveToLocalStorage(customer_land_line_number, customer_email_id,
                                                            customer_mobilenumber, customer_gogreen,
                                                            user_personalnumber, customer_emailid_validated,
                                                            cuatomer_mobileverified, customer_fileuploaded,
                                                            latitude, longitude, customer_doctype,
                                                            customer_updatetype, image, String.valueOf(constants.SYNC_STATUS_FAILED), dateTime);

                                                    SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                                                    final String TRANSITION_PAGE = "LINEMAN_WL";
                                                    String transition_page = sharedPreference.getString(TRANSITION_PAGE, null);


                                                    if(transition_page != null)
                                                    {
                                                        if(transition_page.equals("LINEMAN_WL"))

                                                            linemanworklist_sqLiteHelper.deleteRowLMWorkList(customer_random_string);
                                                        linemanworklist_sqLiteHelper.close();
                                                    }


                                                    showAlertFailureResponse(FAILURE_REMARKS);
                                                    dialog.dismiss();

                                                }
                                            }


                                            //Toast.makeText(getApplicationContext(), obj + "Response", Toast.LENGTH_SHORT).show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }


                            }
                        }) {

                    /*
                     * If you want to add more parameters with the image
                     * you can do it here
                     * here we have only one parameter with the image
                     * which is uploadXML
                     * */
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("uploadXML", uploadXML);
                        return params;
                    }

                    /*
                     * Here we are passing image by renaming it with a unique name
                     * */
                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        params.put("file", new DataPart(customer_land_line_number + ".jpg", image));
                        return params;
                    }


                };


                volleyMultipartRequest.setOnProgressListener(new Response.ProgressListener() {
                    int latestPercentDone, percentDone;
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onProgress(long transferredBytes, long totalSize) {
                        latestPercentDone = (int) ((transferredBytes / (float) totalSize) * 100);
                        if (percentDone != latestPercentDone) {
                            percentDone = latestPercentDone;
                            Log.e(TAG, "percentDone: " + percentDone);

                            try
                            {
                                uploadProgressBar.setProgress(percentDone);
                                textViewUploadProgress.setText(percentDone+"/"+"100%");
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }
                });



                //adding the request to volley
                //volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000,0,0));
                Log.e(TAG, "Retry Policy Volley" + volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000,0,0)));
                Volley.newRequestQueue(this).add(volleyMultipartRequest);
            }

            }

        else
        {
            showDBSaveAlert();

            saveToLocalStorage(customer_land_line_number, customer_email_id,
                    customer_mobilenumber, customer_gogreen,
                    user_personalnumber, customer_emailid_validated,
                    cuatomer_mobileverified, customer_fileuploaded,
                    latitude, longitude, customer_doctype,
                    customer_updatetype, image, String.valueOf(constants.SYNC_STATUS_FAILED), dateTime);

            SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
            final String TRANSITION_PAGE = "LINEMAN_WL";
            String transition_page = sharedPreference.getString(TRANSITION_PAGE, null);


            if(transition_page != null)
            {
                if(transition_page.equals("LINEMAN_WL"))

                    linemanworklist_sqLiteHelper.deleteRowLMWorkList(customer_random_string);
                linemanworklist_sqLiteHelper.close();
            }


            dialog.dismiss();
        }


    }

    private void saveToLocalStorage(String phoneNo, String emailId,
                                    String mobileNo, String goGreenStatus,
                                    String updatedBy, String emailIdValid,
                                    String mobileNoValid, String fileUploaded,
                                    String latitude, String longitude, String docType,
                                    String updateType, byte[] image, String  sync,String dateTime) {

        ClosureActivity_SQLiteHelper closureActivity_sqLiteHelper = new ClosureActivity_SQLiteHelper(this);
        SQLiteDatabase database = closureActivity_sqLiteHelper.getWritableDatabase();

        closureActivity_sqLiteHelper.saveToLocalDatabase(phoneNo, emailId, mobileNo, goGreenStatus, updatedBy,
                emailIdValid, mobileNoValid, fileUploaded, latitude, longitude,
                docType, updateType, image, sync,dateTime, database);
        closureActivity_sqLiteHelper.close();
    }

    private void showAlertSuccess(String success_remarks) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(success_remarks).setTitle("Response from Server").setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(countDownTimer != null)
                {
                    countDownTimer.cancel();
                }


                SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                final String TRANSITION_PAGE = "LINEMAN_WL";
                String transition_page = sharedPreference.getString(TRANSITION_PAGE, null);

                if( transition_page != null) {
                if( transition_page.equals("LINEMAN_WL"))

                    dialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setExitTransition(null);
                    }
                    Intent intent = new Intent(Closure_Activity.this, LinemanWorklist_Activity.class);
                    startActivity(intent);
                    finishAffinity();
                    finish();
                }

                else
                {
                    dialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setExitTransition(null);
                    }
                    Intent intent = new Intent(Closure_Activity.this, DashBoard_Activity.class);
                    startActivity(intent);
                    finishAffinity();
                    finish();
                }

            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void showAlertFailureResponse(String failure_remarks) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(failure_remarks).setTitle("Response from Server").setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(countDownTimer != null)
                {
                    countDownTimer.cancel();
                }


                SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                final String TRANSITION_PAGE = "LINEMAN_WL";
                String transition_page = sharedPreference.getString(TRANSITION_PAGE, null);

                if( transition_page != null) {
                    if( transition_page.equals("LINEMAN_WL"))

                        dialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setExitTransition(null);
                    }
                    Intent intent = new Intent(Closure_Activity.this, LinemanWorklist_Activity.class);
                    startActivity(intent);
                    finishAffinity();
                    finish();
                }

                else
                {
                    dialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setExitTransition(null);
                    }
                    Intent intent = new Intent(Closure_Activity.this, DashBoard_Activity.class);
                    startActivity(intent);
                    finishAffinity();
                    finish();
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    private void showDBSaveAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Closure_Activity.this);
        builder.setMessage("Dear User, Data Has Been Saved To \nLocal DB... It Will SYNC Automatically \nWith FMS Server...")
                .setTitle("Unable to connect To Server...")
                .setCancelable(false)
                .setPositiveButton("OK",
                        (alertdialog, id) -> {

                            if(countDownTimer != null)
                            {
                                countDownTimer.cancel();
                            }


                            SharedPreferences sharedPreference = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                            final String TRANSITION_PAGE = "LINEMAN_WL";
                            String transition_page = sharedPreference.getString(TRANSITION_PAGE, null);

                            if( transition_page != null) {
                                if( transition_page.equals("LINEMAN_WL"))

                                    dialog.dismiss();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setExitTransition(null);
                                }
                                Intent intent = new Intent(Closure_Activity.this, LinemanWorklist_Activity.class);
                                startActivity(intent);
                                finishAffinity();
                                finish();
                            }

                            else
                            {
                                dialog.dismiss();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setExitTransition(null);
                                }
                                Intent intent = new Intent(Closure_Activity.this, DashBoard_Activity.class);
                                startActivity(intent);
                                finishAffinity();
                                finish();
                            }

                        }
                );
        AlertDialog alertdialog = builder.create();
        alertdialog.show();
    }


    @SuppressLint("ShortAlarm")
    public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        if (manager != null) {
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    18000,
                    pendingIntent);
        }
        //Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
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



        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[0]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("BSNL ME REG");
        //builder.setIcon(R.drawable.bsnl_me_reg_icon);
        builder.setMessage("Are You Sure You Want To Leave This Page?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application


                dialog.dismiss();

                if(countDownTimer != null)
                {
                    countDownTimer.cancel();
                }

                Intent intent = new Intent(Closure_Activity.this, SearchSubscriber_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finishAffinity();
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    //////////////////////////////////////////////// Get LAT LONG /////////////////////////////////////////////////////////


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        /* 10 secs */
        long UPDATE_INTERVAL = 2 * 1000;
        /* 2 sec */
        long FASTEST_INTERVAL = 2000;
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        SharedPreferences preferences = getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        /*String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());*/

        editor.putString(LATITUDE, String.valueOf(location.getLatitude()));
        editor.putString(LONGITUDE, String.valueOf(location.getLongitude()));
        editor.apply();
        //mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        //mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


}

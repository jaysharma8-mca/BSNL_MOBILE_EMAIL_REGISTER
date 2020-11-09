package com.mobileemail_register.MISC;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mobileemail_register.CLOSURE_ACTIVITY.ClosureActivity_SQLiteHelper;
import com.mobileemail_register.CLOSURE_ACTIVITY.VolleyMultipartRequest;
import com.mobileemail_register.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


@SuppressWarnings({"Convert2Lambda"})
public class NetworkMonitor extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {


      if(checkNetworkConnection(context))
        {

            Log.d(TAG,"Internet Connected");
            final ClosureActivity_SQLiteHelper closureActivity_sqLiteHelper = new ClosureActivity_SQLiteHelper(context);
            final SQLiteDatabase database = closureActivity_sqLiteHelper.getWritableDatabase();

            Cursor cursor = closureActivity_sqLiteHelper.readFromLocalDatabase(database);

            while (cursor.moveToNext())
            {
                int sync_status = cursor.getInt(cursor.getColumnIndex(constants.sync_status));

                if(sync_status==constants.SYNC_STATUS_FAILED) {
                    String phoneNo = cursor.getString(cursor.getColumnIndex(constants.phoneNo));
                    String emailId = cursor.getString(cursor.getColumnIndex(constants.emailId));
                    String mobileNo = cursor.getString(cursor.getColumnIndex(constants.mobileNo));
                    String goGreenStatus = cursor.getString(cursor.getColumnIndex(constants.goGreenStatus));

                    String updatedBy = cursor.getString(cursor.getColumnIndex(constants.updatedBy));
                    String emailIdValid = cursor.getString(cursor.getColumnIndex(constants.emailIdValid));
                    String mobileNoValid = cursor.getString(cursor.getColumnIndex(constants.mobileNoValid));
                    String fileUploaded = cursor.getString(cursor.getColumnIndex(constants.fileUploaded));

                    String latitude = cursor.getString(cursor.getColumnIndex(constants.latitude));
                    String longitude = cursor.getString(cursor.getColumnIndex(constants.longitude));
                    String docType = cursor.getString(cursor.getColumnIndex(constants.docType));
                    String updateType = cursor.getString(cursor.getColumnIndex(constants.updateType));
                    String dateTime = cursor.getString(cursor.getColumnIndex(constants.dateTime));

                    byte[] kycDoc = cursor.getBlob(cursor.getColumnIndex(constants.image));

                    String uploadXML =
                            "<FileUploadModel>" +
                                    "<phoneNo>" + phoneNo + "</phoneNo>" +
                                    "<emailId>" + emailId + "</emailId>" +
                                    "<mobileNo>" + mobileNo + "</mobileNo>" +
                                    "<goGreenStatus>" + goGreenStatus + "</goGreenStatus>" +
                                    "<updatedBy>" + updatedBy + "</updatedBy>" +
                                    "<emailIdValid>" + emailIdValid + "</emailIdValid>" +
                                    "<mobileNoValid>" + mobileNoValid + "</mobileNoValid>" +
                                    "<fileUploaded>" + fileUploaded + "</fileUploaded>" +
                                    "<latitude>" + latitude + "</latitude>" +
                                    "<longitude>" + longitude + "</longitude>" +
                                    "<docType>" + docType + "</docType>" +
                                    "<updateType>" + updateType + "</updateType>" +
                                    "</FileUploadModel>";

                    Log.d(TAG, "uploadXML" + uploadXML);

                    if (fileUploaded != null && fileUploaded.equals("N")) {

                        Log.d(TAG, "W/O DOC EXECUTED");


                        String URL = "https://fms.bsnl.in/fmswebservices/rest/update/emailmobilereq";
                        RequestQueue queue = Volley.newRequestQueue(context);

                       VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                                new Response.Listener<NetworkResponse>() {
                                    @Override
                                    public void onResponse(NetworkResponse response) {
                                        queue.getCache().clear();

                                        if (response.statusCode == 200) {

                                            try {

                                                queue.getCache().clear();
                                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                                //Toast.makeText(getApplicationContext(), jsonObject.getString("REMARKS"), Toast.LENGTH_SHORT).show();
                                                if (jsonObject.getString("STATUS").contentEquals("SUCCESS")) {
                                                    JSONArray Jarray = jsonObject.getJSONArray("ROWSET");


                                                    for (int i = 0; i < Jarray.length(); i++) {
                                                        jsonObject = Jarray.getJSONObject(i);

                                                        //showAlertSuccess(REMARKS);

                                                        closureActivity_sqLiteHelper.updateLocalDatabase(phoneNo,
                                                                emailId, mobileNo,
                                                                goGreenStatus, updatedBy,
                                                                emailIdValid, mobileNoValid, fileUploaded, latitude,
                                                                longitude, docType, updateType, kycDoc, String.valueOf(constants.SYNC_STATUS_OK), dateTime, database);

                                                        context.sendBroadcast(new Intent(constants.UI_UPDATE_BROADCAST));
                                                        notifyUser(context);



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


                                        NetworkResponse response = error.networkResponse;

                                        if (response != null && response.statusCode == 500) {

                                            try {
                                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                                //Toast.makeText(getApplicationContext(), jsonObject.getString("REMARKS"), Toast.LENGTH_SHORT).show();
                                                if (jsonObject.getString("STATUS").contentEquals("FAILED")) {
                                                    JSONArray Jarray = jsonObject.getJSONArray("ROWSET");
                                                    for (int i = 0; i < Jarray.length(); i++) {
                                                        jsonObject = Jarray.getJSONObject(i);
                                                        //Toast.makeText(getApplicationContext(),"Response"+response,Toast.LENGTH_LONG).show();


                                                        //showAlertSuccess(REMARKS);


                                                    }
                                                }


                                                //Toast.makeText(getApplicationContext(), obj + "Response", Toast.LENGTH_SHORT).show();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                }) {


                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("uploadXML", uploadXML);
                                return params;
                            }

                        };


                        Volley.newRequestQueue(context).add(volleyMultipartRequest);

                        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

                    } else {

                        Log.d(TAG, "WITH DOC EXECUTED");





                        String URL = "https://fms.bsnl.in/fmswebservices/rest/update/emailmobilereq";

                        RequestQueue queue = Volley.newRequestQueue(context);

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

                                                        closureActivity_sqLiteHelper.updateLocalDatabase(phoneNo,
                                                                emailId, mobileNo,
                                                                goGreenStatus, updatedBy,
                                                                emailIdValid, mobileNoValid, fileUploaded, latitude,
                                                                longitude, docType, updateType, kycDoc, String.valueOf(constants.SYNC_STATUS_OK), dateTime, database);

                                                        context.sendBroadcast(new Intent(constants.UI_UPDATE_BROADCAST));
                                                        notifyUser(context);


                                                        //showAlertSuccess(REMARKS);

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

                                        if(response != null && response.statusCode == 500)
                                        {

                                            try {
                                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                                //Toast.makeText(getApplicationContext(), jsonObject.getString("REMARKS"), Toast.LENGTH_SHORT).show();
                                                if(jsonObject.getString("STATUS").contentEquals("FAILED")){
                                                    JSONArray Jarray = jsonObject.getJSONArray("ROWSET");
                                                    for (int i = 0; i < Jarray.length(); i++) {
                                                        jsonObject = Jarray.getJSONObject(i);
                                                        //Toast.makeText(getApplicationContext(),"Response"+response,Toast.LENGTH_LONG).show();



                                                        //showAlertSuccess(REMARKS);


                                                    }
                                                }


                                                //Toast.makeText(getApplicationContext(), obj + "Response", Toast.LENGTH_SHORT).show();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }



                                        }

                                    }
                                }) {


                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("uploadXML", uploadXML);
                                return params;
                            }


                            @Override
                            protected Map<String, DataPart> getByteData() {
                                Map<String, DataPart> params = new HashMap<>();
                                params.put("file", new DataPart(phoneNo + ".jpg", kycDoc));
                                return params;
                            }


                        };

                        //adding the request to volley
                        Volley.newRequestQueue(context).add(volleyMultipartRequest);

                        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));


                    }
                }


            }
            //me_upd_lm_sqlite_helper.close();

        }
        else
        {
            //Toast.makeText(context, "Internet Not Connected", Toast.LENGTH_LONG).show();
            Log.d(TAG,"Internet Not Connected");

        }
    }

    public boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return (networkInfo!= null && networkInfo.isConnected());
    }

    public void notifyUser(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.bsnl_me_reg_icon)
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("BSNL MEREG")
                .setContentText("Offline Data Syncd Successfully")
                .setSound(alarmSound);

        if (notificationManager != null) {
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    public void notifyUserFailed(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.bsnl_me_reg_icon)
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("BSNL MEREG")
                .setContentText("Offline Data Not Syncd... Will Try Again Later...")
                .setSound(alarmSound);

        if (notificationManager != null) {
            notificationManager.notify(1, notificationBuilder.build());
        }
    }


}

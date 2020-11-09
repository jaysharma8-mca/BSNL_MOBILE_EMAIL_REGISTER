package com.mobileemail_register.LINEMAN_WL_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mobileemail_register.R;

public class Co_List_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_list);

        AppCenter.start(getApplication(), "15ee0d52-7eb0-4aa2-9ec5-40b1491add0e",
                Analytics.class, Crashes.class);
    }

    public void getApptovedList(View view) {

        Intent intent = new Intent(Co_List_Activity.this,Co_Approved_List_activity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void getPendingList(View view) {

        Intent intent = new Intent(Co_List_Activity.this,Co_Pending_List_Activity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void getRejectedList(View view) {

        Intent intent = new Intent(Co_List_Activity.this,Co_Rejected_List_Activity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Co_List_Activity.this,LinemanWorklistDashBoard_Activity.class);
        startActivity(intent);
        finishAffinity();
    }

}

package com.test.prototype.broadcastreceivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotifChannel();
        IntentFilter batteryStatusFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        BatteryStatusReceiver batteryStatusReceiver = new BatteryStatusReceiver(getString(R.string.batt_status_notif_channel_id));
        registerReceiver(batteryStatusReceiver, batteryStatusFilter);
    }

    // Create notification channel for Android Oreo and above *REQUIRED*
    private void createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.batt_status_notif_channel);
            String desc = getString(R.string.batt_status_notif_channel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.batt_status_notif_channel_id), name, importance);
            channel.setDescription(desc);
            NotificationManager notifManager = getSystemService(NotificationManager.class);
            notifManager.createNotificationChannel(channel);
        }
    }
}

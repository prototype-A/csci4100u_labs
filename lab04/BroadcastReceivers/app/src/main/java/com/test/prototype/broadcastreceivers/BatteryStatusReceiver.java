package com.test.prototype.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;

public class BatteryStatusReceiver extends BroadcastReceiver {

    private static int NOTIF_ID;
    private static String NOTIF_CHANNEL_ID;

    public BatteryStatusReceiver(String notifChannelID) {
        super();
        NOTIF_ID = 1;
        NOTIF_CHANNEL_ID = notifChannelID;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String notifTitle = "Battery Information";
        String battStatus = "Status: ";
        String charging = "Charging: ";
        String chargingSource = "Source: ";
        String battTemp = "Temp: ";

        // Check battery status
        switch(intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)) {
            case BatteryManager.BATTERY_HEALTH_COLD:
                battStatus += "Cold";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                battStatus += "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                battStatus += "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                battStatus += "Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                battStatus += "Overheating";
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                battStatus += "Unknown";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                battStatus += "Unknown Failure";
                break;
        }

        // Check charging status
        switch(intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                charging += "Yes";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                charging += "No";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                charging += "Full";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                charging += "Not Charging";
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                charging += "Unknown";
                break;
        }

        // Check charging source, if charging
        switch(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                chargingSource += "Plugged into AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                chargingSource += "Plugged into USB Port";
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                chargingSource += "Wirelessly";
                break;
            default:
                chargingSource += "Not plugged in";
        }

        // Check battery temperature
        battTemp += intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) + "°C";

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(notifTitle)
                .setContentText(battStatus)
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(battStatus + "\n" + charging + "\n" + chargingSource + "\n" + battTemp))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notifManager = NotificationManagerCompat.from(context);
        notifManager.notify(NOTIF_ID, nBuilder.build());
    }
}

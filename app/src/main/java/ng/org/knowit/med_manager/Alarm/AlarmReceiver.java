package ng.org.knowit.med_manager.Alarm;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by john on 4/7/18.
 */

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "It is time to take your medicine", Toast.LENGTH_SHORT).show();

        //This will send a notification message and show notification in notification tray
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmNotificationService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        /*if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // Set the alarm here.

        }*/
    }

}

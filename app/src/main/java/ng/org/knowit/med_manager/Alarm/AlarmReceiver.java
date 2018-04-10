/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package ng.org.knowit.med_manager.Alarm;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

            Toast.makeText(context, "Time to take medicine", Toast.LENGTH_SHORT).show();
            ComponentName comp = new ComponentName(context.getPackageName(),
                    AlarmNotificationService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));

            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(comp,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }

    }
}

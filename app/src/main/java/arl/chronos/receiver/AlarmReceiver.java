package arl.chronos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import arl.chronos.service.RestartAlarmsService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Prueba", "AlarmReceiver onReceive");
        if (intent.getStringExtra("alarma").equals("activar")) {
            Log.d("Prueba", "AlarmReceiver onReceive ACTIVAR");
            Intent i = new Intent(context, RestartAlarmsService.class);
            RestartAlarmsService.enqueueWork(context, i);
        }
    }
}


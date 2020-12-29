package arl.chronos.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import arl.chronos.adapters.RcvAdapterAlarmas;

public class AlertReceiver extends BroadcastReceiver {
    private String mensaje;
    private int id;

    @Override
    public void onReceive(Context context, Intent intent) {

        mensaje = intent.getStringExtra("mensaje_alarma");
        id = intent.getIntExtra("id_alarma",1);

        NotificationHelper notificationHelper = new NotificationHelper(context, mensaje, id);
        NotificationCompat.Builder nb = notificationHelper.getCanalNotification();
        notificationHelper.getManager().notify(id, nb.build());
    }
}

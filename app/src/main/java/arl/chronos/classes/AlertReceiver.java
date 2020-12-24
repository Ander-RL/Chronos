package arl.chronos.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import arl.chronos.adapters.RcvAdapterAlarmas;

public class AlertReceiver extends BroadcastReceiver {
    private String mensaje;

    @Override
    public void onReceive(Context context, Intent intent) {

        mensaje = intent.getStringExtra("mensaje_alarma");

        NotificationHelper notificationHelper = new NotificationHelper(context, mensaje);
        NotificationCompat.Builder nb = notificationHelper.getCanalNotification();
        notificationHelper.getManager().notify(1, nb.build());
    }
}

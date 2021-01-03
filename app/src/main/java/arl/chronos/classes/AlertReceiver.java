package arl.chronos.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import arl.chronos.adapters.RcvAdapterAlarmas;

public class AlertReceiver extends BroadcastReceiver {
    private String mensaje;
    private int id;
    public static boolean repetirAlarma = true;

    @Override
    public void onReceive(Context context, Intent intent) {

        mensaje = intent.getStringExtra("mensaje_alarma");
        id = intent.getIntExtra(RcvAdapterAlarmas.ID_ALARMA, 0);

        NotificationHelper notificationHelper = new NotificationHelper(context, mensaje, id);
        NotificationCompat.Builder nb = notificationHelper.getCanalNotification();
        notificationHelper.getManager().notify(id, nb.build());
        Log.d("NOTIF_RECEIV", "AlertReceiver -> id = " + id + " Mensaje = " + mensaje);
        /*//do {
            notificationHelper.getManager().notify(id, nb.build());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //} while (repetirAlarma); // TODO Arreglar, bucle infinito*/
    }
}

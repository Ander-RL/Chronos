package arl.chronos.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import arl.chronos.adapters.RcvAdapterAlarmas;

public class AlertReceiver extends BroadcastReceiver {
    private String mensaje;
    private int id;
    private String parar;
    private Vibrator vibrator;

    @Override
    public void onReceive(Context context, Intent intent) {

        mensaje = intent.getStringExtra("mensaje_alarma");
        id = intent.getIntExtra(RcvAdapterAlarmas.ID_ALARMA, 0);
        parar = intent.getStringExtra("parar");

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{500, 1000, 500, 1000}, 0);

        NotificationHelper notificationHelper = new NotificationHelper(context, mensaje, id);
        NotificationCompat.Builder nb = notificationHelper.getCanalNotification();
        Log.d("NOTIF_RECEIV", "AlertReceiver -> id = " + id + " / Mensaje = " + mensaje + " / Parar = " + parar);
        notificationHelper.getManager().notify(id, nb.build());
    }
}

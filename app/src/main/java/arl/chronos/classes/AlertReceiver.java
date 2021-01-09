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
    private static Thread thread;
    private String mensaje;
    private int id;
    private long threadId;
    private Vibrador vibrador;
    private SharedPreferences sharedPref;
    private String parar;
    private static boolean vibrar;
    private Vibrator vibrator;

    @Override
    public void onReceive(Context context, Intent intent) {

        mensaje = intent.getStringExtra("mensaje_alarma");
        id = intent.getIntExtra(RcvAdapterAlarmas.ID_ALARMA, 0);
        parar = intent.getStringExtra("parar");
        sharedPref = context.getSharedPreferences("thread_id", Context.MODE_PRIVATE);

        /*if (parar.equals("si")) {
            vibrar = false;
        } else {
            vibrar = true;
        }*/

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{500, 1000, 500, 1000}, 0);

        NotificationHelper notificationHelper = new NotificationHelper(context, mensaje, id);
        NotificationCompat.Builder nb = notificationHelper.getCanalNotification();
        Log.d("NOTIF_RECEIV", "AlertReceiver -> id = " + id + " / Mensaje = " + mensaje + " / Parar = " + parar + " / Vibrar = " + vibrar);
        notificationHelper.getManager().notify(id, nb.build());

        /*if (vibrar) {
            vibrador = new Vibrador(context);
            vibrador.setVibrar(vibrar);
            thread = new Thread(vibrador);
            threadId = thread.getId();
            sharedPref.edit().putLong("thead_id", threadId).apply();
            Log.d("NOTIF_RECEIV", "AlertReceiver -> thread id = " + threadId);
            thread.start();
        }
        if (!vibrar) {
            thread.interrupt();
        }*/
    }
}

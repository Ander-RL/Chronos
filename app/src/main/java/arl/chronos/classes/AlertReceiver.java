package arl.chronos.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import arl.chronos.adapters.RcvAdapterAlarmas;

public class AlertReceiver extends BroadcastReceiver {
    private String mensaje;
    private int id;
    private Vibrador vibrador;
    private String parar;
    private boolean vibrar;

    @Override
    public void onReceive(Context context, Intent intent) {

        mensaje = intent.getStringExtra("mensaje_alarma");
        id = intent.getIntExtra(RcvAdapterAlarmas.ID_ALARMA, 0);
        parar = intent.getStringExtra("parar");

        if(parar.equals("si")){
            vibrar = false;
        } else {vibrar = true;}

        /*vibrador = new Vibrador(context);
        vibrador.setVibrar(vibrar);
        vibrador.run();*/

        NotificationHelper notificationHelper = new NotificationHelper(context, mensaje, id);
        NotificationCompat.Builder nb = notificationHelper.getCanalNotification();
        Log.d("NOTIF_RECEIV", "AlertReceiver -> id = " + id + " / Mensaje = " + mensaje);
        notificationHelper.getManager().notify(id, nb.build());

        vibrador = new Vibrador(context);
        vibrador.vibrar();
        
    }
}

package arl.chronos.classes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompatExtras;

import arl.chronos.MainActivity;
import arl.chronos.R;

public class NotificationHelper extends ContextWrapper {

    public static final String CANAL_ID = "canal_id";
    public static final String CANAL_NOMBRE = "Canal Notificaciones Alarma";
    public static final String TITULO_NOTIF = "Alarma";
    public static final String CANCELAR = "cancelar";
    public static final String ID_ALARMA = "id_alarma";
    private String mensaje;
    private int id;
    private NotificationManager manager;

    public NotificationHelper(Context base, String mensaje, int id) {
        super(base);
        this.mensaje = mensaje;
        this.id = id;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            crearCanal();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void crearCanal() {
        NotificationChannel canal = new NotificationChannel(CANAL_ID, CANAL_NOMBRE, NotificationManager.IMPORTANCE_HIGH);
        canal.enableLights(true);
        canal.enableVibration(true);
        canal.setLightColor(R.color.blue_500);
        canal.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(canal);
    }

    public NotificationManager getManager() {
        if(manager == null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getCanalNotification(){
        // Se crea un pending intent para abrir la app al clickar en la notificacion
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, 0);
        // Se crea un pending intent para ejecutar el codigo del broadcast NotificationReceiver (botones)
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra(CANCELAR, mensaje);
        broadcastIntent.putExtra(ID_ALARMA, id);
        PendingIntent broadcastPendingIntent = PendingIntent.getBroadcast(this, id, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Se crea y devuelve la notificacion
        return new NotificationCompat.Builder(getApplicationContext(), CANAL_ID)
                .setContentTitle(TITULO_NOTIF)
                .setContentText(mensaje)
                .setSmallIcon(R.drawable.ic_alarm)
                .setPriority(Notification.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.blue_500, getTheme()))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true) // Cuando se toca la notificacion se borra/elimina/quita esa notificacion
                .addAction(R.mipmap.ic_launcher, CANCELAR, broadcastPendingIntent);
    }
}

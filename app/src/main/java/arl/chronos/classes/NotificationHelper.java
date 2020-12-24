package arl.chronos.classes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompatExtras;

import arl.chronos.R;

public class NotificationHelper extends ContextWrapper {
    public static final String CANAL_ID = "canalID1";
    public static final String CANAL_NOMBRE = "Alarma";
    public static final String TITULO_NOTIF = "Alarma";
    private String mensaje;
    private NotificationManager manager;

    public NotificationHelper(Context base, String mensaje) {
        super(base);
        this.mensaje = mensaje;
        Toast.makeText(base, mensaje, Toast.LENGTH_SHORT).show();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            crearCanal();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void crearCanal() {
        NotificationChannel canal = new NotificationChannel(CANAL_ID, CANAL_NOMBRE, NotificationManager.IMPORTANCE_DEFAULT);
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
        return new NotificationCompat.Builder(getApplicationContext(), CANAL_ID)
                .setContentTitle(TITULO_NOTIF)
                .setContentText(mensaje)
                .setSmallIcon(R.drawable.ic_alarm)
                .setPriority(Notification.PRIORITY_DEFAULT);
    }
}

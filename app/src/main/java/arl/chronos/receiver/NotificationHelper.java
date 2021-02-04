package arl.chronos.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import arl.chronos.MainActivity;
import arl.chronos.R;

public class NotificationHelper extends ContextWrapper {

    public static final String CANAL_ID = "canal_id";
    public static final String CANAL_NOMBRE = "Canal Notificaciones Alarma";
    public static final String TITULO_NOTIF = "Alarma";
    public static final String CANCELAR = "cancelar";
    public static final String CANCELAR_CUENTA_ATRAS = "cancelar cuenta atras";
    public static final String CANCELAR_TIEMPO_AGOTADO = "cancelar tiempo agotado";
    public static final String POSPONER = "posponer";
    public static final String HORA = "hora";
    public static final String ID_ALARMA = "id_alarma";
    public static final String ID_INTENT = "id_intent";
    private String mensaje;
    private int id;
    private NotificationManager manager;

    public NotificationHelper(Context base, String mensaje, int id) {
        super(base);
        this.mensaje = mensaje;
        this.id = id;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            crearCanal();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void crearCanal() {
        NotificationChannel canal = new NotificationChannel(CANAL_ID, CANAL_NOMBRE, NotificationManager.IMPORTANCE_HIGH);
        canal.enableLights(true);
        canal.enableVibration(false); // No cancela la vibracion por defecto
        //canal.setVibrationPattern(new long[] {2000,2000,2000,2000,2000}); // No funciona. Vibra igual.
        canal.setLightColor(R.color.blue_500);
        canal.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(canal);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getCanalNotification() {
        Log.d("NotificationHelper", "Mensaje = " + mensaje);
        if (!mensaje.equals("foregroundService") && !mensaje.equals("onFinish")) {
            // Se crea un pending intent para abrir la app al clickar en la notificacion
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, 0);
            // Se crea un pending intent para ejecutar el codigo del broadcast NotificationReceiver (boton cancelar)
            Intent cancelButtonIntent = new Intent(this, NotificationReceiverCancelar.class);
            cancelButtonIntent.putExtra(ID_INTENT, CANCELAR);
            cancelButtonIntent.putExtra(HORA, mensaje);
            cancelButtonIntent.putExtra(ID_ALARMA, id);
            PendingIntent cancelButtonPendingIntent = PendingIntent.getBroadcast(this, id, cancelButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Se crea un pending intent para ejecutar el codigo del broadcast NotificationReceiver (boton posponer)
            Intent postponerButtonIntent = new Intent(this, NotificationReceiver.class);
            postponerButtonIntent.putExtra(ID_INTENT, POSPONER);
            postponerButtonIntent.putExtra(HORA, mensaje);
            postponerButtonIntent.putExtra(ID_ALARMA, id);
            PendingIntent postponerButtonPendingIntent = PendingIntent.getBroadcast(this, id, postponerButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.d("NotificationHelper", "Helper -> id = " + id + " / Mensaje = " + mensaje);
            // Se crea y devuelve la notificacion
            return new NotificationCompat.Builder(getApplicationContext(), CANAL_ID)
                    .setContentTitle(TITULO_NOTIF)
                    .setContentText(mensaje)
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setPriority(Notification.PRIORITY_HIGH)
                    //.setVibrate(new long[] {2000,2000,2000,2000,2000}) // No funciona. Vibra igual.
                    .setColor(getResources().getColor(R.color.blue_500, getTheme()))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true) // Cuando se toca la notificacion se borra/elimina/quita esa notificacion
                    .addAction(R.mipmap.ic_launcher, CANCELAR, cancelButtonPendingIntent)
                    .addAction(R.mipmap.ic_launcher, POSPONER, postponerButtonPendingIntent);
        }

        if (mensaje.equals("foregroundService")) {
            // Se crea un pending intent para abrir la app al clickar en la notificacion
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, 0);
            // Se crea un pending intent para ejecutar el codigo del broadcast NotificationReceiver (boton cancelar)
            Intent cancelButtonIntent = new Intent(this, NotificationReceiverCancelar.class);
            cancelButtonIntent.putExtra(ID_INTENT, CANCELAR_CUENTA_ATRAS);
            cancelButtonIntent.putExtra(ID_ALARMA, id);
            PendingIntent cancelButtonPendingIntent = PendingIntent.getBroadcast(this, id, cancelButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Se crea y devuelve la notificacion
            return new NotificationCompat.Builder(getApplicationContext(), CANAL_ID)
                    .setContentTitle("Cuenta atras en segundo plano")
                    .setContentText("Pulsa sobre la notificacion para abrir la App")
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setColor(getResources().getColor(R.color.blue_500, getTheme()))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true) // Cuando se toca la notificacion se borra/elimina/quita esa notificacion
                    .addAction(R.mipmap.ic_launcher, CANCELAR, cancelButtonPendingIntent);
        } else {
            // Se crea un pending intent para abrir la app al clickar en la notificacion
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, 0);
            // Se crea un pending intent para ejecutar el codigo del broadcast NotificationReceiver (boton cancelar)
            Intent cancelButtonIntent = new Intent(this, NotificationReceiverCancelar.class);
            cancelButtonIntent.putExtra(ID_INTENT, CANCELAR_TIEMPO_AGOTADO);
            cancelButtonIntent.putExtra(ID_ALARMA, id);
            PendingIntent cancelButtonPendingIntent = PendingIntent.getBroadcast(this, id, cancelButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Se crea y devuelve la notificacion
            return new NotificationCompat.Builder(getApplicationContext(), CANAL_ID)
                    .setContentTitle("Tiempo agotado!!")
                    .setContentText("La cuenta atras ha finalizado")
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setColor(getResources().getColor(R.color.blue_500, getTheme()))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true) // Cuando se toca la notificacion se borra/elimina/quita esa notificacion
                    .addAction(R.mipmap.ic_launcher, CANCELAR, cancelButtonPendingIntent);
        }
    }
}

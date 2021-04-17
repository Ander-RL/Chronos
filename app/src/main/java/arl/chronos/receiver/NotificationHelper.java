package arl.chronos.receiver;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import arl.chronos.MainActivity;
import arl.chronos.R;

public class NotificationHelper extends ContextWrapper {

    public static final String CANAL_ID = "canal_id";
    public static final String CANCELAR = "cancelar";
    public static final String CANCELAR_CUENTA_ATRAS = "cancelar cuenta atras";
    public static final String CANCELAR_TIEMPO_AGOTADO = "cancelar tiempo agotado";
    public static final String POSPONER = "posponer";
    public static final String HORA = "hora";
    public static final String ID_ALARMA = "id_alarma";
    public static final String ID_INTENT = "id_intent";
    private Vibrator vibrator;
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
        NotificationChannel canal = new NotificationChannel(CANAL_ID, getString(R.string.canal_notificaciones_alarma), NotificationManager.IMPORTANCE_HIGH);
        canal.enableLights(true);
        canal.enableVibration(false);
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

            vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{500, 1000, 500, 1000}, 0);

            // Se crea y devuelve la notificacion
            return new NotificationCompat.Builder(getApplicationContext(), CANAL_ID)
                    .setContentTitle(getString(R.string.titulo_notificacion))
                    .setContentText(mensaje)
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setColor(getResources().getColor(R.color.blue_500, getTheme()))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true) // Cuando se toca la notificacion se borra/elimina/quita esa notificacion
                    .addAction(R.mipmap.ic_launcher, getString(R.string.cancelar), cancelButtonPendingIntent)
                    .addAction(R.mipmap.ic_launcher, getString(R.string.posponer), postponerButtonPendingIntent);
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
                    .setContentTitle(getString(R.string.cuenta_atras_segundo_plano))
                    .setContentText(getString(R.string.pulsar_para_abrir_app))
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setColor(getResources().getColor(R.color.blue_500, getTheme()))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true) // Cuando se toca la notificacion se borra/elimina/quita esa notificacion
                    .addAction(R.mipmap.ic_launcher, getString(R.string.cancelar), cancelButtonPendingIntent);
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
                    .setContentTitle(getString(R.string.tiempo_agotado))
                    .setContentText(getString(R.string.cuenta_atras_finalizado))
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setColor(getResources().getColor(R.color.blue_500, getTheme()))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true) // Cuando se toca la notificacion se borra/elimina/quita esa notificacion
                    .addAction(R.mipmap.ic_launcher, getString(R.string.cancelar), cancelButtonPendingIntent);
        }
    }
}

package arl.chronos.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import arl.chronos.service.ServicioSonido;


// Clase para para añadir funcionalidades a las notificaciones (botones)
// Permite ejecutar codigo sin que la app este abierta
public class NotificationReceiverCancelar extends BroadcastReceiver {
    private Calendar c;
    private int id;
    private String mensaje;
    private String hora;
    private Vibrator vibrator;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        c = Calendar.getInstance();

        mensaje = intent.getStringExtra(NotificationHelper.ID_INTENT);
        hora = intent.getStringExtra(NotificationHelper.HORA);
        id = intent.getIntExtra(NotificationHelper.ID_ALARMA, 0);

        if (mensaje.equals(NotificationHelper.CANCELAR)) {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intentCancelar = new Intent(context, AlertReceiver.class);
            intentCancelar.putExtra("parar", "si");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intentCancelar, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(pendingIntent); // Cancela la alarma y ya no vuelve a sonar ese día? Probrar

            Intent intentServicio = new Intent(context, ServicioSonido.class);
            intentServicio.setAction(".classes.ServicioSonido");
            context.stopService(intentServicio);

            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.cancel();

            notificationManager.cancel(id); // Cierra la notificacion

            Toast.makeText(context, "Alarma cancelada", Toast.LENGTH_SHORT).show();
            Log.d("NOTIF_RECEIV", "Cancelar -> id = " + id + " / Mensaje = " + mensaje + " / Hora = " + hora);

        }
    }
}

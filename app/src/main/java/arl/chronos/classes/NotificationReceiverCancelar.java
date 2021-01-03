package arl.chronos.classes;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import arl.chronos.adapters.RcvAdapterAlarmas;


// Clase para para añadir funcionalidades a las notificaciones (botones)
// Permite ejecutar codigo sin que la app este abierta
public class NotificationReceiverCancelar extends BroadcastReceiver {
    private Calendar c;
    private int id;
    private String mensaje;
    private String hora;

    @Override
    public void onReceive(Context context, Intent intent) {
        c = Calendar.getInstance();

        mensaje = intent.getStringExtra(NotificationHelper.ID_INTENT);
        hora = intent.getStringExtra(NotificationHelper.HORA);
        id = intent.getIntExtra(NotificationHelper.ID_ALARMA, 0);

        if (mensaje.equals(NotificationHelper.CANCELAR)) {

            Log.d("NOTIF_RECEIV", "cancelar1");

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intentCancelar = new Intent(context, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intentCancelar, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);

            AlertReceiver.repetirAlarma = false;

            Toast.makeText(context, "Alarma " + hora + " cancelada", Toast.LENGTH_SHORT).show();
            Log.d("NOTIF_RECEIV", "cancelar2 -> id = " + id + " Mensaje = " + mensaje + " Hora = " + hora);

        }
    }
}

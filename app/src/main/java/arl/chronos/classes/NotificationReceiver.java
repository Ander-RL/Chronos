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
public class NotificationReceiver extends BroadcastReceiver {
    private Calendar c;
    private int id;
    private String mensaje;
    private String hora;

    public static final String ID_ALARMA = "id_alarma";
    public static final String MENSAJE = "mensaje_alarma";

    @Override
    public void onReceive(Context context, Intent intent) {
        c = Calendar.getInstance();

        mensaje = intent.getStringExtra(NotificationHelper.ID_INTENT);
        hora = intent.getStringExtra(NotificationHelper.HORA);
        id = intent.getIntExtra(NotificationHelper.ID_ALARMA, 0);

        if (mensaje.equals(NotificationHelper.POSTPONER)) {

            Toast.makeText(context, "Alarma postpuesta 15 minutos", Toast.LENGTH_SHORT).show();

            /*// Se cancela la alarma que se repite
            AlarmManager alarmManagerRepeat = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intentCancelar = new Intent(context, AlertReceiver.class);
            PendingIntent pendingIntentRepeat = PendingIntent.getBroadcast(context, id, intentCancelar, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManagerRepeat.cancel(pendingIntentRepeat);*/

            // Se crea una alarma para que suene en 15 minutos
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent newIntent = new Intent(context, AlertReceiver.class);
            newIntent.putExtra(MENSAJE, hora);
            newIntent.putExtra(ID_ALARMA, id);
            newIntent.putExtra("parar", "si");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, newIntent, PendingIntent.FLAG_UPDATE_CURRENT); // FLAG envia la info de putExtra

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + (2000), pendingIntent); // TODO Postponer a 15000 milisegundos
            Log.d("NOTIF_RECEIV", "Postponer -> id = " + id + " / Mensaje = " + mensaje + " / Hora = " + hora);
        }
    }
}

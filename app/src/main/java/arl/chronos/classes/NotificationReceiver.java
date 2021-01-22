package arl.chronos.classes;

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

import arl.chronos.adapters.RcvAdapterAlarmas;


// Clase para para añadir funcionalidades a las notificaciones (botones)
// Permite ejecutar codigo sin que la app este abierta
public class NotificationReceiver extends BroadcastReceiver {
    private Calendar c;
    private int id;
    private String mensaje;
    private String hora;
    private Vibrator vibrator;

    public static final String ID_ALARMA = "id_alarma";
    public static final String MENSAJE = "mensaje_alarma";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        c = Calendar.getInstance();

        mensaje = intent.getStringExtra(NotificationHelper.ID_INTENT);
        hora = intent.getStringExtra(NotificationHelper.HORA);
        id = intent.getIntExtra(NotificationHelper.ID_ALARMA, 0);

        if (mensaje.equals(NotificationHelper.POSPONER)) {

            Toast.makeText(context, "Alarma postpuesta 15 minutos", Toast.LENGTH_SHORT).show();

            // Se crea una alarma para que suene en 15 minutos
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent newIntent = new Intent(context, AlertReceiver.class);
            newIntent.putExtra(MENSAJE, hora);
            newIntent.putExtra(ID_ALARMA, id);
            newIntent.putExtra("parar", "si");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, newIntent, PendingIntent.FLAG_UPDATE_CURRENT); // FLAG envia la info de putExtra

            Intent intentServicio = new Intent(context, ServicioSonido.class);
            intentServicio.setAction(".classes.ServicioSonido");
            context.stopService(intentServicio);

            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.cancel();
            
            notificationManager.cancel(id);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + (1000*60*15), pendingIntent); // Se activa en 15 minutos
            Log.d("NOTIF_RECEIV", "Postponer -> id = " + id + " / Mensaje = " + mensaje + " / Hora = " + hora);
        }
    }
}

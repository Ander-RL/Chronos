package arl.chronos.classes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;


// Clase para para añadir funcionalidades a las notificaciones (botones)
// Permite ejecutar codigo sin que la app este abierta
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String mensaje = intent.getStringExtra(NotificationHelper.CANCELAR);
        int id = intent.getIntExtra(NotificationHelper.ID_ALARMA, 0);
        // TODO Se cancela la alarma pero la notificacion no desaparece
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentCancelar = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intentCancelar, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(context, "Alarma " + mensaje + " cancelada", Toast.LENGTH_SHORT).show();
    }
}

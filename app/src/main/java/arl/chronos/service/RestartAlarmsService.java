package arl.chronos.service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;

import arl.chronos.CrearEditarAlarma;
import arl.chronos.classes.Alarma;
import arl.chronos.classes.AlarmaUnica;
import arl.chronos.classes.WakeLocker;
import arl.chronos.database.AlarmaDAO;
import arl.chronos.database.BaseDatos;
import arl.chronos.database.Repositorio;
import arl.chronos.receiver.AlarmReceiver;
import arl.chronos.receiver.AlertReceiver;
import arl.chronos.receiver.NotificationHelper;

import static arl.chronos.adapters.RcvAdapterAlarmas.ID_ALARMA;
import static arl.chronos.adapters.RcvAdapterAlarmas.MENSAJE;
import static arl.chronos.adapters.RcvAdapterAlarmas.PARAR;

public class RestartAlarmsService extends JobIntentService {

    public static final String MENSAJE = "mensaje_alarma";
    public static final String ID_ALARMA = "id_alarma";
    public static final String PARAR = "parar";
    public static final String EXTRA_SONIDO = "arl.chronos.EXTRA_SONIDO";
    public static final String EXTRA_SONAR = "arl.chronos.EXTRA_SONAR";
    public static final String EXTRA_URI = "arl.chronos.EXTRA_URI";


    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, RestartAlarmsService.class, 0, work);

        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "Chronos::MyWakelockTag");
        wakeLock.acquire(5*60*1000L /*5 minutes*/);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        Log.d("Prueba", "onHandleWork");

        Context context = getApplicationContext();

        BaseDatos baseDatos = BaseDatos.getInstance(getApplication());
        AlarmaDAO alarmaDAO = baseDatos.alarmaDAO();
        ArrayList<Alarma> alarmas = (ArrayList<Alarma>) alarmaDAO.getTodaAlarma();

        if (!alarmas.isEmpty()) {
            for (Alarma alarma : alarmas) {

                int code = alarma.getId();
                String ho = alarma.getHora();
                String mi = alarma.getMinuto();
                String nombreSonido = alarma.getNombreSonido();
                String sonidoUri = alarma.getSonidoUri();
                boolean sonar = alarma.getSonar();
                Calendar c = Calendar.getInstance();
                c.set(Calendar.SECOND, 0);
                int hora = Integer.parseInt(ho);
                int minuto = Integer.parseInt(mi);

                Log.d("Prueba", "hora = " + ho + "  minuto = " + mi);
                Log.d("Prueba", "Calendar hora = " + c.get(Calendar.HOUR_OF_DAY) + " Calendar minuto = " + c.get(Calendar.MINUTE));
                Log.d("Prueba", "Calendar today = " + c.get(Calendar.DAY_OF_WEEK));

                if ((c.get(Calendar.HOUR_OF_DAY) == hora) && (c.get(Calendar.MINUTE) == minuto)) {
                    if (alarma.getLunes() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                        Log.d("Prueba", "lunes");
                    } else if (alarma.getMartes() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                        Log.d("Prueba", "martes");
                    } else if (alarma.getMiercoles() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                        Log.d("Prueba", "miercoles");
                    } else if (alarma.getJueves() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                        Log.d("Prueba", "jueves");
                    } else if (alarma.getViernes() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                        Log.d("Prueba", "viernes");
                    } else if (alarma.getSabado() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                        Log.d("Prueba", "sabado");
                    } else if (alarma.getDomingo() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                        Log.d("Prueba", "domingo");
                    }
                }

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intento = new Intent(context, AlarmReceiver.class);
                intent.putExtra("alarma", "activar");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intento, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

                c.set(Calendar.HOUR_OF_DAY, hora);
                c.set(Calendar.MINUTE, minuto);

                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(c.getTimeInMillis() + (24 * 60 * 60 * 1000), pendingIntent), pendingIntent);
            }
        }
    }

    private void sendNotification(Context context, String ho, String mi, int code, String nombreSonido, String sonidoUri, boolean sonar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intento = new Intent(context, AlertReceiver.class);
        intento.putExtra(MENSAJE, ho + ":" + mi);
        intento.putExtra(ID_ALARMA, code);
        intento.putExtra(PARAR, "no");
        intento.putExtra(EXTRA_SONIDO, nombreSonido);
        intento.putExtra(EXTRA_URI, sonidoUri);
        intento.putExtra(EXTRA_SONAR, sonar);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intento, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), pendingIntent);
    }
}

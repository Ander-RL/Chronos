package arl.chronos.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executor;

import arl.chronos.classes.Alarma;
import arl.chronos.classes.MyApplication;
import arl.chronos.classes.WakeLocker;
import arl.chronos.database.AlarmaDAO;
import arl.chronos.database.BaseDatos;
import arl.chronos.service.RestartAlarmsService;

import static android.content.Context.POWER_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String MENSAJE = "mensaje_alarma";
    public static final String ID_ALARMA = "id_alarma";
    public static final String PARAR = "parar";
    public static final String EXTRA_SONIDO = "arl.chronos.EXTRA_SONIDO";
    public static final String EXTRA_SONAR = "arl.chronos.EXTRA_SONAR";
    public static final String EXTRA_URI = "arl.chronos.EXTRA_URI";


    AlarmaDAO alarmaDAO;
    ArrayList<Alarma> alarmas;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getExtras().containsKey("android.intent.action.BOOT_COMPLETED") || intent.getExtras().containsKey("alarma")) {

            PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "Chronos::MyWakelockTag");
            wakeLock.acquire(5 * 60 * 1000L /*5 minutes*/);

            Thread thread = new Thread() {
                @Override
                public void run() {
                    BaseDatos baseDatos = BaseDatos.getInstance(MyApplication.get());
                    alarmaDAO = baseDatos.alarmaDAO();
                    alarmas = (ArrayList<Alarma>) alarmaDAO.getTodaAlarma();
                    checkIfActiveAndNotify(context, intent);
                }
            };

            thread.start();
            thread.interrupt();
        }
    }

    private void checkIfActiveAndNotify(Context context, Intent intent) {
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

                if ((c.get(Calendar.HOUR_OF_DAY) == hora) && (c.get(Calendar.MINUTE) == minuto)) {
                    if (alarma.getLunes() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                    } else if (alarma.getMartes() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                    } else if (alarma.getMiercoles() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                    } else if (alarma.getJueves() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                    } else if (alarma.getViernes() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                    } else if (alarma.getSabado() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
                    } else if (alarma.getDomingo() && (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                        sendNotification(context, ho, mi, code, nombreSonido, sonidoUri, sonar);
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

        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(Calendar.getInstance().getTimeInMillis(), pendingIntent), pendingIntent);
    }
}


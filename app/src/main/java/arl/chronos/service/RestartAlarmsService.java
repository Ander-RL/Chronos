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
import arl.chronos.database.AlarmaDAO;
import arl.chronos.database.BaseDatos;
import arl.chronos.database.Repositorio;
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
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        Context context = getApplicationContext();

        BaseDatos baseDatos = BaseDatos.getInstance(getApplication());
        AlarmaDAO alarmaDAO = baseDatos.alarmaDAO();
        ArrayList<Alarma> alarmas = (ArrayList<Alarma>) alarmaDAO.getTodaAlarma();
        ArrayList<AlarmaUnica> alarmasUnicas = (ArrayList<AlarmaUnica>) alarmaDAO.getTodaAlarmaUnica();

        if (!alarmas.isEmpty()) {
            for (Alarma alarma : alarmas) {

                if (isStopped()) return;

                int code = alarma.getId();
                String ho = alarma.getHora();
                String mi = alarma.getMinuto();
                String nombreSonido = alarma.getNombreSonido();
                String sonidoUri = alarma.getSonidoUri();
                boolean sonar = alarma.getSonar();
                boolean activated = alarma.getActivated();

                if (activated) {

                    Log.d("Prueba", "onHandleWork alarma");

                    Calendar c = Calendar.getInstance();

                    if (alarma.getLunes()) {
                        int d = c.get(Calendar.DAY_OF_MONTH);
                        int dif = Math.abs(c.get(Calendar.DAY_OF_WEEK) - 2);
                        int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int day = d + (7 - dif);

                        if (day > max) {
                            dif = day - max;
                            day = dif;
                        }
                        c.set(Calendar.DAY_OF_MONTH, day);
                        setAlarmClock(c, ho, mi, nombreSonido, sonidoUri, sonar, code + 200, context);
                    }

                    if (alarma.getMartes()) {
                        int d = c.get(Calendar.DAY_OF_MONTH);
                        int dif = Math.abs(c.get(Calendar.DAY_OF_WEEK) - 3);
                        int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int day = d + dif;

                        if (day > max) {
                            dif = day - max;
                            day = dif;
                        }
                        c.set(Calendar.DAY_OF_MONTH, day);
                        setAlarmClock(c, ho, mi, nombreSonido, sonidoUri, sonar, code + 300, context);
                    }

                    if (alarma.getMiercoles()) {
                        int d = c.get(Calendar.DAY_OF_MONTH);
                        int dif = Math.abs(c.get(Calendar.DAY_OF_WEEK) - 4);
                        int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int day = d + dif;

                        if (day > max) {
                            dif = day - max;
                            day = dif;
                        }
                        c.set(Calendar.DAY_OF_MONTH, day);
                        setAlarmClock(c, ho, mi, nombreSonido, sonidoUri, sonar, code + 400, context);
                    }

                    if (alarma.getJueves()) {
                        int d = c.get(Calendar.DAY_OF_MONTH);
                        int dif = Math.abs(c.get(Calendar.DAY_OF_WEEK) - 5);
                        int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int day = d + dif;

                        if (day > max) {
                            dif = day - max;
                            day = dif;
                        }
                        c.set(Calendar.DAY_OF_MONTH, day);
                        setAlarmClock(c, ho, mi, nombreSonido, sonidoUri, sonar, code + 500, context);
                    }

                    if (alarma.getViernes()) {
                        int d = c.get(Calendar.DAY_OF_MONTH);
                        int dif = Math.abs(c.get(Calendar.DAY_OF_WEEK) - 6);
                        int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int day = d + dif;

                        if (day > max) {
                            dif = day - max;
                            day = dif;
                        }
                        c.set(Calendar.DAY_OF_MONTH, day);
                        setAlarmClock(c, ho, mi, nombreSonido, sonidoUri, sonar, code + 600, context);
                    }

                    if (alarma.getSabado()) {

                        if(c.get(Calendar.DAY_OF_WEEK) == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) && c.get(Calendar.HOUR_OF_DAY) == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
                            setAlarmClock(c, ho, mi, nombreSonido, sonidoUri, sonar, code + 100, context);
                        }

                        int d = c.get(Calendar.DAY_OF_MONTH);
                        int dif = Math.abs(c.get(Calendar.DAY_OF_WEEK) - 7);
                        int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int day = d + dif;

                        if (day > max) {
                            dif = day - max;
                            day = dif;
                        }
                        c.set(Calendar.DAY_OF_MONTH, day);
                        setAlarmClock(c, ho, mi, nombreSonido, sonidoUri, sonar, code + 700, context);
                    }

                    if (alarma.getDomingo()) {

                        if(c.get(Calendar.DAY_OF_WEEK) == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) && c.get(Calendar.HOUR_OF_DAY) == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
                            setAlarmClock(c, ho, mi, nombreSonido, sonidoUri, sonar, code + 100, context);
                        }

                        int d = c.get(Calendar.DAY_OF_MONTH);
                        int dif = Math.abs(c.get(Calendar.DAY_OF_WEEK) - 1);
                        int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                        int day = d + dif;

                        if (day > max) {
                            dif = day - max;
                            day = dif;
                        }
                        c.set(Calendar.DAY_OF_MONTH, day);
                        setAlarmClock(c, ho, mi, nombreSonido, sonidoUri, sonar, code + 100, context);
                    }


                    //c.set(Calendar.DAY_OF_WEEK, diaAlarma(alarma));
                    /*c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ho));
                    c.set(Calendar.MINUTE, Integer.parseInt(mi));
                    c.set(Calendar.SECOND, 0);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intento = new Intent(context, AlertReceiver.class);
                    intento.putExtra(MENSAJE, ho + ":" + mi);
                    intento.putExtra(ID_ALARMA, code);
                    intento.putExtra(PARAR, "no");
                    intento.putExtra(EXTRA_SONIDO, nombreSonido);
                    intento.putExtra(EXTRA_URI, sonidoUri);
                    intento.putExtra(EXTRA_SONAR, sonar);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intento, PendingIntent.FLAG_UPDATE_CURRENT);

                    //alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(c.getTimeInMillis(), pendingIntent), pendingIntent);*/
                }
            }
        }

        if (!alarmas.isEmpty()) {
            for (AlarmaUnica alarma : alarmasUnicas) {

                if (isStopped()) return;

                int code = alarma.getId();
                String ho = alarma.getHora();
                String mi = alarma.getMinuto();
                String nombreSonido = alarma.getNombreSonido();
                String sonidoUri = alarma.getSonidoUri();
                boolean sonar = alarma.getSonar();
                boolean activated = alarma.getActivated();

                if (activated) {

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(alarma.getDia()));
                    c.set(Calendar.MONTH, Integer.parseInt(alarma.getMes()) - 1);
                    c.set(Calendar.YEAR, Integer.parseInt(alarma.getAno()));
                    c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ho));
                    c.set(Calendar.MINUTE, Integer.parseInt(mi));

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intento = new Intent(context, AlertReceiver.class);
                    intento.putExtra(MENSAJE, ho + ":" + mi);
                    intento.putExtra(ID_ALARMA, code);
                    intento.putExtra(PARAR, "no");
                    intento.putExtra(EXTRA_SONIDO, nombreSonido);
                    intento.putExtra(EXTRA_URI, sonidoUri);
                    intento.putExtra(EXTRA_SONAR, sonar);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intento, PendingIntent.FLAG_UPDATE_CURRENT);

                    //alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(c.getTimeInMillis(), pendingIntent), pendingIntent);
                }
            }
        }
    }

    private void setAlarmClock(Calendar c, String ho, String mi, String nombreSonido, String sonidoUri, boolean sonar, int code, Context context) {
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ho));
        c.set(Calendar.MINUTE, Integer.parseInt(mi));
        c.set(Calendar.SECOND, 0);

        Log.d("Prueba", "setAlarmClock");
        Log.d("Prueba", "millis Alarma = " + c.getTimeInMillis());
        Log.d("Prueba", "millis Calendar.getInstance().getTimeInMillis() = " + Calendar.getInstance().getTimeInMillis());

        Log.d("Prueba", "Alarma day of week = " + c.get(Calendar.DAY_OF_WEEK));
        Log.d("Prueba", "Alarma day of month = " + c.get(Calendar.DAY_OF_MONTH));
        Log.d("Prueba", "Alarma month = " + c.get(Calendar.MONTH));
        Log.d("Prueba", "Alarma year = " + c.get(Calendar.YEAR));
        Log.d("Prueba", "Alarma code = " + code);
        Log.d("Prueba", "Alarma context = " + context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intento = new Intent(context, AlertReceiver.class);
        intento.putExtra(MENSAJE, ho + ":" + mi);
        intento.putExtra(ID_ALARMA, code);
        intento.putExtra(PARAR, "no");
        intento.putExtra(EXTRA_SONIDO, nombreSonido);
        intento.putExtra(EXTRA_URI, sonidoUri);
        intento.putExtra(EXTRA_SONAR, sonar);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intento, PendingIntent.FLAG_UPDATE_CURRENT);

        if (c.before(Calendar.getInstance())) {
            Log.d("Prueba", "BEFORE");
            c.setTimeInMillis(c.getTimeInMillis() + (24 * 60 * 60 * 1000 * 7));
        }
        Log.d("Prueba", "millis Alarma despues = " + c.getTimeInMillis());
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(c.getTimeInMillis(), pendingIntent), pendingIntent);

        try{
        Thread.sleep(5000);} catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int diaAlarma(Alarma alarma) {
        if (alarma.getLunes()) {
            return Calendar.MONDAY;
        } else if (alarma.getMartes()) {
            return Calendar.TUESDAY;
        } else if (alarma.getMiercoles()) {
            return Calendar.WEDNESDAY;
        } else if (alarma.getJueves()) {
            return Calendar.THURSDAY;
        } else if (alarma.getViernes()) {
            return Calendar.FRIDAY;
        } else if (alarma.getSabado()) {
            return Calendar.SATURDAY;
        } else {
            return Calendar.SUNDAY;
        }
    }
}

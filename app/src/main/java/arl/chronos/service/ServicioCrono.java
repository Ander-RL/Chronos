package arl.chronos.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import arl.chronos.fragments.TabFragmentCrono;
import arl.chronos.receiver.AlertReceiver;
import arl.chronos.receiver.NotificationHelper;

public class ServicioCrono extends Service {

    private long tiempoRestante;
    private Context context;
    private CountDownTimer countDownTimer;
    private final long countDownInterval = 1000;
    private final Intent broadCastIntent = new Intent(CUENTA_ATRAS_BR);

    public static boolean IS_RUNNING;

    public static final String CUENTA_ATRAS_BR = "arl.chronos.fragments.TabFragmentCrono";
    public static final String START = "start";
    public static final String PAUSE = "pause";
    public static final String STOP = "stop";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyForeground();
        } else {
            startForeground(3, new Notification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String cronoAccion = intent.getStringExtra(TabFragmentCrono.EXTRA_CRONO_ACCION);
        tiempoRestante = intent.getLongExtra(TabFragmentCrono.EXTRA_CRONO_TIEMPO, 0);

        switch (cronoAccion) {
            case START:
                startCrono();
                Log.d("ServicioCrono", "accion = " + cronoAccion + "    timepoRestante = " + tiempoRestante);
                break;
            case PAUSE:
                pauseCrono();
                Log.d("ServicioCrono", "accion = " + cronoAccion);
                break;
            case STOP:
                stopCrono();
                Log.d("ServicioCrono", "accion = " + cronoAccion);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCrono();
    }

    private void startCrono() {

        IS_RUNNING = true;

        countDownTimer = new CountDownTimer(tiempoRestante, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = millisUntilFinished;
                broadCastIntent.putExtra(CUENTA_ATRAS_BR, tiempoRestante);
                sendBroadcast(broadCastIntent);
                Log.d("ServicioCrono", "millisUntilFinished = " + tiempoRestante);
            }

            @Override
            public void onFinish() {
                IS_RUNNING = false;

                broadCastIntent.putExtra(CUENTA_ATRAS_BR, tiempoRestante);
                sendBroadcast(broadCastIntent);

                Intent alertBroadcastIntent = new Intent(context, AlertReceiver.class);
                alertBroadcastIntent.putExtra(TabFragmentCrono.EXTRA_CRONO_TIEMPO, 0);
                alertBroadcastIntent.putExtra(TabFragmentCrono.EXTRA_CRONO_ACCION, "onFinish");
                context.sendBroadcast(alertBroadcastIntent);
                Log.d("ServicioCrono", "onFinish");
            }

        }.start();
    }

    private void pauseCrono() {
        // El tiempo restante se guarda en el campo tiempoRestante
        countDownTimer.cancel();
    }

    private void stopCrono() {
        countDownTimer.cancel();
    }

    private void startMyForeground() {
        NotificationHelper notificationHelper = new NotificationHelper(context, "foregroundService", 1);
        NotificationCompat.Builder nb = notificationHelper.getCanalNotification();
        notificationHelper.getManager().notify(1, nb.build());
        startForeground(1,nb.build());
    }
}

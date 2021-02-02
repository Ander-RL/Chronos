package arl.chronos.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import arl.chronos.fragments.TabFragmentCrono;

public class ServicioCrono extends Service {

    private long tiempoRestante;
    private CountDownTimer countDownTimer;
    private final long countDownInterval = 1000;
    private final Intent broadCastIntent = new Intent(CUENTA_ATRAS_BR);

    public static final String CUENTA_ATRAS_BR = "arl.chronos.fragments.TabFragmentCrono";
    public static final String CUENTA_ATRAS = "cuenta_atras";

    private static final String START = "start";
    private static final String PAUSE = "pause";
    private static final String STOP = "stop";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String cronoAccion = intent.getStringExtra(TabFragmentCrono.EXTRA_CRONO_ACCION);
        tiempoRestante = intent.getLongExtra(TabFragmentCrono.EXTRA_CRONO_TIEMPO, 0);

        switch (cronoAccion) {
            case START:
                startCrono();
                Log.d("////////SERVICIO//////", "accion = " + cronoAccion);
                break;
            case PAUSE:
                pauseCrono();
                Log.d("////////SERVICIO//////", "accion = " + cronoAccion);
                break;
            case STOP:
                stopCrono();
                Log.d("////////SERVICIO//////", "accion = " + cronoAccion);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startCrono() {

        countDownTimer = new CountDownTimer(tiempoRestante, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = millisUntilFinished;
                broadCastIntent.putExtra(CUENTA_ATRAS, tiempoRestante);
                sendBroadcast(broadCastIntent);
            }

            @Override
            public void onFinish() {}

        }.start();
    }

    private void pauseCrono() {
        // El tiempo restante se guarda en el campo tiempoRestante
        countDownTimer.cancel();
    }

    private void stopCrono() {
        countDownTimer.cancel();
    }
}

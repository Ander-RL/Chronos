package arl.chronos.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

import arl.chronos.EscogerSonido;

public class ServicioSonido extends Service {
    private MediaPlayer mediaPlayer;
    private String nombreSonido;
    private String sonidoUri;

    public static final String EXTRA_NOMBRE_SONIDO = "arl.chronos.EXTRA_NOMBRE_SONIDO";
    public static final String EXTRA_URI_SONIDO = "arl.chronos.EXTRA_URI_SONIDO";

    public ServicioSonido() {
    }

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
        nombreSonido = intent.getStringExtra(EXTRA_NOMBRE_SONIDO);
        sonidoUri = intent.getStringExtra(EXTRA_URI_SONIDO);

        Log.d("EscogerSonido", "ServicioSonido  onStartCommand  sonidoElegido = " + nombreSonido);
        Log.d("EscogerSonido", "ServicioSonido  onStartCommand  sonidoUri = " + sonidoUri);

        try {
            if (mediaPlayer != null) {
                Log.d("EscogerSonido", "MediaPlayer != null");
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(sonidoUri));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
    }
}

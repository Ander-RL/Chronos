package arl.chronos.classes;

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
    private Context context;
    private String nombreSonido;
    private String sonidoUri;

    public ServicioSonido() {
    }

    /*public ServicioSonido(Context context, String nombreSonido, String sonidoUri){
        this.context = context;
        this.nombreSonido = nombreSonido;
        this.sonidoUri = sonidoUri;
    }*/

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
        nombreSonido = intent.getStringExtra(EscogerSonido.EXTRA_NOMBRE_SONIDO);
        sonidoUri = intent.getStringExtra(EscogerSonido.EXTRA_URI_SONIDO);

        Log.d("////////SERVICIO//////",  nombreSonido + " ---> Uri: " + sonidoUri);

        try {
            if (mediaPlayer != null) {
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

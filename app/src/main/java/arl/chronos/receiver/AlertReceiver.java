package arl.chronos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.material.tabs.TabLayout;

import arl.chronos.CrearEditarAlarma;
import arl.chronos.EscogerSonido;
import arl.chronos.adapters.RcvAdapterAlarmas;
import arl.chronos.classes.WakeLocker;
import arl.chronos.fragments.TabFragmentCrono;
import arl.chronos.service.ServicioCrono;
import arl.chronos.service.ServicioSonido;

public class AlertReceiver extends BroadcastReceiver {
    private String mensaje;
    private int id;
    private String parar;
    private Vibrator vibrator;
    private String nombreSonido;
    private String sonidoUri;
    private Boolean sonar;

    public static final String ID_ALARMA = "id_alarma";
    public static final String EXTRA_SONIDO = "arl.chronos.EXTRA_SONIDO";
    public static final String EXTRA_SONAR = "arl.chronos.EXTRA_SONAR";
    public static final String EXTRA_URI = "arl.chronos.EXTRA_URI";
    public static final String EXTRA_NOMBRE_SONIDO = "arl.chronos.EXTRA_NOMBRE_SONIDO";
    public static final String EXTRA_URI_SONIDO = "arl.chronos.EXTRA_URI_SONIDO";

    @Override
    public void onReceive(Context context, Intent intent) {
        WakeLocker.acquire(context);
        if (intent.getExtras().containsKey("mensaje_alarma")) {

            WakeLocker.acquire(context);

            mensaje = intent.getStringExtra("mensaje_alarma");
            id = intent.getIntExtra(ID_ALARMA, 0);
            parar = intent.getStringExtra("parar");
            nombreSonido = intent.getStringExtra(EXTRA_SONIDO);
            sonidoUri = intent.getStringExtra(EXTRA_URI);
            sonar = intent.getBooleanExtra(EXTRA_SONAR, false);

            //vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            //vibrator.vibrate(new long[]{500, 1000, 500, 1000}, 0);

            // Crea Service para reproducir Musica.
            if (sonar && !nombreSonido.equals("")) {
                Intent intentServicio = new Intent(context, ServicioSonido.class);
                intentServicio.putExtra(EXTRA_NOMBRE_SONIDO, nombreSonido);
                intentServicio.putExtra(EXTRA_URI_SONIDO, sonidoUri);
                intentServicio.setAction(".classes.ServicioSonido");
                context.startService(intentServicio);
            }

            NotificationHelper notificationHelper = new NotificationHelper(context, mensaje, id);
            NotificationCompat.Builder nb = notificationHelper.getCanalNotification();
            notificationHelper.getManager().notify(id, nb.build());

            WakeLocker.release();
        }

        WakeLocker.release();

        if (intent.getExtras().containsKey(TabFragmentCrono.EXTRA_CRONO_TIEMPO)) {

            long tiempoRestante = intent.getLongExtra(TabFragmentCrono.EXTRA_CRONO_TIEMPO, (long) 0);
            String accion = intent.getStringExtra(TabFragmentCrono.EXTRA_CRONO_ACCION);

            if (accion.equals("onFinish")){
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(new long[]{500, 1000, 500, 1000}, 0);
                // Lanzar notificacion diciendo que ha terminado
                NotificationHelper notificationHelper = new NotificationHelper(context, accion, 2);
                NotificationCompat.Builder nb = notificationHelper.getCanalNotification();
                notificationHelper.getManager().notify(2, nb.build());
            }
        }
    }
}

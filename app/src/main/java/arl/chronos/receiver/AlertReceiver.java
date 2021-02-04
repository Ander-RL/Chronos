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
    private ServicioSonido servicioSonido;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getExtras().containsKey("mensaje_alarma")) {
            mensaje = intent.getStringExtra("mensaje_alarma");
            id = intent.getIntExtra(RcvAdapterAlarmas.ID_ALARMA, 0);
            parar = intent.getStringExtra("parar");
            nombreSonido = intent.getStringExtra(CrearEditarAlarma.EXTRA_SONIDO);
            sonidoUri = intent.getStringExtra(CrearEditarAlarma.EXTRA_URI);
            sonar = intent.getBooleanExtra(CrearEditarAlarma.EXTRA_SONAR, false);

            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{500, 1000, 500, 1000}, 0);

            Log.d("AlertReceiver", nombreSonido + " ---> Uri: " + sonidoUri + " ---> Sonar: " + sonar);

            // Crea Service para reproducir Musica.
            if (sonar && !nombreSonido.equals("")) {
                Intent intentServicio = new Intent(context, ServicioSonido.class);
                intentServicio.putExtra(EscogerSonido.EXTRA_NOMBRE_SONIDO, nombreSonido);
                intentServicio.putExtra(EscogerSonido.EXTRA_URI_SONIDO, sonidoUri);
                intentServicio.setAction(".classes.ServicioSonido");
                context.startService(intentServicio);
            }

            NotificationHelper notificationHelper = new NotificationHelper(context, mensaje, id);
            NotificationCompat.Builder nb = notificationHelper.getCanalNotification();
            Log.d("AlertReceiver", "AlertReceiver -> id = " + id + " / Mensaje = " + mensaje + " / Parar = " + parar);
            notificationHelper.getManager().notify(id, nb.build());
        }

        if (intent.getExtras().containsKey(TabFragmentCrono.EXTRA_CRONO_TIEMPO)) {

            Log.d("AlertReceiver", "EXTRA_CRONO_TIEMPO ---> " + intent.getLongExtra(TabFragmentCrono.EXTRA_CRONO_TIEMPO, 0));

            long tiempoRestante = intent.getLongExtra(TabFragmentCrono.EXTRA_CRONO_TIEMPO, (long) 0);
            String accion = intent.getStringExtra(TabFragmentCrono.EXTRA_CRONO_ACCION);
            Log.d("AlertReceiver", "accion ---> " + accion);

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

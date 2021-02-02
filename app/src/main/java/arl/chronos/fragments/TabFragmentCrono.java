package arl.chronos.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import arl.chronos.R;
import arl.chronos.service.ServicioCrono;

public class TabFragmentCrono extends Fragment {
    private View view;
    private boolean start;
    private boolean pause;
    private boolean stop;
    private MaterialButton btnPlay;
    private MaterialButton btnPause;
    private MaterialButton btnStop;
    private TextView tvSegundos;
    private TextView tvMinutos;
    private TextView tvHoras;
    private String h;
    private String m;
    private String s;
    private int seg;
    private int min;
    private int hora;
    private long tiempoRestante;

    public static final String EXTRA_CRONO_ACCION = "arl.chronos.fragments.EXTRA_CRONO_ACCION";
    public static final String EXTRA_CRONO_TIEMPO = "arl.chronos.fragments.EXTRA_CRONO_TIEMPO";

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            actualizarContador(intent);
        }
    };

    public TabFragmentCrono() {
        // Constructor por defecto
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el Layout para este Fragment
        view = inflater.inflate(R.layout.fragment_tab_crono, container, false);

        tvSegundos = view.findViewById(R.id.tv_crono_seg);
        tvMinutos = view.findViewById(R.id.tv_crono_min);
        tvHoras = view.findViewById(R.id.tv_crono_hora);
        btnPlay = view.findViewById(R.id.btn_crono_start);
        btnPause = view.findViewById(R.id.btn_crono_pause);
        btnStop = view.findViewById(R.id.btn_crono_stop);

        h = tvHoras.getText().toString();
        m = tvMinutos.getText().toString();
        s = tvSegundos.getText().toString();

        tiempoRestante = convertirAMilis(h, m, s);

        Intent servicioIntent = new Intent(getContext(), ServicioCrono.class);
        servicioIntent.putExtra(EXTRA_CRONO_TIEMPO, tiempoRestante);

        btnPlay.setOnClickListener(btnView -> {
            btnView.setEnabled(false);
            view.getContext().startService(servicioIntent);
        });

        return view;
    }

    private void actualizarContador(Intent intent) {
        if (intent.getExtras() != null) {

            tiempoRestante = intent.getLongExtra(ServicioCrono.CUENTA_ATRAS, 0);

            if (((tiempoRestante / 1000 / 60) % 60) != 0) {
                hora = (int) tiempoRestante / 1000 / 60 / 60;
                min = (int) tiempoRestante / 1000 / 60 % 60;
            } else {
                hora = (int) tiempoRestante / 1000 / 60 / 60;
                min = 0;
            }

            if (((min * 1000) % 60) != 0) {
                seg = min % 60;
            } else {
                seg = 0;
            }

            if(hora < 10){h = "0" + hora;}
            if(min < 10){m = "0" + min;}
            if(seg < 10){s = "0" + seg;}

            tvHoras.setText(h);
            tvMinutos.setText(m);
            tvSegundos.setText(s);
        }
    }

    private Long convertirAMilis(String h, String m, String s) {

        int ho = (Integer.parseInt(h)) * 1000 * 60 * 60;
        int mi = (Integer.parseInt(m)) * 1000 * 60;
        int se = (Integer.parseInt(s)) * 1000;

        return (long) (ho + mi + se);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
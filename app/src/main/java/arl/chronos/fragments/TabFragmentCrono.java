package arl.chronos.fragments;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import arl.chronos.R;
import arl.chronos.classes.Crono;
import arl.chronos.database.MyViewModel;
import arl.chronos.receiver.AlertReceiver;
import arl.chronos.service.ServicioCrono;

public class TabFragmentCrono extends Fragment {
    private View view;
    private MaterialButton btnPlay;
    private MaterialButton btnPause;
    private MaterialButton btnStop;
    private MaterialButton btnReset;
    private MyViewModel myViewModel;
    private TextView tvCrono;
    private TextView etSegundos;
    private TextView etMinutos;
    private TextView etHoras;
    private String h;
    private String m;
    private String s;
    private int seg;
    private int min;
    private int hora;
    private long tiempoRestante;

    public static final String EXTRA_CRONO_ACCION = "arl.chronos.fragments.EXTRA_CRONO_ACCION";
    public static final String EXTRA_CRONO_TIEMPO = "arl.chronos.fragments.EXTRA_CRONO_TIEMPO";

    // Recibe el Broadcast emitido por ServicioCrono en onTick() de starCrono()
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

        etSegundos = view.findViewById(R.id.et_crono_seg);
        etMinutos = view.findViewById(R.id.et_crono_min);
        etHoras = view.findViewById(R.id.et_crono_hora);
        btnPlay = view.findViewById(R.id.btn_crono_start);
        btnPause = view.findViewById(R.id.btn_crono_pause);
        btnStop = view.findViewById(R.id.btn_crono_stop);
        btnReset = view.findViewById(R.id.btn_crono_reset);
        tvCrono = view.findViewById(R.id.tv_crono);
        tvCrono.setMovementMethod(new ScrollingMovementMethod());

        btnPause.setEnabled(false);
        btnStop.setEnabled(false);
        btnPlay.setBackgroundColor(getResources().getColor(R.color.blue_500, null));

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        myViewModel.getTodoCrono().observe(getViewLifecycleOwner(), new Observer<List<Crono>>() {
            @Override
            public void onChanged(List<Crono> cronosList) {
                for (Crono crono : cronosList) {
                    tvCrono.append(crono.getHoras() + ":" + crono.getMinutos() + ":" + crono.getSegundos() + "\n");
                }
            }
        });

        btnPlay.setOnClickListener(btnView -> {

            h = etHoras.getText().toString();
            m = etMinutos.getText().toString();
            s = etSegundos.getText().toString();

            if (h.isEmpty()) {
                h = "00";
            }
            if (m.isEmpty()) {
                m = "00";
            }
            if (s.isEmpty()) {
                s = "00";
            }

            if (h.equals("00") && m.equals("00") && s.equals("00")) {
                Toast.makeText(getContext(), "Inserta un valor", Toast.LENGTH_SHORT).show();
            } else {
                tiempoRestante = convertirAMilis(h, m, s);
                tvCrono.setText("");

                if (Integer.parseInt(h) < 10 && !h.equals("00")) {
                    h = "0" + h;
                }
                if (Integer.parseInt(m) < 10 && !m.equals("00")) {
                    m = "0" + m;
                }
                if (Integer.parseInt(s) < 10 && !s.equals("00")) {
                    s = "0" + s;
                }

                myViewModel.insertCrono(new Crono(h, m, s));

                Log.d("TabFragmentCrono", h + ":" + m + ":" + s);
                Log.d("TabFragmentCrono", "tiempoRestante = " + tiempoRestante);

                Intent servicioIntent = new Intent(getContext(), ServicioCrono.class);
                servicioIntent.putExtra(EXTRA_CRONO_TIEMPO, tiempoRestante);
                servicioIntent.putExtra(EXTRA_CRONO_ACCION, ServicioCrono.START);
                view.getContext().startService(servicioIntent);

                btnView.setEnabled(false);
                btnPause.setEnabled(true);
                btnStop.setEnabled(true);
                btnView.setBackgroundColor(getResources().getColor(R.color.slightly_darker_white, null));
                btnPause.setBackgroundColor(getResources().getColor(R.color.blue_500, null));
                btnStop.setBackgroundColor(getResources().getColor(R.color.blue_500, null));
            }
        });

        btnPause.setOnClickListener(btnView -> {
            Intent servicioIntent = new Intent(getContext(), ServicioCrono.class);
            view.getContext().stopService(servicioIntent);

            btnView.setEnabled(false);
            btnView.setBackgroundColor(getResources().getColor(R.color.slightly_darker_white, null));
            btnPlay.setEnabled(true);
            btnPlay.setBackgroundColor(getResources().getColor(R.color.blue_500, null));
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                Intent servicioIntent = new Intent(getContext(), ServicioCrono.class);
                view.getContext().stopService(servicioIntent);

                btnView.setEnabled(false);
                btnView.setBackgroundColor(getResources().getColor(R.color.slightly_darker_white, null));
                btnPause.setEnabled(false);
                btnPause.setBackgroundColor(getResources().getColor(R.color.slightly_darker_white, null));
                btnPlay.setEnabled(true);
                btnPlay.setBackgroundColor(getResources().getColor(R.color.blue_500, null));

                etHoras.setText("");
                etMinutos.setText("");
                etSegundos.setText("");
            }
        });

        btnReset.setOnClickListener(btnView -> {
            tvCrono.setText("");
            myViewModel.deleteTodoCronos();
        });

        return view;
    }

    private void actualizarContador(Intent intent) {
        if (intent.getExtras() != null) {

            tiempoRestante = intent.getLongExtra(ServicioCrono.CUENTA_ATRAS_BR, 0);
            Log.d("TabFragmentCrono", "tiempoRestante = " + tiempoRestante);

            hora = (int) tiempoRestante / 1000 / 60 / 60;
            min = (int) (tiempoRestante / 1000 / 60) % 60;
            seg = (int) (tiempoRestante / 1000) % 60;

            if (hora < 10) {
                h = "0" + hora;
            } else {
                h = String.valueOf(hora);
            }
            if (min < 10) {
                m = "0" + min;
            } else {
                m = String.valueOf(min);
            }
            if (seg < 10) {
                s = "0" + seg;
            } else {
                s = String.valueOf(seg);
            }

            etHoras.setText(h);
            etMinutos.setText(m);
            etSegundos.setText(s);
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
        requireActivity().registerReceiver(br, new IntentFilter(ServicioCrono.CUENTA_ATRAS_BR));

        Log.d("TabFragmentCrono", "onResume() ---> tiempoRestante = " + tiempoRestante);
        // Si el tiempo llego a 0, vacia el TextView
        if (tiempoRestante == 0) {
            etHoras.setText("");
            etMinutos.setText("");
            etSegundos.setText("");
        }
        // Si el servicio sigue funcionando, actualiza los botones
        if (ServicioCrono.IS_RUNNING) {
            btnPlay.setEnabled(false);
            btnPause.setEnabled(true);
            btnStop.setEnabled(true);
            btnPlay.setBackgroundColor(getResources().getColor(R.color.slightly_darker_white, null));
            btnPause.setBackgroundColor(getResources().getColor(R.color.blue_500, null));
            btnStop.setBackgroundColor(getResources().getColor(R.color.blue_500, null));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Se lanza un broadcast a AlestReceiver para poner en marcha el contador en notificacion
        if (ServicioCrono.IS_RUNNING) {
            Intent alertBroadcastIntent = new Intent(getContext(), AlertReceiver.class);
            alertBroadcastIntent.putExtra(EXTRA_CRONO_TIEMPO, tiempoRestante);
            alertBroadcastIntent.putExtra(EXTRA_CRONO_ACCION, "onPause");
            getContext().sendBroadcast(alertBroadcastIntent);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TabFragmentCrono", "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(br);
    }
}
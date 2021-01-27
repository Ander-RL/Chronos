package arl.chronos.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import arl.chronos.R;
import arl.chronos.classes.Alarma;
import arl.chronos.classes.MostrarEventos;
import arl.chronos.database.MyViewModel;


public class TabFragmentCalendario extends Fragment {
    private View view;
    private List<EventDay> eventos = new ArrayList<>();
    private com.applandeo.materialcalendarview.CalendarView calendarView;
    private MyViewModel myViewModel;
    private ArrayList<Alarma> listAlarmas = new ArrayList<>();
    private Thread popularCalendario;
    private ExecutorService executorService;

    public TabFragmentCalendario() {
        // Constructor por defecto
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el Layout para este Fragment
        view = inflater.inflate(R.layout.fragment_tab_calendario, container, false);
        Log.d("FragmentCalendario", "onCreateView()");
        calendarView = view.findViewById(R.id.calendarView);
        executorService = Executors.newSingleThreadExecutor();
        // Se recoge la lista de la base de datos mediante ViewModel y se pasa la lista
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        myViewModel.getTodasAlarmas().observe(getViewLifecycleOwner(), new Observer<List<Alarma>>() {
            @Override
            public void onChanged(List<Alarma> alarmas) {
                listAlarmas = (ArrayList<Alarma>) alarmas;
                executorService.execute(new MostrarEventos(calendarView, eventos, listAlarmas));
            }
        });
        // TODO Dibujar el calendario le supone mucho trabajo al hilo principal (Main Thread)
        // TODO Reducir el numero de meses a dibujar?

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
package arl.chronos.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
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
    private CalendarView calendarView;
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
        // Se recoge la lista de la base de datos mediante ViewModel y se pasa la lista
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        myViewModel.getTodasAlarmas().observe(getViewLifecycleOwner(), new Observer<List<Alarma>>() {
            @Override
            public void onChanged(List<Alarma> alarmas) {
                listAlarmas = (ArrayList<Alarma>) alarmas;
            }
        });

        Log.d("FragmentCalendario", "onCreateView()");

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        //popularCalendario = new Thread(new MostrarEventos(calendarView, eventos, listAlarmas));
        //popularCalendario.start();

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new MostrarEventos(calendarView, eventos, listAlarmas));
        /*calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                String day = calendarView.getFirstSelectedDate().toString();
                Toast.makeText(getContext(), day, Toast.LENGTH_SHORT).show();
            }
        });*/

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //popularCalendario.interrupt();
        executorService.shutdown();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("FragmentCalendario", "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FragmentCalendario", "onResume()");
    }
}
package arl.chronos.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import arl.chronos.R;
import arl.chronos.classes.Alarma;
import arl.chronos.database.MyViewModel;


public class TabFragmentCalendario extends Fragment {
    private View view;
    private List<EventDay> eventos = new ArrayList<>();
    private CalendarView calendarView;
    private MyViewModel myViewModel;
    private ArrayList<Alarma> listAlarmas = new ArrayList<>();

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
        //listAlarmas.addAll(Objects.requireNonNull(myViewModel.getTodasAlarmas().getValue()));
        myViewModel.getTodasAlarmas().observe(getViewLifecycleOwner(), new Observer<List<Alarma>>() {
            @Override
            public void onChanged(List<Alarma> alarmas) {
                listAlarmas = (ArrayList<Alarma>) alarmas;
            }
        });

        //setDiasAlarma(listAlarmas, eventos);
        /*
        Log.d("CALENDARIO", "Year = " + calendar.get(Calendar.YEAR) +
                " --- Month = " + calendar.get(Calendar.MONTH) + " --- Week = " + calendar.get(Calendar.WEEK_OF_MONTH) +
                " --- Day = " + calendar.get(Calendar.DAY_OF_WEEK));
         */

        //Calendar calendar = Calendar.getInstance();
        //eventos.add(new EventDay(calendar, R.drawable.ic_alarm));

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        //calendarView.setEvents(eventos);
        calendarView.setEvents(getDiasAlarma(listAlarmas, eventos));
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                String day = calendarView.getFirstSelectedDate().toString();
                Toast.makeText(getContext(), day, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private List<EventDay> getDiasAlarma(ArrayList<Alarma> listAlarmas, List<EventDay> eventos) {
        for (Alarma alarma : listAlarmas) {
            if (alarma.getLunes()) {
                for (int i = 1; i <= 5; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK, 2);
                    calendar.set(Calendar.WEEK_OF_MONTH, i);
                    eventos.add(new EventDay(calendar, R.drawable.ic_alarm));
                    Log.d("CALENDARIO", "Year = " + calendar.get(Calendar.YEAR) +
                            " --- Month = " + calendar.get(Calendar.MONTH) + " --- Week = " + calendar.get(Calendar.WEEK_OF_MONTH) +
                            " --- Day = " + calendar.get(Calendar.DAY_OF_WEEK));
                }
            }
        }

            /*calendar.set(Calendar.WEEK_OF_MONTH, 2);
            Log.d("CALENDARIO", "Year = " + calendar.get(Calendar.YEAR) +
                    " --- Month = " + calendar.get(Calendar.MONTH) + " --- Week = " + calendar.get(Calendar.WEEK_OF_MONTH) +
                    " --- Day = " + calendar.get(Calendar.DAY_OF_WEEK));
            //if (alarma.getLunes()) {eventos.add(new EventDay(calendar.M, R.drawable.ic_alarm));}*/

        return eventos;

    }

    /*private void verAlarmas() {
        // Se recoge la lista de la base de datos mediante ViewModel y se pasa al adaptador
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        listAlarmas.addAll(Objects.requireNonNull(myViewModel.getTodasAlarmas().getValue()));

        EventDay eventDay = new EventDay(calendarView.getFirstSelectedDate(), R.drawable.ic_alarm);
        Calendar diaSelec = calendarView.getFirstSelectedDate();

        for (Alarma alarma : listAlarmas) {
            if (diaSelec.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
                
            }
        }
    }*/
}
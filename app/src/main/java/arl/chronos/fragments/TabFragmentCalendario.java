package arl.chronos.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

//import com.applandeo.materialcalendarview.EventDay;

//import com.applandeo.materialcalendarview.EventDay;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import arl.chronos.R;
import arl.chronos.classes.Alarma;
//import arl.chronos.classes.MostrarEventos;
import arl.chronos.classes.EventDecorator;
import arl.chronos.classes.PoblarCalendario;
import arl.chronos.database.MyViewModel;


public class TabFragmentCalendario extends Fragment {
    private View view;
    //private List<EventDay> eventos = new ArrayList<>();
    //private com.applandeo.materialcalendarview.CalendarView calendarView;
    private MaterialCalendarView calendarView;
    private MyViewModel myViewModel;
    private ArrayList<Alarma> listAlarmas = new ArrayList<>();
    private ExecutorService executorService;
    private HashSet<CalendarDay> days = new HashSet<>();

    public TabFragmentCalendario() {
        // Constructor por defecto
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el Layout para este Fragment
        view = inflater.inflate(R.layout.fragment_tab_calendario, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        executorService = Executors.newSingleThreadExecutor();
        // Se recoge la lista de la base de datos mediante ViewModel y se pasa la lista
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);


//-------------------------------------------------------------------------------------------------------------------

        calendarView.setTitleMonths(R.array.material_calendar_months_array);
        calendarView.setWeekDayLabels(R.array.material_calendar_day_array);
        int dia = calendarView.getCurrentDate().getDay();
        //EventDecorator eventDecorator = new EventDecorator(view, listAlarmas);
        CalendarDay calendarDay = CalendarDay.today();

        //ExecutorService executorService = Executors.newSingleThreadExecutor();

        myViewModel.getTodasAlarmas().observe(getViewLifecycleOwner(), new Observer<List<Alarma>>() {
            @Override
            public void onChanged(List<Alarma> alarmas) {
                listAlarmas = (ArrayList<Alarma>) alarmas;

                calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
                    @Override
                    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                        //int month = date.getMonth();
                        //int year = date.getYear();
                        //Log.d("fragmentcalendario", "Month = " + month + "    year = " + year);

                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        PoblarCalendario poblarCalendario = new PoblarCalendario(view, listAlarmas, calendarView, date);
                        //executorService.execute(poblarCalendario);
                        try {
                            days = executorService.submit(poblarCalendario).get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }

                        EventDecorator eventDecorator = new EventDecorator(view, days);

                        for (CalendarDay day : days) {
                            if (eventDecorator.shouldDecorate(day)) {
                                calendarView.addDecorator(eventDecorator);
                            }
                        }
                        executorService.shutdown();
                    }
                });
            }
        });


        /*myViewModel.getTodasAlarmas().observe(getViewLifecycleOwner(), new Observer<List<Alarma>>() {
            @Override
            public void onChanged(List<Alarma> alarmas) {
                listAlarmas = (ArrayList<Alarma>) alarmas;

                PoblarCalendario poblarCalendario = new PoblarCalendario(view, listAlarmas, calendarView);
                executorService.execute(poblarCalendario);

                //executorService.execute(new MostrarEventos(calendarView, eventos, listAlarmas));
                //EventDecorator eventDecorator = new EventDecorator(view, days);

                //addCalendarDays(listAlarmas);

                /*new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (CalendarDay day : days) {
                            //Log.d("FragmentCalendario", " --- Dentro de Handler ---");
                            if (eventDecorator.shouldDecorate(day)) {
                                calendarView.addDecorator(eventDecorator);
                            }
                        }
                        Log.d("FragmentCalendario", " days.size() = " + days.size());
                    }
                });
            }
        });*/

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    private void addCalendarDays(ArrayList<Alarma> listAlarmas) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        for (Alarma alarma : listAlarmas) {
            //for (int j = 0; j < 12; j++) {
            for (int j = 0; j < 4; j++) {
                for (int i = 1; i <= 5; i++) {
                    for (int k = 1; k <= 7; k++) {

                        calendar.set(Calendar.MONTH, j);
                        calendar.set(Calendar.DAY_OF_WEEK, k);
                        calendar.set(Calendar.WEEK_OF_MONTH, i);

                        int month = calendar.get(Calendar.MONTH) + 1;
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        //Log.d("FragmentCalendario", " dayOfMonth ---> " + dayOfMonth + "       month ---> " + month);

                        // Domingo
                        if (dayOfWeek == Calendar.SUNDAY && alarma.getDomingo()) {
                            days.add(CalendarDay.from(year, month, dayOfMonth));
                        }
                        // Lunes
                        if (dayOfWeek == Calendar.MONDAY && alarma.getLunes()) {
                            days.add(CalendarDay.from(year, month, dayOfMonth));
                        }
                        // Martes
                        if (dayOfWeek == Calendar.TUESDAY && alarma.getMartes()) {
                            days.add(CalendarDay.from(year, month, dayOfMonth));
                        }
                        // Miercoles
                        if (dayOfWeek == Calendar.WEDNESDAY && alarma.getMiercoles()) {
                            days.add(CalendarDay.from(year, month, dayOfMonth));
                        }
                        // Jueves
                        if (dayOfWeek == Calendar.THURSDAY && alarma.getJueves()) {
                            days.add(CalendarDay.from(year, month, dayOfMonth));
                        }
                        // Viernes
                        if (dayOfWeek == Calendar.FRIDAY && alarma.getViernes()) {
                            days.add(CalendarDay.from(year, month, dayOfMonth));
                        }
                        // Sabado
                        if (dayOfWeek == Calendar.SATURDAY && alarma.getSabado()) {
                            days.add(CalendarDay.from(year, month, dayOfMonth));
                        }
                    }
                }
            }
        }
    }
}
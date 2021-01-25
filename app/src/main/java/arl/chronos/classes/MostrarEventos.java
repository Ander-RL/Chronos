package arl.chronos.classes;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import arl.chronos.R;
import arl.chronos.database.MyViewModel;

// Mostrar eventos en el calendario
public class MostrarEventos implements Runnable {
    private final List<EventDay> eventos;
    private final CalendarView calendarView;
    private final ArrayList<Alarma> listAlarmas;

    public MostrarEventos(CalendarView calendarView, List<EventDay> eventos, ArrayList<Alarma> listAlarmas) {
        this.calendarView = calendarView;
        this.eventos = eventos;
        this.listAlarmas = listAlarmas;
    }

    @Override
    public void run() {
        for (Alarma alarma : listAlarmas) {
            for (int j = 0; j < 12; j++) {
                if (alarma.getLunes()) {
                    for (int i = 1; i <= 5; i++) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, j);
                        calendar.set(Calendar.DAY_OF_WEEK, 2);
                        calendar.set(Calendar.WEEK_OF_MONTH, i);
                        eventos.add(new EventDay(calendar, R.drawable.ic_alarm));
                        Log.d("CALENDARIO", "Year = " + calendar.get(Calendar.YEAR) +
                                " --- Month = " + calendar.get(Calendar.MONTH) + " --- Week = " + calendar.get(Calendar.WEEK_OF_MONTH) +
                                " --- Day = " + calendar.get(Calendar.DAY_OF_WEEK));
                    }
                }
                if (alarma.getMartes()) {
                    for (int i = 1; i <= 5; i++) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, j);
                        calendar.set(Calendar.DAY_OF_WEEK, 3);
                        calendar.set(Calendar.WEEK_OF_MONTH, i);
                        eventos.add(new EventDay(calendar, R.drawable.ic_alarm));
                    }
                }
                if (alarma.getMiercoles()) {
                    for (int i = 1; i <= 5; i++) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, j);
                        calendar.set(Calendar.DAY_OF_WEEK, 4);
                        calendar.set(Calendar.WEEK_OF_MONTH, i);
                        eventos.add(new EventDay(calendar, R.drawable.ic_alarm));
                    }
                }
                if (alarma.getJueves()) {
                    for (int i = 1; i <= 5; i++) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, j);
                        calendar.set(Calendar.DAY_OF_WEEK, 5);
                        calendar.set(Calendar.WEEK_OF_MONTH, i);
                        eventos.add(new EventDay(calendar, R.drawable.ic_alarm));
                    }
                }
                if (alarma.getViernes()) {
                    for (int i = 1; i <= 5; i++) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, j);
                        calendar.set(Calendar.DAY_OF_WEEK, 6);
                        calendar.set(Calendar.WEEK_OF_MONTH, i);
                        eventos.add(new EventDay(calendar, R.drawable.ic_alarm));
                    }
                }
                if (alarma.getSabado()) {
                    for (int i = 1; i <= 5; i++) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, j);
                        calendar.set(Calendar.DAY_OF_WEEK, 7);
                        calendar.set(Calendar.WEEK_OF_MONTH, i);
                        eventos.add(new EventDay(calendar, R.drawable.ic_alarm));
                    }
                }
                if (alarma.getDomingo()) {
                    for (int i = 1; i <= 5; i++) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, j);
                        calendar.set(Calendar.DAY_OF_WEEK, 1);
                        calendar.set(Calendar.WEEK_OF_MONTH, i);
                        eventos.add(new EventDay(calendar, R.drawable.ic_alarm));
                    }
                }
            }
        }
        calendarView.setEvents(eventos);
    }
}

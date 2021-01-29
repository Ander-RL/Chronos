package arl.chronos.classes;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import arl.chronos.R;

public class EventDecorator implements DayViewDecorator {
    private ArrayList<Alarma> listAlarmas;
    private HashSet<CalendarDay> days = new HashSet<>();
    private final Calendar calendar = Calendar.getInstance();
    private final View view;
    private boolean decorate;
    private CalendarDay calendarDay = CalendarDay.today();

    public EventDecorator(View view, HashSet<CalendarDay> days) {
        this.view = view;
        this.days = days;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        //int dia = day.getDay();
        //calendar.set(Calendar.DAY_OF_MONTH, dia);
        //Log.d("FragmentCalendario", days.contains(day) + "");
        return days.contains(day);

        /*for (Alarma alarma : listAlarmas) {
            // Domingo
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && alarma.getDomingo()) {
                decorate = true;
            }
            // Lunes
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && alarma.getLunes()) {
                decorate = true;
            }
            // Martes
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY && alarma.getMartes()) {
                decorate = true;
            }
            // Miercoles
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY && alarma.getMiercoles()) {
                decorate = true;
            }
            // Jueves
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY && alarma.getJueves()) {
                decorate = true;
            }
            // Viernes
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && alarma.getViernes()) {
                decorate = true;
            }
            // Sabado
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && alarma.getSabado()) {
                decorate = true;
            }
        }

        boolean prueba = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;

        Log.d("FragmentCalendario", "Decorate = " + decorate + " --- Day = " + dia +
                 " --- DayOfMonth = " + calendar.get(Calendar.DAY_OF_MONTH) +
                " --- DayOfWeek = " + calendar.get(Calendar.DAY_OF_WEEK) +
                " --- calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY = " + prueba);

                return decorate;
                */
    }

    @Override
    public void decorate(DayViewFacade viewDay) {
        //viewDay.setSelectionDrawable(view.getResources().getDrawable(R.drawable.ic_alarm, view.getContext().getTheme()));
        //viewDay.setSelectionDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_alarm, null));
        viewDay.addSpan(new DotSpan(10, ResourcesCompat.getColor(view.getResources(), R.color.blue_500, null)));
    }

}

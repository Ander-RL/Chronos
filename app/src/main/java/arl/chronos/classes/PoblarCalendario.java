package arl.chronos.classes;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.concurrent.Callable;

public class PoblarCalendario implements Callable<HashSet<CalendarDay>> {
    private HashSet<CalendarDay> days = new HashSet<>();
    private ArrayList<Alarma> listAlarmas;
    private View view;
    private MaterialCalendarView calendarView;
    private CalendarDay date;

    public PoblarCalendario(View view, ArrayList<Alarma> listAlarmas, MaterialCalendarView calendarView, CalendarDay date) {
        this.view = view;
        this.listAlarmas = listAlarmas;
        this.calendarView = calendarView;
        this.date = date;
    }

    @Override
    public HashSet<CalendarDay> call() throws Exception {
        addCalendarDays(listAlarmas, date.getMonth(), date.getYear());

        return days;
    }

    private void addCalendarDays(ArrayList<Alarma> listAlarmas, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);

        //int year = calendar.get(Calendar.YEAR);

        for (Alarma alarma : listAlarmas) {
            for (int i = 1; i <= 5; i++) {

                /*calendar.set(Calendar.WEEK_OF_MONTH, i);
                calendar.set(Calendar.DAY_OF_WEEK, 1);
                LocalDate time = LocalDate.of(year, month, calendar.get(Calendar.DAY_OF_MONTH));
                int dayOfWeek = time.getDayOfWeek().getValue();
                int diff = 7 - dayOfWeek;
                int day = calendar.get(Calendar.DAY_OF_MONTH) + diff;
                int maxDays = time.getMonth().maxLength();*/

                // Domingo
                if (alarma.getDomingo() && alarma.getActivated()) {

                    YearMonth yearMonthObject = YearMonth.of(year, month);
                    int maxDays = yearMonthObject.lengthOfMonth();
                    int cont = 0;
                    int day = 1;
                    LocalDate time;

                    while (cont < maxDays) {
                        time = LocalDate.of(year, month, day + cont);
                        String weekDay = time.getDayOfWeek().toString();
                        if (weekDay.equals("SUNDAY")) {
                            days.add(CalendarDay.from(year, month, day + cont));
                        }
                        cont++;
                    }
                }

                // Lunes
                if (alarma.getLunes() && alarma.getActivated()) {

                    YearMonth yearMonthObject = YearMonth.of(year, month);
                    int maxDays = yearMonthObject.lengthOfMonth();
                    int cont = 0;
                    int day = 1;
                    LocalDate time;

                    while (cont < maxDays) {
                        time = LocalDate.of(year, month, day + cont);
                        String weekDay = time.getDayOfWeek().toString();
                        if (weekDay.equals("MONDAY")) {
                            days.add(CalendarDay.from(year, month, day + cont));
                        }
                        cont++;
                    }
                    /*calendar.set(Calendar.WEEK_OF_MONTH, i);
                    calendar.set(Calendar.DAY_OF_WEEK, 2);

                    YearMonth yearMonthObject = YearMonth.of(year, month);
                    int maxDays = yearMonthObject.lengthOfMonth();

                    LocalDate time;

                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    if (dayOfMonth <= maxDays) {
                        time = LocalDate.of(year, month, dayOfMonth);
                    } else {
                        int oldDay = dayOfMonth;
                        dayOfMonth = oldDay - (oldDay - maxDays);
                        //Log.d("FragmentCalendario", " max = " + maxDays + "  dayOfMonth = " + dayOfMonth);
                        time = LocalDate.of(year, month, dayOfMonth);
                    }

                    int dayOfWeek = time.getDayOfWeek().getValue();
                    int diff = 1 - dayOfWeek;
                    int day = dayOfMonth + diff;
                    //Log.d("FragmentCalendario", " diff = " + diff + "  dayOfWeek = " + dayOfWeek + "  day = " + day);
                    if (!(day > maxDays) && !(day < 1)) {
                        days.add(CalendarDay.from(year, month, day));
                    }*/
                    /*for (CalendarDay d : days) {
                        Log.d("FragmentCalendario", "day = " + d.getDay()
                                + "   dayOfWeek = " + calendar.get(Calendar.DAY_OF_WEEK)
                                + "   dayOfMonth = " + calendar.get(Calendar.DAY_OF_MONTH)
                                + "   weekOfMonth = " + calendar.get(Calendar.WEEK_OF_MONTH)
                                + "   month = " + d.getMonth()
                                + "   year = " + d.getYear()
                                + "   time DayOfWeek = " + time.getDayOfWeek()
                                + "   value = " + time.getDayOfWeek().getValue()
                                + "   diff = " + diff
                                + "   alarma = " + alarma.getDomingo()
                                + "   day = " + day
                                + "   maxDays = " + maxDays);
                    }*/
                }

                // Martes
                if (alarma.getMartes() && alarma.getActivated()) {
                    YearMonth yearMonthObject = YearMonth.of(year, month);
                    int maxDays = yearMonthObject.lengthOfMonth();
                    int cont = 0;
                    int day = 1;
                    LocalDate time;

                    while (cont < maxDays) {
                        time = LocalDate.of(year, month, day + cont);
                        String weekDay = time.getDayOfWeek().toString();
                        if (weekDay.equals("TUESDAY")) {
                            days.add(CalendarDay.from(year, month, day + cont));
                        }
                        cont++;
                    }
                    /*calendar.set(Calendar.WEEK_OF_MONTH, i);
                    calendar.set(Calendar.DAY_OF_WEEK, 3);

                    YearMonth yearMonthObject = YearMonth.of(year, month);
                    int maxDays = yearMonthObject.lengthOfMonth();

                    LocalDate time;

                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    if (dayOfMonth <= maxDays) {
                        time = LocalDate.of(year, month, dayOfMonth);
                    } else {
                        int oldDay = dayOfMonth;
                        dayOfMonth = oldDay - (oldDay - maxDays);
                        time = LocalDate.of(year, month, dayOfMonth);
                    }

                    int dayOfWeek = time.getDayOfWeek().getValue();
                    int diff = 2 - dayOfWeek;
                    int day = dayOfMonth + diff;
                    if (!(day > maxDays) && !(day < 1)) {
                        days.add(CalendarDay.from(year, month, day));
                    }*/
                }

                // Miercoles
                if (alarma.getMiercoles() && alarma.getActivated()) {
                    YearMonth yearMonthObject = YearMonth.of(year, month);
                    int maxDays = yearMonthObject.lengthOfMonth();
                    int cont = 0;
                    int day = 1;
                    LocalDate time;

                    while (cont < maxDays) {
                        time = LocalDate.of(year, month, day + cont);
                        String weekDay = time.getDayOfWeek().toString();
                        if (weekDay.equals("WEDNESDAY")) {
                            days.add(CalendarDay.from(year, month, day + cont));
                            //Log.d("FragmentCalendario", "day en if = " + day);
                        }
                        cont++;
                        //Log.d("FragmentCalendario", "cont = " + cont + "  day = " + day + "  weekDay = " + weekDay + "  maxDays = " + maxDays);
                    }
                }
                /*if (alarma.getMiercoles() && alarma.getActivated()) {

                    calendar.set(Calendar.WEEK_OF_MONTH, i);
                    calendar.set(Calendar.DAY_OF_WEEK, 4);

                    YearMonth yearMonthObject = YearMonth.of(year, month);
                    int maxDays = yearMonthObject.lengthOfMonth();

                    LocalDate time;

                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    Log.d("FragmentCalendario", "dayOfMonth inicial = " + dayOfMonth);
                    if (dayOfMonth <= maxDays) {
                        time = LocalDate.of(year, month, dayOfMonth);
                        Log.d("FragmentCalendario", "dentro del if + dayOfMonth = " + dayOfMonth);
                    } else {
                        int oldDay = dayOfMonth;
                        dayOfMonth = oldDay - (oldDay - maxDays);
                        time = LocalDate.of(year, month, dayOfMonth);
                    }

                    int dayOfWeek = time.getDayOfWeek().getValue();
                    int diff = 3 - dayOfWeek;
                    int day = dayOfMonth + diff;
                    Log.d("FragmentCalendario", "max = " + maxDays +
                            "  dayOfMonth = " + dayOfMonth +
                            "  diff = " + diff +
                            "  dayOfWeek = " + dayOfWeek +
                            "  day = " + day +
                            "  miercoles = " + time.getDayOfWeek() +
                            "  n = " + time.getDayOfWeek().getValue());
                    if (!(day > maxDays) && !(day < 1)) {
                        days.add(CalendarDay.from(year, month, day));
                    }

                    if (day == 0) {
                        int monthN = time.getMonth().getValue();
                        Log.d("FragmentCalendario", "monthN = " + monthN);
                        String weekDay = time.getDayOfWeek().toString();
                        int cont = 0;
                        Log.d("FragmentCalendario", "weekDay = " + weekDay);
                        while (!weekDay.equals("WEDNESDAY")) {
                            day = maxDays - cont;
                            time = LocalDate.of(year, month, day);
                            weekDay = time.getDayOfWeek().toString();
                            Log.d("FragmentCalendario", "day = " + day + "   weekDay = " + weekDay);
                            cont++;
                        }
                        days.add(CalendarDay.from(year, month, day));
                    }
                }*/

                // Jueves
                if (alarma.getJueves() && alarma.getActivated()) {
                    YearMonth yearMonthObject = YearMonth.of(year, month);
                    int maxDays = yearMonthObject.lengthOfMonth();
                    int cont = 0;
                    int day = 1;
                    LocalDate time;

                    while (cont < maxDays) {
                        time = LocalDate.of(year, month, day + cont);
                        String weekDay = time.getDayOfWeek().toString();
                        if (weekDay.equals("THURSDAY")) {
                            days.add(CalendarDay.from(year, month, day + cont));
                        }
                        cont++;
                    }
                }

                // Viernes
                if (alarma.getViernes() && alarma.getActivated()) {
                    YearMonth yearMonthObject = YearMonth.of(year, month);
                    int maxDays = yearMonthObject.lengthOfMonth();
                    int cont = 0;
                    int day = 1;
                    LocalDate time;

                    while (cont < maxDays) {
                        time = LocalDate.of(year, month, day + cont);
                        String weekDay = time.getDayOfWeek().toString();
                        if (weekDay.equals("FRIDAY")) {
                            days.add(CalendarDay.from(year, month, day + cont));
                        }
                        cont++;
                    }
                }

                // Sabado
                if (alarma.getSabado() && alarma.getActivated()) {
                    YearMonth yearMonthObject = YearMonth.of(year, month);
                    int maxDays = yearMonthObject.lengthOfMonth();
                    int cont = 0;
                    int day = 1;
                    LocalDate time;

                    while (cont < maxDays) {
                        time = LocalDate.of(year, month, day + cont);
                        String weekDay = time.getDayOfWeek().toString();
                        if (weekDay.equals("SATURDAY")) {
                            days.add(CalendarDay.from(year, month, day + cont));
                        }
                        cont++;
                    }
                }
            }
        }
    }
}


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
            //for (int j = 0; j < 12; j++) {
            //for (int j = 0; j < 4; j++) {
            for (int i = 1; i <= 5; i++) {
                //for (int k = 1; k <= 7; k++) {

                /*calendar.set(Calendar.WEEK_OF_MONTH, i);
                calendar.set(Calendar.DAY_OF_WEEK, 1);
                LocalDate time = LocalDate.of(year, month, calendar.get(Calendar.DAY_OF_MONTH));
                int dayOfWeek = time.getDayOfWeek().getValue();
                int diff = 7 - dayOfWeek;
                int day = calendar.get(Calendar.DAY_OF_MONTH) + diff;
                int maxDays = time.getMonth().maxLength();*/

                // Domingo
                if (alarma.getDomingo() && alarma.getActivated()) {

                    calendar.set(Calendar.WEEK_OF_MONTH, i);
                    calendar.set(Calendar.DAY_OF_WEEK, 1);
                    LocalDate time = LocalDate.of(year, month, calendar.get(Calendar.DAY_OF_MONTH));
                    int dayOfWeek = time.getDayOfWeek().getValue();
                    int diff = 7 - dayOfWeek;
                    int day = calendar.get(Calendar.DAY_OF_MONTH) + diff;
                    int maxDays = time.getMonth().maxLength();


                    if (!(day > maxDays)) {
                        days.add(CalendarDay.from(year, month, day));
                    }


                }

                // Lunes
                if (alarma.getLunes() && alarma.getActivated()) {


                    calendar.set(Calendar.WEEK_OF_MONTH, i);
                    calendar.set(Calendar.DAY_OF_WEEK, 2);

                    YearMonth yearMonthObject = YearMonth.of(2021, 6);
                    int maxDays = yearMonthObject.lengthOfMonth();

                    LocalDate time;

                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    if (dayOfMonth <= maxDays) {
                        time = LocalDate.of(year, month, dayOfMonth);
                    } else {
                        int oldDay = dayOfMonth;
                        dayOfMonth = oldDay - (oldDay - maxDays);
                        Log.d("FragmentCalendario", " max = " + maxDays + "  dayOfMonth = " + dayOfMonth);
                        time = LocalDate.of(year, month, dayOfMonth);
                    }

                    int dayOfWeek = time.getDayOfWeek().getValue();
                    int diff = 1 - dayOfWeek;
                    int day = dayOfMonth + diff;
                    Log.d("FragmentCalendario", " diff = " + diff + "  dayOfWeek = " + dayOfWeek + "  day = " + day);
                    if (!(day > maxDays) && !(day < 1)) {
                        days.add(CalendarDay.from(year, month, day));
                    }
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
            }


            //}
        }
    }
}


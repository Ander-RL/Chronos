package arl.chronos.classes;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.concurrent.Callable;

public class PoblarCalendario implements Callable<HashSet<CalendarDay>> {
    private final HashSet<CalendarDay> days = new HashSet<>();
    private final ArrayList<Alarma> listAlarmas;
    private final CalendarDay date;

    public PoblarCalendario(ArrayList<Alarma> listAlarmas, CalendarDay date) {
        this.listAlarmas = listAlarmas;
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

        for (Alarma alarma : listAlarmas) {
            for (int i = 1; i <= 5; i++) {

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
                        }
                        cont++;
                    }
                }

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


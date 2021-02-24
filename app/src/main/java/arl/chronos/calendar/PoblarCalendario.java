package arl.chronos.calendar;

import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.concurrent.Callable;

import arl.chronos.classes.Alarma;
import arl.chronos.classes.AlarmaUnica;

public class PoblarCalendario implements Callable<HashSet<CalendarDay>> {
    private final HashSet<CalendarDay> days = new HashSet<>();
    private ArrayList<Alarma> listAlarmas = new ArrayList<>();
    private ArrayList<AlarmaUnica> listAlarmasUnicas = new ArrayList<>();
    private final CalendarDay date;
    private Boolean unica;

    public PoblarCalendario(ArrayList<Alarma> listAlarmas, CalendarDay date) {
        this.listAlarmas = listAlarmas;
        this.date = date;
    }

    public PoblarCalendario(ArrayList<AlarmaUnica> listAlarmasUnicas, CalendarDay date, Boolean unica) {
        this.listAlarmasUnicas = listAlarmasUnicas;
        this.date = date;
        this.unica = unica;
    }

    @Override
    public HashSet<CalendarDay> call() throws Exception {
        if (unica == null) {
            ArrayList<Object> listAlarmasObj = new ArrayList<>(listAlarmas);
            addCalendarDays(listAlarmasObj, date.getMonth(), date.getYear());
        } else {
            ArrayList<Object> listAlarmasObj = new ArrayList<>(listAlarmasUnicas);
            //Log.d("POBLAR_CALENDARIO", "call()  --->  date.getYear() = " + date.getYear() + "  date.getMonth() = " + date.getMonth() + "  date.getDay() = " + date.getDay());
            addCalendarDays(listAlarmasObj, date.getMonth(), date.getYear());
        }
        return days;
    }

    private void addCalendarDays(ArrayList<Object> listAlarmas, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);

        if (unica == null) {
            for (Object alarmaO : listAlarmas) {
                Alarma alarma = (Alarma) alarmaO;
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
        } else {
            for (Object alarmaO : listAlarmas) {
                AlarmaUnica alarmaUnica = (AlarmaUnica) alarmaO;
                int ano = Integer.parseInt(alarmaUnica.getAno());
                int mes = Integer.parseInt(alarmaUnica.getMes());
                int dia = Integer.parseInt(alarmaUnica.getDia());

                Log.d("POBLAR_CALENDARIO", "ano = " + ano + "  mes = " + mes + "  dia = " + dia);
                Log.d("POBLAR_CALENDARIO", "date.getYear() = " + date.getYear() + "  date.getMonth() = " + date.getMonth() + "  date.getDay() = " + date.getDay());

                if (alarmaUnica.getActivated()) {
                    days.add(CalendarDay.from(ano, mes, dia));
                }
            }
        }
    }
}


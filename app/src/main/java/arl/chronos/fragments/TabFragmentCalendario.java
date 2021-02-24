package arl.chronos.fragments;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.EasyEditSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import arl.chronos.MainActivity;
import arl.chronos.R;
import arl.chronos.classes.Alarma;
import arl.chronos.calendar.EventDecorator;
import arl.chronos.calendar.PoblarCalendario;
import arl.chronos.classes.AlarmaUnica;
import arl.chronos.database.MyViewModel;

import static arl.chronos.R.*;


public class TabFragmentCalendario extends Fragment {
    private View view;
    private MaterialCalendarView calendarView;
    private MyViewModel myViewModel;
    private ArrayList<Alarma> listAlarmas = new ArrayList<>();
    private ArrayList<AlarmaUnica> listAlarmasUnicas = new ArrayList<>();
    private HashSet<CalendarDay> days = new HashSet<>();
    private ExecutorService executorService;
    private static final String MONDAY = "MONDAY";
    private static final String TUESDAY = "TUESDAY";
    private static final String WEDNESDAY = "WEDNESDAY";
    private static final String THURSDAY = "THURSDAY";
    private static final String FRIDAY = "FRIDAY";
    private static final String SATURDAY = "SATURDAY";
    private static final String SUNDAY = "SUNDAY";

    public TabFragmentCalendario() {
        // Constructor por defecto
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el Layout para este Fragment
        view = inflater.inflate(layout.fragment_tab_calendario, container, false);
        calendarView = view.findViewById(id.calendarView);
        // Se recoge la lista de la base de datos mediante ViewModel y se pasa la lista
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        // Se crea el hilo de fonde en cuanto se recoge la info de la BBDD
        executorService = Executors.newSingleThreadExecutor();
        // Dibuja un circulo alrededor del día actual
        marcarHoy();

        myViewModel.getTodasAlarmas().observe(getViewLifecycleOwner(), new Observer<List<Alarma>>() {
            @Override
            public void onChanged(List<Alarma> alarmas) {
                listAlarmas = (ArrayList<Alarma>) alarmas;

                PoblarCalendario poblarCalendario = new PoblarCalendario(listAlarmas, CalendarDay.today());

                try {
                    days = executorService.submit(poblarCalendario).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                // Objeto para decorar el CalendarView
                EventDecorator eventDecorator = new EventDecorator(view, days);
                // Se añade una tarea al hilo de fondo (executorService)
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Se crea un Handler que relaciona el hilo de fondo con el Main Thread
                        // El Handler se relaciona con el Looper de la App (Looper.getMainLooper())
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                for (CalendarDay day : days) {
                                    if (eventDecorator.shouldDecorate(day)) {
                                        calendarView.addDecorator(eventDecorator);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });

        myViewModel.getTodasAlarmasUnicas().observe(getViewLifecycleOwner(), new Observer<List<AlarmaUnica>>() {
            @Override
            public void onChanged(List<AlarmaUnica> alarmas) {
                listAlarmasUnicas = (ArrayList<AlarmaUnica>) alarmas;

                PoblarCalendario poblarCalendario = new PoblarCalendario(listAlarmasUnicas, CalendarDay.today(), true);

                try {
                    days = executorService.submit(poblarCalendario).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                // Objeto para decorar el CalendarView
                EventDecorator eventDecorator = new EventDecorator(view, days);
                // Se añade una tarea al hilo de fondo (executorService)
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Se crea un Handler que relaciona el hilo de fondo con el Main Thread
                        // El Handler se relaciona con el Looper de la App (Looper.getMainLooper())
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                for (CalendarDay day : days) {
                                    if (eventDecorator.shouldDecorate(day)) {
                                        calendarView.addDecorator(eventDecorator);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                PoblarCalendario poblarCalendario = new PoblarCalendario(listAlarmas, date);

                try {
                    days = executorService.submit(poblarCalendario).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                EventDecorator eventDecorator = new EventDecorator(view, days);

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                for (CalendarDay day : days) {
                                    if (eventDecorator.shouldDecorate(day)) {
                                        calendarView.addDecorator(eventDecorator);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mostrarAlarmas(date);
            }
        });

        return view;
    }

    private void mostrarAlarmas(CalendarDay date) {
        int dia = date.getDay();
        int mes = date.getMonth();
        int ano = date.getYear();

        LocalDate localDate = LocalDate.of(ano, mes, dia);
        String diaElegido = localDate.getDayOfWeek().toString();
        StringBuilder mostrar = new StringBuilder();
        String hora = "";

        for (AlarmaUnica alarmaUnica : listAlarmasUnicas) {
            if (alarmaUnica.getActivated()) {
                if (Integer.parseInt(alarmaUnica.getAno()) == ano) {
                    if (Integer.parseInt(alarmaUnica.getMes()) == mes) {
                        if (Integer.parseInt(alarmaUnica.getDia()) == dia) {
                            hora = alarmaUnica.getHora() + ":" + alarmaUnica.getMinuto() + "\n";
                            mostrar.append(hora);
                        }
                    }
                }
            }
        }

        switch (diaElegido) {
            case MONDAY:
                for (Alarma alarma : listAlarmas) {
                    if (alarma.getLunes() && alarma.getActivated()) {
                        hora = alarma.getHora() + ":" + alarma.getMinuto() + "\n";
                        mostrar.append(hora);
                    }
                }
                break;

            case TUESDAY:
                for (Alarma alarma : listAlarmas) {
                    if (alarma.getMartes() && alarma.getActivated()) {
                        hora = alarma.getHora() + ":" + alarma.getMinuto() + "\n";
                        mostrar.append(hora);
                    }
                }
                break;

            case WEDNESDAY:
                for (Alarma alarma : listAlarmas) {
                    if (alarma.getMiercoles() && alarma.getActivated()) {
                        hora = alarma.getHora() + ":" + alarma.getMinuto() + "\n";
                        mostrar.append(hora);
                    }
                }
                break;

            case THURSDAY:
                for (Alarma alarma : listAlarmas) {
                    if (alarma.getJueves() && alarma.getActivated()) {
                        hora = alarma.getHora() + ":" + alarma.getMinuto() + "\n";
                        mostrar.append(hora);
                    }
                }
                break;

            case FRIDAY:
                for (Alarma alarma : listAlarmas) {
                    if (alarma.getViernes() && alarma.getActivated()) {
                        hora = alarma.getHora() + ":" + alarma.getMinuto() + "\n";
                        mostrar.append(hora);
                    }
                }
                break;

            case SATURDAY:
                for (Alarma alarma : listAlarmas) {
                    if (alarma.getSabado() && alarma.getActivated()) {
                        hora = alarma.getHora() + ":" + alarma.getMinuto() + "\n";
                        mostrar.append(hora);
                    }
                }
                break;

            case SUNDAY:
                for (Alarma alarma : listAlarmas) {
                    if (alarma.getDomingo() && alarma.getActivated()) {
                        hora = alarma.getHora() + ":" + alarma.getMinuto() + "\n";
                        mostrar.append(hora);
                    }
                }
                break;
        }
        if (mostrar.length() != 0) {
            int ultimo = mostrar.lastIndexOf("\n");
            mostrar.delete(ultimo, mostrar.length());
            Toast.makeText(view.getContext(), mostrar.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void marcarHoy() {

        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                int calday1 = day.getDay();
                int calmonth1 = day.getMonth();
                int calday2 = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int calmonth2 = Calendar.getInstance().get(Calendar.MONTH) + 1;

                return ((calday1 == calday2) && (calmonth1 == calmonth2));
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setSelectionDrawable(ResourcesCompat.getDrawable(getResources(), drawable.shape_circle, null));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
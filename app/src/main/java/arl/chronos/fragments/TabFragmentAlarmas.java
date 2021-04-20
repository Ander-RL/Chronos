package arl.chronos.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import arl.chronos.CrearEditarAlarma;
import arl.chronos.R;
import arl.chronos.adapters.RcvAdapterAlarmas;
import arl.chronos.classes.Alarma;
import arl.chronos.classes.AlarmaUnica;
import arl.chronos.database.MyViewModel;
import arl.chronos.receiver.AlarmReceiver;
import arl.chronos.receiver.AlertReceiver;

import static android.app.Activity.RESULT_OK;

public class TabFragmentAlarmas extends Fragment {

    FloatingActionButton fab;

    private RecyclerView recyclerView;
    private RcvAdapterAlarmas rcvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MyViewModel myViewModel;
    private View view;
    private int hora;
    private int min;
    private int ano;
    private int mes;
    private int dia;
    private Boolean activar;
    private Boolean l;
    private Boolean m;
    private Boolean x;
    private Boolean j;
    private Boolean v;
    private Boolean s;
    private Boolean d;
    private final String CERO = "0";
    private final String DOS_PUNTOS = ":";
    private String horaFormateada = "";
    private String minutoFormateado = "";
    private String nombreSonido;
    private Uri sonidoUri;
    private Boolean sonar;

    public static final int ADD_ALARMAS_REQUEST = 1;
    public static final int EDIT_ALARMAS_REQUEST = 2;

    public TabFragmentAlarmas() {
        // Constructor por defecto
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflar el Layout para este Fragment
        view = inflater.inflate(R.layout.fragment_tab_alarmas, container, false);
        fab = view.findViewById(R.id.fab_alarmas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CrearEditarAlarma.class);
                startActivityForResult(intent, ADD_ALARMAS_REQUEST);
            }
        });

        recyclerView = view.findViewById(R.id.rcv_alarmas);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        rcvAdapter = new RcvAdapterAlarmas(getContext());
        recyclerView.setAdapter(rcvAdapter);

        // Se recoge la lista de la base de datos mediante ViewModel y se pasa al adaptador
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        myViewModel.getTodasAlarmas().observe(getViewLifecycleOwner(), new Observer<List<Alarma>>() {
            @Override
            public void onChanged(List<Alarma> alarmas) {;
                rcvAdapter.setAlarmas(alarmas);
            }
        });

        myViewModel.getTodasAlarmasUnicas().observe(getViewLifecycleOwner(), new Observer<List<AlarmaUnica>>() {
            @Override
            public void onChanged(List<AlarmaUnica> alarmasUnicas) {
                rcvAdapter.setAlarmasUnicas(alarmasUnicas);
            }
        });

        // Para poder hacer swipe a la lista de alarmas. Primer valor = 0 para que no haya Drag and Drop. Segundo valor
        // direccion de los swipes.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (rcvAdapter.getAlarmaAt(viewHolder.getAdapterPosition()) instanceof Alarma) {
                    myViewModel.delete((Alarma) rcvAdapter.getAlarmaAt(viewHolder.getAdapterPosition()));

                    Alarma a = (Alarma) rcvAdapter.getAlarmaAt(viewHolder.getAdapterPosition());
                    cancelAlarma(a.getId(), view.getContext());
                } else {
                    myViewModel.deleteUnica((AlarmaUnica) rcvAdapter.getAlarmaAt(viewHolder.getAdapterPosition()));

                    AlarmaUnica a = (AlarmaUnica) rcvAdapter.getAlarmaAt(viewHolder.getAdapterPosition());
                    cancelAlarma(a.getId(), view.getContext());
                }
                Snackbar.make(view, (R.string.alarma_borrada), Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        // Edita la alarma seleccionada
        rcvAdapter.setOnItemClickListener(new RcvAdapterAlarmas.OnItemClickListener() {
            @Override
            public void onItemClick(Object alarmaO, Boolean editar) {
                Alarma alarma = (Alarma) alarmaO;
                if (!editar) { // Si se clicka en el switch
                    if (alarma.getActivated()) {
                        alarma.setActivated(false);
                    } else {
                        alarma.setActivated(true);
                    }
                    myViewModel.update(alarma);
                } else { // Si se clicka fuera del switch
                    // Vuelve a la activy de crear alarmas con los datos de la alarma clickada

                    Intent intent = new Intent(getContext(), CrearEditarAlarma.class);
                    intent.putExtra(CrearEditarAlarma.EXTRA_ID, alarma.getId());
                    intent.putExtra(CrearEditarAlarma.EXTRA_TIPO_ALARMA, "semanal");
                    intent.putExtra(CrearEditarAlarma.EXTRA_HORA, alarma.getHora());
                    intent.putExtra(CrearEditarAlarma.EXTRA_MIN, alarma.getMinuto());
                    intent.putExtra(CrearEditarAlarma.EXTRA_LUN, alarma.getLunes());
                    intent.putExtra(CrearEditarAlarma.EXTRA_MAR, alarma.getMartes());
                    intent.putExtra(CrearEditarAlarma.EXTRA_MIE, alarma.getMiercoles());
                    intent.putExtra(CrearEditarAlarma.EXTRA_JUE, alarma.getJueves());
                    intent.putExtra(CrearEditarAlarma.EXTRA_VIE, alarma.getViernes());
                    intent.putExtra(CrearEditarAlarma.EXTRA_SAB, alarma.getSabado());
                    intent.putExtra(CrearEditarAlarma.EXTRA_DOM, alarma.getDomingo());
                    intent.putExtra(CrearEditarAlarma.EXTRA_ACT, alarma.getActivated());
                    intent.putExtra(CrearEditarAlarma.EXTRA_SONIDO, alarma.getNombreSonido());
                    intent.putExtra(CrearEditarAlarma.EXTRA_URI, alarma.getSonidoUri());
                    intent.putExtra(CrearEditarAlarma.EXTRA_SONAR, alarma.getSonar());
                    startActivityForResult(intent, EDIT_ALARMAS_REQUEST);
                }
            }
        });

        rcvAdapter.setOnItemClickListenerUnica(new RcvAdapterAlarmas.OnItemClickListenerUnica() {
            @Override
            public void onItemClickUnica(Object alarmaO, Boolean editar) {
                AlarmaUnica alarma = (AlarmaUnica) alarmaO;
                if (!editar) { // Si se clicka en el switch
                    if (alarma.getActivated()) {
                        alarma.setActivated(false);
                    } else {
                        alarma.setActivated(true);
                    }
                    myViewModel.updateUnica(alarma);
                } else { // Si se clicka fuera del switch

                    // Vuelve a la activy de crear alarmas con los datos de la alarma clickada

                    Intent intent = new Intent(getContext(), CrearEditarAlarma.class);
                    intent.putExtra(CrearEditarAlarma.EXTRA_ID, alarma.getId());
                    intent.putExtra(CrearEditarAlarma.EXTRA_TIPO_ALARMA, "unica");
                    intent.putExtra(CrearEditarAlarma.EXTRA_HORA, alarma.getHora());
                    intent.putExtra(CrearEditarAlarma.EXTRA_MIN, alarma.getMinuto());
                    intent.putExtra(CrearEditarAlarma.EXTRA_ANO, alarma.getAno());
                    intent.putExtra(CrearEditarAlarma.EXTRA_MES, alarma.getMes());
                    intent.putExtra(CrearEditarAlarma.EXTRA_DIA, alarma.getDia());
                    intent.putExtra(CrearEditarAlarma.EXTRA_ACT, alarma.getActivated());
                    intent.putExtra(CrearEditarAlarma.EXTRA_SONIDO, alarma.getNombreSonido());
                    intent.putExtra(CrearEditarAlarma.EXTRA_URI, alarma.getSonidoUri());
                    intent.putExtra(CrearEditarAlarma.EXTRA_SONAR, alarma.getSonar());
                    startActivityForResult(intent, EDIT_ALARMAS_REQUEST);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // En caso de que se retorne de crear la alarma
        if (requestCode == ADD_ALARMAS_REQUEST && resultCode == RESULT_OK) {
            hora = data.getIntExtra(CrearEditarAlarma.EXTRA_HORA, 0);
            min = data.getIntExtra(CrearEditarAlarma.EXTRA_MIN, 0);
            l = data.getBooleanExtra(CrearEditarAlarma.EXTRA_LUN, false);
            m = data.getBooleanExtra(CrearEditarAlarma.EXTRA_MAR, false);
            x = data.getBooleanExtra(CrearEditarAlarma.EXTRA_MIE, false);
            j = data.getBooleanExtra(CrearEditarAlarma.EXTRA_JUE, false);
            v = data.getBooleanExtra(CrearEditarAlarma.EXTRA_VIE, false);
            s = data.getBooleanExtra(CrearEditarAlarma.EXTRA_SAB, false);
            d = data.getBooleanExtra(CrearEditarAlarma.EXTRA_DOM, false);
            ano = data.getIntExtra(CrearEditarAlarma.EXTRA_ANO, -1);
            mes = data.getIntExtra(CrearEditarAlarma.EXTRA_MES, -1);
            dia = data.getIntExtra(CrearEditarAlarma.EXTRA_DIA, -1);
            activar = data.getBooleanExtra(CrearEditarAlarma.EXTRA_ACT, true);
            nombreSonido = data.getStringExtra(CrearEditarAlarma.EXTRA_SONIDO);
            sonidoUri = Uri.parse(data.getStringExtra(CrearEditarAlarma.EXTRA_URI));
            sonar = data.getBooleanExtra(CrearEditarAlarma.EXTRA_SONAR, false);

            //Formateo el hora obtenido: antepone el 0 si son menores de 10
            String horaFormateada = (hora < 10) ? (CERO + hora) : String.valueOf(hora);
            //Formateo el minuto obtenido: antepone el 0 si son menores de 10
            String minutoFormateado = (min < 10) ? (CERO + min) : String.valueOf(min);
            //Formateo el mes obtenido: antepone el 0 si son menores de 10
            String mesFormateado = (mes < 10) ? (CERO + mes) : String.valueOf(mes);
            //Formateo el día obtenido: antepone el 0 si son menores de 10
            String diaFormateado = (dia < 10) ? (CERO + dia) : String.valueOf(dia);

            if ((ano == -1) && (mes == -1) && (dia == -1)) {
                Alarma alarma = new Alarma(horaFormateada, minutoFormateado, l, m, x, j, v, s, d, activar, nombreSonido, sonidoUri.toString(), sonar);
                myViewModel.insert(alarma);
            }

            if ((ano != -1) && (mes != -1) && (dia != -1)) {
                AlarmaUnica alarmaUnica = new AlarmaUnica(horaFormateada, minutoFormateado, String.valueOf(ano), mesFormateado, diaFormateado, activar, nombreSonido, sonidoUri.toString(), sonar);
                myViewModel.insertUnica(alarmaUnica);
            }

            Snackbar.make(view, (R.string.alarma_creada), Snackbar.LENGTH_LONG).show();
            rcvAdapter.notifyDataSetChanged();

            // En caso de que se retorne de modificar la alarma
        } else if (requestCode == EDIT_ALARMAS_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(CrearEditarAlarma.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getContext(), (R.string.no_se_pudo_actualizar), Toast.LENGTH_SHORT).show();
            }

            hora = data.getIntExtra(CrearEditarAlarma.EXTRA_HORA, 0);
            min = data.getIntExtra(CrearEditarAlarma.EXTRA_MIN, 0);
            l = data.getBooleanExtra(CrearEditarAlarma.EXTRA_LUN, false);
            m = data.getBooleanExtra(CrearEditarAlarma.EXTRA_MAR, false);
            x = data.getBooleanExtra(CrearEditarAlarma.EXTRA_MIE, false);
            j = data.getBooleanExtra(CrearEditarAlarma.EXTRA_JUE, false);
            v = data.getBooleanExtra(CrearEditarAlarma.EXTRA_VIE, false);
            s = data.getBooleanExtra(CrearEditarAlarma.EXTRA_SAB, false);
            d = data.getBooleanExtra(CrearEditarAlarma.EXTRA_DOM, false);
            ano = data.getIntExtra(CrearEditarAlarma.EXTRA_ANO, -1);
            mes = data.getIntExtra(CrearEditarAlarma.EXTRA_MES, -1);
            dia = data.getIntExtra(CrearEditarAlarma.EXTRA_DIA, -1);
            activar = data.getBooleanExtra(CrearEditarAlarma.EXTRA_ACT, true);
            nombreSonido = data.getStringExtra(CrearEditarAlarma.EXTRA_SONIDO);
            sonidoUri = Uri.parse(data.getStringExtra(CrearEditarAlarma.EXTRA_URI));
            sonar = data.getBooleanExtra(CrearEditarAlarma.EXTRA_SONAR, false);

            //Formateo el hora obtenido: antepone el 0 si son menores de 10
            String horaFormateada = (hora < 10) ? (CERO + hora) : String.valueOf(hora);
            //Formateo el minuto obtenido: antepone el 0 si son menores de 10
            String minutoFormateado = (min < 10) ? (CERO + min) : String.valueOf(min);
            //Formateo el mes obtenido: antepone el 0 si son menores de 10
            String mesFormateado = (mes < 10) ? (CERO + mes) : String.valueOf(mes);
            //Formateo el día obtenido: antepone el 0 si son menores de 10
            String diaFormateado = (dia < 10) ? (CERO + dia) : String.valueOf(dia);

            if ((ano == -1) && (mes == -1) && (dia == -1)) {
                Alarma alarma = new Alarma(horaFormateada, minutoFormateado, l, m, x, j, v, s, d, activar, nombreSonido, sonidoUri.toString(), sonar);
                alarma.setId(id); // Necesario para que ROOM pueda identificar la entrada para actualizar la alarma
                myViewModel.update(alarma);
            }

            if ((ano != -1) && (mes != -1) && (dia != -1)) {
                AlarmaUnica alarmaUnica = new AlarmaUnica(horaFormateada, minutoFormateado, String.valueOf(ano), mesFormateado, diaFormateado, activar, nombreSonido, sonidoUri.toString(), sonar);
                alarmaUnica.setId(id);
                myViewModel.updateUnica(alarmaUnica);
            }

            Snackbar.make(view, (R.string.alarma_actualizada), Snackbar.LENGTH_LONG).show();

        } else {
            Snackbar.make(view, (R.string.alarma_no_creada), Snackbar.LENGTH_LONG).show();
        }

        rcvAdapter.notifyDataSetChanged();
    }

    private void cancelAlarma(int code, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        alarmManager.cancel(pendingIntent);

        AlarmManager alarmManager2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, code, intent2, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        alarmManager2.cancel(pendingIntent2);
    }
}
package arl.chronos.fragments;

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
import arl.chronos.database.MyViewModel;

import static android.app.Activity.RESULT_OK;

public class TabFragmentAlarmas extends Fragment {

    FloatingActionButton fab;

    private RecyclerView recyclerView;
    private RcvAdapterAlarmas rcvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Alarma> listAlarmas;
    private MyViewModel myViewModel;
    private View view;
    private int hora;
    private int min;
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

        //listAlarmas = new ArrayList<>();

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
            public void onChanged(List<Alarma> alarmas) {
                rcvAdapter.setAlarmas(alarmas);
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
                myViewModel.delete(rcvAdapter.getAlarmaAt(viewHolder.getAdapterPosition()));
                Snackbar.make(view, "Alarma borrada", Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        // Edita la alarma seleccionada
        rcvAdapter.setOnItemClickListener(new RcvAdapterAlarmas.OnItemClickListener() {
            @Override
            public void onItemClick(Alarma alarma, Boolean editar) {
                if (!editar) { // Si se clicka en el switch
                    if (alarma.getActivated()) {
                        alarma.setActivated(false);
                    } else {
                        alarma.setActivated(true);
                    }
                    myViewModel.update(alarma);
                } else { // Si se clicka fuera del switch
                    // Vuelve a la activy de crear alarmas con los datos de la alarma clickada

                    Log.d("TABFRAGMENT_EDITALARMA", alarma.getHora() + ":" + alarma.getMinuto() + " id: " + alarma.getId() +
                            " Nombre Sonido: " + alarma.getNombreSonido() + " Uri: " + alarma.getSonidoUri() + " Sonar: " + alarma.getSonar());

                    Intent intent = new Intent(getContext(), CrearEditarAlarma.class);
                    intent.putExtra(CrearEditarAlarma.EXTRA_ID, alarma.getId());
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
            activar = data.getBooleanExtra(CrearEditarAlarma.EXTRA_ACT, true);
            nombreSonido = data.getStringExtra(CrearEditarAlarma.EXTRA_SONIDO);
            sonidoUri = Uri.parse(data.getStringExtra(CrearEditarAlarma.EXTRA_URI));
            sonar = data.getBooleanExtra(CrearEditarAlarma.EXTRA_SONAR, false);

            //Log.d("//////////TABFRAGMENT_RESULT//////", hora + ":" + min);

            //Formateo el hora obtenido: antepone el 0 si son menores de 10
            String horaFormateada = (hora < 10) ? (CERO + hora) : String.valueOf(hora);
            //Formateo el minuto obtenido: antepone el 0 si son menores de 10
            String minutoFormateado = (min < 10) ? (CERO + min) : String.valueOf(min);

            //Log.d("//////////TABFRAGMENT_RESULT//////", horaFormateada + ":" + minutoFormateado);

            Alarma alarma = new Alarma(horaFormateada, minutoFormateado, l, m, x, j, v, s, d, activar, nombreSonido, sonidoUri.toString(), sonar);
            myViewModel.insert(alarma);

            Snackbar.make(view, "Alarma creada", Snackbar.LENGTH_LONG).show();

        // En caso de que se retorne de modificar la alarma
        } else if (requestCode == EDIT_ALARMAS_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(CrearEditarAlarma.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getContext(), "No se pudo actualizar la alarma", Toast.LENGTH_SHORT).show();
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
            activar = data.getBooleanExtra(CrearEditarAlarma.EXTRA_ACT, true);
            nombreSonido = data.getStringExtra(CrearEditarAlarma.EXTRA_SONIDO);
            sonidoUri = Uri.parse(data.getStringExtra(CrearEditarAlarma.EXTRA_URI));
            sonar = data.getBooleanExtra(CrearEditarAlarma.EXTRA_SONAR, false);

            //Formateo el hora obtenido: antepone el 0 si son menores de 10
            String horaFormateada = (hora < 10) ? (CERO + hora) : String.valueOf(hora);
            //Formateo el minuto obtenido: antepone el 0 si son menores de 10
            String minutoFormateado = (min < 10) ? (CERO + min) : String.valueOf(min);

            Alarma alarma = new Alarma(horaFormateada, minutoFormateado, l, m, x, j, v, s, d, activar, nombreSonido, sonidoUri.toString(), sonar);
            alarma.setId(id); // Necesario para que ROOM pueda identificar la entrada para actualizar la alarma
            myViewModel.update(alarma);

            Snackbar.make(view, "Alarma actualizada", Snackbar.LENGTH_LONG).show();

        } else {
            Snackbar.make(view, "Alarma no creada", Snackbar.LENGTH_LONG).show();
        }
    }

    public MyViewModel getMyViewModel() {
        return myViewModel;
    }

    public String getMensaje() {
        return horaFormateada + DOS_PUNTOS + minutoFormateado;
    }
}
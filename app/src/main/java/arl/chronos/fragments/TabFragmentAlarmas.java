package arl.chronos.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import arl.chronos.AlarmasActivity;
import arl.chronos.MainActivity;
import arl.chronos.R;
import arl.chronos.adapters.RcvAdapterAlarmas;
import arl.chronos.classes.Alarmas;

public class TabFragmentAlarmas extends Fragment {

    FloatingActionButton fab;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rcvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Alarmas> listAlarmas;

    public TabFragmentAlarmas() {
        // Constructor por defecto
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_alarmas, container, false);

        fab = view.findViewById(R.id.fab_alarmas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getContext(), AlarmasActivity.class);
                startActivity(intent);
            }
        });

        listAlarmas = new ArrayList<>();
        llenarLista();

        recyclerView = view.findViewById(R.id.rcv_alarmas);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext()); // view.getContext()?????
        recyclerView.setLayoutManager(layoutManager);

        rcvAdapter = new RcvAdapterAlarmas(listAlarmas, getContext()); // view.getContext()?????
        recyclerView.setAdapter(rcvAdapter);

        return view;
    }

    private void llenarLista(){
        // Llenar desde Querys de SQLite TODO Cambiar
        listAlarmas.add(new Alarmas("01:33", true,true,true,true,true,true,true, true));
        listAlarmas.add(new Alarmas("11:15", true,false,true,false,true,false,true, false));
    }
}
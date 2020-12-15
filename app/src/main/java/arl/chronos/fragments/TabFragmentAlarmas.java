package arl.chronos.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import arl.chronos.CrearAlarma;
import arl.chronos.MainActivity;
import arl.chronos.R;
import arl.chronos.adapters.RcvAdapterAlarmas;
import arl.chronos.classes.Alarmas;
import arl.chronos.database.Alarma;
import arl.chronos.database.MyViewModel;

public class TabFragmentAlarmas extends Fragment {

    FloatingActionButton fab;

    private RecyclerView recyclerView;
    private RcvAdapterAlarmas rcvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Alarmas> listAlarmas;
    private MyViewModel myViewModel;

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
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent = new Intent(view.getContext(), CrearAlarma.class);
                startActivity(intent);
            }
        });

        listAlarmas = new ArrayList<>();

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

        return view;
    }
}
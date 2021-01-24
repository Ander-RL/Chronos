package arl.chronos.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import arl.chronos.R;

public class TabFragmentCrono extends Fragment {

    public TabFragmentCrono() {
        // Constructor por defecto
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el Layout para este Fragment
        return inflater.inflate(R.layout.fragment_tab_crono, container, false);
    }
}
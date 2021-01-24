package arl.chronos.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import arl.chronos.fragments.TabFragmentAlarmas;
import arl.chronos.fragments.TabFragmentCalendario;
import arl.chronos.fragments.TabFragmentCrono;

/*
 * La clase PagerAdapter, extiende de FragmentPagerAdapter.
 * Su funcion es acoger las paginas/fragments que iran dentro
 * del ViewPager(layout que permite mover de izq a derch).
 */

public class PagerAdapter extends FragmentPagerAdapter {
    int numTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int numTabs) {
        super(fm, numTabs);
        this.numTabs = numTabs;
    }

    /*
     * Devuelve el Fragment asociado con la posicion especificada.
     */

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new TabFragmentAlarmas();
            case 1: return new TabFragmentCrono();
            case 2: return new TabFragmentCalendario();
            default: return null;
        }
    }

    /*
     * Devuelve el numero de views disponibles.
     */

    @Override
    public int getCount() {
        return numTabs;
    }
}

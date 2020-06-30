package com.brahimali.administradordetareas.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Adaptador encargado de gestionar las pestañas de fragmentos en la actividad principal.
 */
public class TabAdapter extends FragmentPagerAdapter {

    public static int numOfTabs;
    public static String[] validTabs;

    /**
     * Crea un adaptador preparado para gestionar un número fijo y limitado de pestañas que
     * hereda la clase {@link FragmentPagerAdapter}.
     * @param fm El {@link FragmentManager} usado por la actividad ó fragmento que desée hacer uso
     *           del nuevo adaptador.
     * @param validTabs Un Array de Strings contieniendo los nombres válidos de todas las pestañas.
     */
    public TabAdapter(@NonNull FragmentManager fm, String[] validTabs) {

        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.validTabs = validTabs;
        numOfTabs = validTabs.length;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // TODO: Crear fragmentos luego de haber determinado su diseño.
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return validTabs[position];
    }

}

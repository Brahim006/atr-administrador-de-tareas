package com.brahimali.administradordetareas.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.brahimali.administradordetareas.MainActivity;
import com.brahimali.administradordetareas.utils.TabNamer;

/**
 * Adaptador encargado de gestionar las pestañas de fragmentos en la actividad principal.
 * La cantidad y título de pestañas está determinada por el arrleglo validTabs creado en
 * {@link MainActivity}.
 */
public class TabAdapter extends FragmentPagerAdapter {

    // Keys para la transferencia de datos a los fragmentos
    public static final String TAB_NUM_REFERENCE = "tab_num";

    private Context parentActivityContext;

    /**
     * Crea un adaptador preparado para gestionar un número fijo y limitado de pestañas que
     * hereda la clase {@link FragmentPagerAdapter}.
     * @param fm El {@link FragmentManager} usado por la actividad ó fragmento que desée hacer uso
     *           del nuevo adaptador.
     * @param parentActivityContext El contexto de la actividad ó fragmento que desée hacer uso
     *                              del nuevo adaptador.
     */
    public TabAdapter(@NonNull FragmentManager fm, Context parentActivityContext) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.parentActivityContext = parentActivityContext;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        TabFragment fragment = new TabFragment();

        Bundle args = new Bundle();
        args.putInt(TAB_NUM_REFERENCE, position); // Se envía el código de la pestaña al fragmento

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return TabNamer.AVAILABLE_NUM_OF_TABS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return TabNamer.getValidTabName(parentActivityContext, position);

    }

}

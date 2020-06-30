package com.brahimali.administradordetareas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.brahimali.administradordetareas.fragments.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    // Objetos para la gestión de pestañas
    TabAdapter tabAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    // Nombre de las pestañas a mostrar en esta actividad
    public static String[] validTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializacíon de modelo de pestañas
        initValidTabs();
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabAdapter= new TabAdapter(getSupportFragmentManager(),validTabs);

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

    } // fin onCreate

    /*
     * Sección con métodos privados para facilitar la lectura de código
     */

    /**
     * Inicializa el arreglo que contiene el nombre de las pestañas a crear.
     */
    private void initValidTabs(){

        validTabs = new String[]{
                getResources().getString(R.string.tab_all),
                getResources().getString(R.string.state_0),
                getResources().getString(R.string.state_1),
                getResources().getString(R.string.state_2)
        };

    }

}

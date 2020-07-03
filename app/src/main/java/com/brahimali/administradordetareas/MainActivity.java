package com.brahimali.administradordetareas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.brahimali.administradordetareas.data.Task;
import com.brahimali.administradordetareas.data.database.DBAdapter;
import com.brahimali.administradordetareas.fragments.TabAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

// TODO: Investigar la necesidad de heredar de la clase FragmentActivity
public class MainActivity extends AppCompatActivity {

    // Objetos para la gestión de pestañas
    private TabAdapter tabAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    // Nombre de las pestañas a mostrar en esta actividad
    public static String[] validTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa la base de datos en el contexto de esta actividad, la instancia perdura
        // iniciada gracas al patrón singletton
        DBAdapter.getInstance(this);

        // Inicializacíon de modelo de pestañas
        initValidTabs();
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabAdapter= new TabAdapter(getSupportFragmentManager());

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
                getResources().getString(R.string.state_1),
                getResources().getString(R.string.state_2),
                getResources().getString(R.string.state_3)
        };

    }

}

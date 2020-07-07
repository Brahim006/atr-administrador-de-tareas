package com.brahimali.administradordetareas;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.brahimali.administradordetareas.data.Task;
import com.brahimali.administradordetareas.data.database.DBAdapter;
import com.brahimali.administradordetareas.fragments.TabAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;    // Gestor de paginado de fragmentos
    private TabAdapter tabAdapter;  // Adaptador para el viewPager
    private TabLayout tabLayout;    // Layout para las pestañas

    private FloatingActionButton fab;

    public static final int DEFAULT_STATUS_CODE = 1;

    // Códigos para las peticiones a otras actividades
    public static final int CREATE_TASK_REQUEST_CODE = 1;
    public static final int EDIT_TASK_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGui(); // Inicializa los componentes

    }

    /* Inicializa las vistas de la actividad */
    private void initGui() {

        // Inicializa la base de datos en este contexto, la instancia perdura iniciada gracias al
        // patrón singletton
        DBAdapter.getInstance(this);

        fab = (FloatingActionButton)findViewById(R.id.addTaskFAB);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabAdapter= new TabAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void onClickAddTaskFAB(View view){ // Comportamiento del floating action button.

        Intent intent = new Intent(this, ManipulateTaskActivity.class);

        startActivityForResult(intent, CREATE_TASK_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            switch (requestCode){

                case CREATE_TASK_REQUEST_CODE:
                    // Crea una tarea con los datos enviados por la actividad de creación de tareas
                    // y la añade a la base de datos
                    String newTitle = data.getStringExtra(ManipulateTaskActivity.TASK_TITLE_IDENTIFIER);
                    String newDescription = data.getStringExtra
                                            (ManipulateTaskActivity.TASK_DESCRIPTION_IDENTIFIER);
                    int newStatusCode = data.getIntExtra(ManipulateTaskActivity.TASK_STATE_IDENTIFIER,
                                                         DEFAULT_STATUS_CODE);

                    Task newTask = new Task(newTitle, newDescription, newStatusCode);

                    DBAdapter.getInstance(this).insertTask(newTask);

                    // TODO: Averiguar cómo notificar de estos cambios a los fragmentos
                    // a través de un ViewModel.. ó de un FragmentManager...

                    String successMessage = getResources().getString(R.string.adding_task_success);
                    Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                break;

            }

        } // fin if

    }

}

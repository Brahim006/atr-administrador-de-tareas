package com.brahimali.administradordetareas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.brahimali.administradordetareas.data.Task;
import com.brahimali.administradordetareas.data.database.DBAdapter;
import com.brahimali.administradordetareas.fragments.TabAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

// TODO: Investigar la necesidad de heredar de la clase FragmentActivity
public class MainActivity extends AppCompatActivity {

    // Objetos para la gestión de pestañas
    private TabAdapter tabAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private FloatingActionButton fab;

    public static final int DEFAULT_STATUS_CODE = 1;

    // Nombre de las pestañas a mostrar en esta actividad
    public static String[] validTabs;

    // Request codes para iniciar actividades que devuelven resultados
    public static final int CREATE_TASK_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton)findViewById(R.id.addTaskFAB);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            switch (requestCode){

                case CREATE_TASK_REQUEST_CODE:
                    // Crea una tarea con los datos enviados por la actividad de creación de tareas
                    // y la añade a la base de datos
                    String newTitle = data.getStringExtra(AddTaskActivity.NEW_TASK_TITLE);
                    String newDescription = data.getStringExtra
                                            (AddTaskActivity.NEW_TASK_DESCRIPTION);
                    int newStatusCode = data.getIntExtra(AddTaskActivity.NEW_TASK_STATE,
                                                         DEFAULT_STATUS_CODE);

                    Task newTask = new Task(newTitle, newDescription, newStatusCode);

                    DBAdapter.getInstance(this).insertTask(newTask);

                    String successMessage = getResources().getString(R.string.adding_task_success);
                    Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();

                    break;

            }

        } // fin if

    }

    /*
     * Comportamiento del floating action button.
     */
    public void onClickAddTaskFAB(View view){

        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivityForResult(intent, CREATE_TASK_REQUEST_CODE);

    }

     // Sección con métodos privados para facilitar la lectura del código

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

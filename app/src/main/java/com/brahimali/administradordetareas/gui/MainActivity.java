package com.brahimali.administradordetareas.gui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.brahimali.administradordetareas.R;
import com.brahimali.administradordetareas.database.entity.Task;
import com.brahimali.administradordetareas.fragments.TabAdapter;
import com.brahimali.administradordetareas.viewmodel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;            // Gestor de paginado de fragmentos
    private TabAdapter tabAdapter;          // Adaptador para el viewPager
    private TabLayout tabLayout;            // Layout para las pestañas
    private TaskViewModel taskViewModel;    // ViewModel para observar datos y acceso a db


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

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

    }

    private void initGui() {

        fab = (FloatingActionButton)findViewById(R.id.addTaskFAB);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabAdapter= new TabAdapter(getSupportFragmentManager(), getApplicationContext());

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void onClickAddTaskFAB(View view){ // Comportamiento del floating action button.

        Intent intent = new Intent(this, ManipulateTaskActivity.class);
        intent.putExtra(ManipulateTaskActivity.REQUEST_CODE_IDENTIFIER, CREATE_TASK_REQUEST_CODE);

        startActivityForResult(intent, CREATE_TASK_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == CREATE_TASK_REQUEST_CODE){

            // Crea una tarea según los resultados devueltos por la actividad de creación de tareas
            String newTitle =
                    data.getStringExtra(ManipulateTaskActivity.TASK_TITLE_IDENTIFIER);
            String newDescription =
                    data.getStringExtra(ManipulateTaskActivity.TASK_DESCRIPTION_IDENTIFIER);
            int newStatusCode =
                    data.getIntExtra(ManipulateTaskActivity.TASK_STATE_IDENTIFIER,
                                     DEFAULT_STATUS_CODE);

            Task newTask = new Task(newTitle, newDescription, newStatusCode);

            taskViewModel.insert(newTask);

            Toast.makeText(this, getResources().getString(R.string.adding_task_success),
                    Toast.LENGTH_SHORT).show();

        } else if(resultCode == ManipulateTaskActivity.RESULT_NULL_TITLE){
            Toast.makeText(this, getResources().getString(R.string.null_title_warning),
                    Toast.LENGTH_SHORT).show();
        }

    }

}

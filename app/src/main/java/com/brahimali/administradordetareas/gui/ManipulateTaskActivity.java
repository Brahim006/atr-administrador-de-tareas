package com.brahimali.administradordetareas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.brahimali.administradordetareas.R;
import com.brahimali.administradordetareas.database.TaskRoomDatabase;
import com.brahimali.administradordetareas.fragments.TaskListAdapter;
import com.brahimali.administradordetareas.utils.TabNamer;

/**
 * Actividad que se encarga de añadir nuevas tareas ó editar las ya existentes, está pensada para
 * ser invocada esperando resultados a través de intents.
 */
public class ManipulateTaskActivity extends AppCompatActivity {

    private EditText taskTitle;
    private EditText taskDescription;
    private Button selectStateButton;
    private Button addTaskButton;

    // Almacena el código de estado de la nueva tarea, puede ser manipulado por el popUp menu
    private int taskState;
    // Las tareas que se editan vienen con su posición en el dataset
    private int taskPosition;

    public static final int DEFAULT_STATE = 1;

    // Identificadores de las variables retornadas a las actividades de llamada
    public static final String TASK_TITLE_IDENTIFIER =
            "com.brahimali.administradordetareas.TASK_TITLE";
    public static final String TASK_DESCRIPTION_IDENTIFIER =
            "com.brahimali.administradordetareas.TASK_DESCRIPTION";
    public static final String TASK_STATE_IDENTIFIER =
            "com.brahimali.administradordetareas.TASK_STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initGui();

        taskState = getIntent().getIntExtra(TASK_STATE_IDENTIFIER, DEFAULT_STATE);
        // El valor -1 significa que es una tarea nueva, ya que no tiene posición todavía
        taskPosition = getIntent().getIntExtra(TaskListAdapter.EDITING_TASK_POSITION, -1);

        // Si no se ha enviado información de ninguna tarea, significa que se está creando una
        if(taskPosition != -1){
            taskTitle.setText(getIntent().getStringExtra(TASK_TITLE_IDENTIFIER));
            taskDescription.setText(getIntent().getStringExtra(TASK_DESCRIPTION_IDENTIFIER));
            selectStateButton.setText(TabNamer.getValidTabName(getApplicationContext(), taskState));
        }

    }

    private void initGui(){
        taskTitle = findViewById(R.id.newTaskTitle);
        taskDescription = findViewById(R.id.newTaskDescription);
        selectStateButton = findViewById(R.id.selectStateButton);
        addTaskButton = findViewById(R.id.addTasKButton);
    }

    // Lógica de los botones

    public void onClickStateButton(View view){

        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.task_state_selector, popupMenu.getMenu());

        // Lógica del PopUp Menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int statusCode = DEFAULT_STATE;

                switch (item.getItemId()){
                    case R.id.item_state_2:
                        statusCode = 2;
                        break;
                    case R.id.item_state_3:
                        statusCode = 3;
                        break;
                }

                taskState = statusCode;
                selectStateButton.setText(TabNamer.getValidTabName(getApplicationContext(),
                                                                   statusCode));
                return true;

            }

        }); // fin listener

        popupMenu.show();

    } // fin onClickStateButton

    public void onClickAddTaskButton(View view){

        // TODO: Tratar los casos en los que se inserta un título vacío

        String title = taskTitle.getText().toString();
        String description = taskDescription.getText().toString();

        Intent returnedArgs = new Intent();

        returnedArgs.putExtra(TASK_TITLE_IDENTIFIER, title);
        returnedArgs.putExtra(TASK_DESCRIPTION_IDENTIFIER, description);
        returnedArgs.putExtra(TASK_STATE_IDENTIFIER, taskState);

        // TODO: Analizar la posibilidad de mandar un extra con la tarea (parselable, serializable)

        if(taskPosition != -1){
            returnedArgs.putExtra(TaskListAdapter.EDITING_TASK_POSITION, taskPosition);
            // Se borra la tarea modificada antes de que se cargue la tarea con nuevos datos
            String oldTitle = getIntent().getStringExtra(TASK_TITLE_IDENTIFIER);

            TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().delete(oldTitle);
        }

        setResult(RESULT_OK, returnedArgs);
        finish();

    }

}

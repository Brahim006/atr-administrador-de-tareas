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

    private int taskState;
    private int requestCode;

    public static final int DEFAULT_STATE = 1;
    public static final int RESULT_NULL_TITLE = 2;  // resultCode para títulos vacíos

    // Identificadores de las variables retornadas a las actividades de llamada
    public static final String TASK_TITLE_IDENTIFIER =
            "com.brahimali.administradordetareas.TASK_TITLE";
    public static final String TASK_DESCRIPTION_IDENTIFIER =
            "com.brahimali.administradordetareas.TASK_DESCRIPTION";
    public static final String TASK_STATE_IDENTIFIER =
            "com.brahimali.administradordetareas.TASK_STATUS";
    public static final String OLD_TITLE_IDENTIFIER =
            "com.brahimali.administradordetareas.OLD_TITLE";
    public static final String REQUEST_CODE_IDENTIFIER =
            "com.brahimali.administradordetareas.REQUEST_CODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initGui();

        taskState = getIntent().getIntExtra(TASK_STATE_IDENTIFIER, DEFAULT_STATE);
        requestCode = getIntent().getIntExtra(ManipulateTaskActivity.REQUEST_CODE_IDENTIFIER, 0);

        // Si se ha pedido editar la tarea, inicializa la gui con sus datos
        if(requestCode == MainActivity.EDIT_TASK_REQUEST_CODE){
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

        String title = taskTitle.getText().toString();

        if(!title.equals("")){
            String description = taskDescription.getText().toString();

            Intent returnedArgs = new Intent();

            returnedArgs.putExtra(TASK_TITLE_IDENTIFIER, title);
            returnedArgs.putExtra(TASK_DESCRIPTION_IDENTIFIER, description);
            returnedArgs.putExtra(TASK_STATE_IDENTIFIER, taskState);

            String oldTitle = getIntent().getStringExtra(TASK_TITLE_IDENTIFIER);

            if(requestCode == MainActivity.EDIT_TASK_REQUEST_CODE && !title.equals(oldTitle)){
                // Retorna el viejo título en caso de que éste haya sido editado
                returnedArgs.putExtra(OLD_TITLE_IDENTIFIER, oldTitle);
            }

            setResult(RESULT_OK, returnedArgs);
            finish();

        } else {
            // Retorna el resultCode avisando que se ingresó un título vacío
            setResult(RESULT_NULL_TITLE);
            finish();
        }
    }

}

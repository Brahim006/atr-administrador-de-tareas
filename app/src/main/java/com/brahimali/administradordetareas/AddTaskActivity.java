package com.brahimali.administradordetareas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

/**
 * Actividad que se encarga de añadir nuevas tareas a la base de datos y a las listas.
 */
public class AddTaskActivity extends AppCompatActivity {

    private EditText newTaskTitle;
    private EditText newTaskDescription;
    private Button selectStateButton;
    private Button addTaskButton;

    // Almacena el código de estado de la nueva tarea, puede ser manipulado por el popUp menu
    private int newState;

    public static final int DEFAULT_STATE = 1;

    // Identificadores de las variables retornadas a la actividad principal
    public static final String NEW_TASK_TITLE = "new task title";
    public static final String NEW_TASK_DESCRIPTION = "new task description";
    public static final String NEW_TASK_STATE = "new task state code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        newTaskTitle = (EditText)findViewById(R.id.newTaskTitle);
        newTaskDescription = (EditText)findViewById(R.id.newTaskDescription);
        selectStateButton = (Button)findViewById(R.id.selectStateButton);
        addTaskButton = (Button)findViewById(R.id.addTasKButton);

        newState = DEFAULT_STATE;

    }

    // Lógica de los botones

    public void onClickStateButton(View view){

        PopupMenu popupMenu = new PopupMenu(this, view);

        // Lógica del PopUp Menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){ // Cambia el código de estado y el texto del botón
                    case R.id.item_state_1:
                        newState = 1;
                        selectStateButton.setText(
                                getResources().getString(R.string.state_1)
                        );
                        break;
                    case R.id.item_state_2:
                        newState = 2;
                        selectStateButton.setText(
                                getResources().getString(R.string.state_2)
                        );
                        break;
                    case R.id.item_state_3:
                        newState = 3;
                        selectStateButton.setText(
                                getResources().getString(R.string.state_3)
                        );
                        break;
                }

                return true;

            }

        });

        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.new_task_state_selector, popupMenu.getMenu());

        popupMenu.show();

    } // fin onClickStateButton

    public void onClickAddTaskButton(View view){

        String title = newTaskTitle.getText().toString();
        String description = newTaskDescription.getText().toString();

        Intent returnedArgs = new Intent();

        returnedArgs.putExtra(NEW_TASK_TITLE, title);
        returnedArgs.putExtra(NEW_TASK_DESCRIPTION, description);
        returnedArgs.putExtra(NEW_TASK_STATE, newState);

        // Retorna los resultados a la actividad principal
        setResult(RESULT_OK, returnedArgs);
        finish();

    }

}

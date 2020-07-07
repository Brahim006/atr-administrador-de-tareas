package com.brahimali.administradordetareas.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brahimali.administradordetareas.MainActivity;
import com.brahimali.administradordetareas.ManipulateTaskActivity;
import com.brahimali.administradordetareas.R;
import com.brahimali.administradordetareas.data.Task;
import com.brahimali.administradordetareas.data.database.DBAdapter;
import com.brahimali.administradordetareas.fragments.recyclerViewClasses.TaskListAdapter;

/**
 * Fragmento contenedor del modelo para cada una de las pestañas.
 */
public class TabFragment extends Fragment {

    private RecyclerView recyclerView;      // Distribución tipo lista reciclable
    private TaskListAdapter taskListAdapter;// Adaptador para la creacción de elementos de la lista

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab, container, false);

        int tabCode = getArguments().getInt(TabAdapter.TAB_NUM_REFERENCE);

        recyclerView = (RecyclerView)v.findViewById(R.id.taskList);
        // Indica qué criterio debe usar cada RecyclerView para filtrar sus dataSets
        taskListAdapter = new TaskListAdapter(this, tabCode);

        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));// Distribución lineal

        setFragmentBackgroundColor(tabCode);

        return v;

    } // fin onCreateView

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Método a usar para restaurar el fragmento

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK){
            switch (requestCode){
                case MainActivity.EDIT_TASK_REQUEST_CODE:
                    // Creación del objeto con datos ya editados
                    String editedTitle = data.getStringExtra(ManipulateTaskActivity
                                                             .TASK_TITLE_IDENTIFIER);
                    String editedDescription = data.getStringExtra(ManipulateTaskActivity
                                                                   .TASK_DESCRIPTION_IDENTIFIER);
                    int editedState = data.getIntExtra(ManipulateTaskActivity
                                                       .TASK_STATE_IDENTIFIER, 0);

                    Task editedTask = new Task(editedTitle, editedDescription, editedState);

                    // Actualización en la base de datos
                    DBAdapter.getInstance(getContext()).insertTask(editedTask);

                    int position = data.getIntExtra(TaskListAdapter.EDITING_TASK_POSITION, 0);
                    // Actualización del recyclerview
                    taskListAdapter.getDataSet().remove(position);
                    taskListAdapter.getDataSet().add(position, editedTask);
                    taskListAdapter.notifyItemChanged(position);

                    break;
            }
        }
    }

    /* Determina el color de fondo del fragmento */
    private void setFragmentBackgroundColor(int tabCode){

        int colorCode = R.color.allTasksColor;

        switch (tabCode){ // Color del fondo
            case 1:
                colorCode = R.color.pendantColor;
                break;
            case 2:
                colorCode = R.color.inProgressColor;
                break;
            case 3:
                colorCode = R.color.finishedColor;
                break;
        }

        recyclerView.setBackgroundColor(getResources().getColor(colorCode));
    }

}

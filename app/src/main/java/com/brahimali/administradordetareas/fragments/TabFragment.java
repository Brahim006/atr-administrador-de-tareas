package com.brahimali.administradordetareas.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.brahimali.administradordetareas.gui.MainActivity;
import com.brahimali.administradordetareas.gui.ManipulateTaskActivity;
import com.brahimali.administradordetareas.R;
import com.brahimali.administradordetareas.database.entity.Task;
import com.brahimali.administradordetareas.viewmodel.TaskViewModel;

import java.util.List;

/**
 * Fragmento contenedor del modelo para cada una de las pestañas.
 */
public class TabFragment extends Fragment {

    private RecyclerView recyclerView;      // Distribución tipo lista reciclable
    private TaskListAdapter taskListAdapter;// Adaptador para la creacción de elementos de la lista
    private TaskViewModel taskViewModel;    // Viewmodel para la comunicación de fragmentos

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab, container, false);

        int tabCode = getArguments().getInt(TabAdapter.TAB_NUM_REFERENCE);

        recyclerView = v.findViewById(R.id.taskList);
        taskListAdapter = new TaskListAdapter(this);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        onBindLiveData(tabCode);

        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));// Distribución lineal

        return v;

    } // fin onCreateView

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == getActivity().RESULT_OK &&
                requestCode == MainActivity.EDIT_TASK_REQUEST_CODE){

            // Creación del objeto con datos ya editados
            String editedTitle = data.getStringExtra(ManipulateTaskActivity
                    .TASK_TITLE_IDENTIFIER);
            String editedDescription = data.getStringExtra(ManipulateTaskActivity
                    .TASK_DESCRIPTION_IDENTIFIER);
            int editedState = data.getIntExtra(ManipulateTaskActivity
                    .TASK_STATE_IDENTIFIER, 0);

            String oldTitle = data.getStringExtra(ManipulateTaskActivity.OLD_TITLE_IDENTIFIER);

            if(oldTitle != null){
                // Cambia el título de la tarea en caso de que éste haya sido editado
                taskViewModel.updateTitle(oldTitle, editedTitle);
            }

            Task editedTask = new Task(editedTitle, editedDescription, editedState);

            // Actualización en la base de datos
            taskViewModel.update(editedTask);

            Toast.makeText(getContext(),getResources().getString(R.string.edit_task_success)
                    ,Toast.LENGTH_SHORT).show();

        } else if(resultCode == ManipulateTaskActivity.RESULT_NULL_TITLE){
            Toast.makeText(getContext(),getResources().getString(R.string.null_title_warning)
                    ,Toast.LENGTH_SHORT).show();
        }

    }

    /* Se encarga de enlazar la lista de objetos del ViewModel con el dataSet del adaptador */
    private void onBindLiveData(int tabCode){

        LiveData<List<Task>> observedList;

        switch (tabCode){
            case 1:
                observedList = taskViewModel.getPendantTasksData();
                break;
            case 2:
                observedList = taskViewModel.getInProgressTasksData();
                break;
            case 3:
                observedList = taskViewModel.getFinishedTasksData();
                break;
            default:
                observedList = taskViewModel.getAllTasksData();
                break;
        }

        observedList.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskListAdapter.setTasksList(tasks);
            }
        });

    }

    public TaskViewModel getTaskViewModel() {
        return taskViewModel;
    }

}

package com.brahimali.administradordetareas.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.brahimali.administradordetareas.gui.ManipulateTaskActivity;
import com.brahimali.administradordetareas.gui.MainActivity;
import com.brahimali.administradordetareas.R;
import com.brahimali.administradordetareas.database.entity.Task;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskHolder> {

    private List<Task> tasksList;
    private TabFragment ownerTabFragment;

    /**
     * Crea un adaptador para manejar los elementos de la lista en la que se muestran los datos.
     * @param ownerTabFragment Una referencia al fragmento contenedor de la lista.
     */
    public TaskListAdapter(TabFragment ownerTabFragment){
        this.ownerTabFragment = ownerTabFragment;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent,false);

        return new TaskHolder(taskItem);
    }

    @Override
    public int getItemCount() {
        if(tasksList != null){
            return tasksList.size();
        } else
            return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskHolder holder, final int position) {

        final Task task = tasksList.get(position);

        holder.titleView.setText(task.getTitle());
        holder.descriptionView.setText(task.getDescription());

        // Inicialización del spinner
        holder.spinnerAdapter = ArrayAdapter.createFromResource(ownerTabFragment.getContext(),
                                R.array.valid_states_array, android.R.layout.simple_spinner_item);
        holder.spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.cardStateSpinner.setAdapter(holder.spinnerAdapter);
        // Se muestra el estado actual de la tarea en el spinner
        holder.cardStateSpinner.setSelection(task.getStatusCode());

        // Lógica del botón de borrado de tareas
        holder.deleteButton.setOnClickListener(v -> {

            // Borrado de la base de datos
            ownerTabFragment.getTaskViewModel().delete(task);

        });

        //  Lógica para el botón de edición de tareas
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ownerTabFragment.getContext(),
                                           ManipulateTaskActivity.class);

                intent.putExtra(ManipulateTaskActivity.TASK_TITLE_IDENTIFIER, task.getTitle());
                intent.putExtra(ManipulateTaskActivity.TASK_DESCRIPTION_IDENTIFIER,
                                task.getDescription());
                intent.putExtra(ManipulateTaskActivity.TASK_STATE_IDENTIFIER, task.getStatusCode());
                intent.putExtra(ManipulateTaskActivity.REQUEST_CODE_IDENTIFIER,
                                MainActivity.EDIT_TASK_REQUEST_CODE);

                ownerTabFragment.startActivityForResult(intent, MainActivity.EDIT_TASK_REQUEST_CODE);

            }
        });

        // Lógica para los cambios de estado en el spinner
        holder.cardStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Se modifica el estado de la tarea representada por la tarjeta
                task.setStatusCode(position);
                ownerTabFragment.getTaskViewModel().update(task);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    } // fin onBindViewHolder

    // Setter para el dataSet
    public void setTasksList(List<Task> tasksList){
        this.tasksList = tasksList;
        notifyDataSetChanged();
    }

    /* Holder que se encarga de generar las vistas para cada elemento de la lista. */
    public static class TaskHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView titleView;
        TextView descriptionView;
        Button deleteButton;
        Button editButton;
        Spinner cardStateSpinner;
        ArrayAdapter<CharSequence> spinnerAdapter;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.task_cardView);

            titleView = itemView.findViewById(R.id.titleView);
            descriptionView = itemView.findViewById(R.id.descriptionView);

            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
            cardStateSpinner = itemView.findViewById(R.id.cardStateSpinner);
        }
    } // fin clase TaskHolder

}

package com.brahimali.administradordetareas.fragments.recyclerViewClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.brahimali.administradordetareas.MainActivity;
import com.brahimali.administradordetareas.R;
import com.brahimali.administradordetareas.data.Task;
import com.brahimali.administradordetareas.data.database.DBAdapter;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskHolder> {

    private ArrayList<Task> dataSet;

    public TaskListAdapter(int tabCode){

        if(tabCode == 0){
            // Si el código de la pestaña es 0, significa que es la pestaña general, por lo tanto
            // el dataSet debe contener todas las tareas
            dataSet = DBAdapter.getInstance().getAllTasks();
        } else {
            // Caso contrario, sólo se cargan las tareas del estado correspondiente
            dataSet = DBAdapter.getInstance().getByStatus(tabCode);
        }

    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Crea la vista correspondiente a un item de la lista.
        View taskItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent,false);

        return new TaskHolder(taskItem); // Retorna el holder correspondiente a esa vista

    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, final int position) {

        Task task = dataSet.get(position);

        holder.titleView.setText(task.getTitle());
        holder.descriptionView.setText(task.getDescription());

        // Determino el nombre del estado según su código
        String state = MainActivity.validTabs[task.getStatusCode()];

        holder.stateView.setText(state);

        // Comportamiento de los botones

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dataSet.remove(position);
                notifyDataSetChanged();
            }

        });

        //  TODO: asignarle el comportamiento al botón editButton para que inicie una nueva
        //  actividad for result, cuando la actividad de edición esté diseñada

        // TODO: asignarle el comportamiento al botón changeStateButton para que realize cambios de
        // estado presentando las opciones a través de un popUp menu, ó algo por el estilo

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    /**
     *  Holder que se encarga de generar las vistas para cada elemento de la lista.
     */
    public static class TaskHolder extends RecyclerView.ViewHolder{

        public TextView titleView;
        public TextView descriptionView;
        public TextView stateView;
        public Button deleteButton;
        public Button editButton;
        public Button changeStateButton;
        public CardView cardView;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            // Inicialización de recursos

            cardView = (CardView)itemView.findViewById(R.id.task_cardView);

            titleView = (TextView)itemView.findViewById(R.id.titleView);
            descriptionView = (TextView)itemView.findViewById(R.id.descriptionView);
            stateView = (TextView)itemView.findViewById(R.id.stateView);

            deleteButton = (Button)itemView.findViewById(R.id.deleteButton);
            editButton = (Button)itemView.findViewById(R.id.editButton);
            changeStateButton = (Button)itemView.findViewById(R.id.change_state_button);

        }

    } // fin clase TaskHolder

}

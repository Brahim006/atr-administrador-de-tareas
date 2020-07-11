package com.brahimali.administradordetareas.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.brahimali.administradordetareas.gui.ManipulateTaskActivity;
import com.brahimali.administradordetareas.gui.MainActivity;
import com.brahimali.administradordetareas.R;
import com.brahimali.administradordetareas.database.entity.Task;
import com.brahimali.administradordetareas.utils.TabNamer;

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

        // Determino el nombre del estado según su código
        String state = TabNamer.getValidTabName(ownerTabFragment.getContext(), task.getStatusCode());

        holder.stateView.setText(state);

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

        // Lógica del botón de cambio de estado
        holder.changeStateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(ownerTabFragment.getContext(), v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.task_state_selector, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int newStatus = ManipulateTaskActivity.DEFAULT_STATE;

                        switch (item.getItemId()){
                            // El item 1 no necesita cambiar el valor por defecto, son lo mismo
                            case R.id.item_state_2:
                                newStatus = 2;
                                break;
                            case R.id.item_state_3:
                                newStatus = 3;
                                break;
                        }

                        Task task = tasksList.get(position);
                        task.setStatusCode(newStatus);
                        ownerTabFragment.getTaskViewModel().update(task);

                        return true;
                    }
                });
                popupMenu.show();
            }
        }); // fin listener

    } // fin onBindViewHolder

    // Setter para el dataSet
    public void setTasksList(List<Task> tasksList){
        this.tasksList = tasksList;
        notifyDataSetChanged();
    }

    /* Holder que se encarga de generar las vistas para cada elemento de la lista. */
    public static class TaskHolder extends RecyclerView.ViewHolder{

        TextView titleView;
        TextView descriptionView;
        TextView stateView;
        Button deleteButton;
        Button editButton;
        Button changeStateButton;
        CardView cardView;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

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

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.brahimali.administradordetareas.database.TaskRoomDatabase;
import com.brahimali.administradordetareas.database.dao.TaskDao;
import com.brahimali.administradordetareas.gui.ManipulateTaskActivity;
import com.brahimali.administradordetareas.gui.MainActivity;
import com.brahimali.administradordetareas.R;
import com.brahimali.administradordetareas.database.entity.Task;
import com.brahimali.administradordetareas.utils.TabNamer;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskHolder> {

    private List<Task> dataSet;
    private Fragment parentFragment;

    public static final String EDITING_TASK_POSITION = "editing task position";

    // TODO: revisar el tema de los contextos

    /**
     * Crea un adaptador para manejar los elementos de la lista en la que se muestran los datos.
     * Está pensada para usarse dentro de un fragmento, por lo que se requiere una referencia
     * a éste.
     * Ciertos parámetros están restrictos al código de tablas expuesto en {@link MainActivity}.
     * @param parentFragment El fragmento contenedor de la lista.
     * @param tabCode El código de tabla que se representa.
     */
    public TaskListAdapter(Fragment parentFragment, int tabCode){

        this.parentFragment = parentFragment;

        TaskDao dao =
                TaskRoomDatabase.getInstance(parentFragment.getContext().getApplicationContext())
                                .getTaskDao();

        if(tabCode == 0){
            // Si el código de la pestaña es 0, significa que es la pestaña general
            dataSet = dao.getAllTasks();
        } else {
            // Caso contrario, sólo se cargan las tareas del estado correspondiente
            dataSet = dao.getByStatus(tabCode);
        }

    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crea la vista correspondiente a un item de la lista.
        View taskItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent,false);

        return new TaskHolder(taskItem);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskHolder holder, final int position) {

        final Task task = dataSet.get(position);

        holder.titleView.setText(task.getTitle());
        holder.descriptionView.setText(task.getDescription());

        // Determino el nombre del estado según su código
        String state = TabNamer.getValidTabName(parentFragment.getContext(),
                                                task.getStatusCode());

        holder.stateView.setText(state);

        // TODO: Investigar cómo notificar a los fragments hermanos

        // Lógica del botón de borrado de tareas
        holder.deleteButton.setOnClickListener(v -> {

            // Borrado de la base de datos
            String taskTitle = dataSet.get(position).getTitle();

            TaskDao dao = TaskRoomDatabase
                    .getInstance(parentFragment.getContext().getApplicationContext())
                    .getTaskDao();
            dao.delete(taskTitle);
            // Borrado de la lista
            dataSet.remove(position);
            notifyItemRemoved(position);

        });

        //  Lógica para el botón de edición de tareas
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(parentFragment.getContext(),
                                           ManipulateTaskActivity.class);

                intent.putExtra(ManipulateTaskActivity.TASK_TITLE_IDENTIFIER, task.getTitle());
                intent.putExtra(ManipulateTaskActivity.TASK_DESCRIPTION_IDENTIFIER,
                                task.getDescription());
                intent.putExtra(ManipulateTaskActivity.TASK_STATE_IDENTIFIER, task.getStatusCode());
                // Envía la posición de la tarea dentro del dataSet
                intent.putExtra(EDITING_TASK_POSITION, position);

                parentFragment.startActivityForResult(intent, MainActivity.EDIT_TASK_REQUEST_CODE);

            }
        });

        // Lógica del botón de cambio de estado
        holder.changeStateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(parentFragment.getContext(), v);
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

                        Task task = dataSet.get(position);
                        task.setStatusCode(newStatus);

                        TaskDao dao = TaskRoomDatabase
                                .getInstance(parentFragment.getContext().getApplicationContext())
                                .getTaskDao();
                        dao.update(task);

                        notifyItemChanged(position); // Cambio en el dataSet

                        return true;
                    }
                });

                popupMenu.show();

            }
        }); // fin listener

    } // fin onBindViewHolder

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

    // Getter para el dataSet
    public List<Task> getDataSet(){
        return dataSet;
    }

}

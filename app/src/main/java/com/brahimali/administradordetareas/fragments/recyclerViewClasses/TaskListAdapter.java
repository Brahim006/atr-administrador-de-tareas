package com.brahimali.administradordetareas.fragments.recyclerViewClasses;

import android.content.Context;
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

import java.util.ArrayList;

import com.brahimali.administradordetareas.AddTaskActivity;
import com.brahimali.administradordetareas.MainActivity;
import com.brahimali.administradordetareas.R;
import com.brahimali.administradordetareas.data.Task;
import com.brahimali.administradordetareas.data.database.DBAdapter;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskHolder> {

    private ArrayList<Task> dataSet;
    private Context fragmentContext;

    /**
     * Crea un adaptador para manejar los elementos de la lista en la que se muestran los datos.
     * Está pensada para usarse dentro de un fragmento, por lo que se requiere usar el contexto de
     * la actividad asociada.
     * Ciertos parámetros están restrictos al código de tablas expuesto en {@link MainActivity}.
     * @param fragmentContext El contexto de la actividad a la que está asociado el fragmento
     *                        contenedor.
     * @param tabCode El código de tabla que se representa.
     */
    public TaskListAdapter(Context fragmentContext, int tabCode){
        this.fragmentContext = fragmentContext;

        if(tabCode == 0){
            // Si el código de la pestaña es 0, significa que es la pestaña general, por lo tanto
            // el dataSet debe contener todas las tareas
            dataSet = DBAdapter.getInstance(fragmentContext).getAllTasks();
        } else {
            // Caso contrario, sólo se cargan las tareas del estado correspondiente
            dataSet = DBAdapter.getInstance(fragmentContext).getByStatus(tabCode);
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
        String state = MainActivity.validTabs[task.getStatusCode()];

        holder.stateView.setText(state);

        // Lógica del botón de borrado de tareas
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Borrado de la base de datos
                String taskTitle = dataSet.get(position).getTitle();
                DBAdapter.getInstance(fragmentContext).deleteTask(taskTitle);
                // Borrado de la lista
                dataSet.remove(position);
                notifyItemRemoved(position);
            }
        });

        //  TODO: asignarle el comportamiento al botón editButton para que inicie una nueva
        //  actividad for result, cuando la actividad de edición esté diseñada

        // Lógica del botón de cambio de estado
        holder.changeStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creación del menú contextual para la selección del nuevo estado, la idea
                // es reciclar el modelo del menú usado en la actividad de creación de tareas
                PopupMenu popupMenu = new PopupMenu(fragmentContext, v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.task_state_selector, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int newState = AddTaskActivity.DEFAULT_STATE;

                        switch (item.getItemId()){
                            // El item 1 no necesita cambiar el valor por defecto, son lo mismo
                            case R.id.item_state_2:
                                newState = 2;
                                break;
                            case R.id.item_state_3:
                                newState = 3;
                                break;
                        }

                        DBAdapter.getInstance(fragmentContext)
                                 .changeStatus(task.getTitle(), newState);// Cambio en base de datos

                        dataSet.get(position).setStatusCode(newState);
                        notifyItemChanged(position); // Cambio en el dataSet

                        return true;
                    }
                });

            }
        }); // fin listener

    } // fin onBindViewHolder

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

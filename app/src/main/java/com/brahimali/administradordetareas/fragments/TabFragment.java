package com.brahimali.administradordetareas.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brahimali.administradordetareas.R;
import com.brahimali.administradordetareas.fragments.recyclerViewClasses.TaskListAdapter;

/**
 * Fragmento que sirve de modelo para crar cada una de las pestañas.
 */
public class TabFragment extends Fragment {

    // Elementos de lista
    private RecyclerView recyclerView;
    private TaskListAdapter taskListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public TabFragment() {
        // Constructor nulo requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab, container, false);

        int tabCode = getArguments().getInt(TabAdapter.TAB_NUM_REFERENCE);

        recyclerView = (RecyclerView)v.findViewById(R.id.taskList);

        // Indica qué criterio debe usar cada RecyclerView para filtrar sus dataSets
        taskListAdapter = new TaskListAdapter(getContext(), tabCode);
        layoutManager = new LinearLayoutManager(container.getContext());

        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setLayoutManager(layoutManager);

        switch (tabCode){ // Color del fondo
            case 0:
                recyclerView.setBackgroundColor(
                        container.getResources().getColor(R.color.allTasksColor)
                );
                break;
            case 1:
                recyclerView.setBackgroundColor(
                        container.getResources().getColor(R.color.pendantColor)
                );
                break;
            case 2:
                recyclerView.setBackgroundColor(
                        container.getResources().getColor(R.color.inProgressColor)
                );
                break;
            case 3:
                recyclerView.setBackgroundColor(
                        container.getResources().getColor(R.color.finishedColor)
                );
                break;
        }

        return v;

    } // fin onCreateView

}

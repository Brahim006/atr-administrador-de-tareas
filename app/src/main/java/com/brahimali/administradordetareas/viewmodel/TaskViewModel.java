package com.brahimali.administradordetareas.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.brahimali.administradordetareas.TaskRepository;
import com.brahimali.administradordetareas.database.entity.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;
    // DataSets observables para todos los datos de las listas de tareas
    private LiveData<List<Task>> allTasksData;
    private LiveData<List<Task>> pendantTasksData;
    private LiveData<List<Task>> inProgressTasksData;
    private LiveData<List<Task>> finishedTasksData;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        // Inicialización de los dataSets
        allTasksData = repository.getAllTasksData();
        pendantTasksData = repository.getPendantTasksData();
        inProgressTasksData = repository.getInProgressTasksData();
        finishedTasksData = repository.getFinishedTasksData();
    }

    public void insert(Task task){ repository.insert(task); }

    public void delete(Task task){ repository.delete(task); }

    public void update(Task task){ repository.update(task); }

    public void updateTitle(@NonNull String title, @NonNull String newTitle){
        repository.updateTitle(title, newTitle);
    }

    // Getters para los LiveData observables

    public LiveData<List<Task>> getAllTasksData() {
        return allTasksData;
    }

    public LiveData<List<Task>> getPendantTasksData() {
        return pendantTasksData;
    }

    public LiveData<List<Task>> getInProgressTasksData() {
        return inProgressTasksData;
    }

    public LiveData<List<Task>> getFinishedTasksData() {
        return finishedTasksData;
    }

}

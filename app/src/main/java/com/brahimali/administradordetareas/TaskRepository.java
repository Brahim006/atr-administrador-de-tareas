package com.brahimali.administradordetareas;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.brahimali.administradordetareas.database.TaskRoomDatabase;
import com.brahimali.administradordetareas.database.dao.TaskDao;
import com.brahimali.administradordetareas.database.entity.Task;

import java.util.List;

/**
 * Repositorio global de la aplicación para petición de datos. Se encapsula el acceso a base de
 * datos aquí ya que Room requiere que sus peticiones se hagan a través de hilos alternos a la
 * interfaz de usuario.
 */
public class TaskRepository {

    private TaskDao taskDao;
    // DataSets observables para todos los datos de las listas de tareas
    private LiveData<List<Task>> allTasksData;
    private LiveData<List<Task>> pendantTasksData;
    private LiveData<List<Task>> inProgressTasksData;
    private LiveData<List<Task>> finishedTasksData;

    /**
     * Inicia el repositorio de la aplicación, el mismo es quien se encarga de proveer acceso a
     * datos y manipular/inicializar las colecciones de datos mutables de las que se abastecen
     * las listas de la GUI.
     * @param application El estado de la aplicación global, ya que es necesario mantener
     *                    colecciones de datos que sobrevivan a los cambios de estado de las
     *                    actividades.
     */
    public TaskRepository(Application application){
        TaskRoomDatabase db = TaskRoomDatabase.getInstance(application.getApplicationContext());
        taskDao = db.getTaskDao();
        // Inicia todos los dataSets
        allTasksData = taskDao.getAllTasks();
        pendantTasksData = taskDao.getByStatus(1);
        inProgressTasksData = taskDao.getByStatus(2);
        finishedTasksData = taskDao.getByStatus(3);
    }

    /**
     * Inserta enla base de datos una tarea representada por el objeto de tipo Task.
     * @param task Un objeto Task que contiene todos los parámetros representando a una tarea.
     */
    public void insert(Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insert(task);
        });
    }

    /**
     * Borra una tarea de la base de datos.
     * @param task Un objeto tipo Task que tenga los parámetros de la tarea a borrar.
     */
    public void delete(Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.delete(task);
        });
    }

    /**
     * Actualiza en la base de datos la entrada correspondiente a la tarea cuyo título coincide con
     * el de la tarea pasada como parámetro.
     * Este método está pensado para actualizar tareas a las cuales se les ha modificado su
     * descripción y/o estado, ya que el título de las tareas funge como clave primaria y no es
     * posible actualizar la entrada en la base de datos si este título se desconoce.
     * Para cambios en los títulos, use el método que recibe dos parámetros.
     * @param task Un objeto task el cual haya sido modificado y se quiera registrar estos cambios
     *             en la base de datos (excluyendo el título).
     */
    public void update(Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.update(task);
        });
    }

    /**
     * Modifica seguramente el título de una tarea siempre y cuando el nuevo título sea único y
     * válido. Sólo modifica el título, por lo que para modificar otros parámetros de una tarea debe
     * usarse el método que recibe un objeto Task como parámetro.
     * @param title El título actual de la tarea a modificar.
     * @param newTitle El nuevo título único y válido.
     */
    public void updateTitle(@NonNull String title, @NonNull String newTitle){
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.update(title, newTitle);
        });
    }

    // Getters para los LiveData

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

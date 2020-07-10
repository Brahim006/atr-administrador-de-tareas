package com.brahimali.administradordetareas.database.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.brahimali.administradordetareas.database.DBContract;
import com.brahimali.administradordetareas.database.entity.Task;

import java.util.List;

/**
 * Interfaz que define el comportamiento a ser ejecutado por Room para acceder a la tabla de tareas.
 */
@Dao
public interface TaskDao {

    /**
     * Trae desde la base de datos todas las tareas registradas.
     * @return Una colección List de objetos Task que representan a todas las tareas.
     */
    @Query(DBContract.GET_ALL_TASKS_QUERY)
    public List<Task> getAllTasks();

    /**
     * Retorna una tarea según su título (único en la base de datos).
     * @param title El título de la tarea especificada. Dicho atributo es único dentro de toda la
     *              base de datos.
     * @return Un objeto Task corresponidiente a la tarea buscada.
     */
    @Query(DBContract.GET_TASK_BY_TITLE)
    public Task getByTitle(@NonNull String title);

    /**
     * Retorna todas las tareas según su código de estado.
     * @param status El código de estado válido que pueden tener las tareas. Los códigos deben
     *               coincidir con los asignados en el archvo strigs.xml
     * @return Una colección tipo List de objetos Task con todas las tareas que tengan dicho código
     *         de estado.
     */
    @Query(DBContract.GET_TASKS_BY_STATUS)
    public List<Task> getByStatus(int status);

    /**
     * Inserta enla base de datos una tarea representada por el objeto de tipo Task.
     * @param task Un objeto Task que contiene todos los parámetros representando a una tarea.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insert(Task task);

    /**
     * Borra una tarea según su título.
     * @param title El título (único) de la tarea que se quiera borrar.
     */
    @Query(DBContract.DELETE_TASK_BY_TITLE)
    public void delete(@NonNull String title);

    /**
     * Borra todas las tareas que compartan un código de estado.
     * @param status El código de estado válido que pueden tener las tareas. Los códigos deben
     *                   coincidir con los asignados en el archivo strnigs.xml
     */
    @Query(DBContract.DELETE_TASK_BY_STATUS)
    public void delete(int status);

    /**
     * Borra una tarea de la base de datos.
     * @param task Un objeto tipo Task que tenga los parámetros de la tarea a borrar.
     */
    @Delete
    public void delete(Task task);

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
    @Update(onConflict = OnConflictStrategy.ABORT)
    public void update(Task task);

    // TODO: Ver la necesidad de arrojar una excepción por conflictos con los títulos

    /**
     * Modifica seguramente el título de una tarea siempre y cuando el nuevo título sea único y
     * válido. Sólo modifica el título, por lo que para modificar otros parámetros de una tarea debe
     * usarse el método que recibe un objeto Task como parámetro.
     * @param title El título actual de la tarea a modificar.
     * @param newTitle El nuevo título único y válido.
     */
    @Query(DBContract.UPDATE_TASK_TITLE)
    public void update(@NonNull String title, @NonNull String newTitle);

}

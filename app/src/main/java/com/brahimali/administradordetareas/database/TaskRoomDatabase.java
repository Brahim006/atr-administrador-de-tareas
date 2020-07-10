package com.brahimali.administradordetareas.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.brahimali.administradordetareas.database.dao.TaskDao;
import com.brahimali.administradordetareas.database.entity.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database( entities = {Task.class} , version = 1 , exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {

    private static volatile TaskRoomDatabase instance;
    private static final String DB_NAME = "task_db";
    private static final int NUM_OF_THREADS = 4;    // Idealmente, un hilo para cada lista de tareas
    public static final ExecutorService databaseWriteExecutor =
                        Executors.newFixedThreadPool(NUM_OF_THREADS); // Ejecutor de hilos

    /**
     * Retorna una instancia de la base de datos gestionada por Room.
     * @param context El contexto sobre el cual opera esta base de datos, generalmente es el
     *                contexto de la aplicación.
     * @return Una instancia única para toda la ejecución de la base de datos. No provee métodos de
     *         acceso a datos ya que estas se resuelven a través de una implementación de TaskDao.
     */
    public static TaskRoomDatabase getInstance(final Context context){    // Singletton pattern
        if (instance == null){
            synchronized (TaskRoomDatabase.class){
                instance = Room.databaseBuilder(context.getApplicationContext(),
                                                TaskRoomDatabase.class,
                                                DB_NAME).build();
            }
        }
        return instance;
    }

    /**
     * Retorna una implementación de la interfaz TaskDao la cual es instanciada por Room.
     * @return Un objeto tipo TaskDao mediante la cual se puede tener acceso a la base de datos.
     */
    public abstract TaskDao getTaskDao();

}

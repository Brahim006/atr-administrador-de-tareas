package com.brahimali.administradordetareas.database.dbaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.brahimali.administradordetareas.database.Task;

import java.util.ArrayList;

/**
 * Clase tipo adaptador para el manejo del acceso a la base de datos. Maneja un patrón singletton
 * para asegurar que haya una instancia única por ejecución.
 */
public class DBAdapter {

    private TaskDBHelper dbHelper;
    private SQLiteDatabase db;

    private static DBAdapter dbAdapter; // Instancia para ser usada en un patrón Singleton

    // Tablas requeridas en las consultas a la base de datos. Se incluyean todas las necesarias
    // para la construcción de tareas.
    public static final String[] TASKS_BUILDING_PROJECTION = {
            DBContract.COLUMN_NAME_TITLE,
            DBContract.COLUMN_NAME_DESCRIPTION,
            DBContract.COLUMN_NAME_STATUS_CODE
    };

    /**
     * Constructor privado para la obtención de instancias sólo a través del patrón singleton.
     * @param context El contexto de la actividad ó aplicación que hace uso del acceso a datos.
     *                Se recomienda que provenga desde la actividad principal de la app.
     */
    private DBAdapter(Context context){

        dbHelper = new TaskDBHelper(context);
        db = dbHelper.getWritableDatabase();

    }

    private DBAdapter(){} // Evita una instanciación sin contexto de la base de datos

    /**
     * Retorna una instancia única del adaptador para acceder a la base de datos. En caso de no
     * existir dicha instancia, se crea a partir del contexto especificado.
     * @param context Contexto del fragmento ó actividad en el que se desee hacer uso del acceso a
     *                base de datos.
     * @return Una instancia única del objeto {@link DBAdapter}.
     */
    public static DBAdapter getInstance(Context context){

        if(dbAdapter == null){
            dbAdapter = new DBAdapter(context);
        }

        return dbAdapter;

    }

    /**
     * Cierra el acceso a la base de datos. Este método está pensado para llamarse desde el método
     * onDestroy() de la actividad que haga uso del accesoa  datos.
     */
    public void closeDB(){
        db.close();
        dbAdapter = null;
    }

    /**
     * Trae desde la base de datos todas las tareas registradas.
     * @return Un ArrayLists de objetos Task que representan a todas las tareas.
     */
    public ArrayList<Task> getAllTasks(){

        Cursor cursor = db.query(
                DBContract.TABLE_NAME,
                TASKS_BUILDING_PROJECTION,
                null,
                null,
                null,
                null,
                DBContract.COLUMN_NAME_TITLE
        );

        ArrayList<Task> ret = new ArrayList<>();
        Task task;

        while(cursor.moveToNext()){ // Crea las nuevas tareas y las añade a la colección

            task = new Task(
                    cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_NAME_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_NAME_STATUS_CODE))
            );

            ret.add(task);

        }

        return ret;

    } // fin getAllTasks

    /**
     * Retorna una tarea según su título (único en la base de datos).
     * @param title El título de la tarea especificada. Dicho atributo es único dentro de toda la
     *              base de datos.
     * @return Un objeto Task corresponidiente a la tarea buscada.
     */
    public Task getByTitle(String title){

        String selection = DBContract.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {title};

        Cursor cursor = db.query(
                DBContract.TABLE_NAME,
                TASKS_BUILDING_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToNext();

        // Crea la nueva tarea a partir del único resultado del Cursor
        return new Task(
                cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_NAME_TITLE)),
                cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_NAME_DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_NAME_STATUS_CODE))
        );

    }

    /**
     * Retorna todas las tareas según s código de estado.
     * @param status El código de estado válido que pueden tener las tareas. Los códigos deben
     *               coincidir con los asignados en el archvo strigs.xml
     * @return u ArrayList de objetos Task con todas las tareas qe tengan dicho código de estad.
     */
    public ArrayList<Task> getByStatus(int status){

        String selection = DBContract.COLUMN_NAME_STATUS_CODE + " = ?";
        String[] selectionArgs = {Integer.toString(status)};


        Cursor cursor = db.query(
                DBContract.TABLE_NAME,
                TASKS_BUILDING_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                DBContract.COLUMN_NAME_TITLE
        );

        ArrayList<Task> ret = new ArrayList<>();
        Task task;

        while(cursor.moveToNext()){ // Crea las nuevas tareas y las añade a la colección

            task = new Task(
                    cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DBContract.COLUMN_NAME_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(DBContract.COLUMN_NAME_STATUS_CODE))
            );

            ret.add(task);

        }

        task = null;
        return ret;

    } // fin getByStatus

    /**
     *  Inserta enla base de datos una tarea representada por el objeto task de tipo Task.
     * @param task Un objeto Task que contiene todos los parámetros representando a una tarea.
     * @return Un primitivo long indicando el ID de la nueva fila en la base de datos ó -1 en caso
     *         de que no haya sido posible insertar la tarea.
     */
    public long insertTask(Task task){

        ContentValues values = new ContentValues();

        values.put(DBContract.COLUMN_NAME_TITLE, task.getTitle());
        values.put(DBContract.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(DBContract.COLUMN_NAME_STATUS_CODE, task.getStatusCode());

        return db.insert(DBContract.TABLE_NAME, null, values);

    }

    /**
     * Inserta en la base de datos todas las tareas incluidas en un ArrayList.
     * @param tasks Un ArrayList de objetos Task representando todas las tareas que se quieran
     *              insertar.
     */
    public void insertTask(ArrayList<Task> tasks){

        // Inserta una a una las tareas haciendo uso del método insertTask
        for(Task task : tasks){
            insertTask(task);
        }

    }

    /**
     * Borra una tarea según su título.
     * @param title El título (único) de la tarea que se quiera borrar.
     */
    public void deleteTask(String title){

        String selection = DBContract.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {title};

        db.delete(DBContract.TABLE_NAME, selection, selectionArgs);

    }

    /**
     * Borra todas las tareas que compartan un código de estado.
     * @param statusCode El código de estado válido que pueden tener las tareas. Los códigos deben
     *                   coincidir con los asignados en el archvo strigs.xml
     */
    public void deleteTask(int statusCode){

        String selection = DBContract.COLUMN_NAME_STATUS_CODE + " = ?";
        String[] selectionArgs = {Integer.toString(statusCode)};

        db.delete(DBContract.TABLE_NAME, selection, selectionArgs);

    }

    /**
     * Cambia el estado de una tarea específica en la base de datos.
     * @param title El título único de la tarea a la cual se quiere cambiarle el estado.
     * @param newStatus El código del nuevo estado. Los estados disponibles están especificados en
     *                  el archivo strings.xml o también dentro de la clase {@link Task}.
     */
    public void changeStatus(String title, int newStatus){

        Task task = getByTitle(title); // Busca la tarea original

        // Valores a ser insertados en el update
        ContentValues values = new ContentValues();
        values.put(DBContract.COLUMN_NAME_TITLE, title);
        values.put(DBContract.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(DBContract.COLUMN_NAME_STATUS_CODE, newStatus);

        String where = DBContract.COLUMN_NAME_TITLE + " = ?";
        String[] whereArgs = {title};

        db.update(
                DBContract.TABLE_NAME,
                values,
                where,
                whereArgs
        );

    }

    /**
     * Cambia el título de una tarea en la base de datos
     * @param currentTitle El título actual.
     * @param newTitle El nuevo título.
     */
    public void changeTitle(String currentTitle, String newTitle){

        Task task = getByTitle(currentTitle); // Busca la tarea original

        // Valores a ser insertados en el update
        ContentValues values = new ContentValues();
        values.put(DBContract.COLUMN_NAME_TITLE, newTitle);
        values.put(DBContract.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(DBContract.COLUMN_NAME_STATUS_CODE, task.getStatusCode());

        String where = DBContract.COLUMN_NAME_TITLE + " = ?";
        String[] whereArgs = {currentTitle};

        db.update(
                DBContract.TABLE_NAME,
                values,
                where,
                whereArgs
        );

    }

    /**
     * Cambia la descripción de la tarea especificada en la base de datos.
     * @param title El título único de la tarea a modificar.
     * @param newDescription La nueva descripción.
     */
    public void changeDescription(String title, String newDescription){

        Task task = getByTitle(title); // Busca la tarea original

        // Valores a ser insertados en el update
        ContentValues values = new ContentValues();
        values.put(DBContract.COLUMN_NAME_TITLE, title);
        values.put(DBContract.COLUMN_NAME_DESCRIPTION, newDescription);
        values.put(DBContract.COLUMN_NAME_STATUS_CODE, task.getStatusCode());

        String where = DBContract.COLUMN_NAME_TITLE + " = ?";
        String[] whereArgs = {title};

        db.update(
                DBContract.TABLE_NAME,
                values,
                where,
                whereArgs
        );

    } // fin changeDescription

}

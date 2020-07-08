/**
 * Helper para la creación de la base de datos SQLite.
 */
package com.brahimali.administradordetareas.database.dbaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "taskManager.db";

    public TaskDBHelper(Context context){

        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBContract.CREATE_TASK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(newVersion > oldVersion){
            upgradeDataBase(db);
        }

    }

    /*
     * Este método queda disponible por si son necesarias nuevas actualizaciones para la db.
     */
    private void upgradeDataBase(SQLiteDatabase db){
        // Lógica de la actualización
    }

}

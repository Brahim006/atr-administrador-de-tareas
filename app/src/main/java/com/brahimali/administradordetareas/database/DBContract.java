/**
 * Clase tipo Contract que contiene las constanstes usadas para  manipular la base de datos.
 * (Este patrón de diseño es recomendado por Google en su guía de desarrollo Android de Google)
 */
package com.brahimali.administradordetareas.database;

public final class DBContract {

    // Este constructor sólo existe para evitar que esta clase pueda ser instanciada
    private DBContract(){}

    // Nombres de tablas y columnas
    public static final String TABLE_NAME = "tasks";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_STATUS_CODE = "status_code";

    // Queries
    public static final String CREATE_TASK_TABLE = "CREATE TABLE " + DBContract.TABLE_NAME +
            " ( " + DBContract.COLUMN_NAME_TITLE + " TEXT PRIMARY KEY, " +
            DBContract.COLUMN_NAME_DESCRIPTION + " TEXT, " +
            DBContract.COLUMN_NAME_STATUS_CODE + " INTEGER )";

    public static final String GET_ALL_TASKS_QUERY = "SELECT * FROM " + TABLE_NAME +
                                                     " ORDER BY " + COLUMN_NAME_TITLE + " ASC";

    public static final String GET_TASK_BY_TITLE = "SELECT * FROM " + TABLE_NAME +
                                                   " WHERE " + COLUMN_NAME_TITLE + " = :title";

    public static final String GET_TASKS_BY_STATUS = "SELECT * FROM " + TABLE_NAME +
                                                     " WHERE " + COLUMN_NAME_STATUS_CODE +
                                                     " = :status";

    public static final String DELETE_TASK_BY_TITLE = "DELETE FROM " + TABLE_NAME +
                                                      " WHERE " + COLUMN_NAME_TITLE +
                                                      " = :title";

    public static final String DELETE_TASK_BY_STATUS = "DELETE FROM " + TABLE_NAME +
                                                       " WHERE " + COLUMN_NAME_STATUS_CODE +
                                                       " = :status";

    public static final String UPDATE_TASK_TITLE = "UPDATE " + TABLE_NAME + " SET " +
                                                   COLUMN_NAME_TITLE + " = :newTitle WHERE " +
                                                   COLUMN_NAME_TITLE + " = :title";

}

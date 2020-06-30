/**
 * Clase tipo Contract que contiene las constanstes usadas para definir la base de datos.
 * (Este patrón de diseño es recomendado por Google en su guía de desarrollo Android)
 */
package com.brahimali.administradordetareas.data.database;

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
            "( " + DBContract.COLUMN_NAME_TITLE + " TEXT PRIMARY KEY, " +
            DBContract.COLUMN_NAME_DESCRIPTION + " TEXT " +
            DBContract.COLUMN_NAME_STATUS_CODE + " INTEGER )";

}

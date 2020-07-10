/**
 * Clase entidad que es mapeada por Room para crear la tabla en la base de datos.
 * Esta entidad representa a cada tarea manipulable.
 */
package com.brahimali.administradordetareas.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.brahimali.administradordetareas.database.DBContract;

@Entity (tableName = DBContract.TABLE_NAME)
public class Task {

    @PrimaryKey
    @NonNull
    @ColumnInfo (name = DBContract.COLUMN_NAME_TITLE)
    private String title;

    @ColumnInfo (name = DBContract.COLUMN_NAME_DESCRIPTION)
    private String description;

    @ColumnInfo (name = DBContract.COLUMN_NAME_STATUS_CODE)
    private int statusCode;

    // Códigos de estado válidos
    public static int[] VALID_STATES = {1,2,3};

    public Task(@NonNull String title, String description, int statusCode){

        this.title = title;
        this.description = description;
        this.statusCode = stateVerifier(statusCode);

    }

    private int stateVerifier(int statusCode){ // Valida el código de estado

        for(int i : VALID_STATES){
            if(statusCode == i) return statusCode;
        }

        return VALID_STATES[0]; // Valor por defecto

    }

    // Getters

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}

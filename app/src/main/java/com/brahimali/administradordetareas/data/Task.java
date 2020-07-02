/**
 * Objeto que representa a una tarea programada.
 */
package com.brahimali.administradordetareas.data;

public class Task {

    private String title;
    private String description;
    private int statusCode;

    // Códigos de estados dsponibles.
    // Estos deben corresponderse con la cantidad provista en el archivo strings.xml
    public static int[] VALID_STATES = {1,2,3};

    public Task(String title, String description, int statusCode){

        this.title = title;
        this.description = description;
        this.statusCode = stateVerifier(statusCode);

    }

    /*
     * Valida el código de estado según lo especificado en el arreglo estático. Por defecto se
     * retorna el primer valor de éste.
     */
    private int stateVerifier(int statusCode){

        boolean flag = false;

        for(int i = 0; i < VALID_STATES.length; i++){

            if(statusCode == VALID_STATES[i]){
                flag = true;
                break;
            }

        }

        return flag ? statusCode : VALID_STATES[0];

    } // finstateVerifier

    // Setters y getters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = stateVerifier(statusCode);
    }
}

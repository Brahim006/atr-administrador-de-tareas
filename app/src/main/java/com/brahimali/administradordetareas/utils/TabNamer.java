package com.brahimali.administradordetareas.utils;

import android.content.Context;

import com.brahimali.administradordetareas.R;

/**
 * Clase utilitaria para la devolución del nombre de cada pestaña.
 */
public class TabNamer {

    public static final int AVAILABLE_NUM_OF_TABS = 4;  // Cantidad de pestañas disponibles

    /**
     * Devuelve el nombre de la pestaña según su código, también puede usarse para retornar el
     * nombre de un estado siempre y cuando se ingrese un código de estado válido.
     * @param context El contexto en el cual se acceden a los recursos String en el cual se almacena
     *                el nombre de las pestañas y estados.
     * @param tabCode El código de la pestaña ó estado válido, el cual coincide con el nombre del
     *                estado de las tareas mostradas en cada una ó cualquier otro entero para el
     *                nombre de la pestaña que muestra a todas las tareas.
     * @return El nombre del estado del cual se haya especificado su código ó "todas" en caso de que
     *         no se haya ingresado un código de estado válido.
     */
    public static String getValidTabName(Context context, int tabCode){

        int tabNameID = R.string.tab_all;

        switch (tabCode){
            case 1:
                tabNameID = R.string.state_1;
                break;
            case 2:
                tabNameID = R.string.state_2;
                break;
            case 3:
                tabNameID = R.string.state_3;
                break;
        }

        return context.getResources().getString(tabNameID);
    }

}

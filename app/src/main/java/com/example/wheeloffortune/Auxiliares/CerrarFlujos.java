package com.example.wheeloffortune.Auxiliares;


import java.io.Closeable;
import java.io.IOException;

/**
 * Clase con métodos para cerrar flujos
 */
public class CerrarFlujos {
    /**
     * Cierra los flujos que lleguen de entrada. Antes de intentar eso, averigua
     * si el objeto no es nulo
     * @param flujos Debe ser un objeto que implemente Closeable
     */
    public static void cerrarStream(Closeable... flujos) {
        int flujos_cerrados = 0;
        try {
            for (Closeable i : flujos) {
                if (i != null) {
                    i.close();
                    flujos_cerrados++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            flujos_cerrados++;
            cerrarStream(flujos_cerrados, flujos);
        }
    }
    /**
     * Cierra los flujos que lleguen de entrada a partir de la posición indicada
     * por el entero.
     * <p>Si el indice es menor que 0, inmediatamente sube a 0.</p>
     * @param comienzo Índice desde el que empezar a cerrar flujos
     * @param flujos Flujos para cerrar
     */
    public static void cerrarStream(int comienzo, Closeable... flujos) {
        if (comienzo < 0) comienzo = 0;

        int flujos_cerrados = comienzo;
        try {
            for (int i = comienzo; i < flujos.length; i++) {
                if (flujos[i] != null) {
                    flujos[i].close();
                    flujos_cerrados++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            flujos_cerrados++;
            cerrarStream(flujos_cerrados, flujos);
        }
    }
}

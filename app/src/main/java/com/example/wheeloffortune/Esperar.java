package com.example.wheeloffortune;

/**
 * Clase auxiliar para llamar a Thread.sleep(long)
 */
public class Esperar {
    /**
     * Detiene el hilo que llame esta función
     * @param tiempo Tiempo de pausa en milisegundos
     */
    public static void segundos(int tiempo){
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

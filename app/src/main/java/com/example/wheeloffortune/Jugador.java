package com.example.wheeloffortune;

public class Jugador implements Comparable<Jugador> {
    private String nombre;
    private int puntuacion;

    public Jugador(String nombre, int puntuacion) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public Jugador(Jugador entrada) {
        this.nombre = entrada.getNombre();
        this.puntuacion = entrada.getPuntuacion();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public int compareTo(Jugador jugador) {

        if (jugador.getPuntuacion() < puntuacion) {
            return -1;
        } else if (jugador.getPuntuacion() < puntuacion) {

            return 0;
        } else {
            return 1;
        }
    }

    public  String toStringJugador() {
        return this.getNombre().toUpperCase() + "$" + String.valueOf(this.getPuntuacion()) + "\n";
    }


    public static Jugador recuperarJugador(String entrada) {
        String auxiliar = "";
        String nombre = "";
        int puntuacion = 0;
        for (int i = 0; i < entrada.length(); i++) {
            if (entrada.charAt(i) != '$') {
                auxiliar = auxiliar + entrada.charAt(i);

            } else {
                nombre = auxiliar;
                auxiliar = "";
            }
        }

        puntuacion = Integer.valueOf(auxiliar);

        return new Jugador(nombre, puntuacion);
    }
}

package com.example.wheeloffortune.Auxiliares;

public class Jugador implements Comparable<Jugador> {
    private String nombre;
    private int puntuacion;

    public Jugador(String nombre, int puntuacion) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public Jugador(Jugador jugador) {
        this.nombre = jugador.getNombre();
        this.puntuacion = jugador.getPuntuacion();
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
        }
        if (jugador.getPuntuacion() > puntuacion) {
            return 1;
        }

        for (int i = 0; i < nombre.length(); i++) {
            if (jugador.getNombre().charAt(i) < nombre.charAt(i)) {
                return 1;
            }
            if (jugador.getNombre().charAt(i) > nombre.charAt(i)) {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return this.getNombre().toUpperCase() + "$" + String.valueOf(this.getPuntuacion()) + "\n";
    }


    public static Jugador recuperarJugador(String entrada) {
        String bufer = "";

        String nombre = "";
        int puntuacion = 0;
        for (int i = 0; i < entrada.length(); i++) {
            if (entrada.charAt(i) != '$') {
                bufer = bufer + entrada.charAt(i);

            } else {
                nombre = bufer;
                bufer = "";
            }
        }

        puntuacion = Integer.valueOf(bufer);
        return new Jugador(nombre, puntuacion);
    }
}

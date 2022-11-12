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

        //DE MAYOR A MENOR SEGUN LA PUNTUACION
        int iSalComaprison = Integer.compare(this.puntuacion, jugador.getPuntuacion());
        if (iSalComaprison == 0)//Salaries are equal use name as comparison criteria
        {
            //lhs name comparison with rhs name
            return this.getNombre().compareTo(jugador.getNombre());
        }
        //Now if salaries are not equal, return comparison of salries
        return iSalComaprison;
    }
}

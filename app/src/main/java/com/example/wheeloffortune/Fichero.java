package com.example.wheeloffortune;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Fichero {
    private File fichero;
    public static ArrayList<Jugador> jugadoresAntiguos = new ArrayList<>();

    public Fichero() {
        String rutaProyecto = new File("").getAbsolutePath().toString();
        String nombreCarpeta = "Registro";
        String nombretxt = "Lista de puntuacion.txt";
        File carpeta = new File(rutaProyecto + File.separator + nombreCarpeta);

            carpeta.mkdir();
            File archivo = new File(carpeta.getAbsolutePath() + File.separator + nombretxt);
            try {
                archivo.createNewFile();
                this.fichero = archivo;
            } catch (IOException ex) {
                System.err.println("ERROR - NO SE CREO EL FICHERO");
            }


    }


    private void guardarPuntuacionJugador(Jugador j1) {
        File buscarArchivo = new File(this.fichero.getAbsolutePath());
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(buscarArchivo, true));
            bw.write(formatearJugadorToTxt(j1));
            bw.flush();

        } catch (IOException ex) {
            System.err.println("ERROR - NO SE GUARDO JUGADOR");
        } finally {
            cerrarFlujo(bw);
        }
    }

    public File getFichero() {
        return fichero;
    }

    public void setFichero(File fichero) {
        this.fichero = fichero;
    }

    public static ArrayList<Jugador> getJugadoresAntiguos() {
        return jugadoresAntiguos;
    }

    public static void setJugadoresAntiguos(ArrayList<Jugador> jugadoresAntiguos) {
        Fichero.jugadoresAntiguos = jugadoresAntiguos;
    }

    private void recuperarJugadores() {

        Jugador jugador;
        String informacion;

        File buscarArchivo = new File(this.fichero.getAbsolutePath());
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(buscarArchivo));
            while ((informacion = br.readLine()) != null) {
                jugador = formateartxtToJugador(informacion);
                jugadoresAntiguos.add(jugador);
            }
            //ORDENAR JUGADORES SEGUN SU PUNTUACION DE MAYOR A MENOR
            Collections.sort(jugadoresAntiguos);
        } catch (IOException ex) {
            ex.getStackTrace();
        } finally {
            cerrarFlujo(br);
        }
    }


    private void cerrarFlujo(Closeable c1) {
        try {
            if (c1 != null) {
                c1.close();
            }
        } catch (IOException ex) {

        }
    }

    private String formatearJugadorToTxt(Jugador j1) {
        return j1.getNombre().toUpperCase() + "$" + String.valueOf(j1.getPuntuacion()) + "\n";
    }

    private Jugador formateartxtToJugador(String entrada) {
        Jugador jugador;
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

        return jugador = new Jugador(nombre, puntuacion);
    }





}

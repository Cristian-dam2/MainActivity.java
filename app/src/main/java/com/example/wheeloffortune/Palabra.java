package com.example.wheeloffortune;

import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Palabra extends AppCompatActivity {
    private boolean musicapersonalizada;
    private String palabra;
    private String informacion;
    private TextView[] cuadros;
    public static int coincidencias = 0;
    public static int encuentros = 0;
    private static int posicion_inicial;
    private static ArrayList<String> letrasMarcadas = new ArrayList<>();
    private static TextView[] letrasGeneradas = new TextView[27];
    public static ArrayList<String> letrasUtilizadas = new ArrayList<>();
    public static HashMap<String, List<Integer>> letrasAsignadas = new HashMap<>();
    public static String[] letra = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "y", "z"};

    public boolean isMusicaPersonalizada() {
        return musicapersonalizada;
    }
    public String getPalabra() {
        return palabra;
    }
    public String getInformacion() {
        return informacion;
    }


    // CONSTRUCTOR (la entrada es el panel donde dibujará)
    public Palabra(TextView[] cuadros) {
        String[] adivinar = palabraseInformacion();

        this.palabra = adivinar[0];
        this.informacion = adivinar[1];

        this.musicapersonalizada = palabraHasCustomAudio(adivinar[0]);
        this.cuadros = cuadros;

        // INDICA LA POSICION DONDE SE VA A PINTAR EN GRIS LOS RECUADROS.
        this.posicion_inicial = (cuadros.length / 2) - (palabra.length() / 2);
        for (int i = this.posicion_inicial; i < this.posicion_inicial + palabra.length(); i++) {
            cuadros[i].setBackgroundResource(R.drawable.letra_sindescifrar);
        }

        // SEGÚN LA PALABRA SELECCIONADA, SE REALIZA UN HASHMAP PARA GUARDAR LA LETRA Y LA POSICION.
        int posicion_actual = this.posicion_inicial;
        for (int i = 0; i < palabra.length(); i++) {
            String letra = String.valueOf(palabra.charAt(i));

            if (letrasAsignadas.containsKey(letra)) {
                letrasAsignadas.get(letra).add(posicion_actual);

            } else {
                List<Integer> list = new ArrayList<>();
                list.add(posicion_actual);
                letrasAsignadas.put(letra, list);
            }
            posicion_actual++;
        }

        Log.d("LETRAS ASIGNADAS: ", letrasAsignadas.toString());
    }

    public String[] palabraseInformacion() {
        String[] seleccionado = new String[2];

        int a = (int)((Math.random() * (7 - 1)) + 1);

        switch (0) {
            case 0: //debug
                seleccionado[0] = "AZ";
                seleccionado[1] = "A y Z";
                break;
            case 1:
                seleccionado[0] = "IVAN";
                seleccionado[1] = "Profesor de Android";
                break;
            case 2:
                seleccionado[0] = "VICENTE";
                seleccionado[1] = "Profesor de Sistemas informaticos";
                break;
            case 3:
                seleccionado[0] = "SONIC";
                seleccionado[1] = "Erizo azul";
                break;
            case 4:
                seleccionado[0] = "PIQUE";
                seleccionado[1] = "EX DE SHAKIRA";
                break;
            case 5:
                seleccionado[0] = "YATRA";
                seleccionado[1] = "Mi pedazo de Sol, la niña de mis ojos";
                break;
            case 6:
                seleccionado[0] = "RUBEN";
                seleccionado[1] = "Profesor de SGE";
                break;
            case 7:
                seleccionado[0] = "JESUS";
                seleccionado[1] = "Profesor de Bases de Datos";
                break;
        }

        return seleccionado;
    }


    private ArrayList<String> getPanelConLetra() {
        String LETRA = "letra";

        for (int i = 0; i < letra.length; i++) {
            letrasMarcadas.add(LETRA + letra[i]);
        }
        return letrasMarcadas;
    }

    public boolean analizarLetra(String letra) {
        if (letra.isEmpty() || letra == null) {
            return false;
        }
        boolean aux = true;
        if (copias(letrasUtilizadas, letra)) {
            return false;
        }
        letrasUtilizadas.add(letra);
        if (!ExisteLetramiPalabra(this.getPalabra(), letra)) {
            return false;
        }
        return true;
    }


    public void pintarLetra(String Vocal) {
        String minusVocal = Vocal.toLowerCase();
        String letraMarcada =  "letra" + minusVocal;
        ArrayList<String> id = getPanelConLetra();
        int codigoImagen;
        for (int i = 0; i < id.size(); i++) {
            if (id.get(i).equals(letraMarcada)) {

                codigoImagen = getDrawableId(id.get(i));
                if (letrasAsignadas.get(minusVocal.toUpperCase()).size() > 1) {
                    for (int j = 0; j < letrasAsignadas.get(Vocal.toUpperCase()).size(); j++) {
                        Integer numero = letrasAsignadas.get(Vocal.toUpperCase()).get(j);
                        cuadros[numero].setBackgroundResource(codigoImagen);
                        encuentros = letrasAsignadas.get(minusVocal.toUpperCase()).size();
                    }

                } else {
                    Integer numero = letrasAsignadas.get(Vocal.toUpperCase()).get(0);
                    cuadros[numero].setBackgroundResource(codigoImagen);
                    encuentros = letrasAsignadas.get(minusVocal.toUpperCase()).size();
                }

            }
        }

    }

    public void pintarPalabra(){
        String palabra  = quitarLetrasRepetidasDelNombre(getPalabra());
        for (int i = 0; i < palabra.length(); i++) {
            pintarLetra(String.valueOf(palabra.charAt(i)));
        }

    }

    public int getDrawableId(String name) {
        Field f = null;
        int id = 0;

        try {
            f = R.drawable.class.getField(name);
            id = f.getInt(null);
        } catch (NoSuchFieldException e) {
            Log.i("TAG", "Missing drawable " + name);
        } catch (IllegalAccessException e) {
            Log.i("TAG", "Illegal access to field " + name);
        }

        return id;
    }

    public void limpiarValoreStaticos() {
        letrasAsignadas = new HashMap<>();
        coincidencias = 0;
        letrasUtilizadas = new ArrayList<>();

    }

    private boolean copias(ArrayList<String> e, String letra) {
        boolean flag = false;
        if (e.size() == 0 || e == null) {
            return flag;
        }

        for (int i = 0; i < e.size(); i++) {
            if (e.get(i).equals(letra)) {
                flag = true;
                return flag;
            }
        }

        return flag;
    }


    private boolean ExisteLetramiPalabra(String nombre, String letrapequenha) {
        String nombre_sin_letras_repetidas = quitarLetrasRepetidasDelNombre(nombre.toLowerCase());

        char[] letras = new char [nombre_sin_letras_repetidas.length()];
        for (int i = 0; i < nombre_sin_letras_repetidas.length(); i++) {
            letras[i] = nombre_sin_letras_repetidas.charAt(i);
        }

        for (int i = 0; i < letras.length; i++) {
            if (letras[i] == letrapequenha.charAt(0)) {
                return true;
            }
        }

        return false;
    }

    // Método auxiliar que quita las letras repetidas del nombre
    private String quitarLetrasRepetidasDelNombre(String e) {
        StringBuilder sinRepetir = new StringBuilder();
        for (int i = 0; i < e.length(); i++) {
            String si = e.substring(i, i + 1);
            if (sinRepetir.indexOf(si) == -1) {
                sinRepetir.append(si);
            }
        }
        return sinRepetir.toString();
    }

    private boolean palabraHasCustomAudio(String entrada) {
        String entrada_en_minusculas = entrada.toLowerCase();
        Field[] f = null;
        f = R.raw.class.getFields();
        for (int i = 0; i < f.length; i++) {
            if (f[i].getName().equals(entrada_en_minusculas)) {
                return true;
            }
        }

        return false;
    }
}

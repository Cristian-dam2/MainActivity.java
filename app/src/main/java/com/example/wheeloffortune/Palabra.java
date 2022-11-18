package com.example.wheeloffortune;

import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Palabra extends AppCompatActivity {
    private String palabra;
    private TextView[] cuadros;
    private String informacion;
    private static int posicion_inicial;
    ArrayList<String> letrasMarcadas = new ArrayList<>();
    private static TextView[] letrasGeneradas = new TextView[27];
    public static HashMap<String, List<Integer>> letrasAsignadas = new HashMap<>();
    public static int coincidencias = 0;
    public static ArrayList<String> letrasUtilizadas = new ArrayList<>();
    public static  String[] letra = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "y", "z"};

    public static int getPosicion_inicial() {
        return posicion_inicial;
    }

    public static void setPosicion_inicial(int posicion_inicial) {
        Palabra.posicion_inicial = posicion_inicial;
    }

    public ArrayList<String> getLetrasMarcadas() {
        return letrasMarcadas;
    }

    public void setLetrasMarcadas(ArrayList<String> letrasMarcadas) {
        this.letrasMarcadas = letrasMarcadas;
    }

    public static TextView[] getLetrasGeneradas() {
        return letrasGeneradas;
    }

    public static void setLetrasGeneradas(TextView[] letrasGeneradas) {
        Palabra.letrasGeneradas = letrasGeneradas;
    }

    public static HashMap<String, List<Integer>> getLetrasAsignadas() {
        return letrasAsignadas;
    }

    public static void setLetrasAsignadas(HashMap<String, List<Integer>> letrasAsignadas) {
        Palabra.letrasAsignadas = letrasAsignadas;
    }

    public static int getCoincidencias() {
        return coincidencias;
    }

    public static void setCoincidencias(int coincidencias) {
        Palabra.coincidencias = coincidencias;
    }

    public static ArrayList<String> getLetrasUtilizadas() {
        return letrasUtilizadas;
    }

    public static void setLetrasUtilizadas(ArrayList<String> letrasUtilizadas) {
        Palabra.letrasUtilizadas = letrasUtilizadas;
    }

    public static String[] getLetra() {
        return letra;
    }

    public static void setLetra(String[] letra) {
        Palabra.letra = letra;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public TextView[] getCuadros() {
        return cuadros;
    }

    public void setCuadros(TextView[] cuadros) {
        this.cuadros = cuadros;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public Palabra(TextView[] cuadros) {

        String[] adivinar = palabraseInformacion();
        this.palabra = adivinar[0];
        this.cuadros = cuadros;
        this.informacion = adivinar[1];

        this.posicion_inicial = (cuadros.length / 2) - (palabra.length() / 2);
        for (int i = this.posicion_inicial; i < this.posicion_inicial + palabra.length(); i++) {
            cuadros[i].setBackgroundResource(R.drawable.letra_sindescifrar);

        }
        for (int i = 0; i < palabra.length(); i++) {
            String letra = String.valueOf(palabra.charAt(i));
            System.out.println(letra);

            if (letrasAsignadas.containsKey(letra)) {
                letrasAsignadas.get(letra).add(this.posicion_inicial);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(this.posicion_inicial);
                letrasAsignadas.put(letra, list);
            }
            this.posicion_inicial++;
            System.out.println(letrasAsignadas);


        }

        System.out.println(letrasAsignadas);


    }

    public String[] palabraseInformacion() {
        String[] seleccionado = new String[2];

            int a = (int) ((Math.random() * (7 - 1)) + 1);

        switch (a) {
            case 1:
                seleccionado[0] = "IVAN";
                seleccionado[1] = "Profesor de Android";
                return seleccionado;

            case 2:
                seleccionado[0] = "VICENTE";
                seleccionado[1] = "Profesor de Sistemas informaticos";
                return seleccionado;
            case 3:
                seleccionado[0] = "SONIC";
                seleccionado[1] = "Erizo azul";
                return seleccionado;
            case 4:
                seleccionado[0] = "PIQUE";
                seleccionado[1] = "EX DE SHAKIRA";
                return seleccionado;
            case 5:
                seleccionado[0] = "YATRA";
                seleccionado[1] = "Mi pedazo de Sol, la niña de mis ojos";
                return seleccionado;
            case 6:
                seleccionado[0] = "RUBEN";
                seleccionado[1] = "Profesor de SGE";
                return seleccionado;
            case 7:
                seleccionado[0] = "JESUS";
                seleccionado[1] = "Profesor de Bases de Datos";
                return seleccionado;


        }

        return seleccionado;
    }


    private ArrayList<String> generadorCartasMarcadas() {
        String empieza = "letra";

        for (int i = 0; i < letra.length; i++) {
            letrasMarcadas.add(empieza + letra[i]);
        }
        return letrasMarcadas;

    }

    public boolean mostrarLetra(String Vocal) {
        boolean aux = false;
        String minusVocal = Vocal;
        String letraMarcada = "letra" + Vocal;
        int temp;
        int contador = 0;
        ArrayList<String> id = generadorCartasMarcadas();
        if(copias(letrasUtilizadas,Vocal)){
            return false;
        }
        letrasUtilizadas.add(Vocal);

        if(ExisteLetramiPalabra(getPalabra(),Vocal)){

        }else{
            return false;
        }

        for (int i = 0; i < id.size(); i++) {
            if (id.get(i).equals(letraMarcada)) {
                aux = true;
                temp = getDrawableId(id.get(i));
                if (letrasAsignadas.get(minusVocal.toUpperCase()).size() > 1) {
                    for (int j = 0; j < letrasAsignadas.get(Vocal.toUpperCase()).size(); j++) {
                        Integer numero = letrasAsignadas.get(Vocal.toUpperCase()).get(j);
                        cuadros[numero].setBackgroundResource(temp);
                    }

                } else {
                    Integer numero = letrasAsignadas.get(Vocal.toUpperCase()).get(0);
                 //  letrasAsignadas.remove(Vocal.toUpperCase());
                    cuadros[numero].setBackgroundResource(temp);
                }

            }
        }
        return aux;



    }

    public int getDrawableId(String name) {
        Class<?> c = R.drawable.class;
        Field f = null;
        int id = 0;

        try {
            f = R.drawable.class.getField(name);
            id = f.getInt(null);
        } catch (NoSuchFieldException e) {
            Log.i("Reflection", "Missing drawable " + name);
        } catch (IllegalAccessException e) {
            Log.i("Reflection", "Illegal access to field " + name);
        }

        return id;
    }

    public void limpiarValoreStaticos() {
        letrasAsignadas = new HashMap<>();
        coincidencias = 0;
        letrasUtilizadas = new ArrayList<>();

    }

    private boolean copias(ArrayList<String> e , String letra){
        boolean flag = false;
        if(e.size() == 0 || e == null){
            return flag;
        }

        for (int i = 0; i < e.size(); i++) {
            if(e.get(i).equals(letra)){
                flag = true;
                return flag;
            }
        }

        return flag;
    }


    private boolean ExisteLetramiPalabra(String nombre, String letrapequenha){
        String namesinletrasRepetidas = quitarLetrasRepetidasDelNombre(nombre.toLowerCase());

        char [] letras = new char[namesinletrasRepetidas.length()];
        for (int i = 0; i < namesinletrasRepetidas.length(); i++) {
            letras[i] = namesinletrasRepetidas.charAt(i);
        }

        for (int i = 0; i < letras.length; i++) {
            if(letras[i] == letrapequenha.charAt(0)){
                System.out.println(letras[i]);
                return true;
            }
        }


        return false;


    }


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





}

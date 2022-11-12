package com.example.wheeloffortune;

import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Palabra extends AppCompatActivity {
    private String palabra;
    private TextView[] cuadros;
    private String informacion;
    private static int posicion_inicial;
    ArrayList<String> letrasMarcadas = new ArrayList<>();
    private static TextView[] letrasGeneradas = new TextView[27];
    public static HashMap<String, List<Integer>> letrasAsignadas = new HashMap<>();
    public static int coincidencias = 0;
    public static ArrayList<String> vocales = new ArrayList<>();


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

        int longidudpalabra = palabra.length();
        int longitudmatriz = cuadros.length;
        int mitadlongitudmatriz = longitudmatriz / 2;
        int mitadlongitudpalabra = longidudpalabra / 2;
        int principio = mitadlongitudmatriz - mitadlongitudpalabra;
        posicion_inicial = principio;


        for (int i = principio; i < principio + longidudpalabra; i++) {
            cuadros[i].setBackgroundResource(R.drawable.letra_sindescifrar);

        }
        for (int i = 0; i < palabra.length(); i++) {
            String letra = String.valueOf(palabra.charAt(i));
            System.out.println(letra);

            if (letrasAsignadas.containsKey(letra)) {
                letrasAsignadas.get(letra).add(principio);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(principio);
                letrasAsignadas.put(letra, list);
            }
            principio++;
            System.out.println(letrasAsignadas);


        }

        System.out.println(letrasAsignadas);


    }

    public String[] palabraseInformacion() {
        String[] seleccionado = new String[2];
        //(5 es el num MAX, -1 el num MIN +1) +1
        int random = new Random().nextInt(5 - 1 + 1) + 1;

        switch (random) {
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
            case 4:
                seleccionado[0] = "PIQUE";
                seleccionado[1] = "EX DE SHAKIRA";
            case 5:
                seleccionado[0] = "YATRA";
                seleccionado[1] = "Mi pedazo de Sol, la niña de mis ojos";
        }

        return seleccionado;
    }


    private ArrayList<String> generadorCartasMarcadas() {
        String empieza = "letra";
        String[] letra = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "y", "z"};
        for (int i = 0; i < letra.length; i++) {
            letrasMarcadas.add(empieza + letra[i]);
        }
        return letrasMarcadas;

    }

    public boolean mostrarLetra(String Vocal) {
        boolean aux = false;
        // SI NO RECOJO ENTRADA EN ENTRADA2 EXPLOTA AL INTRODUCIR POR TECLADO UNA LETRA EN MAYUSCULA
        String minusVocal = Vocal;
        String letraMarcada = "letra" + Vocal;
        int temp;
        ArrayList<String> id = generadorCartasMarcadas();
        if(copias(vocales,Vocal)){
            return false;
        }
        vocales.add(Vocal);


        int qwerty = 0;
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
                    letrasAsignadas.remove(Vocal.toUpperCase());
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
        letrasAsignadas.clear();
        coincidencias = 0;
        vocales.clear();
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

}

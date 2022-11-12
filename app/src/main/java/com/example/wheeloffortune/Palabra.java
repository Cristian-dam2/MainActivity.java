package com.example.wheeloffortune;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Palabra extends AppCompatActivity {
    private String palabra;
    private TextView [] cuadros;
    private String informacion;
    private static int posicion_inicial;
    ArrayList<String> letrasMarcadas = new ArrayList<>();
    private static TextView[] letrasGeneradas = new TextView[27];
    public static HashMap<String, List<Integer>> cartas = new HashMap<>();



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

    public Palabra( TextView[] cuadros) {

        String [] adivinar = palabraseInformacion();
        this.palabra = adivinar[0];
        this.cuadros = cuadros;
        this.informacion = adivinar[1];

        int longidudpalabra = palabra.length();
        int longitudmatriz = cuadros.length;
        int mitadlongitudmatriz = longitudmatriz/2;
        int mitadlongitudpalabra = longidudpalabra/2;
        int principio= mitadlongitudmatriz - mitadlongitudpalabra;
        posicion_inicial = principio;
        int contador = 0;

        for (int i = principio; i < principio+longidudpalabra; i++) {
            cuadros[i].setBackgroundResource(R.drawable.letra_sindescifrar);

        }
        for (int i = 0; i < palabra.length(); i++) {
            String letra = String.valueOf(palabra.charAt(i));
            System.out.println(letra);

            if(cartas.containsKey(letra)){
                cartas.get(letra).add(principio);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(principio);
                cartas.put(letra, list);
            }
            principio++;
            System.out.println(cartas);


        }

        System.out.println(cartas);



    }

    public String [] palabraseInformacion(){
        String [] seleccionado = new String[2];
        //(3 es el num MAX, 1 el num MIN +1) +1
        int random = new Random().nextInt(3-1+1)+1;
        int a = 1;
        switch (a){
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
        }

        return seleccionado;
    }

    public boolean existealgunaLetra(String entrada){
        boolean acierto = true;
        int contador =0;
        for (int i = 0; i < palabra.length(); i++) {
            if(entrada.charAt(0) == palabra.charAt(i)){
                mostraLetra( entrada);

            }
            contador ++;
        }

     return acierto;
    }

    private ArrayList<String> cmultiples(){
        String empieza = "letra";
        String [] letra= {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","y","z"};
        for (int i = 0; i < letra.length; i++) {
            letrasMarcadas.add(empieza + letra[i]);
        }
        return letrasMarcadas;

    }

    public boolean mostraLetra(String entrada) {
        boolean aux = false;
        // SI NO RECOJO ENTRADA EN ENTRADA2 EXPLOTA AL INTRODUCIR POR TECLADO UNA LETRA EN MAYUSCULA
        String entrada2 = entrada;
        String ayuda = "letra" + entrada;
        int temp;
        ArrayList<String> id = cmultiples();
        for (int i = 0; i < id.size(); i++) {
            if(id.get(i).equals(ayuda)){
                aux = true;
                temp = getDrawableId(id.get(i));

                if(cartas.get(entrada2.toUpperCase()).size() >1){
                    for (int j = 0; j < cartas.get(entrada.toUpperCase()).size(); j++) {
                        Integer numero = cartas.get(entrada.toUpperCase()).get(j);
                        cuadros[numero].setBackgroundResource(temp);
                    }
                }else{
                    Integer numero = cartas.get(entrada.toUpperCase()).get(0);
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




}

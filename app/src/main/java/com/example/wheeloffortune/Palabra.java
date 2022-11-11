package com.example.wheeloffortune;

import android.widget.TextView;

import java.util.HashMap;

public class Palabra {
    private String palabra;
    private TextView [] cuadros;
    private String informacion;

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

    public Palabra(String palabra, TextView[] cuadros, String informacion) {
        this.palabra = palabra;
        this.cuadros = cuadros;
        this.informacion = informacion;

        HashMap<String,Object> mapeo  = new HashMap <String, Object> ();

        for (int i = 10; i < 17; i++) {
            cuadros[i].setBackgroundResource(R.drawable.letra_sindescifrar);
        }




    }
}

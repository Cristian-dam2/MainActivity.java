package com.example.wheeloffortune.Auxiliares;

import android.graphics.Bitmap;

public class JugadorModelo {
    private Bitmap imagen;
    private String nombre;
    private String puntuacion;

    public JugadorModelo(Bitmap imagen, String usuario) {
        this.imagen = imagen;
        String info = "";
        for (int i = 0; i < usuario.length(); i++) {
            if(usuario.charAt(i) != '$'){
                info = info + usuario.charAt(i);
            }else{
                this.nombre = info;
                info = "";
            }
        }
        this.puntuacion = info;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }
}

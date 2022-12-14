package com.example.wheeloffortune;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Datos extends RecyclerView.Adapter<Datos.ViewHolderDatos> {
    private ArrayList<Jugador> jugadores;

    public Datos(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    @NonNull
    @Override
    public Datos.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderDatos(LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_lista_jugadores,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Datos.ViewHolderDatos holder, int position) {
        holder.formatearDatos(jugadores.get(position));
    }

    @Override
    public int getItemCount() {
        return jugadores.size();
    }


    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView datos;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            datos = (TextView) itemView.findViewById(R.id.idDato);
        }
        public void formatearDatos(Jugador jugador) {
             datos.setText(jugador.getNombre() + " _____________________ " + jugador.getPuntuacion());
        }
    }
}

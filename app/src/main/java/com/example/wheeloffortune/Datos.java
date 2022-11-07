package com.example.wheeloffortune;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Datos extends RecyclerView.Adapter<Datos.ViewHolderDatos> {
    ArrayList<Jugador> jugadores;

    public Datos(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    @NonNull
    @Override
    public Datos.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_lista_jugadores,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Datos.ViewHolderDatos holder, int position) {

    }

    @Override
    public int getItemCount() {
        return jugadores.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView datos;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            datos = itemView.findViewById(R.id.textViewPrueba);
        }
    }
}

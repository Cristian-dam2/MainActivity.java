package com.example.wheeloffortune.Auxiliares;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wheeloffortune.R;

import java.util.List;

public class JugadorAdaptador extends RecyclerView.Adapter<JugadorAdaptador.JugadorViewHolder> {
    private List<JugadorModelo> mJugadorList;

    public JugadorAdaptador(List<JugadorModelo> jugadorList) {
        mJugadorList = jugadorList;
    }

    @NonNull
    @Override
    public JugadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_lista_jugadores, parent, false);
        return new JugadorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JugadorViewHolder holder, int position) {
        JugadorModelo jugador = mJugadorList.get(position);
        holder.mImagen.setImageBitmap(jugador.getImagen());
        holder.mNombre.setText(jugador.getNombre());
        holder.mPuntuacion.setText(jugador.getPuntuacion());
    }

    @Override
    public int getItemCount() {
        return mJugadorList.size();
    }

    public class JugadorViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImagen;
        public TextView mNombre;
        public TextView mPuntuacion;

        public JugadorViewHolder(View itemView) {
            super(itemView);
            mImagen = itemView.findViewById(R.id.image_view);
            mNombre = itemView.findViewById(R.id.player_name);
            mPuntuacion = itemView.findViewById(R.id.player_score);
        }
    }
}

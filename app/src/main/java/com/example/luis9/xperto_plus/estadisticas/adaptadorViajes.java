package com.example.luis9.xperto_plus.estadisticas;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luis9.xpertp.R;

import java.util.ArrayList;

public class adaptadorViajes extends RecyclerView.Adapter<adaptadorViajes.ViewHolderTarjetas> {

    ArrayList<variablesBoViajes> listDatos;

    public adaptadorViajes(ArrayList<variablesBoViajes> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderTarjetas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_estadisticas_viajes,null,false);
        return new ViewHolderTarjetas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTarjetas holder, int position) {
        holder.fecha.setText("Fecha: " + listDatos.get(position).getFecha());
        holder.hora.setText("Hora: " + listDatos.get(position).getHora());
        holder.horaFin.setText("Hora fin: " + listDatos.get(position).getHoraFin());
        holder.duracion.setText("Duración: " + listDatos.get(position).getDuracion());
        holder.velocidad.setText("Velocidad: " + listDatos.get(position).getVelocidad());
        holder.ritmo.setText("Ritmo: " + listDatos.get(position).getRitmo());
        holder.respiracion.setText("Respiración: " + listDatos.get(position).getRespiracion());
        holder.presion.setText("Presión: " + listDatos.get(position).getPresion());
        holder.fNormal.setText("Fatiga normal: " + listDatos.get(position).getfNormal());
        holder.fCansado.setText("Fatiga cansado: " + listDatos.get(position).getfCansado());
        holder.fMuy.setText("Muy cansado: " + listDatos.get(position).getfMuy());
        holder.mCalmado.setText("Calmado: " + listDatos.get(position).getmCalmado());
        holder.mExcite.setText("Emocionado: " + listDatos.get(position).getmExcite());
        holder.mDepri.setText("Deprimido: " + listDatos.get(position).getmDepri());
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderTarjetas extends RecyclerView.ViewHolder {

        TextView fecha,hora,duracion,velocidad,ritmo,respiracion,presion,fNormal,fCansado,fMuy,mCalmado,mExcite,mDepri,horaFin;

        public ViewHolderTarjetas(View itemView) {
            super(itemView);
            fecha = itemView.findViewById(R.id.fecha);
            hora = itemView.findViewById(R.id.hora);
            horaFin = itemView.findViewById(R.id.horaFin);
            duracion = itemView.findViewById(R.id.duracion);
            velocidad = itemView.findViewById(R.id.velocidad);
            ritmo = itemView.findViewById(R.id.ritmo);
            respiracion = itemView.findViewById(R.id.respiracion);
            presion = itemView.findViewById(R.id.presion);
            fNormal = itemView.findViewById(R.id.fNormal);
            fCansado = itemView.findViewById(R.id.fCansado);
            fMuy = itemView.findViewById(R.id.fMuy);
            mCalmado = itemView.findViewById(R.id.mCalmado);
            mExcite = itemView.findViewById(R.id.mExcite);
            mDepri = itemView.findViewById(R.id.mDepri);

        }

    }
}

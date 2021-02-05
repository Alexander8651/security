package com.example.myapplication;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InformeAdapter extends RecyclerView.Adapter<InformeAdapter.ViewHolderInforme> {

    ArrayList<Informe> informes;

    public InformeAdapter(ArrayList<Informe> informes) {
        this.informes = informes;
    }

    @NonNull
    @Override
    public ViewHolderInforme onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteminforme, parent, false);
        return new ViewHolderInforme(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInforme holder, int position) {
        final Informe informe = informes.get(position);
        holder.prioridad.setText(informe.getPriority());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(informe.getDate()));
        Log.d("pepepepe", dateString);

        holder.fecha.setText(dateString);

        holder.itemView.setOnClickListener(v ->{
            Intent intent = new Intent(holder.itemView.getContext(), DetalleInformeActivity.class);
            intent.putExtra("informe", informe);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return informes.size();
    }

    public class ViewHolderInforme extends RecyclerView.ViewHolder{

        TextView prioridad;
        TextView fecha;

        public ViewHolderInforme(@NonNull View itemView) {
            super(itemView);

            prioridad = itemView.findViewById(R.id.prioridad);
            fecha = itemView.findViewById(R.id.fecha);
        }
    }
}

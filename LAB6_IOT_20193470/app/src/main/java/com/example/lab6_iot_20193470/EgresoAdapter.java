package com.example.lab6_iot_20193470;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EgresoAdapter extends RecyclerView.Adapter<EgresoAdapter.EgresoViewHolder> {

    private List<Egreso> egresos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Egreso egreso);
        void onDeleteClick(Egreso egreso);
        void onDetalleClick(Egreso egreso);
    }

    public EgresoAdapter(List<Egreso> egresos, OnItemClickListener listener) {
        this.egresos = egresos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EgresoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.egreso_item, parent, false);
        return new EgresoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EgresoViewHolder holder, int position) {
        Egreso egreso = egresos.get(position);
        holder.titulo.setText(egreso.getTitulo());
        holder.fecha.setText(egreso.getFecha());
        holder.monto.setText(String.valueOf(egreso.getMonto()));
        //holder.editButton.setOnClickListener(v -> listener.onEditClick(egreso));
        //holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(egreso));
        holder.detalleButton.setOnClickListener(v -> listener.onDetalleClick(egreso));
    }

    @Override
    public int getItemCount() {
        return egresos.size();
    }

    public static class EgresoViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo, fecha, monto;
        //public Button editButton, deleteButton;
        public Button detalleButton;

        public EgresoViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.egreso_titulo);
            fecha = itemView.findViewById(R.id.egreso_fecha);
            monto = itemView.findViewById(R.id.egreso_monto);
            //editButton = itemView.findViewById(R.id.btn_edit_egreso);
            //deleteButton = itemView.findViewById(R.id.btn_delete_egreso);
            detalleButton = itemView.findViewById(R.id.btn_detalle_egreso);
        }
    }
}



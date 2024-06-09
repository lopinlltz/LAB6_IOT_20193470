package com.example.lab6_iot_20193470;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngresoAdapter extends RecyclerView.Adapter<IngresoAdapter.IngresoViewHolder> {

    private List<Ingreso> ingresos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Ingreso ingreso);
        void onDeleteClick(Ingreso ingreso);
    }

    public IngresoAdapter(List<Ingreso> ingresos, OnItemClickListener listener) {
        this.ingresos = ingresos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngresoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingreso_item, parent, false);
        return new IngresoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngresoViewHolder holder, int position) {
        Ingreso ingreso = ingresos.get(position);
        holder.titulo.setText(ingreso.getTitulo());
        holder.fecha.setText(ingreso.getFecha());
        holder.monto.setText(String.valueOf(ingreso.getMonto()));
        holder.editButton.setOnClickListener(v -> listener.onEditClick(ingreso));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(ingreso));
    }

    @Override
    public int getItemCount() {
        return ingresos.size();
    }

    public static class IngresoViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo, fecha, monto;
        public Button editButton, deleteButton;

        public IngresoViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.ingreso_titulo);
            fecha = itemView.findViewById(R.id.ingreso_fecha);
            monto = itemView.findViewById(R.id.ingreso_monto);
            editButton = itemView.findViewById(R.id.btn_edit_ingreso);
            deleteButton = itemView.findViewById(R.id.btn_delete_ingreso);
        }
    }
}


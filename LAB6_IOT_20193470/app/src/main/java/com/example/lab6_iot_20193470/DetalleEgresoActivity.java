package com.example.lab6_iot_20193470;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetalleEgresoActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String egresoId;
    private TextView tituloTextView, fechaTextView, montoTextView, descripcionTextView;
    private Button editarButton, eliminarButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_egreso);

        db = FirebaseFirestore.getInstance();
        egresoId = getIntent().getStringExtra("egresoId");

        tituloTextView = findViewById(R.id.text_view_titulo);
        fechaTextView = findViewById(R.id.text_view_fecha);
        montoTextView = findViewById(R.id.text_view_monto);
        descripcionTextView = findViewById(R.id.text_view_descripcion);
        editarButton = findViewById(R.id.button_editar);
        eliminarButton = findViewById(R.id.button_eliminar);

        cargarDetallesEgreso();

        editarButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleEgresoActivity.this, EditarEgresoActivity.class);
            intent.putExtra("egresoId", egresoId);
            startActivity(intent);
        });

        eliminarButton.setOnClickListener(v -> mostrarDialogoConfirmacion());
    }

    private void cargarDetallesEgreso() {
        db.collection("egresos").document(egresoId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Egreso egreso = task.getResult().toObject(Egreso.class);
                if (egreso != null) {
                    tituloTextView.setText(egreso.getTitulo());
                    fechaTextView.setText(egreso.getFecha());
                    montoTextView.setText(String.valueOf(egreso.getMonto()));
                    descripcionTextView.setText(egreso.getDescripcion());
                }
            } else {
            }
        });
    }

    private void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Estás seguro de querer borrar este egreso?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrarEgreso();
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void borrarEgreso() {
        db.collection("egresos").document(egresoId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
            } else {
            }
        });
    }
}

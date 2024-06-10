package com.example.lab6_iot_20193470;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class DetalleIngresoActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String ingresoId;
    private TextView tituloTextView, fechaTextView, montoTextView, descripcionTextView;
    private Button editarButton, eliminarButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_ingreso);

        db = FirebaseFirestore.getInstance();
        ingresoId = getIntent().getStringExtra("ingresoId");

        tituloTextView = findViewById(R.id.text_view_titulo);
        fechaTextView = findViewById(R.id.text_view_fecha);
        montoTextView = findViewById(R.id.text_view_monto);
        descripcionTextView = findViewById(R.id.text_view_descripcion);
        editarButton = findViewById(R.id.button_editar);
        eliminarButton = findViewById(R.id.button_eliminar);

        cargarDetallesIngreso();

        editarButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleIngresoActivity.this, EditarIngresoActivity.class);
            intent.putExtra("ingresoId", ingresoId);
            startActivity(intent);
        });

        eliminarButton.setOnClickListener(v -> mostrarDialogoConfirmacion());
    }

    private void cargarDetallesIngreso() {
        db.collection("ingresos").document(ingresoId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Ingreso ingreso = task.getResult().toObject(Ingreso.class);
                if (ingreso != null) {
                    tituloTextView.setText(ingreso.getTitulo());
                    fechaTextView.setText(ingreso.getFecha());
                    montoTextView.setText(String.valueOf(ingreso.getMonto()));
                    descripcionTextView.setText(ingreso.getDescripcion());
                }
            } else {
            }
        });
    }

    private void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Estás seguro de querer borrar este ingreso?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrarIngreso();
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void borrarIngreso() {
        db.collection("ingresos").document(ingresoId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
            } else {
            }
        });
    }
}

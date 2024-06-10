package com.example.lab6_iot_20193470;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditarEgresoActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String egresoId;
    private TextView tituloTextView, fechaTextView;
    private EditText montoEditText, descripcionEditText;
    private Button guardarButton, cancelarButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_egreso_form);

        db = FirebaseFirestore.getInstance();
        egresoId = getIntent().getStringExtra("egresoId");

        tituloTextView = findViewById(R.id.text_view_titulo);
        fechaTextView = findViewById(R.id.text_view_fecha);
        montoEditText = findViewById(R.id.edit_text_monto);
        descripcionEditText = findViewById(R.id.edit_text_descripcion);
        guardarButton = findViewById(R.id.button_guardar);
        cancelarButton = findViewById(R.id.button_cancelar);

        cargarDetallesEgreso();

        guardarButton.setOnClickListener(v -> guardarCambios());
        cancelarButton.setOnClickListener(v -> cancelarEdicion());
    }

    private void cargarDetallesEgreso() {
        db.collection("egresos").document(egresoId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Egreso egreso = task.getResult().toObject(Egreso.class);
                if (egreso != null) {
                    tituloTextView.setText(egreso.getTitulo());
                    fechaTextView.setText(egreso.getFecha());
                    montoEditText.setText(String.valueOf(egreso.getMonto()));
                    descripcionEditText.setText(egreso.getDescripcion());
                }
            } else {
            }
        });
    }

    private void guardarCambios() {
        double nuevoMonto = Double.parseDouble(montoEditText.getText().toString());
        String nuevaDescripcion = descripcionEditText.getText().toString();

        db.collection("egresos").document(egresoId).update("monto", nuevoMonto, "descripcion", nuevaDescripcion)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(EditarEgresoActivity.this, MainActivity.class);
                        intent.putExtra("fragment", "egresos");
                        startActivity(intent);
                        //finish();
                    } else {
                    }
                });
    }

    private void cancelarEdicion() {
        Intent intent = new Intent(EditarEgresoActivity.this, MainActivity.class);
        intent.putExtra("fragment", "egresos");
        startActivity(intent);
    }
}

package com.example.lab6_iot_20193470;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditarIngresoActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String ingresoId;
    private TextView tituloTextView, fechaTextView;
    private EditText montoEditText, descripcionEditText;
    private Button guardarButton, cancelarButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_ingreso_form);

        db = FirebaseFirestore.getInstance();
        ingresoId = getIntent().getStringExtra("ingresoId");

        tituloTextView = findViewById(R.id.text_view_titulo);
        fechaTextView = findViewById(R.id.text_view_fecha);
        montoEditText = findViewById(R.id.edit_text_monto);
        descripcionEditText = findViewById(R.id.edit_text_descripcion);
        guardarButton = findViewById(R.id.button_guardar);
        cancelarButton = findViewById(R.id.button_cancelar);

        cargarDetallesIngreso();

        guardarButton.setOnClickListener(v -> guardarCambios());
        cancelarButton.setOnClickListener(v -> cancelarEdicion());
    }

    private void cargarDetallesIngreso() {
        db.collection("ingresos").document(ingresoId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Ingreso ingreso = task.getResult().toObject(Ingreso.class);
                if (ingreso != null) {
                    tituloTextView.setText(ingreso.getTitulo());
                    fechaTextView.setText(ingreso.getFecha());
                    montoEditText.setText(String.valueOf(ingreso.getMonto()));
                    descripcionEditText.setText(ingreso.getDescripcion());
                }
            } else {
            }
        });
    }

    private void guardarCambios() {
        double nuevoMonto = Double.parseDouble(montoEditText.getText().toString());
        String nuevaDescripcion = descripcionEditText.getText().toString();

        db.collection("ingresos").document(ingresoId).update("monto", nuevoMonto, "descripcion", nuevaDescripcion)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(EditarIngresoActivity.this, MainActivity.class);
                        intent.putExtra("fragment", "ingresos");
                        startActivity(intent);
                        //finish();
                    } else {
                    }
                });
    }

    private void cancelarEdicion() {
        Intent intent = new Intent(EditarIngresoActivity.this, MainActivity.class);
        intent.putExtra("fragment", "ingresos");
        startActivity(intent);
    }
}

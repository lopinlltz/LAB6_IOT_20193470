package com.example.lab6_iot_20193470;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NuevoEgresoActivity extends AppCompatActivity {

    private EditText editTextTitulo, editTextMonto, editTextDescripcion;
    private Button buttonGuardar, buttonCancelar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_egreso_form);

        db = FirebaseFirestore.getInstance();

        editTextTitulo = findViewById(R.id.edit_text_titulo);
        editTextMonto = findViewById(R.id.edit_text_monto);
        editTextDescripcion = findViewById(R.id.edit_text_descripcion);
        buttonGuardar = findViewById(R.id.button_guardar);
        buttonCancelar = findViewById(R.id.button_cancelar);

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarEgreso();
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelar();
            }
        });
    }

    private void guardarEgreso() {
        String titulo = editTextTitulo.getText().toString();
        double monto = Double.parseDouble(editTextMonto.getText().toString());
        String descripcion = editTextDescripcion.getText().toString();
        String fecha = getCurrentDate();

        Egreso egreso = new Egreso(titulo, monto, descripcion, fecha);

        CollectionReference egresosRef = db.collection("egresos");

        egresosRef.add(egreso)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(NuevoEgresoActivity.this, "Egreso guardado", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(NuevoEgresoActivity.this, "Error al guardar el egreso", Toast.LENGTH_SHORT).show();
                    Log.e("Agregar egreso", "Error al guardar el egreso", e);
                });
    }

    private void cancelar() {
        finish();
    }

    //claude.ai para conseguir datos de la fecha
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
}
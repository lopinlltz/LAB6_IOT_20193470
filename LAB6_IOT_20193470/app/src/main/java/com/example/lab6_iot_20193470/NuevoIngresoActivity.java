package com.example.lab6_iot_20193470;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NuevoIngresoActivity extends AppCompatActivity {

    private EditText editTextTitulo, editTextMonto, editTextDescripcion;
    private Button buttonGuardar, buttonCancelar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_ingreso_form);

        db = FirebaseFirestore.getInstance();

        editTextTitulo = findViewById(R.id.edit_text_titulo);
        editTextMonto = findViewById(R.id.edit_text_monto);
        editTextDescripcion = findViewById(R.id.edit_text_descripcion);
        buttonGuardar = findViewById(R.id.button_guardar);
        buttonCancelar = findViewById(R.id.button_cancelar);

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarIngreso();
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelar();
            }
        });
    }

    private void guardarIngreso() {
        String titulo = editTextTitulo.getText().toString();
        double monto = Double.parseDouble(editTextMonto.getText().toString());
        String descripcion = editTextDescripcion.getText().toString();
        String fecha = getCurrentDate();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String usuarioId = user.getUid();
            Ingreso ingreso = new Ingreso(titulo, monto, descripcion, fecha, usuarioId);

            CollectionReference ingresosRef = db.collection("ingresos");

            ingresosRef.add(ingreso)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(NuevoIngresoActivity.this, "Ingreso guardado", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NuevoIngresoActivity.this, "Error al guardar el ingreso", Toast.LENGTH_SHORT).show();
                        Log.e("Agregar ingreso", "Error al guardar el ingreso", e);
                    });

        } else {
        }
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

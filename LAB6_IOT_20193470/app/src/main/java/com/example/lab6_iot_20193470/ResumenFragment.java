package com.example.lab6_iot_20193470;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Locale;

public class ResumenFragment extends Fragment {

    private TextView textViewIngresosTotales;
    private TextView textViewEgresosTotales;
    private TextView textViewSaldo;
    private Button monthPickerButton;
    private int selectedYear, selectedMonth;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public ResumenFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        textViewIngresosTotales = view.findViewById(R.id.text_view_ingresos_totales);
        textViewEgresosTotales = view.findViewById(R.id.text_view_egresos_totales);
        textViewSaldo = view.findViewById(R.id.text_view_saldo);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ingresosTotales();
        egresosTotales();

        // claude para usar el month picker
        monthPickerButton = view.findViewById(R.id.month_picker_button);
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        monthPickerButton.setOnClickListener(v -> showMonthPicker());

        return view;
    }

    private void showMonthPicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            selectedYear = year;
            selectedMonth = month;
        }, selectedYear, selectedMonth, calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void ingresosTotales() {
        db.collection("ingresos")
                .whereEqualTo("usuarioId", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double totalIngresos = 0.0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Ingreso ingreso = document.toObject(Ingreso.class);
                                totalIngresos += ingreso.getMonto();
                            }
                            textViewIngresosTotales.setText(String.format(Locale.getDefault(), "$%.2f", totalIngresos));
                            actualizarSaldo();
                        } else {
                        }
                    }
                });
    }

    private void egresosTotales() {
        db.collection("egresos")
                .whereEqualTo("usuarioId", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double totalEgresos = 0.0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Egreso egreso = document.toObject(Egreso.class);
                                totalEgresos += egreso.getMonto();
                            }
                            textViewEgresosTotales.setText(String.format(Locale.getDefault(), "$%.2f", totalEgresos));
                            actualizarSaldo();
                        } else {
                        }
                    }
                });
    }

    //chat gpt para el saldo y los formatos de ingresos y egresos
    private void actualizarSaldo() {

        String ingresosStr = textViewIngresosTotales.getText().toString().replace("$", "");
        String egresosStr = textViewEgresosTotales.getText().toString().replace("$", "");

        double ingresos = ingresosStr.isEmpty() ? 0.0 : Double.parseDouble(ingresosStr);
        double egresos = egresosStr.isEmpty() ? 0.0 : Double.parseDouble(egresosStr);
        double saldo = ingresos - egresos;
        textViewSaldo.setText(String.format(Locale.getDefault(), "$%.2f", saldo));
    }
}




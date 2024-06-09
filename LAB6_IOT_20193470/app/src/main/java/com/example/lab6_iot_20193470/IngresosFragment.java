package com.example.lab6_iot_20193470;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngresosFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Ingreso> listaIngresos;
    private IngresoAdapter adapter;
    private FirebaseFirestore db;
    private Map<String, Ingreso> ingresosMap;

    public IngresosFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingresos, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_ingresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button agregarIngreso = view.findViewById(R.id.btn_add_ingreso);
        agregarIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NuevoIngresoActivity.class);
                startActivity(intent);
            }
        });


        listaIngresos = new ArrayList<>();
        ingresosMap = new HashMap<>();
        adapter = new IngresoAdapter(listaIngresos, new IngresoAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Ingreso ingreso) {

            }

            @Override
            public void onDeleteClick(Ingreso ingreso) {
                mostrarDialogoConfirmacion(ingreso);
            }
        });
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        listarIngresos();

        return view;
    }

    private void listarIngresos() {

        db.collection("ingresos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Ingreso ingreso = document.toObject(Ingreso.class);
                                String id = document.getId();
                                listaIngresos.add(ingreso);
                                ingresosMap.put(id, ingreso);
                            }
                            adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        } else {
                        }
                    }
                });
    }

    private void mostrarDialogoConfirmacion(Ingreso ingreso) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Estás seguro de querer borrar este ingreso?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrarIngreso(ingreso);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void borrarIngreso(Ingreso ingreso) {
        String id = obtenerIdIngreso(ingreso);
        db.collection("ingresos").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listaIngresos.remove(ingreso);
                            adapter.notifyDataSetChanged();
                            ingresosMap.remove(id);
                        } else {
                        }
                    }
                });
    }

    private String obtenerIdIngreso(Ingreso ingreso) {
        for (Map.Entry<String, Ingreso> entry : ingresosMap.entrySet()) {
            if (entry.getValue().equals(ingreso)) {
                return entry.getKey();
            }
        }
        return null;
    }
}


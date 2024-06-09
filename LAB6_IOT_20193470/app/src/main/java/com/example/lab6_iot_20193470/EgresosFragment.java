package com.example.lab6_iot_20193470;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EgresosFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Egreso> listaEgresos;
    private EgresoAdapter adapter;
    private FirebaseFirestore db;
    private Map<String, Egreso> egresosMap;

    public EgresosFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_egresos, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_egresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button agregarEgreso = view.findViewById(R.id.btn_add_egreso);
        agregarEgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NuevoEgresoActivity.class);
                startActivity(intent);
            }
        });


        listaEgresos = new ArrayList<>();
        egresosMap = new HashMap<>();

        adapter = new EgresoAdapter(listaEgresos, new EgresoAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Egreso egreso) {

            }

            @Override
            public void onDeleteClick(Egreso egreso) {
                mostrarDialogoConfirmacion(egreso);
            }
        });
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        listarEgresos();


        return view;
    }

    private void listarEgresos() {

        db.collection("egresos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Egreso egreso = document.toObject(Egreso.class);
                                String id = document.getId();
                                listaEgresos.add(egreso);
                                egresosMap.put(id, egreso);
                            }
                            adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        } else {
                        }
                    }
                });
    }

    private void mostrarDialogoConfirmacion(Egreso egreso) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Estás seguro de querer borrar este egreso?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrarEgreso(egreso);
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

    private void borrarEgreso(Egreso egreso) {
        String id = obtenerIdEgreso(egreso);
        db.collection("egresos").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listaEgresos.remove(egreso);
                            adapter.notifyDataSetChanged();
                            egresosMap.remove(id);
                        } else {
                        }
                    }
                });
    }

    private String obtenerIdEgreso(Egreso egreso) {
        for (Map.Entry<String, Egreso> entry : egresosMap.entrySet()) {
            if (entry.getValue().equals(egreso)) {
                return entry.getKey();
            }
        }
        return null;
    }
}


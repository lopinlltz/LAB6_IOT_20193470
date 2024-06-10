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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


            }

            @Override
            public void onDetalleClick(Ingreso ingreso) {
                Intent intent = new Intent(getActivity(), DetalleIngresoActivity.class);
                intent.putExtra("ingresoId", obtenerIdIngreso(ingreso));
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        listarIngresos();

        return view;
    }

    private void listarIngresos() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userID = user.getUid();

            db.collection("ingresos")
                    .whereEqualTo("usuarioId", userID)
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
                                adapter.notifyDataSetChanged();
                            } else {
                            }
                        }
                    });
        } else {

        }
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


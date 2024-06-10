package com.example.lab6_iot_20193470;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {

            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("fragment")) {
                String fragmentName = intent.getStringExtra("fragment");
                if ("egresos".equals(fragmentName)) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EgresosFragment()).commit();
                } else if ("resumen".equals(fragmentName)) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ResumenFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IngresosFragment()).commit();
                }
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IngresosFragment()).commit();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_ingresos:
                        selectedFragment = new IngresosFragment();
                        break;
                    case R.id.navigation_egresos:
                        selectedFragment = new EgresosFragment();
                        break;
                    case R.id.navigation_resumen:
                        selectedFragment = new ResumenFragment();
                        break;
                    case R.id.navigation_logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        return true;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            };
}
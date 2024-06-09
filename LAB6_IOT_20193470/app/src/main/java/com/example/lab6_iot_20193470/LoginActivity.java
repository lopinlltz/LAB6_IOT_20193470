package com.example.lab6_iot_20193470;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab6_iot_20193470.databinding.ActivityLoginBinding;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private final static String TAG = "msg-test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()) {
            Log.d(TAG, "Firebase uid: " + currentUser.getUid());
            goToMainActivity();
        }

        binding.loginBtn.setOnClickListener(view -> {
            binding.loginBtn.setEnabled(false);

            AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.login)
                    .setGoogleButtonId(R.id.btn_login_google)
                    .setEmailButtonId(R.id.btn_login_mail)
                    .build();

            Intent intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setTheme(R.style.Base_Theme_LAB6_IOT_20193470)
                    .setIsSmartLockEnabled(false)
                    .setAuthMethodPickerLayout(authMethodPickerLayout)
                    .setLogo(R.drawable.pucplogo)
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build()
                    ))
                    .build();

            signInLauncher.launch(intent);
        });
    }

    ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        Log.d(TAG, "Firebase uid: " + user.getUid());
                        Log.d(TAG, "Display name: " + user.getDisplayName());
                        Log.d(TAG, "Email: " + user.getEmail());

                        user.reload().addOnCompleteListener(task -> {
                            if (user.isEmailVerified()) {
                                goToMainActivity();
                            } else {
                                user.sendEmailVerification().addOnCompleteListener(task2 -> {
                                    Toast.makeText(LoginActivity.this, "Se le ha enviado un correo para validar su cuenta", Toast.LENGTH_LONG).show();
                                });
                            }
                        });
                    } else {
                        Log.d(TAG, "user == null");
                    }
                } else {
                    Log.d(TAG, "Canceló el Log-in");
                }
                binding.loginBtn.setEnabled(true);
            }
    );

    public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
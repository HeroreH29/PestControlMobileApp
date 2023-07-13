package com.example.edpestcontrolservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout email, password;
    private Button signIn;

    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.etLogInEmail);
        password = findViewById(R.id.etLogInPassword);
        signIn = findViewById(R.id.btnSignIn);

        auth = FirebaseAuth.getInstance();
    }

    public void checkLogIn(View view) {

        try {
            String em;

            em = email.getEditText().getText().toString();

            if (em.isEmpty()) {
                email.setError("Field cannot be empty");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
                email.setError("Please enter a valid email address");
            } else {
                email.setError(null);
            }

            String pass;

            pass = password.getEditText().getText().toString().trim();

            if (pass.isEmpty()) {
                password.setError("Field cannot be empty");
            } else if (pass.length() > 20) {
                password.setError("Password exceeds the limit");
            } else if (pass.length() <= 6) {
                password.setError("Password too short");
            } else {
                password.setError(null);
            }

            if (!em.isEmpty() && !pass.isEmpty()) {
                userLogIn(em, pass);
            }


        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void userLogIn(String em, String pass) {
        auth.signInWithEmailAndPassword(em, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Sign in success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MainInterface.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Username/Password do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createAccount(View view) {
        try {
            startActivity(new Intent(this, UserRegister.class));

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    //Forgot Password
    public void forgotPassword(View view) {
        startActivity(new Intent(this, ForgotPassword.class));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(MainActivity.this, MainInterface.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
package com.example.edpestcontrolservices;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserRegister extends AppCompatActivity {

    private TextInputLayout emailAddress, password, confirmPassword;
    private Button proceed;
    private FirebaseAuth auth;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{7,20}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //Text Inputs
        emailAddress = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);

        //Button
        proceed = findViewById(R.id.btnProceed);

    }

    public void registerUser(View view) {
        String email, pass, confirm;
        email = emailAddress.getEditText().getText().toString();
        pass = password.getEditText().getText().toString().trim();
        confirm = confirmPassword.getEditText().getText().toString();

        auth = FirebaseAuth.getInstance();

        //Email Field
        if (email.isEmpty()) {
            emailAddress.setError("Field cannot be empty");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddress.setError("Please enter a valid email address");
        } else {
            emailAddress.setError(null);
        }

        //Password Field
        if (pass.isEmpty()) {
            password.setError("Field cannot be empty");
        } else if (pass.length() <= 6) {
            password.setError("Password is too short");
        } else if (pass.length() > 20) {
            password.setError("Password too long");
        } else if (!PASSWORD_PATTERN.matcher(pass).matches()) {
            password.setError("At least 1 digit, 1 lower and upper case letter, and no spaces");
        } else {
            password.setError(null);
        }

        //Confirm Password Field
        if (confirm.isEmpty()) {
            confirmPassword.setError("Field cannot be empty");
        } else {
            confirmPassword.setError(null);
        }

        if (!pass.equals(confirm)) {
            confirmPassword.setError("Passwords do not match");
        } else {
            confirmPassword.setError(null);
        }

        //Password confirmation validation
        if (!email.isEmpty() && !pass.isEmpty() && !confirm.isEmpty() && pass.equals(confirm) && pass.length() > 6 && PASSWORD_PATTERN.matcher(pass).matches()) {
            emailPassAuthentication(email, pass);
        }
    }

    //Register user using Email and Password & applying authentication
    private void emailPassAuthentication(String email, String pass) {
        auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(UserRegister.this, "Email/Password Authenticated", Toast.LENGTH_SHORT).show();
                userLogIn(email, pass);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                checkEmail(email);
            }
        });
    }

    //Checking Firebase to see if email already exist
    private void checkEmail(String email) {
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        Boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser == false) {
                            Toast.makeText(UserRegister.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //User will log in after creating new account
    private void userLogIn(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(UserRegister.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(UserRegister.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            Intent intent = new Intent(UserRegister.this, MainInterface.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
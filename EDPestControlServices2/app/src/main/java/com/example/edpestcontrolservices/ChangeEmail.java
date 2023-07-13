package com.example.edpestcontrolservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmail extends AppCompatActivity {

    TextInputLayout currentEmailAddress;
    Button changeEmailAddressBtn;
    String emailAddress;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        Toolbar toolbar = findViewById(R.id.changeEmailToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Email Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //EditText
        currentEmailAddress = findViewById(R.id.displayCurrentEmail);

        //Button
        changeEmailAddressBtn = findViewById(R.id.changeEmailAddressBtn);

        //Reload Firebase
        user.reload();

        //Display Current Email Address
        getAndDisplayCurrentEmail();


    }

    private void getAndDisplayCurrentEmail() {
        if (user != null) {
            emailAddress = user.getEmail();
        }

        currentEmailAddress.getEditText().setTextColor(getResources().getColor(android.R.color.black));
        currentEmailAddress.getEditText().setText(emailAddress);
    }

    public void changeEmailAddress(View view) {
        String newEmailAddress = currentEmailAddress.getEditText().getText().toString();
        if (newEmailAddress.equals("")) {
            currentEmailAddress.setError("Field cannot be empty");
        } else if (newEmailAddress.equals(currentEmailAddress.getEditText().getText().toString())) {
            currentEmailAddress.setError("Provide a new email address");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(newEmailAddress).matches()) {
            currentEmailAddress.setError("Please enter a valid email address");
        } else {
            currentEmailAddress.setError(null);
        }

        if (newEmailAddress.equals("") || newEmailAddress.equals(currentEmailAddress.getEditText().getText().toString())) {

        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            user.updateEmail(currentEmailAddress.getEditText().getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangeEmail.this, "Email Address Changed. Email verification will be sent again. Please verify your new email address.", Toast.LENGTH_LONG).show();

                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ChangeEmail.this, "Email Verification Sent", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(ChangeEmail.this, ChangeEmail.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(ChangeEmail.this, "Email Verification Not Sent", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
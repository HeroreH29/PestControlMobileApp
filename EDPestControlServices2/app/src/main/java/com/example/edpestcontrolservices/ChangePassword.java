package com.example.edpestcontrolservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {

    TextInputLayout newPassword, confirmPassword;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.changePasswordToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newPassword = findViewById(R.id.changePasswordNew);
        confirmPassword = findViewById(R.id.changePasswordConfirm);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changePassword(View view) {
        String newPass, confirmPass;

        newPass = newPassword.getEditText().getText().toString();
        confirmPass = confirmPassword.getEditText().getText().toString();

        if (newPass.isEmpty()) {
            newPassword.setError("Field cannot be empty");
        } else if (newPass.length() > 20) {
            newPassword.setError("Limit exceeded");
        } else if (newPass.length() <= 6) {
            newPassword.setError("Password is too short");
        } else if (!PASSWORD_PATTERN.matcher(newPass).matches()) {
            newPassword.setError("At least 1 digit, 1 lower and upper case letter, and no spaces");
        } else {
            newPassword.setError(null);
        }

        if (confirmPass.isEmpty()) {
            confirmPassword.setError("Field cannot be empty");
        } else if (newPass.length() > 20) {
            confirmPassword.setError("Limit exceeded");
        } else {
            confirmPassword.setError(null);
        }

        if (!newPass.equals(confirmPass)) {
            confirmPassword.setError("Passwords do not match");
        }
        else {
            confirmPassword.setError(null);
        }

        if (!newPass.isEmpty() && !confirmPass.isEmpty() && newPass.equals(confirmPass) && newPass.length() > 6) {

            user.updatePassword(newPass)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ChangePassword.this, AccountSettingsActivity.class));
                                finish();
                            } else {
                                Toast.makeText(ChangePassword.this, "Password Change Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ChangePassError", e.toString());
                        }
                    });
        }
    }
}
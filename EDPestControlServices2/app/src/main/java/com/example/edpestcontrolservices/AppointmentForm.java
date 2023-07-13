package com.example.edpestcontrolservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AppointmentForm extends AppCompatActivity {

    TextInputLayout fullName, mobileNumber, emailAddress, houseNumber, street, barangay, city;
    Button bookAppt;
    EditText apptDate;
    String email;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_form);

        Toolbar toolbar = findViewById(R.id.appointmentToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointment Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Text Input Layouts
        fullName = findViewById(R.id.apptFullName);
        mobileNumber = findViewById(R.id.apptMobileNumber);
        emailAddress = findViewById(R.id.apptEmailAddress);
        houseNumber = findViewById(R.id.apptHouseNumber);
        street = findViewById(R.id.apptStreet);
        barangay = findViewById(R.id.apptBarangay);
        city = findViewById(R.id.apptCity);

        //TextInputEditText
        apptDate = findViewById(R.id.apptDate);

        //Button
        bookAppt = findViewById(R.id.bookAppt);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        apptDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AppointmentForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        String formattedMonth = "" + month;
                        String formattedDayOfMonth = "" + dayOfMonth;

                        if (month < 10) {
                            formattedMonth = "0" + month;
                        }
                        if (dayOfMonth < 10) {
                            formattedDayOfMonth = "0" + dayOfMonth;
                        }
                        String date = formattedMonth + "/" + formattedDayOfMonth + "/" + year;
                        apptDate.setText(date);
                        Toast.makeText(AppointmentForm.this, apptDate.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        getEmail();
    }

    //Get current user's email address
    private void getEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            email = user.getEmail();
        }
    }

    public void storeData (View view) {
        String fullName, mobileNumber, emailAddress, houseNumber, street, barangay, city, apptDate;

        fullName = this.fullName.getEditText().getText().toString();
        mobileNumber = this.mobileNumber.getEditText().getText().toString();
        emailAddress = this.emailAddress.getEditText().getText().toString();
        houseNumber = this.houseNumber.getEditText().getText().toString();
        street = this.street.getEditText().getText().toString();
        barangay = this.barangay.getEditText().getText().toString();
        city = this.city.getEditText().getText().toString();
        apptDate = this.apptDate.getText().toString();

        if (fullName.isEmpty()) {
            this.fullName.setError("Field cannot be empty");
        }

        if (mobileNumber.isEmpty()) {
            this.mobileNumber.setError("Field cannot be empty");
        }
        else if (mobileNumber.length() > 11) {
            this.mobileNumber.setError("Invalid mobile number");
        }
        else if (mobileNumber.length() < 11) {
            this.mobileNumber.setError("Invalid mobile number");
        }

        if (emailAddress.isEmpty()) {
            this.emailAddress.setError("Field cannot be empty");
        }

        if (houseNumber.isEmpty()) {
            this.houseNumber.setError("Field cannot be empty");
        }

        if (street.isEmpty()) {
            this.street.setError("Field cannot be empty");
        }

        if (barangay.isEmpty()) {
            this.barangay.setError("Field cannot be empty");
        }

        if (city.isEmpty()) {
            this.city.setError("Field cannot be empty");
        }

        if (apptDate.isEmpty()) {
            this.apptDate.setError("Field cannot be empty");
        }

        if (!fullName.isEmpty() && !mobileNumber.isEmpty() && !(mobileNumber.length() > 11) && !(mobileNumber.length() < 11) && !emailAddress.isEmpty() && !houseNumber.isEmpty() && !street.isEmpty() && !barangay.isEmpty() && !city.isEmpty() && !apptDate.isEmpty()) {
            Map<String, Object> storeData = new HashMap<>();
            storeData.put("aptFN", fullName);
            storeData.put("aptMN", mobileNumber);
            storeData.put("aptEA", emailAddress);
            storeData.put("aptHN", houseNumber);
            storeData.put("aptSTR", street);
            storeData.put("aptBRGY", barangay);
            storeData.put("aptCITY", city);
            storeData.put("aptDATE", apptDate);

            db.collection("_users").document(email).set(storeData, SetOptions.merge());

            Map<String, Boolean> hasBooked = new HashMap<>();
            hasBooked.put("_hasBooked", true);
            db.collection("_users").document(email).set(hasBooked, SetOptions.merge());

            Toast.makeText(this, "Appointment Booked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AppointmentForm.this, UserAppointmentSlip.class));
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
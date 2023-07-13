package com.example.edpestcontrolservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserAppointmentSlip extends AppCompatActivity {

    TextView fullName, emailAddress, appointmentDate, houseNumber, city;
    EditText mobileNumber, street, barangay;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment_slip);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.userAppointmentSlipToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointment Slip");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TextViews
        fullName = findViewById(R.id.userFullName);
        emailAddress = findViewById(R.id.userEmail);
        appointmentDate = findViewById(R.id.userDate);
        houseNumber = findViewById(R.id.userHouse);
        city = findViewById(R.id.userCity);

        //EditTexts
        mobileNumber = findViewById(R.id.userMobileNumber);
        street = findViewById(R.id.userStreet);
        barangay = findViewById(R.id.userBarangay);

        getEmail();
        displayValues();
    }

    private void getEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            email = user.getEmail();
        }
    }

    private void displayValues () {
        try {
            db.collection("_users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    if (snapshot.exists()) {
                        //Full Name
                        fullName.setText(snapshot.getString("aptFN"));

                        //Mobile Number
                        mobileNumber.setText(snapshot.getString("aptMN"));

                        //Email Address
                        emailAddress.setText(snapshot.getString("aptEA"));

                        //Appointment Date
                        appointmentDate.setText(snapshot.getString("aptDATE"));

                        //House Number
                        houseNumber.setText(snapshot.getString("aptHN"));

                        //Street
                        street.setText(snapshot.getString("aptSTR"));

                        //Barangay
                        barangay.setText(snapshot.getString("aptBRGY"));

                        //City
                        city.setText(snapshot.getString("aptCITY"));
                    }
                }
            });
        }
        catch (Exception e) {
            Log.d("userApptError", e.toString());
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
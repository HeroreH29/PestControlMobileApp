package com.example.edpestcontrolservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminAppointmentSlip extends AppCompatActivity {

    TextView fullName, emailAddress, appointmentDate, houseNumber, city;
    EditText mobileNumber, street, barangay;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> userEmail = new ArrayList<>();
    Integer position = 0;
    Integer arraySize;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_appointment_slip);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.adminAppointmentSlipToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Appointments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TextViews
        fullName = findViewById(R.id.adminFullName);
        emailAddress = findViewById(R.id.adminEmail);
        appointmentDate = findViewById(R.id.adminDate);
        houseNumber = findViewById(R.id.adminHouse);
        city = findViewById(R.id.adminCity);

        //EditTexts
        mobileNumber = findViewById(R.id.adminMobileNumber);
        street = findViewById(R.id.adminStreet);
        barangay = findViewById(R.id.adminBarangay);

        getUserEmail(userEmail);
    }

    private void getUserEmail(ArrayList arrayList) {
        try {
            db.collection("_users").whereEqualTo("_hasBooked", true).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {
                                arrayList.add(snapshot.getId());
                            }
                            getArraySize(arrayList);
                            checkRecords();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("getUserEmailError", e.toString());
                        }
                    });
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }

    private void getArraySize(ArrayList arr) {
        arraySize = arr.size();
    }

    private void checkRecords() {
        if (userEmail.size() == 0) {
            Toast.makeText(this, "There are no records", Toast.LENGTH_SHORT).show();
        } else {
            displayUserData();
        }
    }

    private void displayUserData() {
        email = userEmail.get(position);

        try {
            db.collection("_users").document(email).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                barangay.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

                                //City
                                city.setText(snapshot.getString("aptCITY"));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("displayUserApptError", e.toString());
                        }
                    });
        } catch (Exception e) {
            Log.d("adminApptError", e.toString());
        }
    }

    public void previousAppointment(View view) {
        if (userEmail.size() == 0) {
            Toast.makeText(this, "There are no appointments", Toast.LENGTH_SHORT).show();
        } else {
            if (position == 0) {
                Toast.makeText(this, "No appointments to display", Toast.LENGTH_SHORT).show();
                displayUserData();
            } else if (position >= 1) {
                position = position - 1;
                displayUserData();
            }
        }
    }

    public void nextAppointment(View view) {
        if (userEmail.size() == 0) {
            Toast.makeText(this, "There are no appointments", Toast.LENGTH_SHORT).show();

        } else {
            if (position < arraySize - 1) {
                position = position + 1;
                displayUserData();
            } else if (position == arraySize - 1) {
                Toast.makeText(this, "No more appointments to display", Toast.LENGTH_SHORT).show();
                displayUserData();
            }
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
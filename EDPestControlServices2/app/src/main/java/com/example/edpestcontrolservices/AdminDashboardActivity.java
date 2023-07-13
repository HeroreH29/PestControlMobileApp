package com.example.edpestcontrolservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDashboardActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    EditText adminEstimate, adminNotes;
    Button previousRecord, submitEstimate, nextRecord;

    TextView email, problem, houseSize, houseMaterial, entryPoints, relatives, children, pets, adminAppointments;
    EditText concerns, preventions, vermin, kindOfPet, damages;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> userEmail = new ArrayList<>();
    Integer position = 0;
    Integer arraySize;

    String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Toolbar toolbar = findViewById(R.id.userAppointmentSlipToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //EditText
        adminEstimate = findViewById(R.id.dashboardEstimate);
        adminNotes = findViewById(R.id.dashboardNotes);

        //Buttons
        previousRecord = findViewById(R.id.previousRecord);
        submitEstimate = findViewById(R.id.submitEstimate);
        nextRecord = findViewById(R.id.nextRecord);

        //TextView
        email = findViewById(R.id.userFullName);
        problem = findViewById(R.id.userEmail);
        houseSize = findViewById(R.id.userDate);
        houseMaterial = findViewById(R.id.userHouse);
        entryPoints = findViewById(R.id.adminCity);
        relatives = findViewById(R.id.adminDashboardRelatives);
        children = findViewById(R.id.adminDashboardChildren);
        pets = findViewById(R.id.adminDashboardPets);
        adminAppointments = findViewById(R.id.adminAppointments);


        //EditText
        concerns = findViewById(R.id.adminDashboardConcerns);
        preventions = findViewById(R.id.userMobileNumber);
        vermin = findViewById(R.id.adminStreet);
        kindOfPet = findViewById(R.id.adminDashboardKindOfPet);
        damages = findViewById(R.id.userBarangay);

        //SwipeLayout
        swipeRefreshLayout = findViewById(R.id.swipeLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(AdminDashboardActivity.this, AdminDashboardActivity.class));
                overridePendingTransition(0, 0);
                swipeRefreshLayout.setRefreshing(false);
                finish();
            }
        });

        //Show user booked appointments
        adminAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this, AdminAppointmentSlip.class));
            }
        });

        getUserEmail(userEmail);


    }

    private void getUserEmail(ArrayList arrayList) {
        try {
            db.collection("_users").whereEqualTo("isReviewed", false).get()
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

    private void checkRecords() {
        if (userEmail.size() == 0) {
            Toast.makeText(this, "There are no records", Toast.LENGTH_SHORT).show();
        } else {
            displayUserData();
        }
    }

    private void displayUserData() {
        emailAddress = userEmail.get(position);

        email.setTextColor(getResources().getColor(android.R.color.black));
        email.setText(emailAddress);

        try {
            db.collection("_users").document(emailAddress).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            if (snapshot.exists()) {
                                //Preventions Tried
                                preventions.setTextColor(getResources().getColor(android.R.color.black));
                                preventions.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                                preventions.setText(snapshot.getString("cons_input1"));

                                //Problem Prolonged For
                                Boolean rd1 = snapshot.getBoolean("rd1");
                                Boolean rd2 = snapshot.getBoolean("rd2");
                                Boolean rd3 = snapshot.getBoolean("rd3");

                                if (rd1 == true) {
                                    problem.setTextColor(getResources().getColor(android.R.color.black));
                                    problem.setText("Days");
                                }
                                if (rd2 == true) {
                                    problem.setTextColor(getResources().getColor(android.R.color.black));
                                    problem.setText("Weeks");
                                }
                                if (rd3 == true) {
                                    problem.setTextColor(getResources().getColor(android.R.color.black));
                                    problem.setText("Weeks");
                                }

                                //House Size
                                houseSize.setTextColor(getResources().getColor(android.R.color.black));
                                houseSize.setText(snapshot.getString("dropdown1"));

                                //House Material Type
                                ArrayList<String> houseMat = new ArrayList<>();

                                Boolean chkbx1 = snapshot.getBoolean("chkbx1");
                                Boolean chkbx2 = snapshot.getBoolean("chkbx2");
                                Boolean chkbx3 = snapshot.getBoolean("chkbx3");

                                if (chkbx1 == true) {
                                    houseMat.add("Plastic");
                                }
                                if (chkbx2 == true) {
                                    houseMat.add("Cement");
                                }
                                if (chkbx3 == true) {
                                    houseMat.add("Wood");
                                }

                                houseMaterial.setTextColor(getResources().getColor(android.R.color.black));
                                houseMaterial.setText(houseMat.toString().replace("[", "").replace("]", ""));

                                //Vermin
                                ArrayList<String> verm = new ArrayList<>();

                                Boolean chkbx4 = snapshot.getBoolean("chkbx4");
                                Boolean chkbx5 = snapshot.getBoolean("chkbx5");
                                Boolean chkbx6 = snapshot.getBoolean("chkbx6");
                                Boolean chkbx7 = snapshot.getBoolean("chkbx7");
                                Boolean chkbx8 = snapshot.getBoolean("chkbx8");
                                Boolean chkbx9 = snapshot.getBoolean("chkbx9");
                                Boolean chkbx10 = snapshot.getBoolean("chkbx10");
                                Boolean chkbx11 = snapshot.getBoolean("chkbx11");
                                Boolean chkbx12 = snapshot.getBoolean("chkbx12");

                                if (chkbx4 == true) {
                                    verm.add("Rat");
                                }
                                if (chkbx5 == true) {
                                    verm.add("Mice");
                                }
                                if (chkbx6 == true) {
                                    verm.add("Wasps");
                                }
                                if (chkbx7 == true) {
                                    verm.add("Flies");
                                }
                                if (chkbx8 == true) {
                                    verm.add("Ants");
                                }
                                if (chkbx9 == true) {
                                    verm.add("Bed Bugs");
                                }
                                if (chkbx10 == true) {
                                    verm.add("Termites");
                                }
                                if (chkbx11 == true) {
                                    verm.add("Cockroaches");
                                }
                                if (chkbx12 == true) {
                                    verm.add("Mosquitoes");
                                }

                                vermin.setTextColor(getResources().getColor(android.R.color.black));
                                vermin.setText(verm.toString().replace("[", "").replace("]", ""));

                                //Damages
                                damages.setTextColor(getResources().getColor(android.R.color.black));
                                damages.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                                damages.setText(snapshot.getString("cons_input2"));

                                //Entrypoints
                                ArrayList<String> entry = new ArrayList<>();

                                Boolean chkbx13 = snapshot.getBoolean("chkbx13");
                                Boolean chkbx14 = snapshot.getBoolean("chkbx14");
                                Boolean chkbx15 = snapshot.getBoolean("chkbx15");
                                Boolean chkbx16 = snapshot.getBoolean("chkbx16");

                                if (chkbx13 == true) {
                                    entry.add("Windows");
                                }
                                if (chkbx14 == true) {
                                    entry.add("Doors");
                                }
                                if (chkbx15 == true) {
                                    entry.add("Crevices");
                                }
                                if (chkbx16 == true) {
                                    entry.add("Vents");
                                }

                                entryPoints.setTextColor(getResources().getColor(android.R.color.black));
                                entryPoints.setText(entry.toString().replace("[", "").replace("]", ""));

                                //Relatives in House
                                Boolean rd4 = snapshot.getBoolean("rd4");
                                Boolean rd5 = snapshot.getBoolean("rd5");

                                if (rd4 == true) {
                                    relatives.setTextColor(getResources().getColor(android.R.color.black));
                                    relatives.setText("Yes");
                                }
                                if (rd5 == true) {
                                    relatives.setTextColor(getResources().getColor(android.R.color.black));
                                    relatives.setText("No");
                                }

                                //Children
                                Boolean rd6 = snapshot.getBoolean("rd6");
                                Boolean rd7 = snapshot.getBoolean("rd7");

                                if (rd6 == true) {
                                    children.setTextColor(getResources().getColor(android.R.color.black));
                                    children.setText("Yes");
                                }
                                if (rd7 == true) {
                                    children.setTextColor(getResources().getColor(android.R.color.black));
                                    children.setText("No");
                                }

                                //Pets
                                Boolean rd8 = snapshot.getBoolean("rd8");
                                Boolean rd9 = snapshot.getBoolean("rd9");

                                if (rd8 == true) {
                                    pets.setTextColor(getResources().getColor(android.R.color.black));
                                    pets.setText("Yes");
                                }
                                if (rd9 == true) {
                                    pets.setTextColor(getResources().getColor(android.R.color.black));
                                    pets.setText("No");
                                }

                                //Kind of Pet
                                kindOfPet.setTextColor(getResources().getColor(android.R.color.black));
                                kindOfPet.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                                kindOfPet.setText(snapshot.getString("cons_input3"));

                                //Additional Concerns
                                concerns.setTextColor(getResources().getColor(android.R.color.black));
                                concerns.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                                concerns.setText(snapshot.getString("cons_input4"));

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("displayUserDataError", e.toString());
                        }
                    });
        } catch (Exception e) {
            Log.d("adminDashboardError", e.toString());
        }
    }

    private void getArraySize(ArrayList arr) {
        arraySize = arr.size();
    }

    public void previousRecord(View view) {

        if (userEmail.size() == 0) {
            Toast.makeText(this, "There are no records", Toast.LENGTH_SHORT).show();
        } else {
            if (position == 0) {
                Toast.makeText(this, "No records to display", Toast.LENGTH_SHORT).show();
                adminEstimate.setText("");
                adminNotes.setText("");
                displayUserData();
            } else if (position >= 1) {
                position = position - 1;
                adminEstimate.setText("");
                adminNotes.setText("");
                displayUserData();
            }
        }
    }

    public void submitEstimate(View view) {
        if (userEmail.size() == 0) {
            Toast.makeText(this, "There are no records", Toast.LENGTH_SHORT).show();
        } else {
            String estimate, notes;

            estimate = adminEstimate.getText().toString();
            notes = adminNotes.getText().toString();

            emailAddress = userEmail.get(position);
            try {
                if (estimate.isEmpty()) {
                    adminEstimate.setError("Please provide an estimate");
                } else {
                    Map<String, Object> est = new HashMap<>();
                    est.put("_estimates", estimate);

                    db.collection("_users").document(emailAddress).set(est, SetOptions.merge());
                }

                if (notes.isEmpty()) {
                    adminNotes.setError("Please provide a note to the client");
                } else {
                    Map<String, Object> note = new HashMap<>();
                    note.put("_response", notes);

                    db.collection("_users").document(emailAddress).set(note, SetOptions.merge());
                }


                Map<String, Boolean> isReviewed = new HashMap<>();
                isReviewed.put("isReviewed", true);

                db.collection("_users").document(emailAddress).set(isReviewed, SetOptions.merge())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AdminDashboardActivity.this, "Response Submitted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AdminDashboardActivity.this, "Response Submission Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("SubmissionError", e.toString());
                            }
                        });

                adminEstimate.setText("");
                adminNotes.setText("");
                Toast.makeText(this, "Please swipe down to update the records", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Log.d("Submit Error", e.toString());
            }
        }

    }

    public void nextRecord(View view) {
        if (userEmail.size() == 0) {
            Toast.makeText(this, "There are no records", Toast.LENGTH_SHORT).show();

        } else {
            if (position < arraySize - 1) {
                position = position + 1;
                adminEstimate.setText("");
                adminNotes.setText("");
                displayUserData();
            } else if (position == arraySize - 1) {
                Toast.makeText(this, "No more records to display", Toast.LENGTH_SHORT).show();
                adminEstimate.setText("");
                adminNotes.setText("");
                displayUserData();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminDashboardActivity.this, MainActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AdminDashboardActivity.this, MainActivity.class));
        finish();
    }
}
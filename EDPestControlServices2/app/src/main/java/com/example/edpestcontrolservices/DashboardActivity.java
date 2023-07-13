package com.example.edpestcontrolservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;

    Button delete, comeBack, optAppointment;
    TextView email, problem, houseSize, houseMaterial, entryPoints, relatives, children, pets, estimate, appointmentSlip;
    EditText concerns, preventions, vermin, kindOfPet, damages, notes;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_activity);

        Toolbar toolbar = findViewById(R.id.dashboardToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Buttons
        delete = findViewById(R.id.previousRecord);
        comeBack = findViewById(R.id.submitEstimate);
        optAppointment = findViewById(R.id.nextRecord);

        //TextView
        email = findViewById(R.id.userFullName);
        problem = findViewById(R.id.userEmail);
        houseSize = findViewById(R.id.userDate);
        houseMaterial = findViewById(R.id.userHouse);
        entryPoints = findViewById(R.id.adminCity);
        relatives = findViewById(R.id.adminDashboardRelatives);
        children = findViewById(R.id.adminDashboardChildren);
        pets = findViewById(R.id.adminDashboardPets);
        estimate = findViewById(R.id.dashboardEstimate);
        appointmentSlip = findViewById(R.id.adminAppointments);


        //EditText
        concerns = findViewById(R.id.adminDashboardConcerns);
        preventions = findViewById(R.id.userMobileNumber);
        vermin = findViewById(R.id.adminStreet);
        kindOfPet = findViewById(R.id.adminDashboardKindOfPet);
        damages = findViewById(R.id.userBarangay);
        notes = findViewById(R.id.dashboardNotes);

        //SwipeLayout
                swipeRefreshLayout = findViewById(R.id.swipeLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                overridePendingTransition(0,0);
                swipeRefreshLayout.setRefreshing(false);
                finish();
            }
        });

        //Notify user to swipe down to refresh the activity
        Toast.makeText(this, "Please do refresh the activity by swiping down every few minutes to see dashboard updates", Toast.LENGTH_LONG).show();

        //Show Appointment Slip
        appointmentSlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, UserAppointmentSlip.class));
            }
        });

        getEmail();
        displayValues();

    }

    private void displayValues() {
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
                                houseMaterial.setText(houseMat.toString().replace("[", "").replace("]",""));

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
                                vermin.setText(verm.toString().replace("[", "").replace("]",""));

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
                                entryPoints.setText(entry.toString().replace("[", "").replace("]",""));

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
                                kindOfPet.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
                                kindOfPet.setText(snapshot.getString("cons_input3"));

                                //Additional Concerns
                                concerns.setTextColor(getResources().getColor(android.R.color.black));
                                concerns.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                                concerns.setText(snapshot.getString("cons_input4"));

                                //Estimate
                                estimate.setTextColor(getResources().getColor(android.R.color.black));
                                estimate.setText(snapshot.getString("_estimates"));

                                //Notes
                                notes.setTextColor(getResources().getColor(android.R.color.black));
                                notes.setText(snapshot.getString("_response"));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("displayValuesError", e.toString());
                        }
                    });
        } catch (Exception e) {
            Log.d("DashboardError", e.toString());
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

    public void deleteThisRecord(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete This Record")
                .setMessage("Are you sure? This action is irreversible.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("_users").document(emailAddress)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(DashboardActivity.this, "Record Has Been Deleted", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(DashboardActivity.this, MainInterface.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("RecordDeletionError", e.toString());
                                        Toast.makeText(DashboardActivity.this, "Record Deletion Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("No", null).setCancelable(false);
        builder.create().show();

    }

    public void comeBackLater(View view) {
        startActivity(new Intent(this, MainInterface.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    public void optForAppointment(View view) {
        db.collection("_users").document(emailAddress).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        if (snapshot.exists()) {
                            //Check if form has been reviewed by the Admin
                            Boolean form = snapshot.getBoolean("isReviewed");
                            Boolean book = snapshot.getBoolean("_hasBooked");

                            if (form == false) {
                                Toast.makeText(DashboardActivity.this, "Form is not yet reviewed by the Admin", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (book == false) {
                                    startActivity(new Intent(DashboardActivity.this, AppointmentForm.class));
                                }
                                else {
                                    Toast.makeText(DashboardActivity.this, "You have already booked an appointment", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("appointError", e.toString());
                    }
                });
    }

    private void getEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            emailAddress = user.getEmail();
        }
    }
}
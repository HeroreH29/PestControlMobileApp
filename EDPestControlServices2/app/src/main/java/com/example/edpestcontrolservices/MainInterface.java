package com.example.edpestcontrolservices;


import android.content.ActivityNotFoundException;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.SnapshotMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class MainInterface extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout navigation, profile;
    private ImageView rightNav;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String email;
    TextView userEmail;
    Boolean hasSubmitted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigation = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navigation, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navigation.addDrawerListener(toggle);
        toggle.syncState();

        profile = findViewById(R.id.drawer_layout);
        NavigationView navigationView1 = findViewById(R.id.profile_view);
        navigationView1.setNavigationItemSelectedListener(this);

        rightNav = findViewById(R.id.imageView3);

        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.openDrawer(GravityCompat.END);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        getEmail();
        createDocument();

        userEmail = findViewById(R.id.userEmailAddress);
        userEmail.setText(email);

    }

    private void createDocument() {
        if (email.equals("edpestcontroladmeen1@gmail.com")) {
            startActivity(new Intent(this, AdminDashboardActivity.class));
            finish();
        } else {
            db.collection("_users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                        } else {
                            Map<String, Boolean> hasSubmitted = new HashMap<>();
                            hasSubmitted.put("_hasSubmitted", false);

                            db.collection("_users").document(email).set(hasSubmitted, SetOptions.merge());

                            Map<String, Boolean> hasBooked = new HashMap<>();
                            hasBooked.put("_hasBooked", false);

                            db.collection("_users").document(email).set(hasBooked, SetOptions.merge());
                        }
                    } else {
                        Log.d("createDocument", task.getException().toString());
                    }
                }
            });
        }
    }

    //Get current user's email address
    private void getEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            email = user.getEmail();
        }
    }

    //Navigation Menu Selection
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_location:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LocationFragment()).commit();
                break;
            case R.id.nav_services:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ServicesFragment()).commit();
                break;
            case R.id.nav_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                break;
            case R.id.nav_facebook:
                openFacebook("1782247442076920");
                break;

            //For the case of the right side navigation panel
            case R.id.prof_dashboard:
                db.collection("_users").document(email).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    hasSubmitted = snapshot.getBoolean("_hasSubmitted");

                                    if (hasSubmitted == false) {
                                        Toast.makeText(MainInterface.this, "Submit a Consultation Form first to access Dashboard", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        startActivity(new Intent(MainInterface.this, DashboardActivity.class));
                                    }
                                }
                                else {
                                    Toast.makeText(MainInterface.this, "Document DNE", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("DashboardOnClickError", e.toString());
                            }
                        });
                break;
            case R.id.prof_settings:
                startActivity(new Intent(MainInterface.this, AccountSettingsActivity.class));
                break;
            case R.id.prof_signout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainInterface.this, MainActivity.class));
                finish();
                break;
        }
        navigation.closeDrawer(GravityCompat.START);
        return true;
    }

    //Pressing back button closes navigation drawer/s if it is open.
    @Override
    public void onBackPressed() {
        if (navigation.isDrawerOpen(androidx.core.view.GravityCompat.START) || profile.isDrawerOpen(GravityCompat.END)) {
            navigation.closeDrawer(GravityCompat.START);
            profile.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    //Redirecting user to the Facebook page of the business through Facebook app or installed web browser if Facebook app is not installed
    private void openFacebook(String id) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + id)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ED-PEST-control-services-1782247442076920/")));
        }
    }

}
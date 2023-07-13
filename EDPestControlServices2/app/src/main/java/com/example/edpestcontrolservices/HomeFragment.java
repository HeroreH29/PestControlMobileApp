package com.example.edpestcontrolservices;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email;
    Boolean hasSubmitted;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button consultNow = view.findViewById(R.id.consultNow);

        if (user != null) {
            email = user.getEmail();
        }

        user.reload();
        
        consultNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("_users").document(email).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                if (user.isEmailVerified()) {
                                    if (snapshot.exists()) {
                                        hasSubmitted = snapshot.getBoolean("_hasSubmitted");

                                        if (hasSubmitted == false) {
                                            startActivity(new Intent(getContext(), ConsultationForm.class));
                                        } else {
                                            Toast.makeText(getContext(), "You have already submitted a form. Go to Dashboard and wait for the Estimation.", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Document DNE", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(getContext(), "Email is not yet verified. Please verify your email first to proceed in Consultation.", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("HomeFragmentError", e.toString());
                            }
                        });
            }
        });
        return view;
    }
}

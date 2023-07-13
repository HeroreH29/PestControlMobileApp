package com.example.edpestcontrolservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {

    private ArrayList<GetSetProfile> getSetProfile;
    private RecyclerView recyclerView;
    private editProfileAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        Toolbar toolbar = findViewById(R.id.myProfileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSetProfile = new ArrayList<>();

        recyclerView = findViewById(R.id.profileRecyclerView);

        setProfile();
        setAdapter();
    }

    private void setAdapter() {
        setOnClickListener();
        editProfileAdapter adapter = new editProfileAdapter(getSetProfile, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = new editProfileAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (position == 0) {
                    startActivity(new Intent(MyProfileActivity.this, ChangeEmail.class));
                }
                if (position == 1) {
                    startActivity(new Intent(MyProfileActivity.this, ChangePassword.class));
                }
            }
        };
    }

    private void setProfile() {
        getSetProfile.add(new GetSetProfile("Change Email Address"));
        getSetProfile.add(new GetSetProfile("Change Password"));
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
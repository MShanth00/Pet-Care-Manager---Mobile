package com.example.petcaremanager.Page.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.Page.HealthTracker.HealthTrackerActivity;
import com.example.petcaremanager.Page.PetProfiles.PetProfilesActivity;
import com.example.petcaremanager.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Buttons for features
        Button petProfilesButton = findViewById(R.id.petProfileButton);
        Button taskManagerButton = findViewById(R.id.taskManagerButton);
        Button heaalthManagerButton = findViewById(R.id.petHealthButton);

        // Navigate to Pet Profiles feature
        petProfilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PetProfilesActivity.class);
                startActivity(intent);
            }
        });


        heaalthManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, HealthTrackerActivity.class);
                startActivity(intent);
            }
        });

        // Placeholder for Task Manager feature
        taskManagerButton.setOnClickListener(v ->
                Toast.makeText(DashboardActivity.this, "Task Manager Coming Soon!", Toast.LENGTH_SHORT).show());
    }
}

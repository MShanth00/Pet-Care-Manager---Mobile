package com.example.petcaremanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Buttons for features
        Button petProfilesButton = findViewById(R.id.petProfileButton);
        Button taskManagerButton = findViewById(R.id.taskManagerButton);

        // Navigate to Pet Profiles feature
        petProfilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PetProfilesActivity.class);
                startActivity(intent);
            }
        });

        // Placeholder for Task Manager feature
        taskManagerButton.setOnClickListener(v ->
                Toast.makeText(DashboardActivity.this, "Task Manager Coming Soon!", Toast.LENGTH_SHORT).show());
    }
}

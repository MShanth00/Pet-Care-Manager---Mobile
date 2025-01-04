package com.example.petcaremanager.Page.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.Page.DietPlanner.DietPlannerActivity;
import com.example.petcaremanager.Page.Expense.ExpenseTrackerActivity;
import com.example.petcaremanager.Page.HealthTracker.HealthTrackerActivity;
import com.example.petcaremanager.Page.PetProfiles.PetProfilesActivity;
import com.example.petcaremanager.Page.Task.TaskManagerActivity;
import com.example.petcaremanager.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button petProfilesButton = findViewById(R.id.petProfileButton);
        Button taskManagerButton = findViewById(R.id.taskManagerButton);
        Button heaalthManagerButton = findViewById(R.id.petHealthButton);
        Button dietManagerButton = findViewById(R.id.petDietButton);
        Button expenseManagerButton = findViewById(R.id.petExpenseButton);

        Button petSocialButton = findViewById(R.id.petSocialButton);
        Button petAdoptionButton = findViewById(R.id.petAdoptionButton);
        Button weatherAlertsButton = findViewById(R.id.weatherAlertsButton);
        Button trainingTipsButton = findViewById(R.id.trainingTipsButton);
        Button emergencyContactButton = findViewById(R.id.emergencyContactButton);

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

        dietManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, DietPlannerActivity.class);
                startActivity(intent);
            }
        });

        expenseManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ExpenseTrackerActivity.class);
                startActivity(intent);
            }
        });

        taskManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, TaskManagerActivity.class);
                startActivity(intent);
            }
        });

        // Placeholder for Task Manager feature
        petSocialButton.setOnClickListener(v ->
                Toast.makeText(DashboardActivity.this, "Pet Social Network Coming Soon!", Toast.LENGTH_SHORT).show());

        petAdoptionButton.setOnClickListener(v ->
                Toast.makeText(DashboardActivity.this, "Pet Adoption and Services Directory Coming Soon!", Toast.LENGTH_SHORT).show());

        weatherAlertsButton.setOnClickListener(v ->
                Toast.makeText(DashboardActivity.this, "Weather Alerts for Pet Safety Coming Soon!", Toast.LENGTH_SHORT).show());

        trainingTipsButton.setOnClickListener(v ->
                Toast.makeText(DashboardActivity.this, "Training Tips and Tutorials Coming Soon!", Toast.LENGTH_SHORT).show());

        emergencyContactButton.setOnClickListener(v ->
                Toast.makeText(DashboardActivity.this, "Emergency Contact Info Coming Soon!", Toast.LENGTH_SHORT).show());
    }
}

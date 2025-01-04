package com.example.petcaremanager.Page.DietPlanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.DB.DatabaseHelper;
import com.example.petcaremanager.R;

import java.util.ArrayList;
import java.util.List;

public class DietPlannerActivity extends AppCompatActivity {

    private Spinner petSpinner;
    private ListView dietListView;
    private Button addDietButton;
    private DatabaseHelper dbHelper;

    private int selectedPetId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_planner);

        // Initialize views
        petSpinner = findViewById(R.id.spinnerPets);
        dietListView = findViewById(R.id.listViewDietPlans);
        addDietButton = findViewById(R.id.btnAddDiet);

        dbHelper = new DatabaseHelper(this);

        // Load pets into spinner
        loadPetSpinner();

        // Set spinner listener
        petSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) { // Ignore "Select Pet" default option
                    selectedPetId = (int) petSpinner.getSelectedItemId();
                    loadDietPlans(selectedPetId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set add button listener
        addDietButton.setOnClickListener(view -> {
            if (selectedPetId == -1) {
                Toast.makeText(this, "Please select a pet first", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(DietPlannerActivity.this, AddEditDietActivity.class);
                intent.putExtra("pet_id", selectedPetId);
                startActivity(intent);
            }
        });
    }

    private void loadPetSpinner() {
        List<String> petNames = new ArrayList<>();
        petNames.add("Select Pet");

        Cursor cursor = dbHelper.getReadableDatabase().query(
                DatabaseHelper.TABLE_PETS,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME},
                null, null, null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                petNames.add(cursor.getString(1)); // Pet Name
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, petNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petSpinner.setAdapter(adapter);
    }

    private List<Integer> dietPlanIds = new ArrayList<>();

    private void loadDietPlans(int petId) {
        List<String> dietPlans = new ArrayList<>();
        dietPlanIds.clear();
        Cursor cursor = null;

        try {
            cursor = dbHelper.getReadableDatabase().query(
                    DatabaseHelper.TABLE_DIET_PLANS,
                    new String[]{
                            DatabaseHelper.COLUMN_PET_ID,
                            DatabaseHelper.COLUMN_START_DATE,
                            DatabaseHelper.COLUMN_END_DATE,
                            DatabaseHelper.COLUMN_FOOD_TYPE
                    },
                    DatabaseHelper.COLUMN_PET_ID + "=?",
                    new String[]{String.valueOf(petId)},
                    null, null, null
            );

            Log.d("DietPlannerActivity", "Query executed successfully. Rows fetched: " + cursor.getCount());
        } catch (Exception e) {
            Log.e("DietPlannerActivity", "Error executing query: " + e.getMessage());
        }


        if (cursor.moveToFirst()) {
            do {
                int dietPlanId = cursor.getInt(0); // Retrieve the diet plan ID
                String plan = "From: " + cursor.getString(1) +
                        " To: " + cursor.getString(2) +
                        " - Food: " + cursor.getString(3);
                dietPlans.add(plan);
                dietPlanIds.add(dietPlanId);
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dietPlans);
        dietListView.setAdapter(adapter);

        dietListView.setOnItemClickListener((parent, view, position, id) -> {
            int selectedDietPlanId = dietPlanIds.get(position); // Get the diet plan ID
            Intent intent = new Intent(DietPlannerActivity.this, DietDetailsActivity.class);
            intent.putExtra("diet_plan_id", selectedDietPlanId);
            startActivity(intent);
        });

    }


}
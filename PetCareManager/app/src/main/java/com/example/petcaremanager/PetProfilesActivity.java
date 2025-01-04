package com.example.petcaremanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.DB.DatabaseHelper;

public class PetProfilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profiles);

        // Initialize views
        ImageView petProfileImage = findViewById(R.id.petProfileImage);
        EditText petNameInput = findViewById(R.id.petNameInput);
        EditText petBreedInput = findViewById(R.id.petBreedInput);
        EditText petAgeInput = findViewById(R.id.petAgeInput);
        EditText petMedicalConditionsInput = findViewById(R.id.petMedicalConditionsInput);
        EditText petAllergiesInput = findViewById(R.id.petAllergiesInput);
        Button savePetProfileButton = findViewById(R.id.savePetProfileButton);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Save profile button functionality
        savePetProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String petName = petNameInput.getText().toString().trim();
                String petBreed = petBreedInput.getText().toString().trim();
                String petAge = petAgeInput.getText().toString().trim();
                String medicalConditions = petMedicalConditionsInput.getText().toString().trim();
                String allergies = petAllergiesInput.getText().toString().trim();

                if (petName.isEmpty() || petBreed.isEmpty() || petAge.isEmpty()) {
                    Toast.makeText(PetProfilesActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                } else {


                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_NAME, petName);
                    values.put(DatabaseHelper.COLUMN_AGE, petAge);
                    values.put(DatabaseHelper.COLUMN_BREED, petBreed);

                    long result = db.insert(DatabaseHelper.TABLE_PETS, null, values);
                    if (result != -1) {
                        Toast.makeText(PetProfilesActivity.this, "Pet profile added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PetProfilesActivity.this, "Failed to add pet profile.", Toast.LENGTH_SHORT).show();
                    }
                    db.close();

                    // Save data locally (for now, just showing a success message)
                    Toast.makeText(PetProfilesActivity.this, "Profile saved for " + petName, Toast.LENGTH_SHORT).show();

                    // Clear the inputs after saving
                    petNameInput.setText("");
                    petBreedInput.setText("");
                    petAgeInput.setText("");
                    petMedicalConditionsInput.setText("");
                    petAllergiesInput.setText("");
                }
            }
        });
    }
}
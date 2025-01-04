package com.example.petcaremanager.Page.HealthTracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.DB.DatabaseHelper;
import com.example.petcaremanager.R;

import java.util.ArrayList;
import java.util.List;

public class HealthTrackerActivity extends AppCompatActivity {

    private Spinner spinnerPets;
    private EditText editTextDate, editTextDescription, editTextNotes;
    private Button buttonSaveHealthRecord;
    private ListView listViewHealthRecords;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tracker);

        spinnerPets = findViewById(R.id.spinnerPets);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextNotes = findViewById(R.id.editTextNotes);
        buttonSaveHealthRecord = findViewById(R.id.buttonSaveHealthRecord);
        listViewHealthRecords = findViewById(R.id.listViewHealthRecords);

        dbHelper = new DatabaseHelper(this);

        loadPetsIntoSpinner();
        buttonSaveHealthRecord.setOnClickListener(v -> saveHealthRecord());
    }

    private void loadPetsIntoSpinner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_PETS, null, null, null, null, null, null);

        List<String> petNames = new ArrayList<>();
        final List<Integer> petIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            petNames.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)));
            petIds.add(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, petNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPets.setAdapter(adapter);

        spinnerPets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Assuming petIds is a List<Integer> that maps spinner positions to pet IDs
                int selectedPetId = petIds.get(position);
                loadHealthRecords(selectedPetId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where no item is selected if necessary
            }
        });
    }

    private void saveHealthRecord() {
        String date = editTextDate.getText().toString();
        String description = editTextDescription.getText().toString();
        String notes = editTextNotes.getText().toString();

        if (date.isEmpty() || description.isEmpty() || notes.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get pet ID from spinner (assuming spinner holds pet_id values directly)
        int selectedPetId = (int) spinnerPets.getSelectedItemId();

        // Insert into database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PET_ID, selectedPetId);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_NOTES, notes);

//        long result = db.insert(DatabaseHelper.TABLE_HEALTH_RECORDS, null, values);


        try {
            long result = db.insertOrThrow(DatabaseHelper.TABLE_HEALTH_RECORDS, null, values);
            if (result == -1) {
                Log.e("DB_ERROR", "Insert failed for table: " + DatabaseHelper.TABLE_HEALTH_RECORDS);
            }else {
                Toast.makeText(this, "Health record added successfully", Toast.LENGTH_SHORT).show();
            loadHealthRecords(selectedPetId);
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error inserting record: " + e.getMessage());
        }
    }


    private void loadHealthRecords(int petId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to fetch health records for the specific pet
        Cursor cursor = db.rawQuery(
                "SELECT id AS _id, date, description FROM health_records WHERE pet_id = ?",
                new String[]{String.valueOf(petId)}
        );

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No health records found for this pet.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create SimpleCursorAdapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.simple_list,
                cursor,
                new String[]{"date", "description"}, // Columns from database
                new int[]{R.id.valueDate, R.id.valueDescription}, // Views in the layout
                0
        );

        // Set adapter to ListView
        listViewHealthRecords.setAdapter(adapter);

    }




}

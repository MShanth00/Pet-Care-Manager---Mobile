package com.example.petcaremanager.Page.HealthTracker;


import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.DB.DatabaseHelper;
import com.example.petcaremanager.R;

public class SimpleActivity extends AppCompatActivity {

    private Button deleteButton;
    private DatabaseHelper dbHelper;
    private int recordIdToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list);

        dbHelper = new DatabaseHelper(this);
        deleteButton = findViewById(R.id.deleteButton);

        recordIdToDelete = getIntent().getIntExtra("RECORD_ID", -1);

        deleteButton.setOnClickListener(v -> {
            if (recordIdToDelete != -1) {
                boolean result = dbHelper.deleteHealthRecord(recordIdToDelete);
                if (result) {
                    Toast.makeText(SimpleActivity.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SimpleActivity.this, "Failed to delete record", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SimpleActivity.this, "No record ID specified", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

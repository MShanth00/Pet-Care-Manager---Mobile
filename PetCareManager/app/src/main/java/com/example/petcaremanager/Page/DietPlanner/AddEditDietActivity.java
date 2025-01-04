package com.example.petcaremanager.Page.DietPlanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.DB.DatabaseHelper;
import com.example.petcaremanager.R;

public class AddEditDietActivity extends AppCompatActivity {

    private EditText etStartDate, etEndDate, etFoodType, etFoodQuantity, etMedicine, etDescription;
    private Button btnSave;

    private DatabaseHelper dbHelper;
    private int petId;
    private int dietId = -1; // For editing; default is -1 (add mode)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_diet);

        // Initialize views
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etFoodType = findViewById(R.id.etFoodType);
        etFoodQuantity = findViewById(R.id.etFoodQuantity);
        etMedicine = findViewById(R.id.etMedicine);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DatabaseHelper(this);

        // Get pet ID and (optional) diet ID from intent
        petId = getIntent().getIntExtra("pet_id", -1);
        dietId = getIntent().getIntExtra("diet_id", -1);

        if (dietId != -1) {
            // If dietId is provided, load the diet plan details for editing
            loadDietPlanDetails(dietId);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDietPlan();
            }
        });
    }

    private void loadDietPlanDetails(int dietId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to fetch diet plan details
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_DIET_PLANS + " WHERE " + DatabaseHelper.COLUMN_DIET_ID + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(dietId)});

        if (cursor.moveToFirst()) {
            int startDateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_START_DATE);
            if (startDateIndex != -1) {
                etStartDate.setText(cursor.getString(startDateIndex));
            }

            int endDateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_END_DATE);
            if (endDateIndex != -1) {
                etEndDate.setText(cursor.getString(endDateIndex));
            }

            int foodTypeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FOOD_TYPE);
            if (foodTypeIndex != -1) {
                etFoodType.setText(cursor.getString(foodTypeIndex));
            }

            int foodQuantityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FOOD_QUANTITY);
            if (foodQuantityIndex != -1) {
                etFoodQuantity.setText(cursor.getString(foodQuantityIndex));
            }

            int medicineIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_MEDICINE);
            if (medicineIndex != -1) {
                etMedicine.setText(cursor.getString(medicineIndex));
            }

            int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
            if (descriptionIndex != -1) {
                etDescription.setText(cursor.getString(descriptionIndex));
            }
        }

        cursor.close();
    }

    private void saveDietPlan() {
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        String foodType = etFoodType.getText().toString().trim();
        String foodQuantity = etFoodQuantity.getText().toString().trim();
        String medicine = etMedicine.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate) || TextUtils.isEmpty(foodType) || TextUtils.isEmpty(foodQuantity)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PET_ID, petId);
        values.put(DatabaseHelper.COLUMN_START_DATE, startDate);
        values.put(DatabaseHelper.COLUMN_END_DATE, endDate);
        values.put(DatabaseHelper.COLUMN_FOOD_TYPE, foodType);
        values.put(DatabaseHelper.COLUMN_FOOD_QUANTITY, foodQuantity);
        values.put(DatabaseHelper.COLUMN_MEDICINE, medicine);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);

        if (dietId == -1) {
            // Insert new diet plan
            long newRowId = db.insert(DatabaseHelper.TABLE_DIET_PLANS, null, values);
            if (newRowId != -1) {
                Toast.makeText(this, "Diet plan added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add diet plan", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing diet plan
            int rowsAffected = db.update(DatabaseHelper.TABLE_DIET_PLANS, values,
                    DatabaseHelper.COLUMN_DIET_ID + "=?", new String[]{String.valueOf(dietId)});
            if (rowsAffected > 0) {
                Toast.makeText(this, "Diet plan updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update diet plan", Toast.LENGTH_SHORT).show();
            }
        }

        finish(); // Close the activity
    }
}

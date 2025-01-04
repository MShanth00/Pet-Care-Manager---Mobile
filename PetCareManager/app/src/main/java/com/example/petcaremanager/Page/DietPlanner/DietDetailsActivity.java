package com.example.petcaremanager.Page.DietPlanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.DB.DatabaseHelper;
import com.example.petcaremanager.R;

public class DietDetailsActivity extends AppCompatActivity {

    private EditText etStartDate, etEndDate, etFoodType, etQuantity, etDescription, etMedicine;
    private Button btnSave, btnDelete;
    private DatabaseHelper dbHelper;
    private int dietPlanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_details);

        // Initialize views
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etFoodType = findViewById(R.id.etFoodType);
        etQuantity = findViewById(R.id.etQuantity);
        etDescription = findViewById(R.id.etDescription);
        etMedicine = findViewById(R.id.etMedicine);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        dbHelper = new DatabaseHelper(this);

        // Get diet plan ID from Intent
        dietPlanId = getIntent().getIntExtra("diet_plan_id", -1);

        // Load diet plan details
        loadDietDetails(dietPlanId);

        // Set save button listener
        btnSave.setOnClickListener(v -> {
            if (updateDietPlan(dietPlanId)) {
                Toast.makeText(this, "Diet plan updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Set delete button listener
        btnDelete.setOnClickListener(v -> {
            if (deleteDietPlan(dietPlanId)) {
                Toast.makeText(this, "Diet plan deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadDietDetails(int id) {
        Cursor cursor = dbHelper.getReadableDatabase().query(
                DatabaseHelper.TABLE_DIET_PLANS,
                null,
                DatabaseHelper.COLUMN_DIET_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

//        if (cursor.moveToFirst()) {
//            etStartDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_START_DATE)));
//            etEndDate.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_END_DATE)));
//            etFoodType.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FOOD_TYPE)));
//            etQuantity.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FOOD_QUANTITY)));
//            etDescription.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)));
//            etMedicine.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MEDICINE)));
//        }

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
                etQuantity.setText(cursor.getString(foodQuantityIndex));
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

    private boolean updateDietPlan(int id) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_START_DATE, etStartDate.getText().toString());
        values.put(DatabaseHelper.COLUMN_END_DATE, etEndDate.getText().toString());
        values.put(DatabaseHelper.COLUMN_FOOD_TYPE, etFoodType.getText().toString());
        values.put(DatabaseHelper.COLUMN_FOOD_QUANTITY, etQuantity.getText().toString());
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, etDescription.getText().toString());
        values.put(DatabaseHelper.COLUMN_MEDICINE, etMedicine.getText().toString());

        int rowsAffected = dbHelper.getWritableDatabase().update(
                DatabaseHelper.TABLE_DIET_PLANS,
                values,
                DatabaseHelper.COLUMN_DIET_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        return rowsAffected > 0;
    }

    private boolean deleteDietPlan(int id) {
        int rowsDeleted = dbHelper.getWritableDatabase().delete(
                DatabaseHelper.TABLE_DIET_PLANS,
                DatabaseHelper.COLUMN_DIET_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        return rowsDeleted > 0;
    }
}

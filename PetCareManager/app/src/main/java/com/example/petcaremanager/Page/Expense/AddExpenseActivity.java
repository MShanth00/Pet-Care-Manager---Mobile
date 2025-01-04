package com.example.petcaremanager.Page.Expense;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.DB.DatabaseHelper;
import com.example.petcaremanager.R;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etFoodExpense, etMedicineExpense, etOtherExpense, etDate;
    private Button btnSaveExpense;
    private DatabaseHelper dbHelper;
    private int petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etFoodExpense = findViewById(R.id.etFoodExpense);
        etMedicineExpense = findViewById(R.id.etMedicineExpense);
        etOtherExpense = findViewById(R.id.etOtherExpense);
        etDate = findViewById(R.id.etDate);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);

        dbHelper = new DatabaseHelper(this);
        petId = getIntent().getIntExtra("pet_id", -1);

        btnSaveExpense.setOnClickListener(v -> {
            String date = etDate.getText().toString();
            double foodExpense = Double.parseDouble(etFoodExpense.getText().toString());
            double medicineExpense = Double.parseDouble(etMedicineExpense.getText().toString());
            double otherExpense = Double.parseDouble(etOtherExpense.getText().toString());

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_PET_ID, petId);
            values.put(DatabaseHelper.COLUMN_EXPENSE_DATE, date);
            values.put(DatabaseHelper.COLUMN_EXPENSE_FOOD, foodExpense);
            values.put(DatabaseHelper.COLUMN_EXPENSE_MEDICINE, medicineExpense);
            values.put(DatabaseHelper.COLUMN_EXPENSE_OTHER, otherExpense);

            long result = db.insert(DatabaseHelper.TABLE_EXPENSES, null, values);
            if (result != -1) {
                Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

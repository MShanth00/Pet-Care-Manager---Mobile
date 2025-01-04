package com.example.petcaremanager.Page.Expense;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.DB.DatabaseHelper;
import com.example.petcaremanager.R;

public class ExpenseDetailsActivity extends AppCompatActivity {

    private TextView tvPetName, tvFoodExpense, tvMedicineExpense, tvOtherExpense, tvTotalExpense;
    private Button btnDeleteExpense;
    private DatabaseHelper dbHelper;
    private int expenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        // Initialize views
        tvPetName = findViewById(R.id.tvPetName);
        tvFoodExpense = findViewById(R.id.tvFoodExpense);
        tvMedicineExpense = findViewById(R.id.tvMedicineExpense);
        tvOtherExpense = findViewById(R.id.tvOtherExpense);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        btnDeleteExpense = findViewById(R.id.btnDeleteExpense);

        dbHelper = new DatabaseHelper(this);

        expenseId = getIntent().getIntExtra("expense_id", -1);

        loadExpenseDetails(expenseId);

        btnDeleteExpense.setOnClickListener(v -> deleteExpense());
    }

    private void loadExpenseDetails(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_EXPENSES,
                new String[]{
                        DatabaseHelper.COLUMN_PET_ID,
                        DatabaseHelper.COLUMN_EXPENSE_FOOD,
                        DatabaseHelper.COLUMN_EXPENSE_MEDICINE,
                        DatabaseHelper.COLUMN_EXPENSE_OTHER
                },
                DatabaseHelper.COLUMN_EXPENSE_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            int petId = cursor.getInt(0);
            double foodExpense = cursor.getDouble(1);
            double medicineExpense = cursor.getDouble(2);
            double otherExpense = cursor.getDouble(3);
            double totalExpense = foodExpense + medicineExpense + otherExpense;

            // Get pet name
            Cursor petCursor = db.query(
                    DatabaseHelper.TABLE_PETS,
                    new String[]{DatabaseHelper.COLUMN_NAME},
                    DatabaseHelper.COLUMN_ID + "=?",
                    new String[]{String.valueOf(petId)},
                    null, null, null
            );

            if (petCursor.moveToFirst()) {
                String petName = petCursor.getString(0);
                tvPetName.setText("Pet Name: " + petName);
            }else {
                tvPetName.setText("Pet ID: " + petId);
            }
            petCursor.close();

            tvFoodExpense.setText("Food Expense: LKR" + foodExpense);
            tvMedicineExpense.setText("Medicine Expense: LKR" + medicineExpense);
            tvOtherExpense.setText("Other Expense: LKR" + otherExpense);
            tvTotalExpense.setText("Total Expense: LKR" + totalExpense);
        }
        cursor.close();
    }

    private void deleteExpense() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        int rowsDeleted = db.delete(
                                DatabaseHelper.TABLE_EXPENSES,
                                DatabaseHelper.COLUMN_EXPENSE_ID + "=?",
                                new String[]{String.valueOf(expenseId)}
                        );

                        if (rowsDeleted > 0) {
                            Toast.makeText(ExpenseDetailsActivity.this, "Expense deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ExpenseDetailsActivity.this, "Failed to delete expense", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}

package com.example.petcaremanager.Page.Expense;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

public class ExpenseTrackerActivity extends AppCompatActivity {

    private Spinner petSpinner;
    private ListView expenseListView;
    private Button addExpenseButton;
    private DatabaseHelper dbHelper;

    private int selectedPetId = -1;
    private List<Integer> expenseIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_tracker);

        // Initialize views
        petSpinner = findViewById(R.id.spinnerPets);
        expenseListView = findViewById(R.id.listViewExpenses);
        addExpenseButton = findViewById(R.id.btnAddExpense);

        dbHelper = new DatabaseHelper(this);

        // Load pets into spinner
        loadPetSpinner();

        // Set spinner listener
        petSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) { // Ignore "Select Pet" default option
                    selectedPetId = position - 1; // Get the corresponding pet ID
                    loadExpenses(selectedPetId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set add button listener
        addExpenseButton.setOnClickListener(view -> {
            if (selectedPetId == -1) {
                Toast.makeText(this, "Please select a pet first", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ExpenseTrackerActivity.this, AddExpenseActivity.class);
                intent.putExtra("pet_id", selectedPetId);
                startActivity(intent);
            }
        });

        // Set item click listener for expenses
        expenseListView.setOnItemClickListener((parent, view, position, id) -> {
            int expenseId = expenseIds.get(position);
            Intent intent = new Intent(ExpenseTrackerActivity.this, ExpenseDetailsActivity.class);
            intent.putExtra("expense_id", expenseId);
            startActivity(intent);
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

    private void loadExpenses(int petId) {
        List<String> expenses = new ArrayList<>();
        expenseIds.clear();

        Cursor cursor = dbHelper.getReadableDatabase().query(
                DatabaseHelper.TABLE_EXPENSES,
                new String[]{
                        DatabaseHelper.COLUMN_EXPENSE_ID,
                        DatabaseHelper.COLUMN_EXPENSE_DATE,
                        DatabaseHelper.COLUMN_EXPENSE_FOOD,
                        DatabaseHelper.COLUMN_EXPENSE_MEDICINE,
                        DatabaseHelper.COLUMN_EXPENSE_OTHER
                },
                DatabaseHelper.COLUMN_PET_ID + "=?",
                new String[]{String.valueOf(petId)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                int expenseId = cursor.getInt(0);
                String expenseSummary = "Date: " + cursor.getString(1) +
                        " | Food: LKR" + cursor.getDouble(2) +
                        " | Medicine: LKR" + cursor.getDouble(3) +
                        " | Other: LKR" + cursor.getDouble(4);

                expenses.add(expenseSummary);
                expenseIds.add(expenseId);
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenses);
        expenseListView.setAdapter(adapter);
    }
}

package com.example.petcaremanager.Page.PetProfiles;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.DB.DatabaseHelper;
import com.example.petcaremanager.R;

public class ViewPetProfilesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pet_profiles);

        listView = findViewById(R.id.listViewPets);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Display pet profiles
        displayPetProfiles();
    }

    private void displayPetProfiles() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query the database
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PETS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Columns to display
        String[] from = {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_AGE, DatabaseHelper.COLUMN_BREED};
        int[] to = {R.id.textViewName, R.id.textViewAge, R.id.textViewBreed};

        // Create adapter with custom bindView for setting tags
        adapter = new SimpleCursorAdapter(
                this,
                R.layout.pet_list_item_with_buttons,
                cursor,
                from,
                to,
                0
        ) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                super.bindView(view, context, cursor);

                // Get buttons
                View editButton = view.findViewById(R.id.buttonEdit);
                View deleteButton = view.findViewById(R.id.buttonDelete);

                // Set tags with the current cursor position
                int position = cursor.getPosition();
                editButton.setTag(position);
                deleteButton.setTag(position);

                // Debug log to verify tags
                Log.d("DEBUG", "Setting tag for position: " + position);
            }
        };

        listView.setAdapter(adapter);
    }

    public void onDeleteClick(View view) {
        Object tag = view.getTag();
        if (tag == null) {
            Toast.makeText(this, "Error: Tag not set for this item", Toast.LENGTH_SHORT).show();
            return;
        }

        int position = (int) tag;
        Cursor cursor = (Cursor) adapter.getItem(position);
        int petId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));

        // Confirm deletion
        new AlertDialog.Builder(this)
                .setTitle("Delete Pet")
                .setMessage("Are you sure you want to delete this pet?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete(DatabaseHelper.TABLE_PETS, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(petId)});
                    Toast.makeText(this, "Pet deleted", Toast.LENGTH_SHORT).show();
                    displayPetProfiles();
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void onEditClick(View view) {
        Object tag = view.getTag();
        if (tag == null) {
            Toast.makeText(this, "Error: Tag not set for this item", Toast.LENGTH_SHORT).show();
            return;
        }

        int position = (int) tag;
        Cursor cursor = (Cursor) adapter.getItem(position);
        if (cursor == null) {
            Toast.makeText(this, "Error: Unable to retrieve item details", Toast.LENGTH_SHORT).show();
            return;
        }

        int petId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
        String petName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
        int petAge = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE));
        String petBreed = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BREED));


        EditPetDialogFragment dialogFragment = EditPetDialogFragment.newInstance(petId, petName, petAge, petBreed);
        dialogFragment.setOnPetUpdatedListener(() -> displayPetProfiles());
        dialogFragment.show(getSupportFragmentManager(), "EditPetDialog");
    }
}
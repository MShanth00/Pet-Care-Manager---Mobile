package com.example.petcaremanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.petcaremanager.DB.DatabaseHelper;

public class EditPetDialogFragment extends DialogFragment {

    private int petId;
    private String petName;
    private int petAge;
    private String petBreed;

    private EditText editTextName, editTextAge, editTextBreed;
    private OnPetUpdatedListener onPetUpdatedListener;

    public static EditPetDialogFragment newInstance(int petId, String petName, int petAge, String petBreed) {
        EditPetDialogFragment fragment = new EditPetDialogFragment();
        Bundle args = new Bundle();
        args.putInt("petId", petId);
        args.putString("petName", petName);
        args.putInt("petAge", petAge);
        args.putString("petBreed", petBreed);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            petId = getArguments().getInt("petId");
            petName = getArguments().getString("petName");
            petAge = getArguments().getInt("petAge");
            petBreed = getArguments().getString("petBreed");
        }

        // Inflate the custom layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_pet, null);

        // Initialize EditText fields
        editTextName = dialogView.findViewById(R.id.editTextName);
        editTextAge = dialogView.findViewById(R.id.editTextAge);
        editTextBreed = dialogView.findViewById(R.id.editTextBreed);

        // Set initial values
        editTextName.setText(petName);
        editTextAge.setText(String.valueOf(petAge));
        editTextBreed.setText(petBreed);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Pet");
        builder.setView(dialogView);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = editTextName.getText().toString();
            String ageStr = editTextAge.getText().toString();
            String breed = editTextBreed.getText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(breed)) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = Integer.parseInt(ageStr);
            updatePet(petId, name, age, breed);
        });

        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }

    private void updatePet(int id, String name, int age, String breed) {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_AGE, age);
        values.put(DatabaseHelper.COLUMN_BREED, breed);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated = db.update(DatabaseHelper.TABLE_PETS, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        if (rowsUpdated > 0) {
            Toast.makeText(requireContext(), "Pet updated successfully", Toast.LENGTH_SHORT).show();
            if (onPetUpdatedListener != null) onPetUpdatedListener.onPetUpdated();
        } else {
            Toast.makeText(requireContext(), "Failed to update pet", Toast.LENGTH_SHORT).show();
        }
    }

    public void setOnPetUpdatedListener(OnPetUpdatedListener listener) {
        this.onPetUpdatedListener = listener;
    }

    public interface OnPetUpdatedListener {
        void onPetUpdated();
    }
}

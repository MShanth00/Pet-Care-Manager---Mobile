package com.example.petcaremanager.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PetCare.db";
    private static final int DATABASE_VERSION = 2; // Incremented database version

    // Table and column names for the "pets" table
    public static final String TABLE_PETS = "pets";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_BREED = "breed";

    // Table and column names for the "health_records" table
    public static final String TABLE_HEALTH_RECORDS = "health_records";
    public static final String COLUMN_HEALTH_ID = "id";
    public static final String COLUMN_PET_ID = "pet_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_NOTES = "notes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create "pets" table
        String createPetsTable = "CREATE TABLE " + TABLE_PETS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_AGE + " INTEGER NOT NULL, " +
                COLUMN_BREED + " TEXT)";
        db.execSQL(createPetsTable);

        // Create "health_records" table
        String createHealthRecordsTable = "CREATE TABLE " + TABLE_HEALTH_RECORDS + " (" +
                COLUMN_HEALTH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PET_ID + " INTEGER NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_NOTES + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_PET_ID + ") REFERENCES " + TABLE_PETS + "(" + COLUMN_ID + ") " +
                "ON DELETE CASCADE)";
        db.execSQL(createHealthRecordsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Check if the "health_records" table exists
            String addDescriptionColumn = "ALTER TABLE " + TABLE_HEALTH_RECORDS +
                    " ADD COLUMN " + COLUMN_DESCRIPTION + " TEXT";
            db.execSQL(addDescriptionColumn);

            // Add other necessary upgrades for version 2 if any
        }
    }

    public boolean deleteHealthRecord(int recordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_HEALTH_RECORDS, COLUMN_HEALTH_ID + " = ?", new String[]{String.valueOf(recordId)});
        return rowsAffected > 0;
    }



}

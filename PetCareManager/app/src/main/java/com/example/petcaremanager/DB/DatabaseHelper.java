package com.example.petcaremanager.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PetCare.db";
    private static final int DATABASE_VERSION = 5; // Incremented database version

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


    // Table and column names for the "diet_plans" table
    public static final String TABLE_DIET_PLANS = "diet_plans";
    public static final String COLUMN_DIET_ID = "diet_id";
    public static final String COLUMN_FOOD_TYPE = "food_type";
    public static final String COLUMN_FOOD_QUANTITY = "food_quantity";
    public static final String COLUMN_MEDICINE = "medicine";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";


    // Table and column names for the "expenses" table
    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_EXPENSE_ID = "expense_id";
    public static final String COLUMN_EXPENSE_FOOD = "food_expense";
    public static final String COLUMN_EXPENSE_MEDICINE = "medicine_expense";
    public static final String COLUMN_EXPENSE_OTHER = "other_expense";
    public static final String COLUMN_EXPENSE_DATE = "expense_date";

    // Inside DatabaseHelper class, add this table
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASK_ID = "task_id";
    public static final String COLUMN_TASK_DATE = "task_date";
    public static final String COLUMN_TASK_DESCRIPTION = "task_description";


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

        String createDietPlansTable = "CREATE TABLE " + TABLE_DIET_PLANS + " (" +
                COLUMN_DIET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PET_ID + " INTEGER NOT NULL, " +
                COLUMN_START_DATE + " TEXT NOT NULL, " +
                COLUMN_END_DATE + " TEXT NOT NULL, " +
                COLUMN_FOOD_TYPE + " TEXT NOT NULL, " +
                COLUMN_FOOD_QUANTITY + " TEXT NOT NULL, " +
                COLUMN_MEDICINE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_PET_ID + ") REFERENCES " + TABLE_PETS + "(" + COLUMN_ID + ") " +
                "ON DELETE CASCADE)";
        db.execSQL(createDietPlansTable);

        String createExpensesTable = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PET_ID + " INTEGER NOT NULL, " +
                COLUMN_EXPENSE_FOOD + " REAL DEFAULT 0, " +
                COLUMN_EXPENSE_MEDICINE + " REAL DEFAULT 0, " +
                COLUMN_EXPENSE_OTHER + " REAL DEFAULT 0, " +
                COLUMN_EXPENSE_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_PET_ID + ") REFERENCES " + TABLE_PETS + "(" + COLUMN_ID + ") " +
                "ON DELETE CASCADE)";
        db.execSQL(createExpensesTable);

        String createTasksTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_DATE + " TEXT NOT NULL, " +
                COLUMN_TASK_DESCRIPTION + " TEXT NOT NULL)";
        db.execSQL(createTasksTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            String createTasksTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TASK_DATE + " TEXT NOT NULL, " +
                    COLUMN_TASK_DESCRIPTION + " TEXT NOT NULL)";
            db.execSQL(createTasksTable);
        }
    }

    public boolean deleteHealthRecord(int recordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_HEALTH_RECORDS, COLUMN_HEALTH_ID + " = ?", new String[]{String.valueOf(recordId)});
        return rowsAffected > 0;
    }



}

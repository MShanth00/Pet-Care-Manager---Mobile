package com.example.petcaremanager.Page.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.petcaremanager.DB.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private final DatabaseHelper dbHelper;

    public TaskManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

        public boolean addTask(String date, String description) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TASK_DATE, date);
        values.put(DatabaseHelper.COLUMN_TASK_DESCRIPTION, description);

        long result = db.insert(DatabaseHelper.TABLE_TASKS, null, values);
        return result != -1;
    }

        public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_TASKS + " ORDER BY " + DatabaseHelper.COLUMN_TASK_DATE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DESCRIPTION))
                );
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

        public boolean deleteTask(int taskId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete(DatabaseHelper.TABLE_TASKS, DatabaseHelper.COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        return rowsAffected > 0;
    }

        public static class Task {
            private final int id;
            private final String date;
            private final String description;

            public Task(int id, String date, String description) {
                this.id = id;
                this.date = date;
                this.description = description;
            }

            public int getId() {
                return id;
            }

            public String getDate() {
                return date;
            }

            public String getDescription() {
                return description;
            }
        }
}

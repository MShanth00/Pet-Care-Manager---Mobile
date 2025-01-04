package com.example.petcaremanager.Page.Task;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcaremanager.R;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends AppCompatActivity {
    private TaskManager taskManager;
    private ArrayAdapter<String> adapter;
    private List<TaskManager.Task> tasks;
    private ListView listViewTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        taskManager = new TaskManager(this);
        listViewTasks = findViewById(R.id.listViewTasks);
        EditText editTextDate = findViewById(R.id.editTextDate);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        Button buttonAddTask = findViewById(R.id.buttonAddTask);

        loadTasks();

        buttonAddTask.setOnClickListener(v -> {
            String date = editTextDate.getText().toString();
            String description = editTextDescription.getText().toString();

            if (!date.isEmpty() && !description.isEmpty()) {
                if (taskManager.addTask(date, description)) {
                    Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
                    loadTasks();
                    editTextDate.setText("");
                    editTextDescription.setText("");
                } else {
                    Toast.makeText(this, "Error Adding Task", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });

        listViewTasks.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            TaskManager.Task task = tasks.get(position);
            if (taskManager.deleteTask(task.getId())) {
                Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show();
                loadTasks();
            } else {
                Toast.makeText(this, "Error Deleting Task", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void loadTasks() {
        tasks = taskManager.getAllTasks();
        List<String> taskDescriptions = new ArrayList<>();
        for (TaskManager.Task task : tasks) {
            taskDescriptions.add(task.getDate() + " - " + task.getDescription());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskDescriptions);
        listViewTasks.setAdapter(adapter);
    }
}

package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NotesDatabase db;
    private ListView listViewNotes;
    private ArrayList<String> notesList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new NotesDatabase(this);
        listViewNotes = findViewById(R.id.note_list);

        notesList = db.getAllNoteNames();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        listViewNotes.setAdapter(adapter);

        listViewNotes.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTitle = notesList.get(position);
            String noteContent = db.getNoteContentByName(selectedTitle);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(selectedTitle)
                    .setMessage(noteContent.isEmpty() ? "(Empty note)" : noteContent)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null && adapter != null) {
            notesList.clear();
            notesList.addAll(db.getAllNoteNames());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_delete) {
            Intent intent = new Intent(MainActivity.this, DeleteNoteActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
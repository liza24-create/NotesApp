package com.example.notesapp;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class DeleteNoteActivity extends AppCompatActivity {

    private NotesDatabase db;
    private ListView listViewNotes;
    private ArrayList<String> notesList;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_note);
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
            String selectedNote = notesList.get(position);
            showDeleteConfirmationDialog(position, selectedNote);
        });
    }


    private void showDeleteConfirmationDialog(final int position, String noteTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete \"" + noteTitle + "\"?");

        builder.setPositiveButton(R.string.btn_delete, (dialog, which) -> {
            boolean deleted = db.deleteNoteByName(noteTitle);
            if (deleted) {
                notesList.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(DeleteNoteActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DeleteNoteActivity.this, "Error deleting note", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.btn_cancel, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
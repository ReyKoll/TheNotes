package com.example.thenotes.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thenotes.R;
import com.example.thenotes.adapters.NoteAdapter;
import com.example.thenotes.database.NotesDatabase;
import com.example.thenotes.entities.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
    private RecyclerView recycler_view_notes;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;

    public static final int REQUEST_CODE_ADD_NOTE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        /* Status bar section */
        //  set status text dark
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // set status background color = main background color
        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        NotesActivity.this,
                        R.color.color_main_bg)
        );

        ImageView image_add_fab = findViewById(R.id.image_add_fab);
        image_add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });
        recycler_view_notes = findViewById(R.id.recycler_view_notes);
        recycler_view_notes.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2,
                        StaggeredGridLayoutManager.VERTICAL
                )
        );
        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList);
        recycler_view_notes.setAdapter(noteAdapter);

        getNotes();
    }

    private void getNotes() {

        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase.getNotesDatabase(getApplicationContext())
                        .noteDataAccessObject()
                        .getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (noteList.size() == 0) {
                    noteList.addAll(notes);
                    noteAdapter.notifyDataSetChanged();
                }
                else {
                    noteList.add(0, notes.get(0));
                    noteAdapter.notifyItemInserted(0);
                }
                recycler_view_notes.smoothScrollToPosition(0);
            }
        }
        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes();
        }
    }
}

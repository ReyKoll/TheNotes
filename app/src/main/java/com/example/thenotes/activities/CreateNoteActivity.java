package com.example.thenotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thenotes.R;
import com.example.thenotes.database.NotesDatabase;
import com.example.thenotes.entities.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {
    private EditText input_title, input_subtitle, input_note;
    private TextView text_date;

    final Note note = new Note();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        /* Status bar section */
        //  set status text dark
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // set status background color = main background color
        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                    CreateNoteActivity.this,
                    R.color.color_main_bg)
        );

        ImageView image_back_create_notes = findViewById(R.id.image_back_create_notes);
        image_back_create_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        input_title = findViewById(R.id.input_title);
        input_subtitle = findViewById(R.id.input_subtitle);
        input_note = findViewById(R.id.input_note);

        text_date = findViewById(R.id.text_date);
        text_date.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date())
        );

        ImageView image_save = findViewById(R.id.image_save);
        image_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    private void saveNote() {
        if (input_title.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Title cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        } else if (input_subtitle.getText().toString().trim().isEmpty()
                && input_note.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        note.setTitle(input_title.getText().toString());
        note.setSubtitle(input_subtitle.getText().toString());
        note.setNote(input_note.getText().toString());
        note.setDate(text_date.getText().toString());

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDataAccessObject().insert(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNoteTask().execute();
    }
}
package com.example.thenotes.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thenotes.R;
import com.example.thenotes.database.NotesDatabase;
import com.example.thenotes.entities.Note;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {
    private EditText input_title, input_subtitle, input_note;
    private TextView text_date;
    private ImageView image_back_create_notes, image_add, image_save, image_note, image_note_delete;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    private AlertDialog dialogDeleteNote;

    private String selectedImagePath;

    final Note note = new Note();
    private Note availableNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        /* Status bar section */
        //  set status text dark
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // set status bar color
        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                    CreateNoteActivity.this,
                    R.color.color_main_bg)
        );

        /* Images id */
        image_back_create_notes = findViewById(R.id.image_back_create_notes);
        image_add = findViewById(R.id.image_add);
        image_save = findViewById(R.id.image_save);
        image_note = findViewById(R.id.image_note);

        /* Inputs id */
        input_title = findViewById(R.id.input_title);
        input_subtitle = findViewById(R.id.input_subtitle);
        input_note = findViewById(R.id.input_note);

        /* Texts id */
        text_date = findViewById(R.id.text_date);
        text_date.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date())
        );

        image_back_create_notes.setOnClickListener(view -> onBackPressed());

        image_add.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        CreateNoteActivity.this,
                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                        REQUEST_CODE_STORAGE_PERMISSION
                );
            }
            else {
                selectImage();
            }
        });

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            availableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }

        findViewById(R.id.image_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_note.setImageBitmap(null);
                image_note.setVisibility(View.GONE);
                findViewById(R.id.image_remove).setVisibility(View.GONE);
                selectedImagePath = "";
            }
        });

        image_save.setOnClickListener(view -> saveNote());

        selectedImagePath = "";
    }

    private void setViewOrUpdateNote() {
        input_title.setText(availableNote.getTitle());
        input_subtitle.setText(availableNote.getSubtitle());
        input_note.setText(availableNote.getNote());
        text_date.setText(availableNote.getDate());

        if (availableNote.getImage_path() != null && !availableNote.getImage_path().trim().isEmpty()) {
            image_note.setImageBitmap(BitmapFactory.decodeFile(availableNote.getImage_path()));
            image_note.setVisibility(View.VISIBLE);
            findViewById(R.id.image_remove).setVisibility(View.VISIBLE);
            selectedImagePath = availableNote.getImage_path();
        }

        if (availableNote != null) {
            findViewById(R.id.image_note_delete).setVisibility(View.VISIBLE);
            findViewById(R.id.image_note_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteNoteDialog();
                }
            });
        }
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
        note.setImage_path(selectedImagePath);

        if (availableNote != null)
            note.setId(availableNote.getId());

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

    private void showDeleteNoteDialog() {
        if (dialogDeleteNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layout_delete_note_container)
            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if (dialogDeleteNote.getWindow() != null)
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));

            view.findViewById(R.id.text_delete_note).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    @SuppressLint("StaticFieldLeak")
                    class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            NotesDatabase.getNotesDatabase(getApplicationContext())
                                    .noteDataAccessObject()
                                    .delete(availableNote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute(unused);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                    new DeleteNoteTask().execute();
                }
            });
            view.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDeleteNote.dismiss();
                }
            });
        }
        dialogDeleteNote.show();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        image_note.setImageBitmap(bitmap);
                        image_note.setVisibility(View.VISIBLE);
                        findViewById(R.id.image_remove).setVisibility(View.VISIBLE);

                        selectedImagePath = getPathFromUri(selectedImageUri);
                    }
                    catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;

        Cursor cursor = getContentResolver()
                .query(
                        contentUri,
                        null,
                        null,
                        null,
                        null
                );

        if (cursor == null) {
            filePath = contentUri.getPath();
        }
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }

        return filePath;
    }
}
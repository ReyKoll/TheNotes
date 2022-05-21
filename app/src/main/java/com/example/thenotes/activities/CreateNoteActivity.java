package com.example.thenotes.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
    private ImageView image_back_create_notes, image_add, image_save, image_note;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    private String selectedImagePath;

    final Note note = new Note();

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

        image_save.setOnClickListener(view -> saveNote());

        selectedImagePath = "";
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
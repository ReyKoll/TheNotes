package com.example.thenotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.thenotes.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_TheNotes);
        setContentView(R.layout.activity_main);

        /* Status bar section */
        //  set status text dark
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // set status background color = main background color
        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        MainActivity.this,
                        R.color.color_main_bg)
        );

        LinearLayout layout_note_hub = findViewById(R.id.layout_note_hub);
        layout_note_hub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NotesActivity.class);
                startActivity(intent);
            }
        });
    }
}
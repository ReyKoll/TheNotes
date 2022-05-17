package com.example.thenotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;

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

    }
}
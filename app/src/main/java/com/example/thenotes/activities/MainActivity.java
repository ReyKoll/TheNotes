package com.example.thenotes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.thenotes.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_TheNotes);
        setContentView(R.layout.activity_main);
    }
}
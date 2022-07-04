package com.example.thenotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.service.controls.templates.ControlTemplate;
import android.view.View;
import android.widget.ImageView;

import com.example.thenotes.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        /* Status bar section */
        //  set status text dark
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // set status bar color
        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        ChartActivity.this,
                        R.color.color_main_bg)
        );

        ImageView image_back_chart = findViewById(R.id.image_back_chart);
        image_back_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        PieChart pie_chart = findViewById(R.id.pie_chart);
        ArrayList<PieEntry> currency = new ArrayList<>();

        currency.add(new PieEntry(500, "$"));
        currency.add(new PieEntry(600, "€"));
        currency.add(new PieEntry(700, "₽"));
        currency.add(new PieEntry(800, "BTC"));
        currency.add(new PieEntry(900, "ETH"));

        PieDataSet pieDataSet = new PieDataSet(currency, "Currency");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pie_chart.setData(pieData);
        pie_chart.getDescription().setEnabled(false);
        pie_chart.animate();
    }
}
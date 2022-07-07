package com.example.thenotes.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thenotes.R;
import com.example.thenotes.helpers.ChartHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
    private AlertDialog dialogAddToChart;

    PieChart pie_chart;
    EditText input_value, input_label;
    TextView text_add, text_delete, text_add_dialog_chart, text_cancel_dialog_chart;
    ImageView image_back_chart;

    ChartHelper chartHelper;
    SQLiteDatabase chartDatabase;

    PieDataSet pieDataSet = new PieDataSet(null, null);
    PieData pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        //region Status bar section
        //  set status text dark
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // set status bar color
        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        ChartActivity.this,
                        R.color.color_main_bg)
        );
        // endregion

        chartHelper = new ChartHelper(this);
        try {
            chartHelper.updateDataBase();
        }
        catch (IOException mIOException) {
            throw new Error("Unable To Update Database");
        }

        try {
            chartDatabase = chartHelper.getWritableDatabase();
        }
        catch (SQLException mSQLException) {
            throw mSQLException;
        }

        // region Views ID
        image_back_chart = findViewById(R.id.image_back_chart);
        pie_chart = findViewById(R.id.pie_chart);
        input_value = findViewById(R.id.input_value);
        input_label = findViewById(R.id.input_label);
        text_add = findViewById(R.id.text_add);
        text_delete = findViewById(R.id.text_delete);
        text_add_dialog_chart = findViewById(R.id.text_add_dialog_chart);
        text_cancel_dialog_chart = findViewById(R.id.text_cancel_dialog_chart);
        //endregion

        image_back_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        text_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddToChartDialog();
            }
        });

        pie_chart.setNoDataText("There is no data yet");

        showPieChart();
    }

    private void showAddToChartDialog() {
        if (dialogAddToChart == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChartActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_chart_values,
                    (ViewGroup) findViewById(R.id.layout_add_chart_values_container)
            );
            builder.setView(view);
            dialogAddToChart = builder.create();

            if (dialogAddToChart.getWindow() != null)
                dialogAddToChart.getWindow().setBackgroundDrawable(new ColorDrawable(0));

            view.findViewById(R.id.text_add_dialog_chart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //showPieChart();

                }
            });

            view.findViewById(R.id.text_cancel_dialog_chart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogAddToChart.dismiss();
                }
            });
        }
        dialogAddToChart.show();
    }

    private ArrayList<PieEntry> getDataValues() {
        ArrayList<PieEntry> values = new ArrayList<>();
        Cursor cursor = chartDatabase.rawQuery(
                "SELECT * FROM ChartTable", null
        );
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            values.add(new PieEntry(cursor.getFloat(0), String.valueOf(cursor.getString(1))));
            cursor.moveToNext();
        }
        cursor.close();

        return values;
    }

    private void showPieChart() {
        chartHelper = new ChartHelper(this);

        chartDatabase = chartHelper.getWritableDatabase();

        pieDataSet.setValues(getDataValues());
        pieDataSet.setLabel("Currency");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(15f);

        pieData = new PieData(pieDataSet);

        pie_chart.setData(pieData);
        pie_chart.getDescription().setEnabled(false);
        pie_chart.animate();

        pieDataSet.setValueLineWidth(4);
    }
}

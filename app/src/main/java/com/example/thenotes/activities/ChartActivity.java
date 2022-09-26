package com.example.thenotes.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thenotes.R;
import com.example.thenotes.helpers.ChartHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
    private AlertDialog dialogAddToChart, dialogDelete;

    TextView text_add, text_delete, text_add_dialog_chart, text_cancel_dialog_chart, text_cancel_dialog_chart_delete, text_delete_dialog_chart;
    ImageView image_back_chart;
    EditText input_value, input_label;

    ChartHelper chartHelper;
    SQLiteDatabase chartDatabase;

    PieChart pie_chart;
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

        // region Views ID
        image_back_chart = findViewById(R.id.image_back_chart);
        pie_chart = (PieChart)findViewById(R.id.pie_chart);

        text_add = findViewById(R.id.text_add);
        text_delete = findViewById(R.id.text_delete);
        text_add_dialog_chart = findViewById(R.id.text_add_dialog_chart);
        text_cancel_dialog_chart = findViewById(R.id.text_cancel_dialog_chart);
        text_delete_dialog_chart = findViewById(R.id.text_delete_dialog_chart);
        text_cancel_dialog_chart_delete = findViewById(R.id.text_cancel_dialog_chart_delete);
        //endregion

        chartHelper = new ChartHelper(getApplicationContext());
        chartDatabase = chartHelper.getWritableDatabase();

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

        text_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog();
            }
        });

        pie_chart.setNoDataText("There is no data yet");
        pie_chart.setNoDataTextColor(R.color.color_primary);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chartDatabase = chartHelper.getReadableDatabase();
        showPieChart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chartDatabase.close();
    }

    private void showAddToChartDialog() {
        if (dialogAddToChart == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChartActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_chart_values,
                    findViewById(R.id.layout_add_chart_values_container)
            );

            builder.setView(view);
            dialogAddToChart = builder.create();

            if (dialogAddToChart.getWindow() != null)
                dialogAddToChart.getWindow().setBackgroundDrawable(new ColorDrawable(0));

            view.findViewById(R.id.text_add_dialog_chart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //save();
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

    private void showDeleteDialog() {
        if (dialogDelete == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChartActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_chart_values,
                    findViewById(R.id.layout_delete_chart_values_container)
            );

            builder.setView(view);
            dialogDelete = builder.create();

            if (dialogDelete.getWindow() != null)
                dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(0));

            view.findViewById(R.id.text_delete_dialog_chart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete();
                }
            });

            view.findViewById(R.id.text_cancel_dialog_chart_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDelete.dismiss();
                }
            });
        }
        dialogDelete.show();
    }

    private ArrayList<PieEntry> getDataValues() {
        ArrayList<PieEntry> values = new ArrayList<>();
        Cursor cursor = chartDatabase.rawQuery(
                "SELECT * FROM " + ChartHelper.TABLE_NAME,
                null
        );

        for (int i = 0; i < cursor.getCount(); ++i) {
            cursor.moveToNext();
            values.add(new PieEntry(cursor.getFloat(2), String.valueOf(cursor.getString(1))));
        }
        cursor.close();

        return values;
    }

//    private void save() {
//        ContentValues contentValues = new ContentValues();
//
//        View view = LayoutInflater.from(this).inflate(
//                R.layout.layout_add_chart_values,
//                findViewById(R.id.layout_add_chart_values_container)
//        );
//        input_value = view.findViewById(R.id.input_value);
//        input_label = view.findViewById(R.id.input_label);
//
//        String value = input_value.getText().toString();
//        String label = input_label.getText().toString();
//
//        contentValues.put(ChartHelper.COLUMN_LABEL, label);
//        contentValues.put(ChartHelper.COLUMN_CURRENCY, value);
//
//
//            chartDatabase.update(
//                    ChartHelper.TABLE_NAME,
//                    contentValues,
//                    ChartHelper.COLUMN_ID + "=" + userID,
//                    null
//            );
//        else
//            chartDatabase.insert(
//                    ChartHelper.TABLE_NAME,
//                    null,
//                    contentValues
//            );
//    }
//
//    private void delete() {
//        chartDatabase.delete(
//                ChartHelper.TABLE_NAME,
//                "_id = ?",
//                new String[]{String.valueOf(userID)}
//        );
//    }

    private void showPieChart() {
        chartHelper = new ChartHelper(this);

        chartDatabase = chartHelper.getWritableDatabase();

        pieDataSet.setValues(getDataValues());
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(15f);
        pieDataSet.setValueLineWidth(4);

        pieData = new PieData(pieDataSet);

        pie_chart.setData(pieData);
        pie_chart.getDescription().setEnabled(false);
        pie_chart.animate();
    }
}

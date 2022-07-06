package com.example.thenotes.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ChartHelper extends SQLiteOpenHelper {
    public static final String createTable = "CREATE TABLE ChartTable (currency Real, label Text)";
    public static final String dropTable = "DROP TABLE IF EXISTS ChartTable";

    public ChartHelper(@Nullable Context context) {
        super(
                context,
                "ChartDatabase.db",
                null,
                1
        );
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(createTable);
    }

    public Cursor getValues() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT currency, label FROM ChartTable", null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL(dropTable);
        database.execSQL(createTable);
    }

    public void insert(String currency, String label) {
        SQLiteDatabase database = getWritableDatabase();

        if (database != null) {
            database.execSQL("INSERT INTO ChartTable VALUES ('"+currency+"', '"+label+"')");
            database.close();
        }
    }
}

package com.example.thenotes.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChartHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chart.db";
    private static final int VERSION = 1;

    public static final String TABLE_NAME = "chart";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CURRENCY = "currency";
    public static final String COLUMN_LABEL = "label";

    public ChartHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LABEL + " TEXT, " +
                COLUMN_CURRENCY + " REAL);";

        String PROGRAM_INSERT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_LABEL
                + ", " + COLUMN_CURRENCY  + ") VALUES ('BTC', 300), ('$', 1500), ('€', 1000);";

        database.execSQL(CREATE_TABLE);
        database.execSQL(PROGRAM_INSERT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}

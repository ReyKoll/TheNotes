package com.example.thenotes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.thenotes.dao.NoteDataAccessObject;
import com.example.thenotes.entities.Note;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase notesDatabase;

    public static synchronized NotesDatabase getNotesDatabase(Context context) {
        if (notesDatabase == null) {
            notesDatabase = Room.databaseBuilder(
                    context,
                    NotesDatabase.class,
                    "note_database"
            ).build();
        }
        return notesDatabase;
    }

    public abstract NoteDataAccessObject noteDataAccessObject();
}

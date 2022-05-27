package com.example.thenotes.listeners;

import com.example.thenotes.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
}

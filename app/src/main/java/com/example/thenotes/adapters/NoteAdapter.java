package com.example.thenotes.adapters;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thenotes.R;
import com.example.thenotes.entities.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{
    private List<Note> noteList;

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(noteList.get(position));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView text_title, text_subtitle, text_date;
        ImageView image_in_note;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            text_title = itemView.findViewById(R.id.text_title);
            text_subtitle = itemView.findViewById(R.id.text_subtitle);
            text_date = itemView.findViewById(R.id.text_date);
            image_in_note = itemView.findViewById(R.id.image_in_note);
        }

        void setNote(Note note) {
            text_title.setText(note.getTitle());

            if (note.getSubtitle().trim().isEmpty())
                text_subtitle.setVisibility(View.GONE);
            else
                text_subtitle.setText(note.getSubtitle());

            text_date.setText(note.getDate());

            if (note.getImage_path() != null) {
                image_in_note.setImageBitmap(BitmapFactory.decodeFile(note.getImage_path()));
                image_in_note.setVisibility(View.VISIBLE);
            }
            else {
                image_in_note.setVisibility(View.GONE);
            }
        }
    }
}

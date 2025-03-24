package com.example.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private List<Note> notes;
    private OnNoteClickListener onNoteClickListener;

    public NoteAdapter(Context context, List<Note> notes, OnNoteClickListener onNoteClickListener) {
        this.context = context;
        this.notes = notes;
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.noteHeadingTextView.setText(note.getHeading());
        holder.noteDetailsTextView.setText(note.getDetails());


        // Bấm vào ghi chú
        holder.itemView.setOnClickListener(view -> {
            if (onNoteClickListener != null) {
                onNoteClickListener.onNoteClick(note);
            }
        });

        // Nhấn giữ để xóa
        holder.itemView.setOnLongClickListener(view -> {
            showDeleteDialog(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    // Cập nhật danh sách ghi chú
    public void setNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    Note noteToDelete = notes.get(position);
                    deleteNoteFromDatabase(noteToDelete.getId());
                    notes.remove(position);
                    notifyItemRemoved(position);
                    showToast("Note deleted");
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                })
                .show();
    }

    private void deleteNoteFromDatabase(long noteId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.deleteNoteById(noteId);
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // ViewHolder cho ghi chú
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteHeadingTextView, noteDetailsTextView;
        View colorDot;
        CardView cardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            noteHeadingTextView = itemView.findViewById(R.id.noteHeadingTextView);
            noteDetailsTextView = itemView.findViewById(R.id.noteDetailsTextView);
            colorDot = itemView.findViewById(R.id.colorDot);
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onNoteLongClick(Note note);
    }
}

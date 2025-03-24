package com.example.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    private RecyclerView noteRecyclerView;
    private Button newNoteButton;
    private NoteAdapter noteAdapter;
    private List<Note> notes;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteRecyclerView = findViewById(R.id.noteRecyclerView);
        newNoteButton = findViewById(R.id.newNoteButton);

        databaseHelper = new DatabaseHelper(this);
        notes = fetchNotesFromDatabase();

        noteAdapter = new NoteAdapter(this, notes, this);
        noteRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        noteRecyclerView.setAdapter(noteAdapter);

        // Thêm sự kiện click vào nút tạo ghi chú mới
        newNoteButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNotes();
    }

    private List<Note> fetchNotesFromDatabase() {
        return databaseHelper.getAllNotes();
    }

    private void refreshNotes() {
        notes.clear();
        notes.addAll(fetchNotesFromDatabase());
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(Note note) {
        // Mở NoteDetailActivity để chỉnh sửa
        Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
        intent.putExtra("noteId", note.getId());
        startActivity(intent);
    }

    @Override
    public void onNoteLongClick(Note note) {
        showDeleteDialog(note);
    }

    private void showDeleteDialog(final Note noteToDelete) {
        new AlertDialog.Builder(this)
                .setMessage("Bạn có chắc chắn muốn xóa ghi chú này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    deleteNoteFromDatabase(noteToDelete.getId());
                    refreshNotes();
                    showToast("Ghi chú đã được xóa");
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteNoteFromDatabase(long noteId) {
        databaseHelper.deleteNoteById(noteId);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

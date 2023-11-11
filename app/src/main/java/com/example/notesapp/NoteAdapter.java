package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>{

    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.tittleTextView.setText(note.tittle);
        holder.contentTextView.setText(note.content);

        holder.itemView.setOnClickListener((v)-> {
            Intent intent = new Intent(context, NoteDetailsActivity.class);
            intent.putExtra("tittle", note.tittle);
            intent.putExtra("content", note.content);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView tittleTextView, contentTextView;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tittleTextView = itemView.findViewById(R.id.note_tittle_text_view);
            contentTextView = itemView.findViewById(R.id.notes_content_text_view);
        }
    }
}

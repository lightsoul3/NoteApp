package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.lang.annotation.Documented;
import java.sql.Timestamp;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteButton;
    TextView pageTittleTextView;
    String tittle, content, docId;
    boolean isEditMode= false;
    TextView deleteNoteTextViewButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteButton = findViewById(R.id.save_note_button);
        pageTittleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewButton = findViewById(R.id.delete_note_text_view_button);

        tittle = getIntent().getStringExtra("tittle");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        titleEditText.setText(tittle);
        contentEditText.setText(content);

        if(docId != null && !docId.isEmpty()){
            isEditMode = true;
        }

        if(isEditMode){
            pageTittleTextView.setText("Edit your note");
            deleteNoteTextViewButton.setVisibility(View.VISIBLE);
        }

        saveNoteButton.setOnClickListener((v)-> saveNote());

        deleteNoteTextViewButton.setOnClickListener((v)-> deleteNoteFromFireBase());
    }

    void  saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if(noteTitle==null || noteTitle.isEmpty()){
            titleEditText.setError("Tittle is required");
            return;
        }

        Note note = new Note();
        note.setTittle(noteTitle);
        note.setContent(noteContent);

        saveNoteToFireBase(note);
    }

    void saveNoteToFireBase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            documentReference = Utility.getCollectionReferenceForNote().document(docId);
        }else {
            documentReference = Utility.getCollectionReferenceForNote().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(NoteDetailsActivity.this, "Note added");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this, "Failed while adding note");
                }
           }
        });
    }

    void deleteNoteFromFireBase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNote().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(NoteDetailsActivity.this, "Note deleted");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this, "Failed while deleting note");
                }
            }
        });
    }
}
package com.example.notesapp;

import java.sql.Timestamp;

public class Note {
    String tittle;
    String content;

    public Note() {
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

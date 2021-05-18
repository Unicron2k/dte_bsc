package com.example.a8_2_room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")

public class Word {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id = 0;

    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;

    @NonNull
    @ColumnInfo(name = "description")
    private String mDescription;

    public Word(@NonNull int id, @NonNull String word, @NonNull String description) {
        this.id = id;
        this.mWord = word;
        this.mDescription = description;
    }

    public int getId(){return this.id;}
    public String getWord(){return this.mWord;}
    public String getDescription(){return this.mDescription;}

}
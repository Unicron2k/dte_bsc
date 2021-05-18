package com.example.a8_3_db_comms_room.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.NotSerializableException;
import java.io.Serializable;

@Entity(tableName = "CategoryTable")
public class Category implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idCategory")
    private int idCategory = 0;

    @NonNull
    @ColumnInfo(name = "category")
    private String category = "category";

    public Category(int idCategory, @NonNull String category) {
        this.idCategory = idCategory;
        this.category = category;
    }

    public int getIdCategory() { return idCategory; }

    @NonNull
    public String getCategory() { return category; }

    public void setIdCategory(int idCategory) { this.idCategory = idCategory; }

    public void setCategory(@NonNull String category) { this.category = category; }
}

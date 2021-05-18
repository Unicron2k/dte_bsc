package com.example.a8_3_db_comms_room.db.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryWithContacts {
    @Embedded
    public Category category;

    @Relation(parentColumn = "idCategory", entityColumn = "idCategory")
    public List<Contact> contactList;
}

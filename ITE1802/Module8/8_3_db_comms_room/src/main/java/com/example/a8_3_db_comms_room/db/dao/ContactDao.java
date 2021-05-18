package com.example.a8_3_db_comms_room.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.a8_3_db_comms_room.db.entity.CategoryWithContacts;
import com.example.a8_3_db_comms_room.db.entity.Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contact contact);

    @Insert
    void insertAll(List<Contact> contacts);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateContact(Contact contact);

    @Query("DELETE FROM ContactTable")
    void deleteAllContacts();

    @Delete
    void deleteContact(Contact contact);

    @Query("DELETE FROM ContactTable WHERE idContact = :idContact")
    void deleteContact(int idContact);

    @Query("SELECT * FROM ContactTable WHERE idContact = :idContact")
    Contact getContact(int idContact);

    @Query("SELECT COUNT(idContact) FROM ContactTable")
    LiveData<Integer> getAllContactsCount();

    @Query("SELECT * from ContactTable ORDER BY lastname, firstname ASC")
    LiveData<List<Contact>> getAllAlphabetizedContacts();

    @Query("SELECT * FROM ContactTable WHERE idCategory = :idCategory ORDER BY lastname, firstname ASC")
    LiveData<List<Contact>> getAlphabetizedContactsByCategory(int idCategory);

    @Transaction
    @Query("SELECT * FROM CategoryTable")
    public LiveData<List<CategoryWithContacts>> getCategoriesWithContacts();
}

package com.example.a8_3_db_comms_room.db.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.a8_3_db_comms_room.db.database.ContactDatabase;
import com.example.a8_3_db_comms_room.db.dao.ContactDao;
import com.example.a8_3_db_comms_room.db.entity.CategoryWithContacts;
import com.example.a8_3_db_comms_room.db.entity.Contact;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<Integer> mAllContactsCount;
    private LiveData<List<Contact>> mAllContacts;
    private LiveData<List<CategoryWithContacts>> mAllCategoriesWithContacts;

    public ContactRepository(Application application){
        ContactDatabase db = ContactDatabase.getDatabase(application);
        contactDao = db.contactDao();
        mAllContactsCount = contactDao.getAllContactsCount();
        mAllContacts = contactDao.getAllAlphabetizedContacts();
        mAllCategoriesWithContacts = contactDao.getCategoriesWithContacts();
    }

    //Contact
    public void addContact(Contact contact){
        ContactDatabase.databaseWriteExecutor.execute(()->{
            contactDao.insert(contact);
        });
    }

    public void addAllContact(List<Contact> contacts){
        ContactDatabase.databaseWriteExecutor.execute(()->{
            contactDao.insertAll(contacts);
        });
    }

    public void updateContact(Contact contact){
        ContactDatabase.databaseWriteExecutor.execute(()->{
            contactDao.updateContact(contact);
        });
    }

    public void deleteAllContacts(){
        ContactDatabase.databaseWriteExecutor.execute(()->{
            contactDao.deleteAllContacts();
        });
    }

    public void deleteContact(Contact contact){
        ContactDatabase.databaseWriteExecutor.execute(()->{
            contactDao.deleteContact(contact);
        });
    }

    public void deleteContact(int idContact){
        ContactDatabase.databaseWriteExecutor.execute(()->{
            contactDao.deleteContact(idContact);
        });
    }

    public Contact getContact(int idContact){
        return contactDao.getContact(idContact);
    }

    public LiveData<Integer> getAllContactsCount(){
        return mAllContactsCount;
    }

    public LiveData<List<Contact>> getAllContacts(){
        return mAllContacts;
    }

    public LiveData<List<Contact>> getContactsByCategory(int idCategory){
        return contactDao.getAlphabetizedContactsByCategory(idCategory);
    }

    public LiveData<List<CategoryWithContacts>> getAllCategoriesWithContacts(){
        return mAllCategoriesWithContacts;
    }

}

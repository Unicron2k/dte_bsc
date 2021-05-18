package com.example.a8_3_db_comms_room.db.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.a8_3_db_comms_room.db.repository.ContactRepository;
import com.example.a8_3_db_comms_room.db.entity.Contact;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private ContactRepository mRepository;
    private LiveData<List<Contact>>mAllContacts;
    private LiveData<Integer> mAllContactsCount;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ContactRepository(application);
        mAllContacts = mRepository.getAllContacts();
        mAllContactsCount = mRepository.getAllContactsCount();
    }

    public LiveData<List<Contact>> getContactsByCategory(int categoryId) {
        if(categoryId<=1)
            return mRepository.getAllContacts();
        return mRepository.getContactsByCategory(categoryId);
    }
    public LiveData<List<Contact>> getAllContacts() {
        return mRepository.getAllContacts();
    }
    public LiveData<Integer> getAllContactsCount(){
        return mRepository.getAllContactsCount();
    }
    public void addContact(Contact contact){
        mRepository.addContact(contact);
    }
    public void deleteContact(Contact contact){
        mRepository.deleteContact(contact);
    }
    public void deleteContact(int contactId){
        mRepository.deleteContact(contactId);
    }
    public void updateContact(Contact contact){
        mRepository.updateContact(contact);
    }

}

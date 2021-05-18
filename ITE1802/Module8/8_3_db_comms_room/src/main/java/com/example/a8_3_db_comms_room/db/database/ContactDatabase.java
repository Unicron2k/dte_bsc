package com.example.a8_3_db_comms_room.db.database;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.a8_3_db_comms_room.db.entity.Category;
import com.example.a8_3_db_comms_room.db.dao.CategoryDao;
import com.example.a8_3_db_comms_room.db.entity.Contact;
import com.example.a8_3_db_comms_room.db.dao.ContactDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class, Category.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase{

    public abstract ContactDao contactDao();
    public abstract CategoryDao categoryDao();

    private static volatile ContactDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ContactDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContactDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ContactDatabase.class, "ContactDatabase").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(()->{
                final ContactDao contactDao = INSTANCE.contactDao();
                final CategoryDao categoryDao = INSTANCE.categoryDao();

                if(categoryDao.getAllCategoriesCount()<=0) {
                    categoryDao.deleteAllCategories();

                    //DB-AutoIncrementing starts at 1
                    Category category = new Category(0, "Alle"); // 1
                    categoryDao.insert(category);
                    category = new Category(0, "Venner"); // 2
                    categoryDao.insert(category);
                    category = new Category(0, "Kollegaer"); // 3
                    categoryDao.insert(category);
                    category = new Category(0, "Familie"); // 4
                    categoryDao.insert(category);
                    contactDao.deleteAllContacts();

                    Contact contact = new Contact(0, "Kalleson", "Bob", "", "kalleson.bob@mail.com", "77077923", "2001", 2);
                    contactDao.insert(contact);
                    contact = new Contact(0, "Jenkinsson", "Rolf", "", "jaegermeister@mail.com", "77077923", "1986", 4);
                    contactDao.insert(contact);
                    contact = new Contact(0, "Nilsson", "Hilde", "", "jenteberta85@mail.com", "77077923", "1985", 4);
                    contactDao.insert(contact);
                    contact = new Contact(0, "Goos", "alf", "", "sveis@mail.com", "77077923", "1997", 3);
                    contactDao.insert(contact);
                    contact = new Contact(0, "Hildursen", "Nils", "", "bolle@mail.com", "77077923", "2003", 4);
                    contactDao.insert(contact);
                    contact = new Contact(0, "Karoliussen", "Bertine", "", "nicenicenice@mail.com", "77077923", "1991", 2);
                    contactDao.insert(contact);
                    contact = new Contact(0, "Loffson", "Polly", "", "Polly@mail.com", "77077923", "1982", 2);
                    contactDao.insert(contact);
                    contact = new Contact(0, "Bergersen", "Pia", "", "pb@mail.com", "77078641", "2001", 4);
                    contactDao.insert(contact);
                }
            });
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final ContactDao contactDao;
        private final CategoryDao categoryDao;

        PopulateDbAsync(ContactDatabase db){
            contactDao = db.contactDao();
            categoryDao = db.categoryDao();
        }

        @Override
        protected Void doInBackground(final Void... params){
            if(categoryDao.getAllCategoriesCount()<=0) {
                categoryDao.deleteAllCategories();

                Category category = new Category(0, "Alle");
                categoryDao.insert(category);
                category = new Category(0, "Venner");
                categoryDao.insert(category);
                category = new Category(0, "Kollegaer");
                categoryDao.insert(category);
                category = new Category(0, "Familie");
                categoryDao.insert(category);
            }

            Contact contact = new Contact(0, "Kalleson", "Bob","", "kalleson.bob@mail.com", "77077923", "2001", 1);
            contactDao.insert(contact);
            return null;
        }
    }
}

package com.example.a8_2_room;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {

    public abstract WordDao wordDao();

    private static volatile WordRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static WordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WordRoomDatabase.class, "word_database").addCallback(sRoomDatabaseCallback).build();
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
                WordDao dao = INSTANCE.wordDao();
                dao.deleteAll();

                Word word = new Word(0, "Hello", "This describes Hello");
                dao.insert(word);
                word = new Word(0,"World!", "This describes World");
                dao.insert(word);
                word = new Word(0,"This", "This describes This");
                dao.insert(word);
                word = new Word(0,"is!", "This describes Is");
                dao.insert(word);
                word = new Word(0,"me!", "This describes Me!");
                dao.insert(word);
                word = new Word(0,"speaking!", "This describes Speaking");
                dao.insert(word);
                word = new Word(0, "to", "This describes to");
                dao.insert(word);
                word = new Word(0, "you!!", "This describes You!!");
                dao.insert(word);

            });
        }
    };

}

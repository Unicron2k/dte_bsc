package com.example.a8_3_db_comms_room.db.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.a8_3_db_comms_room.db.database.ContactDatabase;
import com.example.a8_3_db_comms_room.db.dao.CategoryDao;
import com.example.a8_3_db_comms_room.db.entity.Category;

import java.util.List;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> mAllCategories;

    public CategoryRepository(Application application) {
        ContactDatabase db = ContactDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
        mAllCategories = categoryDao.getAllAlphabetizedCategories();
    }

    // Categories
    public void addCategory(Category category) {
        ContactDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.insert(category);
        });
    }

    public void updateCategory(Category category) {
        ContactDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.updateCategory(category);
        });
    }

    public void deleteAllCategories() {
        ContactDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.deleteAllCategories();
        });
    }

    public void deleteCategory(int idCategory) {
        ContactDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.deleteCategory(idCategory);
        });
    }

    public void deleteCategory(Category category) {
        ContactDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.deleteCategory(category);
        });
    }

    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }

    public int getAllCategoriesCount() {
        return categoryDao.getAllCategoriesCount();
    }

}
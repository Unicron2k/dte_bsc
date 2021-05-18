package com.example.a8_3_db_comms_room.db.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.a8_3_db_comms_room.db.repository.CategoryRepository;
import com.example.a8_3_db_comms_room.db.entity.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository mRepository;
    private LiveData<List<Category>> mAllCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        mRepository = new CategoryRepository(application);
        mAllCategories = mRepository.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories(){
        return mAllCategories;
    }
    public int getAllCategoriesCount(){
        return mRepository.getAllCategoriesCount();
    }
    public void addCategory(Category category){
        mRepository.addCategory(category);
    }
    public void deleteCategory(Category category){
        mRepository.deleteCategory(category);
    }
    public void deleteCategory(int categoryId){
        mRepository.deleteCategory(categoryId);
    }
    public void updateCategory(Category category){
        mRepository.updateCategory(category);
    }

}
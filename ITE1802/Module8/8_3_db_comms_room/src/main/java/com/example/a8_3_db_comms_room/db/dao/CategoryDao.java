package com.example.a8_3_db_comms_room.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.a8_3_db_comms_room.db.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Update
    void updateCategory(Category category);

    @Query("DELETE FROM CategoryTable")
    void deleteAllCategories();

    @Query("DELETE FROM CategoryTable WHERE idCategory = :idCategory")
    void deleteCategory(int idCategory);

    @Delete
    void deleteCategory(Category category);

    @Query("SELECT * from CategoryTable ORDER BY category ASC")
    LiveData<List<Category>> getAllAlphabetizedCategories();

    @Query("SELECT COUNT(idCategory) FROM CategoryTable")
    int getAllCategoriesCount();
}
package com.example.a8_3_db_comms_room.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a8_3_db_comms_room.R;
import com.example.a8_3_db_comms_room.db.adapter.CategoryListAdapter;
import com.example.a8_3_db_comms_room.db.adapter.CategorySpinnerAdapter;
import com.example.a8_3_db_comms_room.db.adapter.ContactListAdapter;
import com.example.a8_3_db_comms_room.db.entity.Category;
import com.example.a8_3_db_comms_room.db.entity.Contact;
import com.example.a8_3_db_comms_room.db.viewmodel.CategoryViewModel;
import com.example.a8_3_db_comms_room.db.viewmodel.ContactViewModel;
import com.example.a8_3_db_comms_room.util.RecyclerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CategoriesActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FloatingActionButton fabNewCategory;
    private FloatingActionButton fabEditCategory;
    private FloatingActionButton fabDeleteCategory;
    private TextView tvCategoryName;
    private CategoryListAdapter categoryListAdapter;
    private CategoryViewModel mCategoryViewModel;
    private Observer<List<Category>> categoryListObserver;
    private RecyclerView rvCategoryList;
    private View prevSelected;
    private int rvCurrentPosition;
    private Resources res;


    final static int REQUESTCODE_NEW_CATEGORY = 4;
    final static int REQUESTCODE_EDIT_CATEGORY = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        res = getResources();
        mainToolbar.setTitle(res.getString(R.string.str_categories));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tvCategoryName = findViewById(R.id.tvCategoryName);
        fabNewCategory = findViewById(R.id.fabNewCategory);
        fabEditCategory = findViewById(R.id.fabEditCategory);
        fabDeleteCategory = findViewById(R.id.fabDeleteCategory);
        rvCategoryList = findViewById(R.id.rvCategories);

        categoryListAdapter = new CategoryListAdapter(getApplicationContext());
        categoryListObserver = categories -> categoryListAdapter.setCategories(categories);

        mCategoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        mCategoryViewModel.getAllCategories().observe(this, categoryListAdapter::setCategories);

        rvCategoryList.setAdapter(categoryListAdapter);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(this));
        rvCategoryList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), rvCategoryList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));
                        if(prevSelected!=null && !prevSelected.equals(view)){
                            prevSelected.setBackgroundColor(res.getColor(R.color.colorTransparent, getTheme()));
                        }
                        prevSelected=view;
                        rvCurrentPosition = position;
                        tvCategoryName.setText(categoryListAdapter.getItem(rvCurrentPosition).getCategory());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        fabNewCategory.setOnClickListener((view)->{
            Intent newContactIntent = new Intent(getApplicationContext(), NewCategoryActivity.class);
            startActivityForResult(newContactIntent, REQUESTCODE_NEW_CATEGORY);
        });
        fabEditCategory.setOnClickListener((view)->{
            if (rvCurrentPosition<0){
                Toast.makeText(this, res.getString(R.string.str_no_category), Toast.LENGTH_SHORT).show();
            } else {
                Intent editContactIntent = new Intent(getApplicationContext(), EditCategoryActivity.class);
                editContactIntent.putExtra("category", categoryListAdapter.getItem(rvCurrentPosition));
                startActivityForResult(editContactIntent, REQUESTCODE_EDIT_CATEGORY);
            }
        });
        fabDeleteCategory.setOnClickListener((view)->{
            if(rvCurrentPosition>=0) {
                int selectedCategoryId = categoryListAdapter.getItem(rvCurrentPosition).getIdCategory();
                if(selectedCategoryId>1) {
                    mCategoryViewModel.deleteCategory(selectedCategoryId);
                    Toast.makeText(getApplicationContext(), "Category Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot delete category", Toast.LENGTH_SHORT).show();
                }
                rvCurrentPosition = -1;
                tvCategoryName.setText("");
            }
            if(prevSelected!=null){
                prevSelected.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUESTCODE_NEW_CATEGORY:
                if(resultCode == RESULT_OK && data!=null){
                    Category category = (Category)data.getSerializableExtra("newCategory");
                    mCategoryViewModel.addCategory(category);
                    categoryListAdapter.notifyDataSetChanged();
                }
                tvCategoryName.setText("");
                break;
            case REQUESTCODE_EDIT_CATEGORY:
                if(resultCode == RESULT_OK && data!=null){
                    Category category = (Category)data.getSerializableExtra("editedCategory");
                    mCategoryViewModel.updateCategory(category);

                    AtomicInteger index = new AtomicInteger();
                    rvCategoryList.post(()-> index.set(rvCategoryList.indexOfChild(prevSelected)));
                }
                tvCategoryName.setText("");
                break;
            default:
                break;
        }
    }
}

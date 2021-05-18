package com.example.a8_3_db_comms_room.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.a8_3_db_comms_room.R;
import com.example.a8_3_db_comms_room.db.entity.Category;

public class EditCategoryActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private EditText etCategoryName;
    private Button btSaveChanges;
    private Category category;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        res = getResources();

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        mainToolbar.setTitle(res.getString(R.string.str_edit_category));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        category = (Category)getIntent().getSerializableExtra("category");

        etCategoryName = findViewById(R.id.etCategoryName);
        btSaveChanges = findViewById(R.id.btSaveChanges);

        etCategoryName.setText(category.getCategory());

        btSaveChanges.setOnClickListener((view)-> {

            String categoryName = etCategoryName.getText().toString();


            if (categoryName.equals("")) {
                Toast.makeText(this, res.getString(R.string.str_category_name_empty), Toast.LENGTH_SHORT).show();
            } else {
                category.setCategory(categoryName);
                Intent replyIntent = new Intent();
                replyIntent.putExtra("editedCategory", category);
                setResult(RESULT_OK, replyIntent);
                this.finish();
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
}

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
import com.example.a8_3_db_comms_room.db.entity.Contact;

public class NewCategoryActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private EditText etCategoryName;
    private Button btSaveChanges;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        res = getResources();

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        mainToolbar.setTitle(res.getString(R.string.str_new_category));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        etCategoryName = findViewById(R.id.etCategoryName);
        btSaveChanges = findViewById(R.id.btSaveChanges);

        btSaveChanges.setOnClickListener((view)-> {

            String categoryName = etCategoryName.getText().toString();


            if (categoryName.equals("")) {
                Toast.makeText(this, res.getString(R.string.str_category_name_empty), Toast.LENGTH_SHORT).show();
            } else {
                Category category = new Category(0, categoryName);
                Intent replyIntent = new Intent();
                replyIntent.putExtra("newCategory", category);
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

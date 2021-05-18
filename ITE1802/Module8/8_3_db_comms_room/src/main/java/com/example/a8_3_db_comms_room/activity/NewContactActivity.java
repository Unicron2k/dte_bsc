package com.example.a8_3_db_comms_room.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.example.a8_3_db_comms_room.R;
import com.example.a8_3_db_comms_room.db.adapter.CategorySpinnerAdapter;
import com.example.a8_3_db_comms_room.db.entity.Category;
import com.example.a8_3_db_comms_room.db.entity.Contact;
import com.example.a8_3_db_comms_room.db.viewmodel.CategoryViewModel;

import java.util.ArrayList;

public class NewContactActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPhoneNumber;
    private EditText etEmailAddress;
    private EditText etFbLink;
    private EditText etBirthYear;
    private Button btSaveChanges;
    private Spinner spnrCategory;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private CategoryViewModel mCategoryViewModel;
    private ArrayList<Category> categoryArrayList;
    private Resources res;
    private int spnrCurrentPosition;

    private int selectedCategoryID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        res = getResources();

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        mainToolbar.setTitle(res.getString(R.string.str_new_contact));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        spnrCurrentPosition = getIntent().getIntExtra("spnrCurrentPosition", 1);

        spnrCategory = findViewById(R.id.spnrCategory);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etFbLink = findViewById(R.id.etFbLink);
        etBirthYear = findViewById(R.id.etBirthYear);
        btSaveChanges = findViewById(R.id.btSaveChanges);

        btSaveChanges.setOnClickListener((view)->{

            if(selectedCategoryID>1){
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();
                String eMail = etEmailAddress.getText().toString();
                String fbLink = etFbLink.getText().toString();
                String birthYear = etBirthYear.getText().toString();
                birthYear = birthYear.length()==4?birthYear:"";

                if(firstName.equals("")){
                    Toast.makeText(this, res.getString(R.string.str_first_name_empty), Toast.LENGTH_SHORT).show();
                } else if (lastName.equals("")){
                    Toast.makeText(this, res.getString(R.string.str_last_name_empty), Toast.LENGTH_SHORT).show();
                } else if (phoneNumber.length()<8){
                    Toast.makeText(this, res.getString(R.string.str_phone_number_minimum_8), Toast.LENGTH_SHORT).show();
                } else {
                    Contact contact = new Contact(0, lastName, firstName, fbLink, eMail, phoneNumber, birthYear, selectedCategoryID);
                    Intent replyIntent = new Intent();
                    replyIntent.putExtra("newContact", contact);
                    setResult(RESULT_OK, replyIntent);
                    this.finish();
                }
            } else {
                Toast.makeText(this, res.getString(R.string.str_select_category), Toast.LENGTH_SHORT).show();
            }
        });


        categoryArrayList = new ArrayList<>();
        categorySpinnerAdapter = new CategorySpinnerAdapter(getApplicationContext(), R.layout.spinner_contact_item, categoryArrayList);
        spnrCategory.setAdapter(categorySpinnerAdapter);
        mCategoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        mCategoryViewModel.getAllCategories().observe(this, categories -> {
            categoryArrayList.clear();
            categoryArrayList.addAll(categories);
            categorySpinnerAdapter.notifyDataSetChanged();
        });
        spnrCategory.post(()->spnrCategory.setSelection(spnrCurrentPosition));
        spnrCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryID = categoryArrayList.get(position).getIdCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
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

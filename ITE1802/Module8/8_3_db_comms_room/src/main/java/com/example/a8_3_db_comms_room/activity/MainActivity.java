package com.example.a8_3_db_comms_room.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a8_3_db_comms_room.R;
import com.example.a8_3_db_comms_room.db.adapter.CategorySpinnerAdapter;
import com.example.a8_3_db_comms_room.db.adapter.ContactListAdapter;
import com.example.a8_3_db_comms_room.db.entity.Contact;
import com.example.a8_3_db_comms_room.db.viewmodel.CategoryViewModel;
import com.example.a8_3_db_comms_room.db.viewmodel.ContactViewModel;
import com.example.a8_3_db_comms_room.db.entity.Category;
import com.example.a8_3_db_comms_room.util.RecyclerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private Menu menu;
    private TextView tvContactName;

    private FloatingActionButton fabNewContact;
    private FloatingActionButton fabEditContact;
    private FloatingActionButton fabDeleteContact;

    private RecyclerView rvContactList;
    private ContactListAdapter contactListAdapter;
    private ContactViewModel mContactViewModel;

    private Spinner spnrCategory;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private CategoryViewModel mCategoryViewModel;
    private ArrayList<Category> categoryArrayList;

    private Resources res;

    private Category selectedCategory;
    private int spnrCurrentPosition;
    private int rvCurrentPosition;
    private View prevSelected;

    final static int REQUESTCODE_NEW_CONTACT = 1;
    final static int REQUESTCODE_EDIT_CONTACT = 2;
    final static int REQUESTCODE_CATEGORY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvContactList = findViewById(R.id.rvContacts);
        fabNewContact = findViewById(R.id.fabNewContact);
        fabEditContact = findViewById(R.id.fabEditCategory);
        fabDeleteContact = findViewById(R.id.fabDeleteCategory);
        spnrCategory = findViewById(R.id.spnrCategory);
        tvContactName = findViewById(R.id.tvContactName);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        tvContactName.setText("");

        res = getResources();

        contactListAdapter = new ContactListAdapter(getApplicationContext());

        mContactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        mContactViewModel.getAllContactsCount().observe(MainActivity.this, count -> mainToolbar.setTitle(res.getString(R.string.str_contacts_title,count)));

        rvContactList.setAdapter(contactListAdapter);
        rvContactList.setLayoutManager(new LinearLayoutManager(this));
        rvContactList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), rvContactList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        view.setBackgroundColor(res.getColor(R.color.colorAccent, getTheme()));
                        if(prevSelected!=null && !prevSelected.equals(view)){
                            prevSelected.setBackgroundColor(res.getColor(R.color.colorTransparent, getTheme()));
                        }
                        prevSelected=view;
                        rvCurrentPosition = position;
                        tvContactName.setText(res.getString(R.string.str_contact_name_bottom, contactListAdapter.getItem(rvCurrentPosition).getFirstname(), contactListAdapter.getItem(rvCurrentPosition).getLastname()));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );


        categoryArrayList = new ArrayList<>();
        categorySpinnerAdapter = new CategorySpinnerAdapter(getApplicationContext(), R.layout.spinner_contact_item, categoryArrayList);
        spnrCategory.setAdapter(categorySpinnerAdapter);
        mCategoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        mCategoryViewModel.getAllCategories().observe(this, categories -> {
            categoryArrayList.clear();
            categoryArrayList.addAll(categories);
            categorySpinnerAdapter.notifyDataSetChanged();
        });

        spnrCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnrCurrentPosition = position;
                rvCurrentPosition = -1;

                selectedCategory = categoryArrayList.get(position);

                // Don't know why, but the recycler-list automatically updates the list to contain contacts from an apparently random category...
                mContactViewModel.getContactsByCategory(selectedCategory.getIdCategory()).observe(MainActivity.this, contacts -> contactListAdapter.setContacts(contacts));
                contactListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        fabNewContact.setOnClickListener((view)->{
            Intent newContactIntent = new Intent(getApplicationContext(), NewContactActivity.class);
            newContactIntent.putExtra("spnrCurrentPosition", spnrCurrentPosition);
            startActivityForResult(newContactIntent, REQUESTCODE_NEW_CONTACT);
        });
        fabEditContact.setOnClickListener((view)->{
            if (rvCurrentPosition<0){
                Toast.makeText(this, getResources().getText(R.string.str_no_category), Toast.LENGTH_SHORT).show();
            } else {
                Intent editContactIntent = new Intent(getApplicationContext(), EditContactActivity.class);
                editContactIntent.putExtra("contact", contactListAdapter.getItem(rvCurrentPosition));
                startActivityForResult(editContactIntent, REQUESTCODE_EDIT_CONTACT);
            }
        });
        fabDeleteContact.setOnClickListener((view)->{
            if(rvCurrentPosition>=0) {
                mContactViewModel.deleteContact(contactListAdapter.getItem(rvCurrentPosition).getIdContact());
                contactListAdapter.notifyDataSetChanged();
                rvCurrentPosition =-1;
                tvContactName.setText("");
                Toast.makeText(getApplicationContext(), res.getText(R.string.str_contact_deleted), Toast.LENGTH_SHORT).show();
            }
            if(prevSelected!=null){
                prevSelected.setBackgroundColor(res.getColor(R.color.colorTransparent, getTheme()));
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_category:
                startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
                break;
        }
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUESTCODE_NEW_CONTACT:
                if(resultCode == RESULT_OK && data!=null){
                    Contact contact = (Contact)data.getSerializableExtra("newContact");
                    mContactViewModel.addContact(contact);
                    contactListAdapter.notifyDataSetChanged();
                }
                tvContactName.setText("");
                break;
            case REQUESTCODE_EDIT_CONTACT:
                if(resultCode == RESULT_OK && data!=null){
                    Contact contact = (Contact)data.getSerializableExtra("editedContact");
                    mContactViewModel.updateContact(contact);

                    AtomicInteger index = new AtomicInteger();
                    rvContactList.post(()-> index.set(rvContactList.indexOfChild(prevSelected)));
                }
                tvContactName.setText("");
                break;
            case REQUESTCODE_CATEGORY:

                break;

            default:
                break;
        }
    }

}

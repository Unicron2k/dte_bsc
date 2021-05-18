package com.example.volley;

import android.content.Intent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.View;
import com.example.volley.User.User;
import com.example.volley.User.UserAdapter;
import com.example.volley.User.UserData;
import com.example.volley.User.UserViewModel;

import java.util.ArrayList;

// SE
// https://developer.android.com/topic/libraries/architecture/livedata?authuser=1
// og
// https://medium.com/@ogieben/basics-of-viewmodel-and-livedata-4550a34c19f3
public class MainActivity extends AppCompatActivity {

    private boolean mDownloading = false;
    private ProgressBar progressWork;
    private UserViewModel userViewModel;
    private ListView lvUserResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressWork = findViewById(R.id.progressWork);

        lvUserResultList = findViewById(R.id.lvUserResultList);
        lvUserResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewUserAlbumsIntent = new Intent(getApplicationContext(), ViewUserAlbumsActivity.class);
                long userId = userViewModel.getUserData().getValue().getAllUsers().get(position).getId();
                viewUserAlbumsIntent.putExtra("userId", userId);
                startActivity(viewUserAlbumsIntent);


            }
        });

        // Bruker ViewModelProvider i stedet for ViewModelProviders som er deprecated.
        userViewModel = new ViewModelProvider(this).get(com.example.volley.User.UserViewModel.class);
        this.subscribe();
    }

    private void subscribe() {
        final Observer<UserData> userDataObserver = new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                if (userData != null && userData.getAllUsers() != null) {

                    ArrayList<User> alUserList = (ArrayList<User>)userData.getAllUsers();
                    UserAdapter userAdapter = new UserAdapter(getApplicationContext(), alUserList);

                    lvUserResultList.setAdapter(userAdapter);

                    progressWork.setVisibility(View.INVISIBLE);
                    mDownloading = false;
                }
            }
        };
        userViewModel.getUserData().observe(this, userDataObserver);

        final Observer<String> errorMessageObserver = new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                TextView tvError = new TextView(getApplicationContext());
                tvError.setText(errorMessage);
                //lvResultList.addView(tvError);
                progressWork.setVisibility(View.INVISIBLE);
                mDownloading = false;
            }
        };
        userViewModel.getErrorMessage().observe(this, errorMessageObserver);
    }

    public void download(View view) {
        if (userViewModel != null && !mDownloading) {
            progressWork.setVisibility(View.VISIBLE);
            mDownloading = true;
            userViewModel.startDownload(getApplicationContext());
        }
    }
}

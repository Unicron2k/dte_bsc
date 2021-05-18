package com.example.volley;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class ViewPhotoActivity extends AppCompatActivity {

    private ImageView ivPhoto;
    private String photoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        ivPhoto = findViewById(R.id.ivPhoto);
        photoUrl = getIntent().getStringExtra("photoURL");

        Picasso.get().load(photoUrl).placeholder(R.drawable.ic_hourglass_empty_black_24dp).into(ivPhoto);
    }
}

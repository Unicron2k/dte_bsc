package com.example.volley.Photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.example.volley.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoAdapter extends ArrayAdapter<Photo> {
    public PhotoAdapter(@NonNull Context context, ArrayList<Photo> albumList) {
        super(context, 0, albumList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Photo photo = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photos, parent, false);
        }

        TextView tvUserId = convertView.findViewById(R.id.tvPhotoId);
        TextView tvName = convertView.findViewById(R.id.tvPhotoTitle);
        ImageView ivThumbnail = convertView.findViewById(R.id.ivThumbnail);

        tvUserId.setText(photo.getId() + ",");
        tvName.setText(photo.getTitle());
        ivThumbnail.setImageResource(R.mipmap.ic_launcher);

        //TODO: Fix this shit
        //Glide.with(getContext()).load(photo.getThumbnailUrl()).into(ivThumbnail);
        Picasso.get().load(photo.getThumbnailUrl()).placeholder(R.drawable.ic_hourglass_empty_black_24dp).into(ivThumbnail);

        return convertView;
    }
}
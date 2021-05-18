package com.example.volley.Album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.volley.R;

import java.util.ArrayList;

public class AlbumAdapter extends ArrayAdapter<Album> {
    public AlbumAdapter(@NonNull Context context, ArrayList<Album> albumList) {
        super(context, 0, albumList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Album album = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_albums, parent, false);
        }

        TextView tvUserId = convertView.findViewById(R.id.tvAlbumId);
        TextView tvName = convertView.findViewById(R.id.tvAlbumTitle);

        tvUserId.setText(album.getId() + ",");
        tvName.setText(album.getTitle());

        return convertView;
    }
}
/*
package com.example.volley.Photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import com.bumptech.glide.Glide;
//import com.example.albumphotoslivedata.model.Photo;
import java.util.List;
/**
 * Created by wfa
 /**/
/*
public class PhotoAdapterWFA extends ArrayAdapter<Photo> {
    private int resource;
    public PhotoAdapterWFA(Context context, int resource, List<Photo> items) {
        super(context, resource, items);
        this.resource = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout photoItemView;
        Photo item = getItem(position);
        if (convertView == null) {
            photoItemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi =
                    (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, photoItemView, true);
        } else {
            //Gjenbruker vha. convertView:
            photoItemView = (LinearLayout) convertView;
        }


        TextView tvPhotoId = photoItemView.findViewById(R.id.tvId);
        tvPhotoId.setText(String.valueOf(item.getId()));
        TextView tvPhotoTitle = photoItemView.findViewById(R.id.tvTitle);
        tvPhotoTitle.setText(item.getTitle());
        ImageView thumbView = photoItemView.findViewById(R.id.thumb);
        thumbView.setImageResource(R.mipmap.ic_launcher);

        Glide.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(thumbView);

        return photoItemView;
    }
}

/**/


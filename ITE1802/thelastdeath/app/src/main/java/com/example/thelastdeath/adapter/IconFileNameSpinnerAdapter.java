package com.example.thelastdeath.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.thelastdeath.R;
import com.example.thelastdeath.entity.IconFileNames;
import com.example.thelastdeath.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IconFileNameSpinnerAdapter extends ArrayAdapter<String> {

    private int layoutResource;

    public IconFileNameSpinnerAdapter(Context context, int layoutResource, List<String> items) {
        super(context, layoutResource, items);
        this.layoutResource = layoutResource;
    }

    //NB!! Denne gjør at elementene i lista tegnes som ikoner.
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCategoryView(position, convertView);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCategoryView(position, convertView);
    }

    private View getCategoryView(int position, View convertView) {
        LinearLayout categoryView;
        if (convertView == null) {
            categoryView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            assert vi != null;
            vi.inflate(this.layoutResource, categoryView, true);
        } else {
            //Gjenbruker vha. convertView:
            categoryView = (LinearLayout) convertView;
        }

        String fileName = getItem(position);

        ImageView iconView = categoryView.findViewById(R.id.ivCategoryIcon);
        Picasso.get().load(IconFileNames.getBaseUrl() + fileName).into(iconView);

        TextView titleView = categoryView.findViewById(R.id.tvTitle);
        titleView.setText(Utils.convertFileNameIcon8(fileName));
        return categoryView;
    }


}

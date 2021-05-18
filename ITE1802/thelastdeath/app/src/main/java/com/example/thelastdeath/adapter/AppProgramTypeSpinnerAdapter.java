package com.example.thelastdeath.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thelastdeath.R;
import com.example.thelastdeath.entity.AppProgramType;
import com.example.thelastdeath.entity.IconFileNames;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AppProgramTypeSpinnerAdapter extends ArrayAdapter<AppProgramType> {

    private int layoutResource;

    public AppProgramTypeSpinnerAdapter(Context context, int layoutResource, List<AppProgramType> items) {
        super(context, layoutResource, items);
        this.layoutResource = layoutResource;
    }

    //NB!! Denne gjør at elementene i lista tegnes som ikoner.
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCategoryView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCategoryView(position, convertView, parent);
    }

    public View getCategoryView(int position, View convertView, ViewGroup parent) {
        LinearLayout categoryView;
        if (convertView == null) {
            categoryView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(this.layoutResource, categoryView, true);
        } else {
            //Gjenbruker vha. convertView:
            categoryView = (LinearLayout) convertView;
        }

        AppProgramType categoryItem = getItem(position);
        String title = categoryItem.getDescription();
        TextView titleView = categoryView.findViewById(R.id.tvTitle);
        titleView.setText(title);

        ImageView iconView = categoryView.findViewById(R.id.ivCategoryIcon);
        Picasso.get().load(IconFileNames.getBaseUrl() + categoryItem.getIcon()).into(iconView);
        return categoryView;
    }
}

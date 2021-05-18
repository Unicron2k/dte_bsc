package com.example.thelastdeath.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.thelastdeath.R;
import com.example.thelastdeath.entity.IconFileNames;
import com.example.thelastdeath.entity.UserProgram;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserProgramSpinnerAdapter extends ArrayAdapter<UserProgram> {

    private int layoutResource;

    public UserProgramSpinnerAdapter(Context context, int layoutResource, List<UserProgram> items) {
        super(context, layoutResource, items);
        this.layoutResource = layoutResource;
    }

    //NB!! Denne gjør at elementene i lista tegnes som ikoner.
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCategoryView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCategoryView(position, convertView, parent);
    }

    public View getCategoryView(int position, View convertView, ViewGroup parent) {
        LinearLayout categoryView;
        if (convertView == null) {
            categoryView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            assert vi != null;
            vi.inflate(this.layoutResource, categoryView, true);
        } else {
            //Gjenbruker vha. convertView:
            categoryView = (LinearLayout) convertView;
        }

        UserProgram userProgram = getItem(position);
        assert userProgram != null;
        String title = userProgram.getName();
        TextView titleView = categoryView.findViewById(R.id.tvTitle);
        titleView.setText(title);

        ImageView iconView = categoryView.findViewById(R.id.ivCategoryIcon);
        Picasso.get().load(IconFileNames.getBaseUrl() + userProgram.getApp_program_type().getIcon()).into(iconView);
        return categoryView;
    }


}

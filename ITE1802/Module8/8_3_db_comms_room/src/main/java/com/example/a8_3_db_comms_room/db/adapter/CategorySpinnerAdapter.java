package com.example.a8_3_db_comms_room.db.adapter;

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
import com.example.a8_3_db_comms_room.R;
import com.example.a8_3_db_comms_room.db.entity.Category;

import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

    private int layoutResource;

    public CategorySpinnerAdapter(Context context, int layoutResource, List<Category> items) {
        super(context, layoutResource, items);
        this.layoutResource = layoutResource;
    }

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
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            assert vi != null;
            vi.inflate(this.layoutResource, categoryView, true);
        } else {
            categoryView = (LinearLayout) convertView;
        }

        Category categoryItem = getItem(position);
        assert categoryItem != null;
        String title = categoryItem.getCategory();
        TextView tvText = categoryView.findViewById(R.id.tvText);
        tvText.setText(title);

        ImageView ivIcon = categoryView.findViewById(R.id.ivIcon);
        int imageId = R.drawable.ic_action_category_dark;
        Drawable drawable = ResourcesCompat.getDrawable(this.getContext().getResources(), imageId, null);
        ivIcon.setImageDrawable(drawable);

        return categoryView;
    }
}

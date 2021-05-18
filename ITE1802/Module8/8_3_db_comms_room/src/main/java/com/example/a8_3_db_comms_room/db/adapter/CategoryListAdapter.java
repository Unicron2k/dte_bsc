package com.example.a8_3_db_comms_room.db.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a8_3_db_comms_room.R;
import com.example.a8_3_db_comms_room.db.entity.Category;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategoryName;
        private CategoryViewHolder(View itemView){
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }

    private final LayoutInflater mInflater;
    private List<Category> mCategory;

    public CategoryListAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.recyclerview_category_item, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position){
        if(mCategory !=null){
            Category current = mCategory.get(position);
            holder.tvCategoryName.setText(current.getCategory());
        } else {
            holder.tvCategoryName.setText("No Category");
        }
    }

    public void setCategories(List<Category> categories){
        mCategory = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if(mCategory !=null){
            return mCategory.size();
        } else {
            return 0;
        }
    }
    public Category getItem(int position){
        return mCategory.get(position);
    }
}
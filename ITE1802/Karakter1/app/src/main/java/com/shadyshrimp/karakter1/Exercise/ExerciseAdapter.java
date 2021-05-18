package com.shadyshrimp.karakter1.Exercise;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.shadyshrimp.karakter1.R;

import java.util.ArrayList;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {
    public ExerciseAdapter(@NonNull Context context, ArrayList<Exercise> exerciseList) {
        super(context, 0, exerciseList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Exercise exercise = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_single_textview, parent, false);
        }

        TextView tvExerciseName = convertView.findViewById(R.id.item_name);
        assert exercise != null;
        tvExerciseName.setText(exercise.getName()+"");

        String hex = exercise.getInfobox_color();
        int color = Color.parseColor(hex);

        tvExerciseName.setBackgroundColor(color);

        return convertView;
    }
}
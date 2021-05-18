package com.shadyshrimp.karakter1.Program;

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

public class ProgramAdapter extends ArrayAdapter<Program> {
    public ProgramAdapter(@NonNull Context context, ArrayList<Program> userList) {
        super(context, 0, userList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Program program = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_single_textview, parent, false);
        }

        TextView tvExerciseName = convertView.findViewById(R.id.item_name);
        assert program != null;
        tvExerciseName.setText(program.getName()+"");
        tvExerciseName.setBackgroundColor(Color.LTGRAY);

        return convertView;
    }
}
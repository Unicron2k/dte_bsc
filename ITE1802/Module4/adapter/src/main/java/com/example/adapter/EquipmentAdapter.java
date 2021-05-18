package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EquipmentAdapter extends ArrayAdapter<Equipment> {
    public EquipmentAdapter(@NonNull Context context, ArrayList<Equipment> equipmentList) {
        super(context, 0, equipmentList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Equipment equipment = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_equipment, parent, false);
        }

        TextView tvType = convertView.findViewById(R.id.tvType);
        TextView tvManufacturer = convertView.findViewById(R.id.tvManufacturer);
        TextView tvModel = convertView.findViewById(R.id.tvModel);
        TextView tvLoanStatus = convertView.findViewById(R.id.tvLoanStatus);

        tvType.setText(equipment.getType() + ",");
        tvManufacturer.setText(equipment.getManufacturer());
        tvModel.setText(equipment.getModel());
        tvLoanStatus.setText(", Loan-status: " + equipment.getLoanStatus());

        return convertView;
    }
}

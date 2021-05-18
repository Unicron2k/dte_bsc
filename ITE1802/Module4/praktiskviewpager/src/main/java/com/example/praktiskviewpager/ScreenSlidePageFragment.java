package com.example.praktiskviewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

public class ScreenSlidePageFragment extends Fragment {
    int position;

    public ScreenSlidePageFragment(int contentLayoutId) {
        super(contentLayoutId);
        this.position = contentLayoutId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        Equipment equipment = ScreenSlidePagerActivity.equipmentList.getEquipmentList().get(position);

        // public Equipment(type, manufacturer, model, loanStatus, loanedTo, imageURL, dateOfPurchase)
        TextView tvType = view.findViewById(R.id.tv_type);
        TextView tvManufacturer = view.findViewById(R.id.tv_manufacturer);
        TextView tvModel = view.findViewById(R.id.tv_model);
        TextView tvLoanStatus = view.findViewById(R.id.tv_loanStatus);
        TextView tvLoanedTo = view.findViewById(R.id.tv_loanedTo);
        TextView tvImageURL = view.findViewById(R.id.tv_imageURL);
        TextView tvDateOfPurchase = view.findViewById(R.id.tv_dateOfPurchase);

        tvType.setText("Type: " + equipment.type);
        tvManufacturer.setText("Manufacturer: " + equipment.manufacturer);
        tvModel.setText("Model: " + equipment.model);
        tvLoanStatus.setText("Loan-status: " + equipment.loanStatus);
        tvLoanedTo.setText("Loaned to: " + equipment.loanedTo);
        tvImageURL.setText("Image-URL: " + equipment.imageURL);
        tvDateOfPurchase.setText("Date of purchase: " + equipment.dateOfPurchase.toString());

        return view;
    }
}

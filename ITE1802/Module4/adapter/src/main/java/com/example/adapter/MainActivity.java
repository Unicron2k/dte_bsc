package com.example.adapter;

import android.app.LauncherActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Equipment> alEquipmentList = new EquipmentList().getEquipmentList();
        EquipmentAdapter equipmentAdapter = new EquipmentAdapter(this, alEquipmentList);

        ListView listView = findViewById(R.id.lvListItem);
        listView.setAdapter(equipmentAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Equipment item = alEquipmentList.get(i);

            // public Equipment(type, manufacturer, model, loanStatus, loanedTo, imageURL, dateOfPurchase)
            String toastString = String.format("Item type: %s%nManufacturer: %s%nModel: %s%nLoan Status: %s%nLoaned to: %s%nImage: %s%nDate of Purchase: %s%n",
                    item.getType(), item.getManufacturer(), item.getModel(), item.getLoanStatus(), item.getLoanedTo(), item.getImageURL(), item.getDateOfPurchase().toString());
            Toast.makeText(MainActivity.this, toastString, Toast.LENGTH_LONG).show();
        });

    }
}

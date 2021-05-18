package com.example.inputactivity;

import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText etFirstName,
            etLastName,
            etPostCode,
            etBirthyear;
    TextView tvCurrDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPostCode = findViewById(R.id.etPostCode);
        etBirthyear = findViewById(R.id.etBirthyear);
        tvCurrDate = findViewById(R.id.tvCurrView);

        etBirthyear.setText(String.valueOf(getResources().getInteger(R.integer.birthyear)));

        etBirthyear.setText(String.valueOf(Integer.parseInt(etBirthyear.getText().toString()) + 10));

        tvCurrDate.setText(getResources().getString(R.string.currDate, new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime())));

    }
}

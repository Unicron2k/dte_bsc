package com.example.tidsdifferanse;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String MY_DATE_FORMAT = "dd.MM.yyyy";
    public static final String MY_DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";

    private EditText etDateGiven;
    private TextView tvDisplayMessage;
    private TextView tvDisplayDiff;
    private Button btnCalculate;
    private Calendar calToday;
    private String strToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplayMessage = findViewById(R.id.tvDisplayMessage);
        etDateGiven = findViewById(R.id.etDateGiven);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvDisplayDiff = findViewById(R.id.tvDisplayDiff);

        calToday = Calendar.getInstance();
        strToday = new SimpleDateFormat(MY_DATE_TIME_FORMAT, Locale.getDefault()).format(calToday.getTime());

        tvDisplayMessage.setText(getResources().getString(R.string.bannerMsg, strToday));
    }

    public void calculateDiff(View view) throws ParseException {
        long dateDiffSeconds;
        int days, hours, minutes;
        String strDateGiven = etDateGiven.getText().toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MY_DATE_FORMAT, Locale.getDefault());
        Calendar calGiven = Calendar.getInstance();
        calGiven.setTime(simpleDateFormat.parse(strDateGiven));

        dateDiffSeconds = (calGiven.getTimeInMillis()-calToday.getTimeInMillis())/1000;
        if(dateDiffSeconds<0){
            tvDisplayDiff.setBackgroundColor(0xFFFF0000);
        } else {
            tvDisplayDiff.setBackgroundColor(0xFF00FF00);
        }
        //dateDiffSeconds = Math.abs(dateDiffSeconds);

        minutes = (int)Math.ceil((dateDiffSeconds%3600)/60.0);
        hours = (int)(dateDiffSeconds%86400)/3600;
        days = (int)(dateDiffSeconds/86400);

        tvDisplayDiff.setText(getResources().getString(R.string.strDateDiff, days, hours, minutes));
    }
}

package com.example.tidsdifferanse;

import android.view.View;
import android.widget.DatePicker;
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

    private DatePicker dpDateGiven;
    private TextView tvDisplayMessage;
    private TextView tvDisplayDiff;
    private Calendar calToday;
    private String strToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplayMessage = findViewById(R.id.tvDisplayMessage);
        dpDateGiven = findViewById(R.id.dpDateGiven);
        tvDisplayDiff = findViewById(R.id.tvDisplayDiff);

        calToday = Calendar.getInstance();
        strToday = new SimpleDateFormat(MY_DATE_TIME_FORMAT, Locale.getDefault()).format(calToday.getTime());

        tvDisplayMessage.setText(getResources().getString(R.string.bannerMsg, strToday));
    }

    public void calculateDiff(View view) throws ParseException {
        long dateDiffSeconds;
        int days, hours, minutes;
        String strDateGiven = dpDateGiven.getDayOfMonth()+"."+(dpDateGiven.getMonth()+1)+"."+dpDateGiven.getYear();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MY_DATE_FORMAT, Locale.getDefault());
        Calendar calGiven = Calendar.getInstance();
        calGiven.setTime(simpleDateFormat.parse(strDateGiven));

        dateDiffSeconds = (calGiven.getTimeInMillis()-calToday.getTimeInMillis())/1000;
        minutes = Math.abs((int)Math.ceil((dateDiffSeconds%3600)/60.0));
        hours = Math.abs((int)(dateDiffSeconds%86400)/3600);
        days = Math.abs((int)(dateDiffSeconds/86400));

        if(dateDiffSeconds<0){
            tvDisplayDiff.setBackgroundColor(0xFFFF0000);
        } else {
            tvDisplayDiff.setBackgroundColor(0xFF00FF00);
        }

        tvDisplayDiff.setText(getResources().getString(R.string.strDateDiff, days, hours, minutes));
    }
}

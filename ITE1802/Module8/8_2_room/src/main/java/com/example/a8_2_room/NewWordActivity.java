package com.example.a8_2_room;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class NewWordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_WORD = "com.example.a8_2_room.REPLY.WORD";
    public static final String EXTRA_REPLY_DESCRIPTION = "com.example.a8_2_room.REPLY.DESCRIPTION";
    private EditText mEditWordView;
    private EditText mEditDescriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        mEditWordView = findViewById(R.id.edit_word);
        mEditDescriptionView = findViewById(R.id.edit_description);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditWordView.getText()) || TextUtils.isEmpty(mEditDescriptionView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String word = mEditWordView.getText().toString();
                String description = mEditDescriptionView.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY_WORD, word);
                replyIntent.putExtra(EXTRA_REPLY_DESCRIPTION, description);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}

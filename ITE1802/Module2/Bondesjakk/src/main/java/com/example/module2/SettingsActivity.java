package com.example.module2;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.EditText;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText etSecondsPerMove,
            etColorPlayer1,
            etColorPlayer2,
            etSymbolPlayer1,
            etSymbolPlayer2,
            etBoardSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etSecondsPerMove = findViewById(R.id.et_sec_per_move);
        etColorPlayer1 = findViewById(R.id.et_color_p1);
        etColorPlayer2 = findViewById(R.id.et_color_p2);
        etSymbolPlayer1 = findViewById(R.id.et_symbol_p1);
        etSymbolPlayer2 = findViewById(R.id.et_symbol_p2);
        etBoardSize = findViewById(R.id.et_board_size);

        // toolbar
        toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        // add "up" arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Update textfields with currently used values
        Intent data = getIntent();
        etSecondsPerMove.setText(String.valueOf(data.getIntExtra("secPerMove", 7)));
        etColorPlayer1.setText(data.getStringExtra("colorP1"));
        etColorPlayer2.setText(data.getStringExtra("colorP2"));
        etSymbolPlayer1.setText(data.getStringExtra("symP1"));
        etSymbolPlayer2.setText(data.getStringExtra("symP2"));
        etBoardSize.setText(String.valueOf(data.getIntExtra("boardSize", 3)));


    }
    public boolean onOptionsItemSelected(MenuItem item) {
        setData();
        return super.onOptionsItemSelected(item);
    }

    public void setData(){
        // Add data to "return" intent
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("secPerMove", etSecondsPerMove.getText().toString());
        intent.putExtra("colorP1", etColorPlayer1.getText().toString());
        intent.putExtra("colorP2", etColorPlayer2.getText().toString());
        intent.putExtra("symP1", etSymbolPlayer1.getText().toString());
        intent.putExtra("symP2", etSymbolPlayer2.getText().toString());
        intent.putExtra("boardSize", etBoardSize.getText().toString());
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {
        // Override back-button and force it to behave like the "up" arrow on the toolbar
        setData();
        finish();
        super.onBackPressed();
    }
}

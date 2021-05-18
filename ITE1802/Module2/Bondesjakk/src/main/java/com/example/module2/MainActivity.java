package com.example.module2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    /////////////////////////////////////////////////////////////////
    //  Warning! This code worships the Flying Spaghetti Monster!  //
    //  (i.e. it's spaghetti-code, feast your eyes on the cringe!) //
    /////////////////////////////////////////////////////////////////

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    Button btnStartStop;
    TextView tvPlayerMsg, tvSymbolP1 ,tvSymbolP2, tvGameTimer, tvResultMsg;
    LinearLayout llGameBoard;
    Button[][] btnArrGameBoard; //row, cols
    GameBoard gameBoard;
    CountDownTimer countDownTimer;
    long untilCountdownFinish, p1TotTime, p2TotTime;
    int boardSize, countdownTime;
    char symbolP1, symbolP2;
    String colorP1, colorP2;
    Toolbar mainToolbar;
    Menu menu;
    CountDownTimer tempTimer;


    //TODO: Rewrite entire thing...

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        countdownTime = 7;
        colorP1 = "#0000FF";
        colorP2 = "#FF00FF";
        symbolP1 = getString(R.string.default_symbol_p1).charAt(0);
        symbolP2 = getString(R.string.default_symbol_p2).charAt(0);
        boardSize = 3;

        btnStartStop = findViewById(R.id.btn_start_stop);
        tvPlayerMsg = findViewById(R.id.tv_player_msg);
        tvSymbolP1 = findViewById(R.id.tv_symbol_player1);
        tvSymbolP2 = findViewById(R.id.tv_symbol_player2);
        tvGameTimer = findViewById(R.id.tv_game_timer);
        llGameBoard = findViewById(R.id.ll_game_board);
        tvResultMsg = findViewById(R.id.tv_result_msg);

        tvSymbolP1.setTextColor(Color.parseColor(colorP1));
        tvSymbolP1.setText(getString(R.string.str_sym_p1, symbolP1));
        tvSymbolP2.setTextColor(Color.parseColor(colorP2));
        tvSymbolP2.setText(getString(R.string.str_sym_p2, symbolP2));

        tvGameTimer.setText(getResources().getString(R.string.str_game_timer, 00, 00));

        btnStartStop.setOnClickListener((view) -> startStop());

        countDownTimer = getCountDownTimer(countdownTime*1000);
        if(savedInstanceState==null) {
            gameBoard = new GameBoard(boardSize);
            this.setupGameBoard();
        }

    }

    private CountDownTimer getCountDownTimer(long milliSeconds) {
        return new CountDownTimer(milliSeconds, 1) {
            @Override
            public void onTick(long untilFinished) {
                untilCountdownFinish=untilFinished;
                int sec = (int)(untilFinished/1000);
                int millis = (int)(untilFinished%1000)/10;
                tvGameTimer.setText(getString(R.string.str_game_timer, sec, millis));
            }

            @Override
            public void onFinish() {
                tvGameTimer.setText(getResources().getString(R.string.str_game_timer, 00, 00));
                outOfTime();
            }
        };
    }

    private void setupGameBoard(){
        llGameBoard.post(() -> {
            btnArrGameBoard = new Button[GameBoard.SIZE][GameBoard.SIZE];
            //Gotta do layout-shit here...
            //Can't get the width of the gameboard in onCreate, so we post a runnable to the que
            GameBoard.WIDTH = llGameBoard.getWidth();
            GameBoard.HEIGHT = llGameBoard.getHeight();
            int width = GameBoard.WIDTH/GameBoard.SIZE;
            int height = GameBoard.HEIGHT/GameBoard.SIZE;

            LinearLayout.LayoutParams llLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for(int row=0; row<boardSize; row++){
                LinearLayout llRow = new LinearLayout(this);
                llRow.setLayoutParams(llLayoutParams);

                for(int col=0; col< boardSize; col++){
                    final int fRow=row, fCol=col;
                    Button btn = new Button(this);
                    btn.setOnClickListener((view) -> executeRound(fRow, fCol));
                    btn.setEnabled(false);
                    btn.setWidth(width);
                    btn.setHeight(height);
                    btnArrGameBoard[fRow][fCol] = btn;
                    llRow.addView(btnArrGameBoard[fRow][fCol]);
                }
                llGameBoard.addView(llRow);
            }
        });
    }

    private void startStop() {

        untilCountdownFinish=0;
        p1TotTime=0;
        p2TotTime=0;

        if(btnStartStop.getText().equals(getString(R.string.str_start))){
            //When start is pressed
            btnStartStop.setText(getResources().getText(R.string.str_stopp));
            tvResultMsg.setText(getString(R.string.default_result_msg));
            menu.getItem(0).setEnabled(false);
            for(Button[] btnArr: btnArrGameBoard){
                for(Button btn: btnArr){
                    btn.setEnabled(true);
                    btn.setText("");
                }
            }
            gameBoard.clearBoard();

            if(gameBoard.getCurrentPlayer()==symbolP1){
                tvSymbolP1.setBackground(getDrawable(R.drawable.drw_player_indicator_selected));
                tvSymbolP2.setBackground(getDrawable(R.drawable.drw_player_indicator_background));
            } else {
                gameBoard.setCurrentPlayer(symbolP1);
                tvSymbolP1.setBackground(getDrawable(R.drawable.drw_player_indicator_selected));
                tvSymbolP2.setBackground(getDrawable(R.drawable.drw_player_indicator_background));
            }

            countDownTimer.start();
        } else {
            //When stop is pressed
            btnStartStop.setText(getResources().getText(R.string.str_start));
            clearBtnGameboard();
            tvSymbolP1.setBackground(getDrawable(R.drawable.drw_player_indicator_background));
            tvSymbolP2.setBackground(getDrawable(R.drawable.drw_player_indicator_background));
            countDownTimer.cancel();
            if(tempTimer!=null){
                tempTimer.cancel();
            }
            menu.getItem(0).setEnabled(true);
        }
    }

    private void executeRound(int fRow, int fCol) {
        btnArrGameBoard[fRow][fCol].setText(String.valueOf(gameBoard.getCurrentPlayer()));
        btnArrGameBoard[fRow][fCol].setTextColor(Color.parseColor(gameBoard.getCurrentPlayer()==symbolP1?colorP1:colorP2));
        gameBoard.setMark(fRow, fCol, gameBoard.getCurrentPlayer());

        countDownTimer.cancel();
        if(tempTimer!=null){
            tempTimer.cancel();
        }
        if(gameBoard.getCurrentPlayer()==symbolP1){
            p1TotTime+=countdownTime*1000-untilCountdownFinish;
        }else{
            p2TotTime+=countdownTime*1000-untilCountdownFinish;
        }


        char winner = 0;
        if(gameBoard.checkRows()>-1 || gameBoard.checkCols()>-1 || gameBoard.checkDiags()>-1){
            winner = gameBoard.getCurrentPlayer();
        }
        if(winner>0){
            int sec=0, millis=0;
            if(gameBoard.getCurrentPlayer()==symbolP1){
                sec = (int)(p1TotTime/1000);
                millis = (int)(p1TotTime%1000)/10;
            }else{
                sec = (int)(p2TotTime/1000);
                millis = (int)(p2TotTime%1000)/10;
            }

            tvResultMsg.setText(getString(R.string.str_result_msg, String.valueOf(winner), sec, millis));
            clearBtnGameboard();
            btnStartStop.setText(getText(R.string.str_start));
            menu.getItem(0).setEnabled(true);
        } else if(gameBoard.getRoundsPlayed()==(GameBoard.SIZE*GameBoard.SIZE)) {
            tvResultMsg.setText(getString(R.string.str_draw));
            clearBtnGameboard();
            btnStartStop.setText(getText(R.string.str_start));
            menu.getItem(0).setEnabled(true);
            tvSymbolP1.setBackground(getDrawable(R.drawable.drw_player_indicator_background));
            tvSymbolP2.setBackground(getDrawable(R.drawable.drw_player_indicator_background));
        }
        else {
            switchPlayer();
            countDownTimer.start();
        }
        btnArrGameBoard[fRow][fCol].setEnabled(false);
    }

    private void outOfTime(){
        switchPlayer();
        countDownTimer.cancel();
        countDownTimer.start();
        if(tempTimer!=null){
            tempTimer.cancel();
        }
    }

    private void switchPlayer() {
        gameBoard.switchPlayers();
        if(gameBoard.getCurrentPlayer()==symbolP1){
            tvSymbolP1.setBackground(getDrawable(R.drawable.drw_player_indicator_selected));
            tvSymbolP2.setBackground(getDrawable(R.drawable.drw_player_indicator_background));
        } else {
            tvSymbolP2.setBackground(getDrawable(R.drawable.drw_player_indicator_selected));
            tvSymbolP1.setBackground(getDrawable(R.drawable.drw_player_indicator_background));
        }
    }

    private void clearBtnGameboard() {
        for (Button[] btnArr : btnArrGameBoard) {
            for (Button btn : btnArr) {
                btn.setEnabled(false);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                //Vise settings-skjerm...
                Toast toast = Toast.makeText(this, "Settings", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(this, SettingsActivity.class);

                intent.putExtra("secPerMove", countdownTime);
                intent.putExtra("colorP1", colorP1);
                intent.putExtra("colorP2", colorP2);
                intent.putExtra("symP1", String.valueOf(symbolP1));
                intent.putExtra("symP2", String.valueOf(symbolP2));
                intent.putExtra("boardSize", boardSize);

                startActivityForResult(intent, 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                countdownTime = data.getStringExtra("secPerMove").equals("")?countdownTime:
                        (Integer.parseInt(data.getStringExtra("secPerMove"))>10||Integer.parseInt(data.getStringExtra("secPerMove"))<1)?7:
                                Integer.parseInt(data.getStringExtra("secPerMove"));

                //Gotta avoid them nullpointers...
                colorP1 = data.getStringExtra("colorP1")!=null?(data.getStringExtra("colorP1").length()==7?(data.getStringExtra("colorP1").matches("#[0-9a-fA-F]{6}")?data.getStringExtra("colorP1"):colorP1):colorP1):colorP1;
                colorP2 = data.getStringExtra("colorP2")!=null?(data.getStringExtra("colorP2").length()==7?(data.getStringExtra("colorP2").matches("#[0-9a-fA-F]{6}")?data.getStringExtra("colorP2"):colorP2):colorP2):colorP2;
                symbolP1 = data.getStringExtra("symP1")!=null?(data.getStringExtra("symP1").length()==1?data.getStringExtra("symP1").charAt(0):symbolP1):symbolP1;
                symbolP2 = data.getStringExtra("symP2")!=null?(data.getStringExtra("symP2").length()==1?data.getStringExtra("symP2").charAt(0):symbolP2):symbolP2;

                boardSize = data.getStringExtra("boardSize").equals("")?boardSize:
                        (Integer.parseInt(data.getStringExtra("boardSize"))>10||Integer.parseInt(data.getStringExtra("boardSize"))<3)?3:
                                Integer.parseInt(data.getStringExtra("boardSize"));

                tvSymbolP1.setTextColor(Color.parseColor(colorP1));
                tvSymbolP1.setText(getString(R.string.str_sym_p1, symbolP1));
                tvSymbolP2.setTextColor(Color.parseColor(colorP2));
                tvSymbolP2.setText(getString(R.string.str_sym_p2, symbolP2));

                llGameBoard.removeAllViews();
                gameBoard = new GameBoard(boardSize);
                gameBoard.setPlayerSymbols(symbolP1, symbolP2);
                this.setupGameBoard();
                countDownTimer = getCountDownTimer(countdownTime*1000);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("gameIsRunning", !btnStartStop.getText().equals(getString(R.string.str_start)));
        outState.putInt("secPerMove", countdownTime);
        outState.putString("colorP1", colorP1);
        outState.putString("colorP2", colorP2);
        outState.putString("symP1", String.valueOf(symbolP1));
        outState.putString("symP2", String.valueOf(symbolP2));
        outState.putInt("boardSize", boardSize);
        outState.putInt("roundsPlayed", gameBoard.getRoundsPlayed());
        outState.putChar("currentPlayer", gameBoard.getCurrentPlayer());
        outState.putInt("SIZE", GameBoard.SIZE);
        outState.putLong("p1TotTime", p1TotTime);
        outState.putLong("p2TotTime", p2TotTime);
        outState.putLong("untilCountdownFinish", untilCountdownFinish);

        String[] timer = tvGameTimer.getText().toString().split(":");
        outState.putInt("timeLeftMs", Integer.parseInt(timer[0])*1000+Integer.parseInt(timer[1])*10);


        //Restore btnGameBoard array...
        char[] btnArrGameBoardSaved = new char[GameBoard.SIZE*GameBoard.SIZE];
        for(int row=0; row<GameBoard.SIZE; row++){
            for(int col=0; col<GameBoard.SIZE; col++){
                char symbol = btnArrGameBoard!=null?(btnArrGameBoard[row][col].getText().length()!=0?btnArrGameBoard[row][col].getText().charAt(0):0):0;
                btnArrGameBoardSaved[(row*GameBoard.SIZE+col)] = symbol; //   O_______O'
            }
        }
        outState.putCharArray("btnArrGameBoardSaved", btnArrGameBoardSaved);

        char[] gameBoardSaved = new char[GameBoard.SIZE*GameBoard.SIZE];
        for(int row=0; row<GameBoard.SIZE; row++){
            for(int col=0; col<GameBoard.SIZE; col++){
                gameBoardSaved[(row*GameBoard.SIZE+col)] = gameBoard!=null?gameBoard.getGameBoard()[row][col]:0; //   O_______O'
            }
        }
        outState.putCharArray("gameBoardSaved", gameBoardSaved);


        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        boolean gameIsRunning = savedInstanceState.getBoolean("gameIsRunning");
        countdownTime = savedInstanceState.getInt("secPerMove", 7);
        colorP1 = savedInstanceState.getString("colorP1");
        colorP2 = savedInstanceState.getString("colorP2");
        symbolP1 = savedInstanceState.getString("symP1").charAt(0);
        symbolP2 = savedInstanceState.getString("symP2").charAt(0);
        boardSize = savedInstanceState.getInt("boardSize", 3);
        p1TotTime = savedInstanceState.getLong("p1TotTime");
        p2TotTime = savedInstanceState.getLong("p2TotTime");
        untilCountdownFinish = savedInstanceState.getLong("untilCountdownFinish");

        // Recreate gameboard-arrays
        // But we have to wait until new gameboard-sizes can be calculated before recreating gameboards....
        llGameBoard.post(()->{
            gameBoard = new GameBoard(boardSize);
            btnArrGameBoard = new Button[GameBoard.SIZE][GameBoard.SIZE];
            gameBoard.setRoundsPlayed(savedInstanceState.getInt("roundsPlayed"));
            gameBoard.setCurrentPlayer(savedInstanceState.getChar("currentPlayer"));
            gameBoard.setPlayerSymbols(symbolP1,symbolP2);
            GameBoard.SIZE = savedInstanceState.getInt("SIZE", 3);
            GameBoard.WIDTH = llGameBoard.getWidth();
            GameBoard.HEIGHT = llGameBoard.getHeight();
            int width = GameBoard.WIDTH / GameBoard.SIZE;
            int height = GameBoard.HEIGHT / GameBoard.SIZE;

            LinearLayout.LayoutParams llLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (int row = 0; row < boardSize; row++) {
                LinearLayout llRow = new LinearLayout(MainActivity.this);
                llRow.setLayoutParams(llLayoutParams);

                for (int col = 0; col < boardSize; col++) {
                    final int fRow = row, fCol = col;
                    Button btn = new Button(MainActivity.this);
                    btn.setOnClickListener((view) -> executeRound(fRow, fCol));
                    btn.setEnabled(false);
                    btn.setWidth(width);
                    btn.setHeight(height);
                    btnArrGameBoard[fRow][fCol] = btn;
                    llRow.addView(btnArrGameBoard[fRow][fCol]);
                }
                llGameBoard.addView(llRow);
            }
            //Restore btnGameBoard array...
            char[] btnArrGameBoardSaved = savedInstanceState.getCharArray("btnArrGameBoardSaved");
            for (int row = 0; row < GameBoard.SIZE; row++) {
                for (int col = 0; col < GameBoard.SIZE; col++) {
                    char symbol = btnArrGameBoardSaved != null ? (btnArrGameBoardSaved[(row * GameBoard.SIZE + col)]) : 0;
                    btnArrGameBoard[row][col].setText(String.valueOf(symbol));//   O_______O'
                    btnArrGameBoard[row][col].setEnabled(symbol == 0);
                    btnArrGameBoard[row][col].setTextColor(Color.parseColor(symbol == symbolP1 ? colorP1 : (symbol == symbolP2 ? colorP2 : "#000000")));
                }
            }
            // Restore GameBoard array...
            char[] gameBoardSaved = savedInstanceState.getCharArray("gameBoardSaved");
            for (int row = 0; row < GameBoard.SIZE; row++) {
                for (int col = 0; col < GameBoard.SIZE; col++) {
                    char player = gameBoardSaved != null ? (gameBoardSaved[(row * GameBoard.SIZE + col)]) : 0;
                    gameBoard.setMark(row, col, player);  //   O_______O'
                }
            }
        });

        //timer-state...
        int timeLeftMs = savedInstanceState.getInt("timeLeftMs", 7000);


        if(gameIsRunning){
            btnStartStop.setText(getString(R.string.str_stopp));
            if(savedInstanceState.getChar("currentPlayer")==symbolP1){
                tvSymbolP1.setBackground(getDrawable(R.drawable.drw_player_indicator_selected));
            } else {
                tvSymbolP2.setBackground(getDrawable(R.drawable.drw_player_indicator_selected));
            }
            tempTimer = getCountDownTimer(timeLeftMs);
            tempTimer.start();
        }
    }
}

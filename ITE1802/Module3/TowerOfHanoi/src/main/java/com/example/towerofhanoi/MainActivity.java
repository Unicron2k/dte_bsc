package com.example.towerofhanoi;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private int moves, numRings;
    private long startTime, elapsedTime;
    private TextView tvStatus;
    private ImageView ivRingRed, ivRingBlue, ivRingGreen;
    private Button btnStartStop;
    LinearLayout llTower1, llTower2, llTower3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moves=0;
        elapsedTime = 0;
        numRings =3;

        findViewById(R.id.ring_red).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.ring_blue).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.ring_green).setOnTouchListener(new MyTouchListener());

        findViewById(R.id.tower_1).setOnDragListener(new MyDragListener());
        findViewById(R.id.tower_2).setOnDragListener(new MyDragListener());
        findViewById(R.id.tower_3).setOnDragListener(new MyDragListener());

        llTower1 = findViewById(R.id.tower_1);
        llTower2 = findViewById(R.id.tower_2);
        llTower3 = findViewById(R.id.tower_3);

        btnStartStop = findViewById(R.id.btnStartStop);

        ivRingRed = findViewById(R.id.ring_red);
        ivRingBlue = findViewById(R.id.ring_blue);
        ivRingGreen = findViewById(R.id.ring_green);

        tvStatus = findViewById(R.id.tvStatus);
        tvStatus.setText(getResources().getString(R.string.str_status, moves, (double)elapsedTime));

        ringsEnabled(false);


    }

    public void onClick(View view){
        if(btnStartStop.getText().equals("Start")){
            //When start is pressed
            //Reset game
            resetGame();
        } else {
            //When stop is pressed
            //Stop timer, update status and disable rings
            stopGame();
        }
    }

    protected void ringsEnabled(boolean enabled){
        // Hardcoded
        ivRingRed.setEnabled(enabled);
        ivRingBlue.setEnabled(enabled);
        ivRingGreen.setEnabled(enabled);
    }

    protected void resetGame(){
        // Reset variables
        moves = 0;
        elapsedTime=0;

        // Reset textviews
        btnStartStop.setText(R.string.str_stop);
        tvStatus.setText(getResources().getString(R.string.str_status, moves, (double)elapsedTime));

        // Reset rings
        // Hardcoded
        llTower1.removeAllViews();
        llTower2.removeAllViews();
        llTower3.removeAllViews();
        llTower1.addView(ivRingRed);
        llTower1.addView(ivRingGreen);
        llTower1.addView(ivRingBlue);

        // Enable rings
        ringsEnabled(true);

        // "Start timer"
        startTime = System.currentTimeMillis();
    }

    protected void stopGame(){
        btnStartStop.setText(R.string.str_start);
        elapsedTime = System.currentTimeMillis() - startTime;
        tvStatus.setText(getResources().getString(R.string.str_status, moves, elapsedTime/1000.0));
        ringsEnabled(false);
    }

    // Defining the touch-listener
    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            // TODO: Refactor to pick the top element when any one is touched
            // Get the parent-tower of the currently touched ring
            LinearLayout owner = (LinearLayout) view.getParent();
            // Get the topmost ring in the tower
            View top = owner.getChildAt(0);
            // If the topmost ring matches the touched ring, we can move on with our life
            if (view == top || owner.getChildCount() == 1) {
                // And let it be touched...
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                    // TODO: Fix this one
                    // view.setVisibility(View.INVISIBLE); // <-- Buggs out the view, making it invisible/un-clickable if "dropped" outside the layout-borders...
                    return true;
                }
            }
            // If it wasn't, it was not the droid we were looking for...
            return false;
        }
    }

    //Defining the drag-listener
    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(R.drawable.shape_tower_droptarget, null);
        Drawable normalShape = getResources().getDrawable(R.drawable.shape_tower, null);

        @Override
        public boolean onDrag(View targetView, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    targetView.setBackground(enterShape);
                    break;
                case DragEvent.ACTION_DROP:
                    View draggedView = (View) dragEvent.getLocalState();
                    ViewGroup owner = (ViewGroup) draggedView.getParent();
                    LinearLayout container = (LinearLayout) targetView;

                    // <-- Interesting code starts here -->
                    // Topmost ring in a tower is always at index 0
                    View topRing = ((LinearLayout) targetView).getChildAt(0);
                    boolean canDrop=true;
                    if(topRing!=null) {
                        // If the ring exists, and is larger than the one we are trying to put there, we allow it
                        canDrop = topRing.getWidth() > draggedView.getWidth();
                    }
                    if(canDrop) {
                        owner.removeView(draggedView);
                        container.addView(draggedView, 0);
                        targetView.setVisibility(View.VISIBLE);
                        moves++;
                    }
                    if(llTower3.getChildCount()== numRings) {
                        //Stop timer, update status and disable rings
                        stopGame();
                    }
                    //<-- Interesting code ends here -->
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                case DragEvent.ACTION_DRAG_ENDED:
                    targetView.setBackground(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        LinearLayout towerContainer = findViewById(R.id.towerContainer);
        // We use a int[] to store the state of the towers
        int[] stateTowers = new int[towerContainer.getChildCount() * towerContainer.getChildCount()];
        // Iterating through the tower-list
        for (int towerIndex = 0; towerIndex < towerContainer.getChildCount(); towerIndex++) {
            // Get a reference to a tower
            LinearLayout tower = (LinearLayout) towerContainer.getChildAt(towerIndex);
            // And iterate through the rings
            for (int ringIndex = 0; ringIndex < tower.getChildCount(); ringIndex++) {
                // Calculate the index of where to put it
                int index = (towerIndex * towerContainer.getChildCount()) + ringIndex; //<---
                // Aaaaaand plop it into our array
                stateTowers[index] = tower.getChildAt(ringIndex).getId();
            }
        }

        outState.putInt("moves", moves);
        outState.putLong("startTime", startTime);
        outState.putInt("numRings", numRings);
        outState.putIntArray("stateTowers", stateTowers);
        outState.putString("btnState", btnStartStop.getText().toString());
        outState.putString("tvStatusState", tvStatus.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        moves = savedInstanceState.getInt("moves");
        startTime = savedInstanceState.getLong("startTime");
        numRings = savedInstanceState.getInt("numRings", numRings);
        int[] stateTowers = savedInstanceState.getIntArray("stateTowers");
        String btnState = savedInstanceState.getString("btnState");
        String tvStatusState = savedInstanceState.getString("tvStatusState");

        assert btnState != null;
        if(btnState.equals("Stop")) {
            btnStartStop.setText(R.string.str_stop);
            ringsEnabled(true);
        } else {
            tvStatus.setText(tvStatusState);
        }


        // Some of the following black magic spaghetti voodoo can be avoided by keeping track of
        // the state of the towers/rings continuously during the game.
        // .....but that's not fun!
        LinearLayout towerContainer = findViewById(R.id.towerContainer);
        LinearLayout[] towerList = new LinearLayout[towerContainer.getChildCount()];


        // Get list of towers
        for(int towerIndex = 0; towerIndex<towerContainer.getChildCount(); towerIndex++){
            towerList[towerIndex] = (LinearLayout)towerContainer.getChildAt(towerIndex);
        }

        // Get list of rings
        // On restart, before we set stuff up, all the rings will always be in tower 0
        ImageView[] ringList = new ImageView[towerList[0].getChildCount()];
        for(int ringIndex = 0; ringIndex<towerList[0].getChildCount(); ringIndex++){
            ringList[ringIndex] = (ImageView)towerList[0].getChildAt(ringIndex);
        }

        // Remove all rings currently in place
        for(LinearLayout t: towerList){
            t.removeAllViews();
        }

        // Now we iterate through eeeeeeevery list we have and check which thig should go where...
        // Iterate through list of towers
        for (int towerIndex=0; towerIndex<towerList.length; towerIndex++){
            // iterate through list of rings
            for (int ringIndex=0; ringIndex<ringList.length; ringIndex++){
                // calculate an off-set for the stateTowers-array, corresponding to ringIndex at towerIndex in the 1D array (possibly, there's a WAY easier dynamic solution...)
                int offset = towerIndex*towerContainer.getChildCount();
                // iterate through stateTowers array, utilizing calculated offset
                for (int stateTowersIndex = offset; stateTowersIndex<offset+ringList.length; stateTowersIndex++) {
                    assert stateTowers != null;
                    // get the ringID
                    int ringID = ringList[ringIndex].getId();
                    // get the saved ringID at stored offset
                    int storedID = stateTowers[stateTowersIndex];
                    // do they match?
                    if (ringID == storedID) {
                        // If so, plopp the ring at ringIndex into the tower at towerIndex
                        towerList[towerIndex].addView(ringList[ringIndex]);
                        // Probably an easier way to do it, but this works for now...
                    }
                }
            }
        }
    }
}

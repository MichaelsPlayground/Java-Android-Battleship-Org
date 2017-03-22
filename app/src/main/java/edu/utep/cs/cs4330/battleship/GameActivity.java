/*
 * Author: Jose Perez <josegperez@mail.com>
 */
package edu.utep.cs.cs4330.battleship;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    // Views
    private BoardView boardView;
    private TextView textShots;
    private TextView textSunk;
    private Button btnNew;

    // Models
    private Board board;

    private final int TOTAL_SHIPS = 5;
    private int shotCount = 0;
    private int sunkCount = 0;

    private boolean isSoundEnabled = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuitem_sound)
            isSoundEnabled = !isSoundEnabled;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Debug", "Created");
        setContentView(R.layout.activity_game);

        boardView = (BoardView) findViewById(R.id.boardView);
        textShots = (TextView) findViewById(R.id.txtShots);
        textSunk = (TextView) findViewById(R.id.textSunk);
        btnNew = (Button) findViewById(R.id.btnNew);
        reset();

        //if (!savedInstanceState.isEmpty()) {
            //shotCount = savedInstanceState.getInt("shots");
            //sunkCount = savedInstanceState.getInt("sunks");
        //}
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putInt("shots", shotCount);
        //outState.putInt("sunks", sunkCount);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Debug", "---=== Started");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Debug", "Resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Debug", "Paused");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Debug", "Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Debug", "Destroyed");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Debug", "Restarted");
    }

    public void onNewButton(View v) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.new_title))
                .setMessage(getString(R.string.new_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        reset();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void reset() {
        shotCount = 0;
        sunkCount = 0;
        updateShotCounter();
        updateSunkCounter();

        board = new Board(10);
        Log.d("Cheat", board.boardToString());

        board.addBoardListener(new Board.BoardListener() {
            @Override
            public void onShipHit(Ship ship) {
                shotCount++;

                if(isSoundEnabled) {
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.hit);
                    mp.start();
                }

                if (ship.isDestroyed()) {
                    if(isSoundEnabled) {
                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sink);
                        mp.start();
                    }
                    sunkCount++;
                }

                if (sunkCount == TOTAL_SHIPS) {
                    if(isSoundEnabled) {
                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.gameover);
                        mp.start();
                    }
                }


                updateSunkCounter();
                updateShotCounter();
            }

            @Override
            public void onShipMiss() {
                shotCount++;
                updateShotCounter();
            }
        });
        boardView.setBoard(board);
    }

    private void updateShotCounter() {
        textShots.setText(getString(R.string.num_shots) + shotCount);
    }

    private void updateSunkCounter() {
        textSunk.setText(getString(R.string.num_sunk_ships) + sunkCount);
    }

}

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
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

public class GameActivity extends AppCompatActivity {
    // Views
    private BoardView boardView;
    private Switch switchSound;

    // Models
    private Board board;
    private boolean isSoundEnabled = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuitem_sound) {
            isSoundEnabled = !isSoundEnabled;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        boardView = (BoardView) findViewById(R.id.boardView);
        switchSound = (Switch) findViewById(R.id.switchSound);
        //reset();

        Bundle extras = getIntent().getExtras();
        if(extras == null)
            return;

        String jsonMyObject;
        jsonMyObject = extras.getString("BOARD");
        if(jsonMyObject != null) {
            Board b = new Gson().fromJson(jsonMyObject, Board.class);
            boardView.setBoard(b);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

        board = new Board(10);
        Log.d("Cheat", board.boardToString());

        board.addBoardListener(new Board.BoardListener() {
            @Override
            public void onShipHit(Ship ship) {

            }

            @Override
            public void onShipMiss() {

            }
        });
        boardView.setBoard(board);
    }

}

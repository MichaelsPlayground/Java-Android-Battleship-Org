/*
 * Author: Jose Perez <josegperez@mail.com>
 */
package edu.utep.cs.cs4330.battleship;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.gson.Gson;

public class GameActivity extends AppCompatActivity {
    // Views
    private SpectatorBoardView spectatorBoardView;
    private Switch switchSound;

    // Models
    private Board board;
    private boolean isSoundEnabled = true;

    private AlertDialog.Builder dialogExit;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuitem_sound) {
            isSoundEnabled = !isSoundEnabled;
            switchSound.setChecked(isSoundEnabled);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dialogExit = new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to exit the game and go to the main menu?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getParent(), MainMenuActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        spectatorBoardView = (SpectatorBoardView) findViewById(R.id.spectatorBoardView);
        switchSound = (Switch) findViewById(R.id.switchSound);
        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSoundEnabled = isChecked;
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null)
            return;

        String jsonMyObject;
        jsonMyObject = extras.getString("BOARD");
        if(jsonMyObject != null) {
            Board b = new Gson().fromJson(jsonMyObject, Board.class);
            spectatorBoardView.setBoard(b);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onClickMainMenu(View v) {
        dialogExit.show();
    }

    @Override
    public void onBackPressed() {
        dialogExit.show();
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
        spectatorBoardView.setBoard(board);
    }

}

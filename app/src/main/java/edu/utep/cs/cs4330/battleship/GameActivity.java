/*
 * Author: Jose Perez <josegperez@mail.com>
 */
package edu.utep.cs.cs4330.battleship;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

public class GameActivity extends AppCompatActivity implements Game.GameListener {
    // Views
    private BoardView spectatorBoardView;
    private BoardView opponentBoardView;
    private Switch switchSound;

    private TextView textCurrentPlayer;

    private Game game;

    private Board playerBoard;
    private int playerBoardSunkShips;

    private Board cpuBoard;
    private int cpuBoardSunkShips;

    private boolean isSoundEnabled = true;

    private AlertDialog.Builder dialogExit;
    private AlertDialog.Builder dialogGameover;

    @Override
    public void onTurnChange(Player currentPlayer) {
        /*Log.d("Debug", "Turn change");
        if(currentPlayer instanceof AIPlayer){
            Log.d("Debug", "AI's turn");
            textCurrentPlayer.setText("AI");
            opponentBoardView.disableBoardTouch = true;
            Vector2 simulatedPlay = currentPlayer.onOwnTurn();
            boolean hit = playerBoard.hit(simulatedPlay);

            opponentBoardView.invalidate();
            spectatorBoardView.invalidate();
        }
        else{
            textCurrentPlayer.setText("Player");
            opponentBoardView.disableBoardTouch = false;
        }*/
    }

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
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_game);

        dialogExit = new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to exit the game and go to the main menu?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplication(), MainMenuActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        dialogGameover = new AlertDialog.Builder(this)
                .setTitle("Game over!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplication(), MainMenuActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        spectatorBoardView = (BoardView) findViewById(R.id.spectatorBoardView);
        opponentBoardView = (BoardView) findViewById(R.id.opponentBoardView);

        textCurrentPlayer = (TextView)findViewById(R.id.textCurrentPlayer);

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

        /*String jsonMyObject;
        jsonMyObject = extras.getString("BOARD");
        if(jsonMyObject != null) {
            playerBoard = new Gson().fromJson(jsonMyObject, Board.class);

            playerBoard.addBoardListener(new Board.BoardListener() {
                @Override
                public void onShipHit(Ship ship) {
                    //playSound(Sound.Hit);
                    Log.d("Debug", "AI damaged ship: " + ship.health);
                    if(ship.isDestroyed()) {
                        playSound(Sound.Sink);
                        playerBoardSunkShips++;
                        Log.d("Debug", "AI sunk a ship");
                    }

                    if(playerBoardSunkShips >= playerBoard.totalShips)
                        onGameover(false);
                }

                @Override
                public void onShipMiss() {

                }

                @Override
                public void onHitOutOfBounds(){

                }
            });
        }

        spectatorBoardView.setBoard(playerBoard);

        String strategyText = extras.getString("STRATEGY");
        Strategy strategy = new RandomStrategy();
        if(strategyText != null){
            if(strategyText.equalsIgnoreCase("random"))
                strategy = new RandomStrategy();
            else if(strategyText.equalsIgnoreCase("sweep"))
                strategy = new SweepStrategy();
        }



        Player playerCPU = new AIPlayer(playerBoard, false, strategy);

*/
        cpuBoard = new Board(10);
        cpuBoard.addRandomShips();
        cpuBoard.addBoardListener(new Board.BoardListener() {
            @Override
            public void onShipHit(Ship ship) {
                playSound(Sound.Hit);

                if(ship.isDestroyed()) {
                    playSound(Sound.Sink);
                    cpuBoardSunkShips++;
                }

                if(cpuBoardSunkShips >= cpuBoard.totalShips)
                    onGameover(true);
            }

            @Override
            public void onShipMiss() {

            }

            @Override
            public void onHitOutOfBounds(){

            }
        });

        opponentBoardView.setBoard(cpuBoard);
        Player playerHuman = new Player(cpuBoard, true);

        game = new Game(playerHuman, playerCPU);
        game.addBoardListener(this);
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

    public void onGameover(boolean humanWin){
        String message;

        if(humanWin)
            message = "You won!";
        else
            message = "You lost!";

        playSound(Sound.Gameover);
        dialogGameover.setMessage(message);
        dialogGameover.show();
    }

    enum Sound { Hit, Sink, Gameover };

    public void playSound(Sound sound){
        if(!isSoundEnabled)
            return;

        MediaPlayer mp;

        if(sound == Sound.Hit)
            mp = MediaPlayer.create(getApplicationContext(), R.raw.hit);
        else if(sound == Sound.Sink)
            mp = MediaPlayer.create(getApplicationContext(), R.raw.sink);
        else
            mp = MediaPlayer.create(getApplicationContext(), R.raw.gameover);

        mp.start();
    }
}

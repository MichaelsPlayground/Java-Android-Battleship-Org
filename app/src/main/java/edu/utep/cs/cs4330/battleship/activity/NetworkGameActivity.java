// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import edu.utep.cs.cs4330.battleship.R;
import edu.utep.cs.cs4330.battleship.model.board.Board;
import edu.utep.cs.cs4330.battleship.model.board.Ship;
import edu.utep.cs.cs4330.battleship.model.game.BattleshipGame;
import edu.utep.cs.cs4330.battleship.model.game.Player;
import edu.utep.cs.cs4330.battleship.network.NetworkConnection;
import edu.utep.cs.cs4330.battleship.network.NetworkInterface;
import edu.utep.cs.cs4330.battleship.network.NetworkPlayer;
import edu.utep.cs.cs4330.battleship.network.packet.Packet;
import edu.utep.cs.cs4330.battleship.network.packet.PacketGameover;
import edu.utep.cs.cs4330.battleship.network.packet.PacketHit;
import edu.utep.cs.cs4330.battleship.network.packet.PacketRequestResponse;
import edu.utep.cs.cs4330.battleship.network.packet.PacketRestartRequest;
import edu.utep.cs.cs4330.battleship.view.NetworkBoardView;

public class NetworkGameActivity extends AppCompatActivity implements BattleshipGame.GameListener, NetworkInterface {
    private enum Sound {Hit, Sink, Gameover}

    private boolean isSoundEnabled = true;

    private NetworkConnection networkConnection;

    /**
     * Small BoardView
     * This is our deployed board that the network player will try to beat
     */
    private NetworkBoardView boardViewDeployed;
    private Board boardDeployed;
    private int sunkShipsByCpu;

    /**
     * Big BoardView
     * This is the board we want to beat
     */
    private NetworkBoardView boardViewRandom;
    private Board boardRandom;
    private int sunkShipsByHuman;

    private Switch switchSound;
    private TextView textCurrentPlayer;

    /**
     * Main game manager
     */
    private BattleshipGame gameMain;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("SOUND", isSoundEnabled);
        outState.putInt("SUNKCPU", sunkShipsByCpu);
        outState.putInt("SUNKHUMAN", sunkShipsByHuman);
        outState.putSerializable("DEPLOYED", boardDeployed);
        outState.putSerializable("RAND", boardRandom);
        outState.putSerializable("GAME", gameMain);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        isSoundEnabled = savedInstanceState.getBoolean("SOUND");
        switchSound.setChecked(isSoundEnabled);

        sunkShipsByCpu = savedInstanceState.getInt("SUNKCPU");
        sunkShipsByHuman = savedInstanceState.getInt("SUNKHUMAN");

        boardDeployed = (Board) savedInstanceState.getSerializable("DEPLOYED");
        boardViewDeployed.setBoard(boardDeployed);

        boardRandom = (Board) savedInstanceState.getSerializable("RAND");
        boardViewRandom.setBoard(boardRandom);

        gameMain = (BattleshipGame) savedInstanceState.getSerializable("GAME");
    }

    private Bundle restartBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restartBundle = savedInstanceState;

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_network_game);

        // Get the necessary views
        boardViewDeployed = (NetworkBoardView) findViewById(R.id.board_view_network_small);
        boardViewRandom = (NetworkBoardView) findViewById(R.id.board_view_network_big);

        textCurrentPlayer = (TextView) findViewById(R.id.text_network_current_player);

        switchSound = (Switch) findViewById(R.id.switch_network_sound);
        switchSound.setChecked(isSoundEnabled);

        if (savedInstanceState != null) {
            return;
        }

        Bundle extras = getIntent().getExtras();

        // Small BoardView
        // This is what the CPU will try to beat
        // It contains our deployment units

        // Load the board and strategy from our deployment activity
        boolean weGoFirst = false;
        if (extras != null) {
            boardDeployed = (Board) extras.get("OWN");
            boardRandom = (Board) extras.get("OPPONENT");
            weGoFirst = extras.getBoolean("FIRST");
        }
        boardDeployed.addBoardListener(new Board.BoardListener() {
            @Override
            public void onShipHit(Ship ship) {
                boardViewRandom.invalidate();
                boardViewDeployed.invalidate();
                playSound(Sound.Hit);
                if (ship.isDestroyed()) {
                    playSound(Sound.Sink);
                    sunkShipsByCpu++;

                    if (sunkShipsByCpu >= boardDeployed.getTotalShips())
                        showDialogGameover(false, true);
                }
            }

            @Override
            public void onShipMiss() {
                boardViewRandom.invalidate();
                boardViewDeployed.invalidate();

            }
        });
        boardViewDeployed.setBoard(boardDeployed);
        boardViewDeployed.isSending = false;
        boardViewDeployed.onCreate(this);

        // This AI wants to defeat our deployed map
        //final Player playerAI = new AIPlayer(boardDeployed, isAIAllowedMultipleShots, strategyAI);
        final Player playerNetwork = new NetworkPlayer(boardDeployed, true);

        boardRandom.addBoardListener(new Board.BoardListener() {
            @Override
            public void onShipHit(Ship ship) {
                boardViewRandom.invalidate();
                boardViewDeployed.invalidate();

                playSound(Sound.Hit);

                if (ship.isDestroyed()) {
                    playSound(Sound.Sink);
                    sunkShipsByHuman++;

                    if (sunkShipsByHuman >= boardRandom.getTotalShips())
                        showDialogGameover(true, true);
                }
            }

            @Override
            public void onShipMiss() {
                boardViewRandom.invalidate();
                boardViewDeployed.invalidate();
            }
        });
        // Big BoardView
        // This is what we want to defeat
        boardViewRandom.isSending = true;
        boardViewRandom.onCreate(this);
        boardViewRandom.setBoard(boardRandom);

        // We want to defeat the random board
        Player playerHuman = new Player(boardRandom, true);

        // Start our main game
        gameMain = new BattleshipGame(playerHuman, playerNetwork);
        gameMain.addGameListener(this);

        // Skip turns if we don't start first
        if(!weGoFirst)
            gameMain.nextTurn();

        // For sound control
        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSoundEnabled = isChecked;
            }
        });
    }


    @Override
    public void onBackPressed() {
        showDialogExit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_game_menu_sound) {
            isSoundEnabled = !isSoundEnabled;
            switchSound.setChecked(isSoundEnabled);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTurnChange(Player currentPlayer) {
        if (currentPlayer instanceof NetworkPlayer) {
            boardViewDeployed.ignoreHits = false;
            boardViewRandom.disableBoardTouch = true;
            textCurrentPlayer.setText("Opponent");

        } else {
            boardViewDeployed.ignoreHits = true;
            boardViewRandom.disableBoardTouch = false;
            textCurrentPlayer.setText(getString(R.string.game_turn_player));
        }
        boardViewRandom.invalidate();
        boardViewDeployed.invalidate();
    }

    public void playSound(Sound sound) {
        if (!isSoundEnabled)
            return;

        MediaPlayer mp;

        if (sound == Sound.Hit)
            mp = MediaPlayer.create(getApplicationContext(), R.raw.hit);
        else if (sound == Sound.Sink)
            mp = MediaPlayer.create(getApplicationContext(), R.raw.sink);
        else
            mp = MediaPlayer.create(getApplicationContext(), R.raw.gameover);

        mp.start();
    }

    public void onClickRequestRestart(View v) {
        showDialogRequestRestart();
    }

    public void showDialogRequestRestart(){
        AlertDialog.Builder dialogExit = new AlertDialog.Builder(this);
        dialogExit.setTitle(getString(R.string.game_exit_title));
        dialogExit.setMessage("Want to request a restart?");
        dialogExit.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PacketRestartRequest packet = new PacketRestartRequest();
                networkConnection.sendPacket(packet);
            }
        });
        dialogExit.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialogExit.setIcon(android.R.drawable.ic_dialog_alert);
        dialogExit.show();
    }
    public void showDialogExit() {
        AlertDialog.Builder dialogExit = new AlertDialog.Builder(this);
        dialogExit.setTitle(getString(R.string.game_exit_title));
        dialogExit.setMessage(R.string.game_exit_message);
        dialogExit.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplication(), MainMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
        dialogExit.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialogExit.setIcon(android.R.drawable.ic_dialog_alert);
        dialogExit.show();
    }

    public void showDialogRequestResponse(){
        AlertDialog.Builder dialogExit = new AlertDialog.Builder(this);
        dialogExit.setTitle("Confirmation");
        dialogExit.setMessage("Your opponent has requested a restart. Agree?");
        dialogExit.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            onCreate(restartBundle);
            PacketRequestResponse response = new PacketRequestResponse(true);
            networkConnection.sendPacket(response);
            }
        });
        dialogExit.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PacketRequestResponse response = new PacketRequestResponse(false);
                networkConnection.sendPacket(response);
            }
        });
        dialogExit.setIcon(android.R.drawable.ic_dialog_alert);
        dialogExit.show();
    }

    public void showDialogGameover(boolean humanWin, boolean sendPacket) {
        String message;
        if (humanWin)
            message = getString(R.string.game_won);

        else
            message = getString(R.string.game_lost);

        PacketGameover packet = new PacketGameover(!humanWin);
        if(sendPacket)
            networkConnection.sendPacket(packet);

        playSound(Sound.Gameover);

        AlertDialog.Builder dialogGameover = new AlertDialog.Builder(this);
        dialogGameover.setTitle(getString(R.string.game_gameover_title));
        dialogGameover.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplication(), MainMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
        dialogGameover.setIcon(android.R.drawable.ic_dialog_alert);
        dialogGameover.setMessage(message);
        dialogGameover.show();
    }


    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onReceive(Packet p) {
        if(p instanceof PacketHit){
            // They fired against us
            PacketHit packetHit = (PacketHit)p;
            boardDeployed.hit(packetHit.X, packetHit.Y);
        }
        else if(p instanceof PacketRestartRequest){
            showDialogRequestRestart();
        }
        else if(p instanceof PacketRequestResponse){
            PacketRequestResponse response = (PacketRequestResponse)p;
            if(response.isAgreed)
                onCreate(restartBundle);
        }
        else if(p instanceof PacketGameover){
            PacketGameover packetGameover = (PacketGameover)p;
            boolean humanWin = packetGameover.isWin;
            showDialogGameover(humanWin, false);
        }
    }

    @Override
    public void onPrepareSend(NetworkConnection networkConnection) {
        this.networkConnection = networkConnection;
    }

}

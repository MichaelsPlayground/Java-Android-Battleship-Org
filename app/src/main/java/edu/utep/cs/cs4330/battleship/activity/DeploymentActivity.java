// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import edu.utep.cs.cs4330.battleship.DeployAIFragment;
import edu.utep.cs.cs4330.battleship.DeployMultiFragment;
import edu.utep.cs.cs4330.battleship.R;
import edu.utep.cs.cs4330.battleship.ai.StrategyType;
import edu.utep.cs.cs4330.battleship.model.GameType;
import edu.utep.cs.cs4330.battleship.model.Ship;
import edu.utep.cs.cs4330.battleship.view.DeploymentBoardView;

public class DeploymentActivity extends AppCompatActivity implements DeploymentBoardView.DeploymentListener {

    private TextView textGamemode;
    private TextView textDeployStatus;
    private DeploymentBoardView boardViewDeployment;
    private Switch switchRotation;
    private RadioGroup radioGroupStrategy;
    private Button buttonDeploy;

    private GameType gameType;


    private Fragment fragmentDeployment;
    private boolean isFragmentReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_deployment);

        textGamemode = (TextView) findViewById(R.id.textDepGamemode);
        boardViewDeployment = (DeploymentBoardView) findViewById(R.id.boardViewDeployment);
        buttonDeploy = (Button) findViewById(R.id.btnDeploy);
        switchRotation = (Switch) findViewById(R.id.switch_rotation);
        textDeployStatus = (TextView) findViewById(R.id.textDeployStatus);
        radioGroupStrategy = (RadioGroup) findViewById(R.id.radioGroupStrategy);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gameType = (GameType)extras.get(getString(R.string.main_menu_intent_gamemode));

            Fragment newFragment;

            if(gameType == GameType.Singleplayer){
                textGamemode.setText(getString(R.string.main_menu_singleplayer_description));
                newFragment = (Fragment)new DeployAIFragment();
            }
            else{
                textGamemode.setText(getString(R.string.main_menu_multiplayer_description));
                newFragment = (Fragment)new DeployMultiFragment();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentDeploy, newFragment);
            transaction.commit();

        }

        boardViewDeployment.addListener(this);

        switchRotation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boardViewDeployment.rotationMode = isChecked;
            }
        });

        updateDeployStatus();
    }

    @Override
    public void onShipDeployed(Ship ship) {
        updateDeployStatus();
    }

    public void updateDeployStatus() {
        int shipTotal = boardViewDeployment.getRemainingShips();

        if (shipTotal == 0)
            textDeployStatus.setText(getString(R.string.deployment_all_ships));
        else
            textDeployStatus.setText(getString(R.string.deployment_remaining_ships) + shipTotal);

        checkReady();
    }

    public void onFragmentUpdate(boolean isFragmentReady){
        this.isFragmentReady = isFragmentReady;
        checkReady();
    }

    public void checkReady() {
        int shipTotal = boardViewDeployment.getRemainingShips();
        boolean isFragmentReady = true;
        boolean isReady = (shipTotal == 0 && isFragmentReady);
        buttonDeploy.setEnabled(isReady);
    }

    public void onClickReset(View v) {
        boardViewDeployment.onCreate();
        updateDeployStatus();
        checkReady();
    }

    public void onClickDeploy(View v) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra(getString(R.string.deployment_intent_board), boardViewDeployment.getBoard());

        // Singleplayer VS AI game
        if(gameType == GameType.Singleplayer){
            // Get the strategy from the fragment
            DeployAIFragment fragment = (DeployAIFragment)fragmentDeployment;
            i.putExtra(getString(R.string.deployment_intent_strategy), fragment.strategyAI);
            startActivity(i);
        }

        

    }
}

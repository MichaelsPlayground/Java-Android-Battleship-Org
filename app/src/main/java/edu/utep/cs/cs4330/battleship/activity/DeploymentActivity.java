// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import edu.utep.cs.cs4330.battleship.R;
import edu.utep.cs.cs4330.battleship.ai.StrategyType;
import edu.utep.cs.cs4330.battleship.model.Ship;
import edu.utep.cs.cs4330.battleship.view.DeploymentBoardView;

public class DeploymentActivity extends AppCompatActivity implements DeploymentBoardView.DeploymentListener {

    private TextView textGamemode;
    private TextView textDeployStatus;
    private DeploymentBoardView boardViewDeployment;
    private Switch switchRotation;
    private RadioGroup radioGroupStrategy;
    private Button buttonDeploy;

    private StrategyType strategyAI;

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
            String gamemode = extras.getString(getString(R.string.main_menu_intent_gamemode));
            if (gamemode != null)
                textGamemode.setText(gamemode);
        }

        boardViewDeployment.addListener(this);

        switchRotation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boardViewDeployment.rotationMode = isChecked;
            }
        });

        radioGroupStrategy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioRandom)
                    strategyAI = StrategyType.Random;
                else if (checkedId == R.id.radioSweep)
                    strategyAI = StrategyType.Sweep;

                checkReady();
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

    public void checkReady() {
        int shipTotal = boardViewDeployment.getRemainingShips();
        boolean isReady = (shipTotal == 0 && strategyAI != null);
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
        i.putExtra(getString(R.string.deployment_intent_strategy), strategyAI);
        startActivity(i);
    }
}

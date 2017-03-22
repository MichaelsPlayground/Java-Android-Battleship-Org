package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

public class DeploymentActivity extends AppCompatActivity implements DeploymentBoardView.DeploymentListener {

    private TextView textGamemode;
    private TextView textDeployStatus;
    private DeploymentBoardView boardView;
    private Switch switchRotation;
    private RadioGroup radioGroupStrategy;
    private Button buttonDeploy;

    private String strategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deployment);

        textGamemode = (TextView)findViewById(R.id.textDepGamemode);
        boardView = (DeploymentBoardView) findViewById(R.id.boardViewDeployment);
        buttonDeploy = (Button)findViewById(R.id.btnDeploy);
        switchRotation = (Switch)findViewById(R.id.switch_rotation);
        textDeployStatus = (TextView)findViewById(R.id.textDeployStatus);
        radioGroupStrategy = (RadioGroup)findViewById(R.id.radioGroupStrategy);

        Bundle extras = getIntent().getExtras();
        if(extras == null)
            return;

        String gamemode = extras.getString("GAMEMODE");
        if(gamemode != null)
            textGamemode.setText(gamemode);

        boardView.addListener(this);

        switchRotation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boardView.rotationMode = isChecked;
            }
        });

        radioGroupStrategy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioRandom)
                    strategy = "random";
                else if(checkedId == R.id.radioSmart)
                    strategy = "smart";

                checkReady();
            }
        });

        updateDeployStatus();
    }

    public void updateDeployStatus(){
        int shipTotal = boardView.getRemainingShips();

        if(shipTotal == 0)
            textDeployStatus.setText("All ships deployed!");
        else
            textDeployStatus.setText("Remaining ships to deploy: " + shipTotal);

        checkReady();
    }

    public void checkReady(){
        int shipTotal = boardView.getRemainingShips();
        boolean isReady = (shipTotal == 0 && strategy != null);
        buttonDeploy.setEnabled(isReady);
    }

    public void onClickReset(View v){
        boardView.onCreate();
        updateDeployStatus();
        checkReady();
    }

    public void onClickDeploy(View v){
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("BOARD", new Gson().toJson(boardView.getBoard()));
        i.putExtra("STRATEGY", strategy);
        startActivity(i);
    }

    @Override
    public void onShipDeployed(Ship ship) {
        updateDeployStatus();
    }
}

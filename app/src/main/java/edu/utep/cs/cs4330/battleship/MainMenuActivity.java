package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_main_menu);
    }

    public void onClickSinglePlayer(View v){
        Intent i = new Intent(this, DeploymentActivity.class);
        i.putExtra("GAMEMODE", "Player vs Computer");
        startActivity(i);
    }
}

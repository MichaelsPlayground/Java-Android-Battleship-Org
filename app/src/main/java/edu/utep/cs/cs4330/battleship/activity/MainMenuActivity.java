// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.utep.cs.cs4330.battleship.R;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_main_menu);
    }

    public void onClickSinglePlayer(View v) {
        Intent i = new Intent(this, DeploymentActivity.class);
        i.putExtra(getString(R.string.main_menu_intent_gamemode), getString(R.string.main_menu_singleplayer_description));
        startActivity(i);
    }
}

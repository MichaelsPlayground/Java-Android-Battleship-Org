package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import edu.utep.cs.cs4330.battleship.activity.DeploymentActivity;
import edu.utep.cs.cs4330.battleship.ai.StrategyType;


public class DeployAIFragment extends Fragment {
    private DeploymentActivity activity;
    private RadioGroup radioGroupStrategy;
    public StrategyType strategyAI;

    public DeployAIFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (DeploymentActivity)getActivity();

        View view = inflater.inflate(R.layout.fragment_deploy_ai, container, false);
        radioGroupStrategy = (RadioGroup) view.findViewById(R.id.radioGroupStrategy);

        radioGroupStrategy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioRandom)
                    strategyAI = StrategyType.Random;
                else if (checkedId == R.id.radioSweep)
                    strategyAI = StrategyType.Sweep;

                activity.onFragmentUpdate(true);
            }
        });

        return view;
    }
}

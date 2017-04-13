package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import edu.utep.cs.cs4330.battleship.activity.DeploymentActivity;
import edu.utep.cs.cs4330.battleship.ai.StrategyType;
import edu.utep.cs.cs4330.battleship.model.GameType;


public class DeployMultiFragment extends Fragment {
    private DeploymentActivity activity;
    private RadioGroup radioGroupNetwork;
    public NetworkType networkType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (DeploymentActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_deploy_multi, container, false);
        radioGroupNetwork = (RadioGroup) view.findViewById(R.id.radioGroupNetwork);

        radioGroupNetwork.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioHost)
                    networkType = NetworkType.Host;
                else if (checkedId == R.id.radioClient)
                    networkType = NetworkType.Client;

                activity.onFragmentUpdate(true);
            }
        });

        return view;
    }
}
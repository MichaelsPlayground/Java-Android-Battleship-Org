package edu.utep.cs.cs4330.battleship.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.utep.cs.cs4330.battleship.R;
import edu.utep.cs.cs4330.battleship.network.NetworkConnection;
import edu.utep.cs.cs4330.battleship.network.NetworkInterface;
import edu.utep.cs.cs4330.battleship.network.NetworkManager;
import edu.utep.cs.cs4330.battleship.network.Packet;
import edu.utep.cs.cs4330.battleship.network.thread.HostThread;


public class NetworkHostFragment extends Fragment implements NetworkInterface {
    private static final int REQUEST_DISCOVER = 2;

    private BluetoothAdapter bluetoothAdapter;

    private TextView textHostStatus;
    private ProgressBar progressBarHost;
    private Button btnHost;

    public NetworkHostFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        NetworkManager.registerNetworkInterface(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_network_host, container, false);

        // Get references to the views it uses
        textHostStatus = (TextView)view.findViewById(R.id.textHostStatus);
        progressBarHost = (ProgressBar)view.findViewById(R.id.progressBarHost);
        btnHost = (Button)view.findViewById(R.id.btnHost);

        // Make sure everything is started correctly
        reset();

        return view;
    }

    public void reset(){
        textHostStatus.setText("Ready to host");
        btnHost.setEnabled(true);
        progressBarHost.setIndeterminate(false);
    }

    public void onClickBtnHost(View view){
        // Request that the device is discoverable
        // This will also request Bluetooth to be enabled
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(intent, REQUEST_DISCOVER);

        textHostStatus.setText("Enabling discoverability");
        btnHost.setEnabled(false);
        progressBarHost.setIndeterminate(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_DISCOVER){
            // We were given permission to proceed
            if(resultCode == 300){
                // Start hosting and waiting for the client
                Thread serverHostThread = new HostThread(getResources());
                serverHostThread.start();
                textHostStatus.setText("Waiting for client to connect");
            }
            else
                reset();
        }
    }

    @Override
    public void onConnect(NetworkConnection networkConnection) {
        progressBarHost.setIndeterminate(false);
        textHostStatus.setText("Connected with client");
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onReceive(Packet p) {

    }
}


package edu.utep.cs.cs4330.battleship.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import edu.utep.cs.cs4330.battleship.R;
import edu.utep.cs.cs4330.battleship.fragment.NetworkClientFragment;
import edu.utep.cs.cs4330.battleship.fragment.NetworkHostFragment;
import edu.utep.cs.cs4330.battleship.network.NetworkAdapterType;

public class BluetoothSetupActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroupConnectionType;
    private Fragment fragment;

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        // Dynamically update the bottom of the activity
        // Depending on whether they pick host or client
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment newFragment;

        if(checkedId == R.id.radioHost)
            newFragment = new NetworkHostFragment();
        else
            newFragment = new NetworkClientFragment();

        if(fragment == null)
            transaction.add(R.id.bluetoothFragmentContainer, newFragment);
        else
            transaction.replace(R.id.bluetoothFragmentContainer, newFragment);

        fragment = newFragment;
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_setup);

        radioGroupConnectionType = (RadioGroup) findViewById(R.id.radioGroupConnectionType);
        radioGroupConnectionType.setOnCheckedChangeListener(this);

        // Hosting
        /*
        if(networkType == NetworkAdapterType.Host){

        }
        // Client
        else{
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);

                progressBarDeploy.setIndeterminate(true);
                textDeployProgress.setText("Looking for host");
            }
        }
        private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                new ClientThread(device).start();
                ReceivingThread.setHandler(connectedHandler);
                progressBarDeploy.setIndeterminate(false);
                textDeployProgress.setText("Waiting for handshake");
            }
        }
    };

    private Handler connectedHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MessageConstants.MESSAGE_CONNECTED){
                DeployMultiFragment fragment = (DeployMultiFragment)fragmentDeployment;
                NetworkAdapterType networkType = fragment.networkType;
                if(networkType == NetworkAdapterType.Client){
                    ReceivingThread.addPacket(Packet.createPacketClientHandshake(""));
                }
            }
            else if(msg.what == MessageConstants.MESSAGE_RECEIVE){
                Packet p = (Packet)msg.obj;
                Log.d("Debug", "Received packet ID: " + p.getID());
            }
        }
    };
    }*/
        /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_DISCOVER){
            if(resultCode == 300){
                Thread accept = new HostThread(this);
                accept.start();
                ReceivingThread.setHandler(connectedHandler);
            }
            else{
                progressBarDeploy.setIndeterminate(false);
                textDeployProgress.setText("Waiting for deployment");
            }
        }
        else if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK)
                bluetoothAdapter.startDiscovery();

            else{
                progressBarDeploy.setIndeterminate(false);
                textDeployProgress.setText("Waiting for deployment");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }*/
    }
}

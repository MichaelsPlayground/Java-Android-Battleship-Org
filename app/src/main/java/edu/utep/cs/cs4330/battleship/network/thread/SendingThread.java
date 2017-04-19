package edu.utep.cs.cs4330.battleship.network.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.utep.cs.cs4330.battleship.network.NetworkConnection;
import edu.utep.cs.cs4330.battleship.network.NetworkManager;
import edu.utep.cs.cs4330.battleship.network.Packet;

public class SendingThread extends Thread {
    private static final String TAG = "Debug";
    private final DataOutputStream mmOutStream;
    private final List<Packet> packetList;

    public SendingThread(BluetoothSocket socket){
        DataOutputStream tmpOut = null;
        try {
            tmpOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mmOutStream = tmpOut;
        packetList = new ArrayList<>();

        NetworkManager.broadcastConnect(new NetworkConnection(this));
    }

    @Override
    public void run() {
        while(true){
            for(Packet p : packetList)
                write(p);
        }
    }


    public synchronized void addPacket(Packet p){
        packetList.add(p);
    }

    private void write(Packet p) {
        try {
            p.sendPacket(mmOutStream);
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);
        }
    }
}

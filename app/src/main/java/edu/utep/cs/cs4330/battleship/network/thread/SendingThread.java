// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.utep.cs.cs4330.battleship.network.NetworkConnection;
import edu.utep.cs.cs4330.battleship.network.NetworkManager;
import edu.utep.cs.cs4330.battleship.network.packet.Packet;

public class SendingThread extends Thread {
    private static final String TAG = "Debug";
    private final ObjectOutputStream mmOutStream;
    private final List<Packet> packetList;
    private final NetworkConnection networkConnection;

    public SendingThread(BluetoothSocket socket) {
        ObjectOutputStream tmpOut = null;
        try {
            tmpOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mmOutStream = tmpOut;
        packetList = new ArrayList<>();
        networkConnection = new NetworkConnection(this);
        NetworkManager.broadcastConnect();
    }

    @Override
    public void run() {
        while (true) {
            NetworkManager.broadcastPrepareSend(networkConnection);
            for (Packet p : packetList)
                write(p);
        }
    }


    public synchronized void addPacket(Packet p) {
        packetList.add(p);
    }

    private void write(Packet p) {
        try {
            mmOutStream.writeInt(p.ID);
            p.sendPacket(mmOutStream);
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);
        }
        catch(Exception ex){
            Log.e(TAG, "Error when writing", ex);
        }
    }
}

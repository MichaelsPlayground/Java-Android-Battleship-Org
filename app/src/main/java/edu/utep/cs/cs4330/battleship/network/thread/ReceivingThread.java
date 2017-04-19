// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;

import edu.utep.cs.cs4330.battleship.network.NetworkManager;
import edu.utep.cs.cs4330.battleship.network.packet.Packet;

public class ReceivingThread extends Thread {
    private static final String TAG = "Debug";
    private final DataInputStream mmInStream;

    public ReceivingThread(BluetoothSocket socket) {
        DataInputStream tmpIn = null;

        try {
            tmpIn = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        mmInStream = tmpIn;
    }

    @Override
    public void run() {
        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.
                Packet packetInput = Packet.readPacket(mmInStream);

                // Send the packet to the subscribed interfaces
                NetworkManager.broadcastPacket(packetInput);

            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                NetworkManager.broadcastDisconnect();
                break;
            }
        }
    }
}

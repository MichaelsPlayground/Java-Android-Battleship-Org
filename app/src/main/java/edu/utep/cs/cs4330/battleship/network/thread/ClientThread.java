// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class ClientThread extends Thread {
    private static String TAG = "Debug";
    private final BluetoothSocket mmSocket;
    private final BluetoothAdapter bluetoothAdapter;

    public ClientThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("d3913c42-6128-4427-9674-6374f2690ebf"));
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method faild", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        bluetoothAdapter.cancelDiscovery();
        try {
            mmSocket.connect();
        } catch (IOException connectException) {
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the clients", closeException);
            }
            return;
        }

        manageMyConnectedSocket(mmSocket);
    }

    public void manageMyConnectedSocket(BluetoothSocket socket) {
        new ReceivingThread(socket).start();
        new SendingThread(socket).start();
    }
}
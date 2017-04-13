package edu.utep.cs.cs4330.battleship;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private Context context;
    private Handler connectedHandler;
    private BluetoothAdapter bluetoothAdapter;

    public AcceptThread(Context context, Handler connectedHandler, BluetoothAdapter bluetoothAdapter) {
        this.context = context;
        this.connectedHandler = connectedHandler;
        this.bluetoothAdapter = bluetoothAdapter;
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(context.getString(R.string.app_name), UUID.fromString(context.getString(R.string.app_uuid)));
        } catch (IOException e) {
            Log.e("Debug", "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                Log.d("Debug", "Waiting for socket...");
                socket = mmServerSocket.accept();
                Log.d("Debug", "Got em");
            } catch (IOException e) {
                Log.e("Debug", "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket);
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    Log.e("Debug", "Server Socket's close() method failed", e);
                }
                break;
            }
        }
    }

    public void manageMyConnectedSocket(BluetoothSocket socket) {
        new ConnectedThread(socket).start();
    }
}


package edu.utep.cs.cs4330.battleship;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ConnectedThread extends Thread {
    private static Handler mHandler;
    public static void setHandler(Handler newHandler){
        mHandler = newHandler;
    }
    private static List<Packet> packetList = new ArrayList<>();

    public static synchronized void addPacket(Packet p){
        packetList.add(p);
    }

    private static final String TAG = "DEBUG";
    private final BluetoothSocket mmSocket;
    private final DataInputStream mmInStream;
    private final DataOutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        DataInputStream tmpIn = null;
        DataOutputStream tmpOut = null;

        try {
            tmpIn = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        Message connectedMsg = mHandler.obtainMessage(
                MessageConstants.MESSAGE_CONNECTED, -1, -1, -1);
        connectedMsg.sendToTarget();
    }

    public void run() {
        //mmBuffer = new byte[1024];
        //int numBytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.
                Packet packetInput = Packet.readPacket(mmInStream);
                // Send the obtained bytes to the UI activity.
                Message readMsg = mHandler.obtainMessage(
                        MessageConstants.MESSAGE_RECEIVE, -1, -1,
                        packetInput);
                readMsg.sendToTarget();

                for(Packet p : packetList){
                    write(p);
                }

            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }

    // Call this from the main activity to send data to the remote device.
    private void write(Packet p) {
        try {
            p.sendPacket(mmOutStream);
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);
        }
    }

    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            Message disconnectMsg = mHandler.obtainMessage(
                    MessageConstants.MESSAGE_DISCONNECTED, -1, -1, -1);
            disconnectMsg.sendToTarget();
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}

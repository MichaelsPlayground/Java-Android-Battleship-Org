// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import edu.utep.cs.cs4330.battleship.network.NetworkManager;
import edu.utep.cs.cs4330.battleship.network.packet.Packet;

public class ReceivingThread extends Thread {
    private static final String TAG = "Debug";
    private final ObjectInputStream mmInStream;

    public ReceivingThread(BluetoothSocket socket) {
        Log.d("Debug", "Creating receiving thread");
        ObjectInputStream tmpIn = null;

        try {
            tmpIn = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
        } catch (IOException e) {
            Log.d(TAG, "Error occurred when creating input stream", e);
            try{
                tmpIn.close();
            }
            catch(IOException ex){
                Log.d("Debug", "Couldn't close input");
            }
        }
        mmInStream = tmpIn;
        Log.d("Debug", "Input stream is null: " + (mmInStream == null));
    }

    public void close(){
        try{
            mmInStream.close();
        }
        catch (IOException e){
            Log.d("Debug", "ReceivingThread couldn't close");
        }
    }

    @Override
    public void run() {
        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            if(!NetworkManager.isRunning){
                close();
                return;
            }

            try {
                // Read from the InputStream.
                Packet packetInput = Packet.readPacket(mmInStream);

                Log.d("Debug", "Read a packet: " + packetInput);

                // Send the packet to the subscribed interfaces
                NetworkManager.broadcastPacket(packetInput);

            } catch (Exception e) {
                Log.d(TAG, "Input stream was disconnected", e);
                try{
                    this.mmInStream.close();
                }
                catch(IOException ex){
                    Log.d("Debug", "Couldn't close input");
                }
                NetworkManager.broadcastDisconnect();
                break;
            }
        }
    }
}

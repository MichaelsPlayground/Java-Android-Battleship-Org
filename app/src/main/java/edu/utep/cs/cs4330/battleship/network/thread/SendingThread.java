// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;

import edu.utep.cs.cs4330.battleship.network.NetworkManager;
import edu.utep.cs.cs4330.battleship.network.packet.Packet;

public class SendingThread extends Thread {
    private static final String TAG = "Debug";
    private final ObjectOutputStream mmOutStream;
    private boolean isClosed = false;

    public SendingThread(BluetoothSocket socket) {
        Log.d("Debug", "Creating sending thread");
        ObjectOutputStream tmpOut = null;
        try {
            tmpOut = new ObjectOutputStream(socket.getOutputStream());
            tmpOut.flush();
        } catch (IOException e) {
            Log.d(TAG, "Error occurred when creating output stream", e);
        }

        mmOutStream = tmpOut;
        NetworkManager.broadcastConnect();
        Log.d("Debug", "Sending thread broadcasting connect to everyone");
    }

    @Override
    public void run() {
        while (true) {
            if(isClosed)
                return;

            try {
                Packet p = NetworkManager.packetList.take();
                write(p);
            }
            catch(InterruptedException e){
                Log.d("Debug", "Interrupted :(");
            }

        }
    }



    private void write(Packet p) {
        try {
            mmOutStream.writeInt(p.ID);
            p.sendPacket(mmOutStream);
            mmOutStream.flush();
        } catch (IOException e) {
            Log.d(TAG, "Error occurred when sending data", e);
            isClosed = true;
        }
        catch(Exception ex){
            Log.d(TAG, "Error when writing", ex);
        }
    }
}

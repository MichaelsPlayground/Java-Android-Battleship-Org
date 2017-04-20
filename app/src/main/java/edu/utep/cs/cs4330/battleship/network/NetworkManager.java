// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import edu.utep.cs.cs4330.battleship.network.packet.Packet;

public class NetworkManager {
    private static final List<Tuple> networkInterfaceList = new ArrayList<>();
    public static final BlockingQueue<Packet> packetList = new ArrayBlockingQueue<Packet>(20);

    public static synchronized void sendPacket(Packet p) {
        try {
            packetList.put(p);
        }catch (InterruptedException e){
            Log.d("Debug", "addPacket interrupted");
        }
    }

    public static void registerNetworkInterface(Activity activity, NetworkInterface networkInterface) {
        Tuple t = new Tuple(activity, networkInterface);
        if (!networkInterfaceList.contains(t))
            networkInterfaceList.add(t);
    }

    public static void unregisterNetworkInterface(Activity activity, NetworkInterface networkInterface) {
        networkInterfaceList.remove(new Tuple(activity, networkInterface));
    }

    public static void broadcastConnect() {
        for (final Tuple tuple : networkInterfaceList) {
            tuple.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tuple.networkInterface.onConnect();
                }
            });
        }
    }

    public static void broadcastDisconnect() {
        for (final Tuple tuple : networkInterfaceList) {
            tuple.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tuple.networkInterface.onDisconnect();
                }
            });
        }
    }

    public static void broadcastPacket(final Packet p) {
        for (final Tuple tuple : networkInterfaceList) {
            tuple.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tuple.networkInterface.onReceive(p);
                }
            });
        }
    }

    private NetworkManager() {
    }
}

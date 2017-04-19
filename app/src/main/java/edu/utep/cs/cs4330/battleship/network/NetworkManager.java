package edu.utep.cs.cs4330.battleship.network;

import java.util.ArrayList;
import java.util.List;

public class NetworkManager {
    private static final List<NetworkInterface> networkInterfaceList = new ArrayList<>();

    public static void registerNetworkInterface(NetworkInterface networkInterface){
        if(!networkInterfaceList.contains(networkInterface))
            networkInterfaceList.add(networkInterface);
    }

    public static void unregisterNetworkInterface(NetworkInterface networkInterface){
        networkInterfaceList.remove(networkInterface);
    }

    public static void broadcastConnect(NetworkConnection networkConnection){
        for(NetworkInterface networkInterface : networkInterfaceList)
            networkInterface.onConnect(networkConnection);
    }

    public static void broadcastDisconnect(){
        for(NetworkInterface networkInterface : networkInterfaceList)
            networkInterface.onDisconnect();
    }

    public static void broadcastPacket(Packet p){
        for(NetworkInterface networkInterface : networkInterfaceList)
            networkInterface.onReceive(p);
    }

    private NetworkManager(){ }
}

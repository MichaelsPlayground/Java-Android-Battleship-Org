package edu.utep.cs.cs4330.battleship.network;

public interface NetworkInterface {
    void onConnect(NetworkConnection networkConnection);
    void onDisconnect();
    void onReceive(Packet p);
}

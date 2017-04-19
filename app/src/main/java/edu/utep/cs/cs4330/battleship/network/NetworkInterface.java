package edu.utep.cs.cs4330.battleship.network;

import edu.utep.cs.cs4330.battleship.network.packet.Packet;

public interface NetworkInterface {
    void onConnect(final NetworkConnection networkConnection);
    void onDisconnect();
    void onReceive(final Packet p);
}

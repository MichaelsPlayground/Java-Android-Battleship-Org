// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network;

import edu.utep.cs.cs4330.battleship.network.packet.Packet;
import edu.utep.cs.cs4330.battleship.network.thread.SendingThread;

public class NetworkConnection {
    private SendingThread sendingThread;

    public NetworkConnection(SendingThread sendingThread) {
        this.sendingThread = sendingThread;
    }

    public void sendPacket(Packet p) {
        sendingThread.addPacket(p);
    }
}

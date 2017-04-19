// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class Packet {
    public int ID;

    protected Packet(int ID) {
        this.ID = ID;
    }

    public abstract void sendPacket(ObjectOutputStream output) throws Exception;

    public static Packet readPacket(ObjectInputStream input) throws Exception {
        int ID = input.readInt();

        if (ID == PacketID.HOST_HANDSHAKE)
            return new PacketHostHandshake(input);
        else if (ID == PacketID.CLIENT_HANDSHAKE)
            return new PacketClientHandshake(input);
        else if (ID == PacketID.HIT)
            return new PacketHit(input);
        else if (ID == PacketID.GAMEOVER)
            return new PacketGameover(input);
        else if (ID == PacketID.RESTART_REQUEST)
            return new PacketRestartRequest();
        else if (ID == PacketID.REQUEST_RESPONSE)
            return new PacketRequestResponse(input);

        return null;
    }
}
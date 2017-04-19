// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PacketRequestResponse extends Packet {
    public boolean isAgreed;

    public PacketRequestResponse(ObjectInputStream input) throws IOException{
        super(PacketID.REQUEST_RESPONSE);
        isAgreed = input.readBoolean();
    }

    public PacketRequestResponse(boolean isAgreed) {
        super(PacketID.REQUEST_RESPONSE);
        this.isAgreed = isAgreed;
    }

    @Override
    public void sendPacket(ObjectOutputStream output) throws IOException {
        output.writeBoolean(isAgreed);
    }
}

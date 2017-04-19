// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class PacketHostHandshake extends Packet {
    private String hostName;
    private boolean isClientFirst;

    public PacketHostHandshake(DataInputStream input) throws IOException{
        super(PacketID.HOST_HANDSHAKE);
        hostName = input.readUTF();
        isClientFirst = input.readBoolean();
    }

    public PacketHostHandshake(String hostName, boolean isClientFirst){
        super(PacketID.HOST_HANDSHAKE);

        this.hostName = hostName;
        this.isClientFirst = isClientFirst;
    }

    @Override
    public void sendPacket(DataOutputStream output) throws IOException {
        output.writeUTF(hostName);
        output.writeBoolean(isClientFirst);
    }
}

package edu.utep.cs.cs4330.battleship.network.packet;


import java.io.DataOutputStream;
import java.io.IOException;

public class PacketClientHandshake extends Packet {

    public String clientName;

    public PacketClientHandshake(String clientName){
        super(PacketID.CLIENT_HANDSHAKE);
        this.clientName = clientName;
    }

    @Override
    public void sendPacket(DataOutputStream output) throws IOException {

    }
}

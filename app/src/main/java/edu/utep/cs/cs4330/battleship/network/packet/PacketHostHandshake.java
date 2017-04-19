package edu.utep.cs.cs4330.battleship.network.packet;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by xeroj on 018 4 18 2017.
 */

public class PacketHostHandshake extends Packet {

    private String hostName;
    private boolean isClientFirst;

    public PacketHostHandshake(String hostName, boolean isClientFirst){
        super(PacketID.HOST_HANDSHAKE);

        this.hostName = hostName;
        this.isClientFirst = isClientFirst;
    }

    @Override
    public void sendPacket(DataOutputStream output) throws IOException {

    }
}

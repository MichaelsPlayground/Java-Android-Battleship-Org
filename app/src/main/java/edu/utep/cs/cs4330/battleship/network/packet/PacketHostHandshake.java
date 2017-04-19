// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.utep.cs.cs4330.battleship.model.board.Board;


public class PacketHostHandshake extends Packet {
    public String hostName;
    public boolean isClientFirst;
    public Board hostBoard;

    public PacketHostHandshake(ObjectInputStream input) throws Exception {
        super(PacketID.HOST_HANDSHAKE);
        hostName = input.readUTF();
        isClientFirst = input.readBoolean();
        hostBoard = (Board)input.readObject();
    }

    public PacketHostHandshake(String hostName, boolean isClientFirst, Board hostBoard){
        super(PacketID.HOST_HANDSHAKE);

        this.hostName = hostName;
        this.isClientFirst = isClientFirst;
        this.hostBoard = hostBoard;
    }

    @Override
    public void sendPacket(ObjectOutputStream output) throws IOException {
        output.writeUTF(hostName);
        output.writeBoolean(isClientFirst);
        output.writeObject(hostBoard);
    }
}

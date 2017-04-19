// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.utep.cs.cs4330.battleship.model.board.Board;

public class PacketClientHandshake extends Packet {
    public String clientName;
    public Board clientBoard;

    public PacketClientHandshake(ObjectInputStream input) throws Exception {
        super(PacketID.CLIENT_HANDSHAKE);
        clientName = input.readUTF();
        clientBoard = (Board) input.readObject();
    }

    public PacketClientHandshake(String clientName, Board clientBoard) {
        super(PacketID.CLIENT_HANDSHAKE);
        this.clientName = clientName;
        this.clientBoard = clientBoard;
    }

    @Override
    public void sendPacket(ObjectOutputStream output) throws IOException {
        output.writeUTF(clientName);
        output.writeObject(clientBoard);
    }
}

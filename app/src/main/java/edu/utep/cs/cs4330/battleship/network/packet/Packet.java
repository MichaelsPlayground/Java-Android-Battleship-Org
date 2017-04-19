package edu.utep.cs.cs4330.battleship.network.packet;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet {
    public int ID;

    protected Packet(int ID) {
        super();
        this.ID = ID;
    }

    public abstract void sendPacket(DataOutputStream output) throws IOException;

    public static Packet readPacket(DataInputStream input) throws IOException {
        int ID = input.readInt();

        if(ID == PacketID.HOST_HANDSHAKE) {
            String hostName = input.readUTF();
            boolean isClientFirst = input.readBoolean();
            return new PacketHostHandshake(hostName, isClientFirst);
        }
        else if(ID == PacketID.CLIENT_HANDSHAKE){
            String clientName = input.readUTF();
            return new PacketClientHandshake(clientName);
        }
        else if(ID == PacketID.HIT){
            int x = input.readInt();
            int y = input.readInt();
        }
        else if(ID == PacketID.HIT_RESPONSE){
            int resultType = input.readInt();
        }
        else if(ID == PacketID.GAMEOVER){
            int gameoverType = input.readInt();
        }

        return null;
    }
}
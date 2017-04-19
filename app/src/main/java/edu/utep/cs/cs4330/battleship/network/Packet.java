package edu.utep.cs.cs4330.battleship.network;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Packet {
    private int ID;
    private List<Object> packetData;

    protected Packet(int ID) {
        super();
        this.ID = ID;
        packetData = new LinkedList<>();
    }

    public int getID() {
        return ID;
    }

    public void sendPacket(DataOutputStream output) throws IOException {
        Iterator<Object> it = packetData.iterator();

        while(it.hasNext()){
            Object obj = it.next();

            if(obj instanceof Integer)
                output.writeInt((int)obj);
            else if(obj instanceof Boolean)
                output.writeBoolean((boolean)obj);
            else if(obj instanceof String)
                output.writeUTF((String)obj);
        }
    }

    public static Packet readPacket(DataInputStream input) throws IOException {
        int ID = input.readInt();
        Packet packet = new Packet(ID);

        if(ID == PacketID.HOST_HANDSHAKE) {
            String name = input.readUTF();
            boolean clientFirst = input.readBoolean();
            packet.packetData.add(name);
            packet.packetData.add(clientFirst);
        }
        else if(ID == PacketID.CLIENT_HANDSHAKE){
            String name = input.readUTF();
            packet.packetData.add(name);
        }
        else if(ID == PacketID.HIT){
            int x = input.readInt();
            int y = input.readInt();
            packet.packetData.add(x);
            packet.packetData.add(y);
        }
        else if(ID == PacketID.HIT_RESPONSE){
            int resultType = input.readInt();
            packet.packetData.add(resultType);
        }
        else if(ID == PacketID.GAMEOVER){
            int gameoverType = input.readInt();
            packet.packetData.add(gameoverType);
        }

        return packet;
    }

    public static Packet createPacketHostHandshake(String name, boolean clientFirst) {
        Packet p = new Packet(PacketID.HOST_HANDSHAKE);
        p.packetData.add(name);
        p.packetData.add(clientFirst);
        return p;
    }

    public static Packet createPacketClientHandshake(String name) {
        Packet p = new Packet(PacketID.CLIENT_HANDSHAKE);
        p.packetData.add(name);
        return p;
    }

    public static Packet createPacketHit(int x, int y) {
        Packet p = new Packet(PacketID.HIT);
        p.packetData.add(x);
        p.packetData.add(y);
        return p;
    }

    public static Packet createPacketHitResponse(ResultType result) {
        Packet p = new Packet(PacketID.HIT_RESPONSE);
        p.packetData.add(result);
        return p;
    }

    public static Packet createPacketGameover(GameoverType gameoverType) {
        Packet p = new Packet(PacketID.GAMEOVER);
        p.packetData.add(gameoverType);
        return p;
    }
}
// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PacketHit extends Packet {
    public int X;
    public int Y;

    public PacketHit(ObjectInputStream input) throws IOException {
        super(PacketID.HIT);
        X = input.readInt();
        Y = input.readInt();
    }

    public PacketHit(int X, int Y) {
        super(PacketID.HIT);
        this.X = X;
        this.Y = Y;
    }

    @Override
    public void sendPacket(ObjectOutputStream output) throws IOException {
        output.writeInt(X);
        output.writeInt(Y);
    }

    @Override
    public String toString(){
        return "{PacketHit, X=" + X +", Y=" + Y + "}";
    }
}

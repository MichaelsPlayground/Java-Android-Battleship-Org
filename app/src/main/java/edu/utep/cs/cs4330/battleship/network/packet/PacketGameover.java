// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.utep.cs.cs4330.battleship.model.GameoverType;

public class PacketGameover extends Packet {
    private GameoverType gameoverType;

    public PacketGameover(DataInputStream input) throws IOException{
        super(PacketID.GAMEOVER);
        gameoverType = GameoverType.fromValue(input.readInt());
    }

    public PacketGameover(GameoverType gameoverType){
        super(PacketID.GAMEOVER);
        this.gameoverType = gameoverType;
    }

    @Override
    public void sendPacket(DataOutputStream output) throws IOException {
        output.writeInt(gameoverType.value);
    }
}

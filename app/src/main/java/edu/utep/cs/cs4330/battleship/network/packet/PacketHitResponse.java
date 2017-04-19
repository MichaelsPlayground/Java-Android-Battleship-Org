// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.utep.cs.cs4330.battleship.model.ResultType;

public class PacketHitResponse extends Packet {
    private ResultType resultType;

    public PacketHitResponse(DataInputStream input) throws IOException{
        super(PacketID.HIT_RESPONSE);
        resultType = ResultType.fromValue(input.readInt());
    }

    public PacketHitResponse(ResultType resultType) {
        super(PacketID.HIT_RESPONSE);
        this.resultType = resultType;
    }

    @Override
    public void sendPacket(DataOutputStream output) throws IOException {
        output.writeInt(resultType.value);
    }
}

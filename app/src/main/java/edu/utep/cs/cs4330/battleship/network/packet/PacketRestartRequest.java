// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network.packet;

import java.io.DataOutputStream;
import java.io.IOException;

public class PacketRestartRequest extends Packet {

    public PacketRestartRequest() {
        super(PacketID.RESTART_REQUEST);
    }

    @Override
    public void sendPacket(DataOutputStream output) throws IOException {

    }
}

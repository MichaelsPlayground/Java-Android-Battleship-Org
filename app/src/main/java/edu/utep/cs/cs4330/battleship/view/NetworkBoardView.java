package edu.utep.cs.cs4330.battleship.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import edu.utep.cs.cs4330.battleship.network.NetworkConnection;
import edu.utep.cs.cs4330.battleship.network.NetworkInterface;
import edu.utep.cs.cs4330.battleship.network.NetworkManager;
import edu.utep.cs.cs4330.battleship.network.packet.Packet;
import edu.utep.cs.cs4330.battleship.network.packet.PacketHit;

public class NetworkBoardView extends BoardView implements NetworkInterface {
    public boolean ignoreHits = false;
    public boolean isSending = false;

    private NetworkConnection networkConnection;
    @Override
    public void onBoardTouch(int x, int y) {
        if(isSending){
            PacketHit p = new PacketHit(x, y);
            networkConnection.sendPacket(p);
            return;
        }

        if(ignoreHits)
            return;

        super.onBoardTouch(x, y);
    }

    public void onCreate(Activity activity){
        NetworkManager.registerNetworkInterface(activity, this);
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onReceive(Packet p) {
        if(isSending)
            return;

        if(p instanceof PacketHit){
            if(ignoreHits)
                return;

            PacketHit packetHit = (PacketHit)p;
            getBoard().hit(packetHit.X, packetHit.Y);
        }
    }

    @Override
    public void onPrepareSend(NetworkConnection networkConnection) {
        this.networkConnection = networkConnection;
    }

    public NetworkBoardView(Context context) {
        super(context);
    }

    public NetworkBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetworkBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

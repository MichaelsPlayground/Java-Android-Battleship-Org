package edu.utep.cs.cs4330.battleship.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import edu.utep.cs.cs4330.battleship.model.board.Place;
import edu.utep.cs.cs4330.battleship.network.NetworkInterface;
import edu.utep.cs.cs4330.battleship.network.NetworkManager;
import edu.utep.cs.cs4330.battleship.network.packet.Packet;
import edu.utep.cs.cs4330.battleship.network.packet.PacketHit;

public class NetworkBoardView extends BoardView implements NetworkInterface {
    public boolean isDeployedBoard = false;
    public boolean isCurrentTurn = false;

    @Override
    public boolean isPlacePainted(Place p) {
        if(isDeployedBoard) {
            invalidate();
            return p.isHit() || p.hasShip();
        }
        else
            return super.isPlacePainted(p);
    }

    @Override
    public Paint getPlacePaint(Place p) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (p.isHit() && p.hasShip())
            paint.setColor(Color.BLACK);
        else if (p.hasShip())
            paint.setColor(Color.GREEN);

        else if (p.isHit())
            paint.setColor(Color.RED);

        else
            paint.setColor(Color.MAGENTA);

        invalidate();
        return paint;
    }

    @Override
    public void onBoardTouch(int x, int y) {
        if(isDeployedBoard)
            return;

        if(isCurrentTurn) {
            PacketHit p = new PacketHit(x, y);
            NetworkManager.sendPacket(p);
            super.onBoardTouch(x, y);
        }
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
        if(isCurrentTurn && p instanceof PacketHit){
            PacketHit packetHit = (PacketHit)p;
            Log.d("Debug", "PacketHit at: " + packetHit.X + ", " + packetHit.Y);
            super.onBoardTouch(packetHit.X, packetHit.Y);
        }
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

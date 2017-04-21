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
    public boolean showShips = false;
    public boolean receivesPackets = false;

    @Override
    public boolean isPlacePainted(Place p) {
        if(showShips) {
            invalidate();
            return p.isHit() || p.hasShip();
        }
        else
            return super.isPlacePainted(p);
    }

    @Override
    public Paint getPlacePaint(Place p) {
        if(!showShips)
            return super.getPlacePaint(p);

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
        if(disableBoardTouch)
            return;

        PacketHit p = new PacketHit(x, y);
        NetworkManager.sendPacket(p);
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
        if(receivesPackets && p instanceof PacketHit){
            Log.d("Debug", "Board received PacketHit: " + p);
            PacketHit packetHit = (PacketHit)p;
            int x = packetHit.X;
            int y = packetHit.Y;

            // Ignore duplicate packets
            if(getBoard().placeAt(x, y).isHit())
                return;

            Log.d("Debug", "Hitting");
            getBoard().hit(packetHit.X, packetHit.Y);
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

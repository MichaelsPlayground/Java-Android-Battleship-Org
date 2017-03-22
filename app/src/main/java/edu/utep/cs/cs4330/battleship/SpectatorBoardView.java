package edu.utep.cs.cs4330.battleship;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by xeroj on 021 3 21 2017.
 */

public class SpectatorBoardView extends BoardView {
    public SpectatorBoardView(Context context) {
        super(context);
    }

    public SpectatorBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SpectatorBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isPlacePainted(Place p) {
        return p.isHit() || p.hasShip();
    }

    @Override
    public void onBoardTouch(int x, int y) {
        // Don't hit or do anything
    }

    @Override
    public Paint getPlacePaint(Place p){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (p.hasShip())
            paint.setColor(Color.GREEN);
        else if(p.isHit())
            paint.setColor(Color.RED);
        else if (p.isHit() && p.hasShip())
            paint.setColor(Color.BLACK);

        return paint;
    }
}

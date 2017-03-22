package edu.utep.cs.cs4330.battleship;

import java.util.Random;

/**
 * Created by xeroj on 021 3 21 2017.
 */

public class SweepStrategy extends Strategy {
    private int currentX = 0;
    private int currentY = 0;

    public SweepStrategy(){
    }

    @Override
    public Vector2 play(Board board) {
        Vector2 pos = new Vector2(currentX, currentY);
        currentY++;

        if(currentY >= board.size())
        {
            currentY = 0;
            currentX++;
        }

        return pos;
    }
}

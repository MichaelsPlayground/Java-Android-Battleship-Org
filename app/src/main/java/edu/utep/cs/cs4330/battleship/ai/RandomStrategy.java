// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.ai;

import android.util.Log;

import java.util.Random;

import edu.utep.cs.cs4330.battleship.model.Board;
import edu.utep.cs.cs4330.battleship.util.Vector2;

class RandomStrategy extends Strategy {
    private Random rand;

    public RandomStrategy(){
        rand = new Random(System.nanoTime());
    }

    @Override
    public Vector2 play(Board board) {
        return randomVector(board);
    }

    public Vector2 randomVector(Board board){
        int randomX = rand.nextInt(board.size());
        int randomY = rand.nextInt(board.size());
        Log.d("Debug", "AI Random: " + randomX);
        return new Vector2(randomX, randomY);
    }
}

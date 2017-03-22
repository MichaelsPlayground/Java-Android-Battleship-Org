package edu.utep.cs.cs4330.battleship;

/**
 * Created by xeroj on 021 3 21 2017.
 */

public class Game {
    private Player playerHuman;
    private Player playerOpponent;
    private int currentTurn = 0;

    public void nextTurn(){
        currentTurn++;
        if(currentTurn > 1)
            currentTurn = 0;
    }

    public Player getCurrentPlayer(){
        if (currentTurn == 0)
            return playerHuman;
        else
            return playerOpponent;
    }

    public boolean hit(int x, int y){
        Player p = getCurrentPlayer();
        boolean isHit = p.board.hit(x, y);

        // You missed
        if(!isHit)
            nextTurn();

        // You hit and are not allowed multiple hits
        if(isHit && !p.isAllowedMultipleShots)
            nextTurn();

        return false;
    }
}

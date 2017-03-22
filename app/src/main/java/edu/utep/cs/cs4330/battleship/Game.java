package edu.utep.cs.cs4330.battleship;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xeroj on 021 3 21 2017.
 */

public class Game {
    public interface GameListener {
        void onTurnChange(Player currentPlayer);
    }
    private final List<GameListener> listeners = new ArrayList<>();

    public void addBoardListener(GameListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    private void notifyTurnChange(Player player) {
        for (GameListener listener : listeners)
            listener.onTurnChange(player);
    }

    private Player playerHuman;
    private Player playerOpponent;
    private int currentTurn = 0;

    public Game(Player playerHuman, Player playerOpponent){
        this.playerHuman = playerHuman;
        this.playerOpponent = playerOpponent;

        Board.BoardListener listener = new Board.BoardListener() {
            @Override
            public void onShipHit(Ship ship) {
                if(!getCurrentPlayer().isAllowedMultipleShots)
                    nextTurn();
            }

            @Override
            public void onShipMiss() {
                nextTurn();
            }
        };

        playerHuman.board.addBoardListener(listener);
        playerOpponent.board.addBoardListener(listener);
    }

    public void nextTurn(){
        currentTurn++;
        if(currentTurn > 1)
            currentTurn = 0;

        notifyTurnChange(getCurrentPlayer());
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

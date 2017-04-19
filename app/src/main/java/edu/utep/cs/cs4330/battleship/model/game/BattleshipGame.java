// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.utep.cs.cs4330.battleship.model.board.Board;
import edu.utep.cs.cs4330.battleship.model.board.Ship;

public class BattleshipGame implements Serializable {
    public interface GameListener {
        void onTurnChange(Player currentPlayer);
    }

    private final List<GameListener> listeners = new ArrayList<>();

    public void addGameListener(GameListener listener) {
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

    public BattleshipGame(Player playerHuman, Player playerOpponent) {
        this.playerHuman = playerHuman;
        this.playerOpponent = playerOpponent;

        Board.BoardListener listener = new Board.BoardListener() {
            @Override
            public void onShipHit(Ship ship) {
                // Ship was hit
                // Player is not allowed multiple shots
                // Change turns
                if (!getCurrentPlayer().isAllowedMultipleShots)
                    nextTurn();
                else // Player is allowed more shots so let them know
                    notifyTurnChange(getCurrentPlayer());
            }

            @Override
            public void onShipMiss() {
                // If nothing was hit change turns
                nextTurn();
            }
        };

        playerHuman.board.addBoardListener(listener);
        playerOpponent.board.addBoardListener(listener);
    }

    private void nextTurn() {
        currentTurn++;
        if (currentTurn > 1)
            currentTurn = 0;

        notifyTurnChange(getCurrentPlayer());
    }

    private Player getCurrentPlayer() {
        if (currentTurn == 0)
            return playerHuman;
        else
            return playerOpponent;
    }
}

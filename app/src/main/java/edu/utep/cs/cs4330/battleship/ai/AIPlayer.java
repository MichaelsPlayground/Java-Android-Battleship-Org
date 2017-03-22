// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.ai;

import edu.utep.cs.cs4330.battleship.model.Board;
import edu.utep.cs.cs4330.battleship.model.Player;
import edu.utep.cs.cs4330.battleship.util.Vector2;

public class AIPlayer extends Player {
    private Strategy strategy;
    public AIPlayer(Board board, boolean isAllowedMultipleHits, Strategy strategy){
        super(board, isAllowedMultipleHits);
        this.strategy = strategy;
    }

    @Override
    public Vector2 onOwnTurn() {
        return strategy.play(board);
    }
}

package edu.utep.cs.cs4330.battleship;

/**
 * Created by xeroj on 021 3 21 2017.
 */

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

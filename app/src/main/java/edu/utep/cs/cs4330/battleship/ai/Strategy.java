// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.ai;

import edu.utep.cs.cs4330.battleship.model.board.Board;
import edu.utep.cs.cs4330.battleship.util.Vector2;


public abstract class Strategy {
    public abstract Vector2 play(Board board);

    public static Strategy fromStrategyType(StrategyType strategyType) {
        if (strategyType == StrategyType.Random)
            return new RandomStrategy();
        else
            return new SweepStrategy();
    }
}

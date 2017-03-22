// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.ai;

import edu.utep.cs.cs4330.battleship.model.Board;
import edu.utep.cs.cs4330.battleship.util.Vector2;

class SweepStrategy extends Strategy {
    private int currentX = 0;
    private int currentY = 0;

    @Override
    public Vector2 play(Board board) {
        Vector2 pos = new Vector2(currentX, currentY);
        currentY++;

        if (currentY >= board.size()) {
            currentY = 0;
            currentX++;
        }

        return pos;
    }
}

// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.network;

import edu.utep.cs.cs4330.battleship.model.board.Board;
import edu.utep.cs.cs4330.battleship.model.game.Player;
import edu.utep.cs.cs4330.battleship.util.Vector2;

public class NetworkPlayer extends Player {
    public NetworkPlayer(Board board, boolean isAllowedMultipleShots) {
        super(board, isAllowedMultipleShots);
    }

    @Override
    public Vector2 onOwnTurn() {
        return super.onOwnTurn();
    }
}

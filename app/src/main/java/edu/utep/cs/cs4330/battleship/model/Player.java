// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.model;

import java.io.Serializable;

import edu.utep.cs.cs4330.battleship.util.Vector2;

public class Player implements Serializable {
    public Board board;
    public boolean isAllowedMultipleShots;

    public Player(Board board, boolean isAllowedMultipleShots){
        this.board = board;
        this.isAllowedMultipleShots = isAllowedMultipleShots;
    }

    public Vector2 onOwnTurn(){
        return null;
    }
}

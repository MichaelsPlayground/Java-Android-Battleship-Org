// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.model;

public enum GameoverType {
    HostWin(0), ClientWin(1);

    public int value;
    GameoverType(int value){
        this.value = value;
    }

    public static GameoverType fromValue(int value){
        GameoverType[] values = GameoverType.values();
        for(int i = 0; i < values.length; i++)
        {
            if(values[i].value == value)
                return values[i];
        }

        return null;
    }
}

// Author: Jose Perez <josegperez@mail.com> and Diego Reynoso
package edu.utep.cs.cs4330.battleship.model;

public enum ResultType {
    Hit(0), Miss(1), Invalid(2);

    public int value;
    ResultType(int value){
        this.value = value;
    }

    public static ResultType fromValue(int value){
        ResultType[] values = ResultType.values();
        for(int i = 0; i < values.length; i++)
        {
            if(values[i].value == value)
                return values[i];
        }

        return null;
    }
}

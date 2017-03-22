/*
 * Author: Jose Perez <josegperez@mail.com>
 */
package edu.utep.cs.cs4330.battleship.util;

import java.io.Serializable;

public class Vector2 implements Serializable{
    public int x, y;

    public static Vector2 Zero = new Vector2(0, 0);

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

/*
 * Author: Jose Perez <josegperez@mail.com>
 */
package edu.utep.cs.cs4330.battleship;

public class Vector2 {
    public int x, y;

    public static Vector2 Zero = new Vector2(0, 0);

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

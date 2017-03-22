/*
 * Author: Jose Perez <josegperez@mail.com>
 */
package edu.utep.cs.cs4330.battleship;

import android.util.Log;

public class Place {
    private Vector2 position;
    private boolean hit;
    private Ship ship;

    public Place(int x, int y) {
        position = new Vector2(x, y);
        hit = false;
        ship = null;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean hasShip() {
        return ship != null;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setHit(boolean isHit) {
        hit = isHit;

        if (isHit == true && ship != null) {
            ship.hit();
        }
    }

    public boolean isHit() {
        return hit;
    }

    public String toString() {
        return "{Place}";
    }
}

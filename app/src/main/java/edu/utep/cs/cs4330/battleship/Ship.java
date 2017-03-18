/*
 * Author: Jose Perez <josegperez@mail.com>
 */
package edu.utep.cs.cs4330.battleship;

public class Ship {
    private String name;
    private final int size;
    private int health;

    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        reset();
    }

    public void reset() {
        health = size;
    }

    public void hit() {
        health--;
        if (health < 0)
            health = 0;
    }

    public boolean isDestroyed() {
        return health == 0;
    }

    public int getSize() {
        return size;
    }

    public String toString() {
        return String.format("{Ship: %s, Size=%s, Health=%s}", name, size, health);
    }
}

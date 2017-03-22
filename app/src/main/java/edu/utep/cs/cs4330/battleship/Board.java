/*
 * Author: Jose Perez <josegperez@mail.com>
 */
package edu.utep.cs.cs4330.battleship;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A game board consisting of <code>size</code> * <code>size</code> places
 * where battleships can be placed. A place of the board is denoted
 * by a pair of 0-based indices (x, y), where x is a column index
 * and y is a row index. A place of the board can be shot at, resulting
 * in either a hit or miss.
 */
public class Board {
    private Random rand;

    public interface BoardListener {
        void onShipHit(Ship ship);
        void onShipMiss();
    }

    private final List<BoardListener> boardListeners = new ArrayList<>();

    public void addBoardListener(BoardListener listener) {
        if (!boardListeners.contains(listener))
            boardListeners.add(listener);
    }

    public void removeBoardListener(BoardListener listener) {
        boardListeners.remove(listener);
    }

    private void notifyShipHit(Ship ship) {
        for (BoardListener listener : boardListeners)
            listener.onShipHit(ship);
    }

    private void notifyShipMiss() {
        for (BoardListener listener : boardListeners)
            listener.onShipMiss();
    }

    private final int size;
    private Place[][] boardPlaces;

    /**
     * Create a new board of the given size.
     */
    public Board(int size) {
        this.size = size;

        boardPlaces = new Place[size][size];
        for (int i = 0; i < boardPlaces.length; i++)
            for (int j = 0; j < boardPlaces[i].length; j++)
                boardPlaces[i][j] = new Place(i, j);

        rand = new Random(System.nanoTime());
    }

    public void addRandomShips(){
        List<Ship> ships = Ship.getShips();
        while (ships.size() > 0) {
            Ship ship = ships.get(0);
            Direction dir = getRandomDirection();
            ship.direction = dir;
            int x = getRandomCoordinate();
            int y = getRandomCoordinate();

            boolean placedShip = placeShip(ship, x, y);
            if (placedShip) {
                Log.d("Debug", "Placed ship : " + ship);
                ships.remove(0);
            }
        }
    }


    private Direction getRandomDirection() {
        return rand.nextBoolean() ? Direction.Horizontal : Direction.Vertical;
    }

    private int getRandomCoordinate() {
        return rand.nextInt(size());
    }

    public int size() {
        return size;
    }

    public boolean removeShip(Ship ship){
        if(ship == null || ship.placesOwned.isEmpty())
            return false;

        for (Vector2 pos : ship.placesOwned){
            placeAt(pos).setShip(null);
            placeAt(pos).setHit(false);
        }

        ship.placesOwned.clear();

        return true;
    }

    public boolean placeShip(Ship ship, Vector2 pos){
        return placeShip(ship, pos.x, pos.y);
    }

    public boolean placeShip(Ship ship, int x, int y) {
        List<Vector2> placesForShip = new ArrayList<Vector2>();

        // Get the places where the ship resides
        for (int i = 0; i < ship.size; i++) {
            Vector2 nextPlace;
            if (ship.direction == Direction.Horizontal)
                nextPlace = new Vector2(x + i, y);
            else
                nextPlace = new Vector2(x, y + i);

            // Check if it's out of bounds or if there's a ship there already
            if (!isValidPlace(nextPlace) || placeAt(nextPlace).hasShip())
                return false;

            placesForShip.add(nextPlace);
        }

        for (Vector2 p : placesForShip) {
            placeAt(p).setShip(ship);
            placeAt(p).setHit(false);
            ship.placesOwned.add(p);
        }

        return true;
    }

    public boolean hit(int x, int y) {
        return hit(placeAt(x, y));
    }

    public boolean hit(Place place) {
        Log.d("Debug", String.format("Trying to hit. Valid(%s), Hit before(%s), Ship(%s)", isValidPlace(place), place.isHit(), place.getShip()));
        if (!isValidPlace(place) || place.isHit())
            return false;

        place.setHit(true);
        if (place.hasShip())
            notifyShipHit(place.getShip());
        else
            notifyShipMiss();

        return true;
    }

    public Place placeAt(Vector2 pos) {
        return placeAt(pos.x, pos.y);
    }

    public Place placeAt(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size)
            return null;
        return boardPlaces[y][x];
    }

    public boolean isValidPlace(Vector2 p) {
        if (p == null) return false;

        return !(p.x < 0 || p.x >= size || p.y < 0 || p.y >= size);
    }

    public boolean isValidPlace(Place p) {
        return isValidPlace(p.getPosition());
    }

    public String boardToString() {
        String result = "Hit\n";
        for (int i = 0; i < boardPlaces.length; i++) {
            for (int j = 0; j < boardPlaces.length; j++) {
                result += (boardPlaces[i][j].isHit()) ? "O" : "#";
            }
            result += "\n";
        }
        result += "Ships\n";
        for (int i = 0; i < boardPlaces.length; i++) {
            for (int j = 0; j < boardPlaces.length; j++) {
                result += (boardPlaces[i][j].getShip() == null) ? "#" : "O";
            }
            result += "\n";
        }
        return result;
    }
}

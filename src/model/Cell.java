package model;

import java.util.ArrayList;
import java.util.List;

public class Cell implements CellObserver {

    private boolean mine;
    private boolean revealed;
    private int adjacentMines;
    private boolean flagged = false;
    private final List<CellObserver> observers = new ArrayList<>();

    // Constructor
    public Cell(boolean mine) {
        this.mine = mine;
        this.revealed = false;
    }

    // Methods
    public void addObserver(CellObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (CellObserver observer : observers) {
            observer.cellUpdated(this);
        }
    }

    public boolean isMine() {
        return mine;
    }
    
    public boolean isRevealed() {
        return revealed;    
    }
    
    public void reveal() {
        this.revealed = true;
        notifyObservers();
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void toggleFlag() {
        flagged = !flagged;
        notifyObservers();
    }

    // Setterand Getters
    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
    
    public int getAdjacentMines() {
        return adjacentMines;
    }

    @Override
    public void cellUpdated(Cell cell) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cellUpdated'");
    }

}

package model;

public class Cell {

    private boolean mine;
    private boolean revealed;
    private int adjacentMines;

    public Cell(boolean mine) {
        this.mine = mine;
        this.revealed = false;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isRevealed() {
        return revealed;    
    }

    public void reveal() {
        this.revealed = true;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
    
}

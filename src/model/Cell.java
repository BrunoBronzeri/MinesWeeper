package model;

public class Cell {

    private boolean mine;
    private boolean revealed;
    private int adjacentMines;
    private boolean flagged = false;

    // Constructor
    public Cell(boolean mine) {
        this.mine = mine;
        this.revealed = false;
    }

    // Methods
    public boolean isMine() {
        return mine;
    }
    
    public boolean isRevealed() {
        return revealed;    
    }
    
    public void reveal() {
        this.revealed = true;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void toggleFlag() {
        flagged = !flagged;
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

}

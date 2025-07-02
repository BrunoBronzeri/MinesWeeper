package model;

import java.util.Random;
import java.awt.Point;


public class Board {
    private final int rows; // private final indicates a variable that is both immutable and accessible only within the class in which it's declared
    private final int cols;
    private final Cell[][] grid;
    private boolean minesGenerated = false;
    private final double minePercentage;

    // Constructor
    public Board(int rows, int cols, double minePercentage) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        this.minePercentage = minePercentage;
        generateBoard(minePercentage);
    }

    // Methods
    private void generateBoard(double minePercentage) {
        java.util.Random rand = new Random();

        // Initialize de matrix with all safe cells
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Cell(false);

        // Put some mines randomly
        int mineCount = (int) (rows * cols * minePercentage);
        while (mineCount > 0) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (!grid[r][c].isMine()) {
                grid[r][c].setMine(true);
                mineCount--;
            }
        }

        // Calculates the number of adjacent mines
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c].setAdjacentMines(countAdjacentMines(r, c));

    }

    // private void createEmptyGrid() {
    // for (int r = 0; r < rows; r++)
    //     for (int c = 0; c < cols; c++)
    //         grid[r][c] = new Cell(false);
    // }

    public boolean areMinesGenerated() {
        return minesGenerated;
    }

    public void generateMinesEnsuringFirstZero(int safeRow, int safeCol) {
        do {
            clearMines();
            placeMinesExcluding(safeRow, safeCol);
            updateNumbers();            
        } while (grid[safeRow][safeCol].getAdjacentMines() != 0);

        minesGenerated = true;
    }

    private void clearMines() {
        for (int r = 0; r < cols; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c].setMine(false);
                grid[r][c].setAdjacentMines(0);
            }
        }
    }

    private void placeMinesExcluding(int safeRow, int safeCol) {
        Random rand = new Random();
        int mineCount = (int)(rows * cols * minePercentage);

        while (mineCount > 0) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);

            if (!grid[r][c].isMine() && (r != safeRow || c != safeCol)) {
                grid[r][c].setMine(true);
                mineCount--;
            }
        }
    }

    private void updateNumbers() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c].setAdjacentMines(countAdjacentMines(r, c));
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++)
            for (int dc = -1; dc <= 1; dc++) {
                int nr = row + dr;
                int nc = col + dc;
                if (nr >= 0 && nr < rows && nc >=0 && nc < cols)
                    if (!(dr == 0 && dc == 0) && grid[nr][nc].isMine())
                        count++;
            }
        return count;
    }

    public boolean checkVictory() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                if (!cell.isMine() && !cell.isRevealed())
                    return false;
            }
        }
        return true;
    }

    // DFS (Depth-First Search) — Busca em Profundidade
    public void revealRecursively(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols)
            return;

        Cell cell = grid[row][col];

        if (cell.isRevealed() || cell.isMine())
            return;
        
        cell.reveal();

        if(cell.getAdjacentMines() == 0) {
            // Recursive to neighbor cells
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr != 0 || dc != 0) {
                        revealRecursively(row + dr, col + dc);
                    }
                }
            }
        }
    }

    public void addObserverToAllCells(CellObserver observer) {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c].addObserver(observer);
    }

    // Getters
    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public Point getCellPosition(Cell target) {
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == target) {
                return new Point(r, c);
            }
        }
    }
    return null; // nunca deve acontecer se o cell está no grid
}
}

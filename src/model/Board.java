package model;

import java.util.Random;

public class Board {
    private final int rows; // private inal indicates a variable that is both immutable and accessible only within the class in which it's declared
    private final int cols;
    private final Cell[][] grid;

    public Board(int rows, int cols, double minePercentage) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        generateBoard(minePercentage);
    }

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
            if (!grid[r][c].setMine()) {
                grid[r][c].setMine(true);
                mineCount--;
            }
        }

        // Calculates the number of adjacent mines
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

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
}

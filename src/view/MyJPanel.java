package view;

import model.Board;
import model.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyJPanel extends JPanel {
    private final int rows = 10;
    private final int cols = 10;
    private final JButton[][] buttons;
    private final Board board;

    public MyJPanel() {
        this.setLayout(new GridLayout(rows, cols));
        buttons = new JButton[rows][cols];
        this.board = new Board(rows, cols, 0.2); // 20% os mines

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(40, 40));

                int finalR = r;
                int finalC = c;
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        handleClick(finalR, finalC);
                    }
                });

                buttons[r][c] = btn;
                this.add(btn);
            }
        }
    }

    private void handleClick(int row, int col) {
        
        if (!board.areMinesGenerated()) {
            board.generateMinesEnsuringFirstZero(row, col);
        }

        Cell cell = board.getCell(row, col);

        if (cell.isRevealed()) {
            return;
        }

        // cell.reveal();

        // if (cell.isMine()) {
        //     buttons[row][col].setText("X");
        //     buttons[row][col].setBackground(Color.RED);
        //     System.out.println("BOOM! Uma mina foi explodida.");
        // } else {
        //     int count = cell.getAdjacentMines();
        //     if (count > 0) {
        //         buttons[row][col].setText(String.valueOf(count));
        //     } else {
        //         buttons[row][col].setText("");
        //     }
        //     buttons[row][col].setBackground(Color.LIGHT_GRAY);
        // }

        if (cell.isMine()) {
            cell.reveal();
            buttons[row][col].setText("X");
            buttons[row][col].setBackground(Color.RED);
            System.out.println("BOOM! Uma mina foi explodida.");
        } else {
            board.revealRecursively(row, col);
            updateButtons();
        }
    }

    private void updateButtons() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = board.getCell(r, c);
                if (cell.isRevealed()) {
                    buttons[r][c].setEnabled(false);
                    buttons[r][c].setBackground(Color.LIGHT_GRAY);

                    if (cell.isMine()) {
                        buttons[r][c].setText("X");
                    } else {
                        int count = cell.getAdjacentMines();
                        if (count > 0 )
                            buttons[r][c].setText(String.valueOf(count));
                        else
                            buttons[r][c].setText("");
                    }
                }
            }
        }
    }
}
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
                btn.setFont(new Font("Monospaced", Font.BOLD, 20));
                btn.setBackground(new Color(150, 220, 80));
                btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                // btn.setBackground(new Color(180, 220, 180));


                int finalR = r;
                int finalC = c;
                
                btn.addMouseListener(new MouseAdapter() {

                    // Button:hover simulator ----------------
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!board.getCell(finalR, finalC).isRevealed()) {
                            btn.setBackground(new Color(180, 200, 255));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (!board.getCell(finalR, finalC).isRevealed()) {
                            btn.setBackground(new Color(160, 230, 70));
                        }
                    }
                    // Button:hover simulator ----------------

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            handleFlag(finalR, finalC);
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            handleClick(finalR, finalC);
                        }
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

        if (cell.isRevealed() || cell.isFlagged())
            return;

        if (cell.isMine()) {
            cell.reveal();

            buttons[row][col].setIcon(ImageAssets.MINE_ICON);
            buttons[row][col].setText("");

            buttons[row][col].setBackground(Color.RED);
            System.out.println("BOOM! Uma mina foi explodida.");
        } else {
            board.revealRecursively(row, col);
            updateButtons();

            if (board.checkVictory()) 
                JOptionPane.showMessageDialog(this, "Parabéns, você venceu!");
        }
    }

    private void handleFlag(int row, int col) {
        Cell cell = board.getCell(row, col);

        if (cell.isRevealed()) return;

        cell.toggleFlag();

        if (cell.isFlagged()) {
            buttons[row][col].setIcon(ImageAssets.FLAG_ICON);
            buttons[row][col].setText("");
        } else
            buttons[row][col].setIcon(null);
            buttons[row][col].setText("");
    }

    private void updateButtons() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = board.getCell(r, c);
                if (cell.isRevealed()) {
                    buttons[r][c].setEnabled(true);
                    buttons[r][c].setFocusable(false);
                    
                    buttons[r][c].setBackground(Color.LIGHT_GRAY);

                    if (cell.isMine()) {
                        buttons[r][c].setIcon(ImageAssets.MINE_ICON);
                        buttons[r][c].setText("");
                    } else {
                        int count = cell.getAdjacentMines();
                        if (count > 0 ) {
                            buttons[r][c].setText(String.valueOf(count));
                            buttons[r][c].setForeground(getColorForNumber(count));
                        } else {
                            buttons[r][c].setText("");
                        }
                    }
                } else if (cell.isFlagged()) {
                    buttons[r][c].setIcon(ImageAssets.FLAG_ICON);
                    buttons[r][c].setText("");
                } else {
                    buttons[r][c].setText("");
                }
            }
        }
    }

    private Color getColorForNumber(int number) {
    switch (number) {
        case 1: return Color.BLUE;
        case 2: return new Color(0, 128, 0); // Dark green
        case 3: return Color.RED;
        case 4: return new Color(0, 0, 128); // Dark Blue
        case 5: return new Color(128, 0, 0); // Dark Brown
        case 6: return Color.CYAN;
        case 7: return Color.BLACK;
        case 8: return Color.GRAY;
        default: return Color.BLACK;
    }
}
}
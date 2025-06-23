package view;

import model.Board;
import model.Cell;
import app.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MyJPanel extends JPanel {
    private int rows;
    private int cols;
    private double minePercentage;
    private JButton[][] buttons;
    private Board board;

    private Timer swingTimer;
    private Timer revealTimer;
    private int secondsElapsed = 0;
    private JLabel timerLabel;
    private JComboBox<String> difficultySelector;

    public MyJPanel(JLabel timerLabel, JComboBox<String> difficultySelector, JButton restartButton) {
        this.timerLabel = timerLabel;
        this.difficultySelector = difficultySelector;

        restartButton.addActionListener(e -> {
            String level = (String) difficultySelector.getSelectedItem();
            createField(level);
        });

        difficultySelector.addActionListener(e -> {
            String level = (String) difficultySelector.getSelectedItem();
            restartWithDifficulty(level);
        });

        setLayout(new BorderLayout());
        createField("Medium"); // default
    }

    private void createField(String level) {

        if (revealTimer != null) {
            revealTimer.stop();
            revealTimer = null;
        }
        
        removeAll();
        // stopTimer();
        secondsElapsed = 0;
        timerLabel.setText("Timer: 0");

        int fontSize;
        Dimension buttonsSize;
        
        switch (level) {
            case "Easy":
                rows = 8;
                cols = 8;
                minePercentage = 0.2;
                fontSize = 20;
                buttonsSize = new Dimension(50,50);
                break;
            case "Medium":
                rows = 12;
                cols = 12;
                minePercentage = 0.2;                
                fontSize = 18;
                buttonsSize = new Dimension(40,40);
                break;
            case "Hard":
                rows = 18;
                cols = 18;
                minePercentage = 0.25;
                fontSize = 14;
                buttonsSize = new Dimension(50,50);
                break;
            default:
                rows = 12;
                cols = 12;
                minePercentage = 0.2;
                fontSize = 18;
                buttonsSize = new Dimension(40,40);
        }

        board = new Board(rows, cols, minePercentage);
        buttons = new JButton[rows][cols];

        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(buttonsSize)); // 40x40
                btn.setFont(new Font("Monospaced", Font.BOLD, fontSize));
                btn.setBackground(new Color(150, 220, 80));
                btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                int finalR = r;
                int finalC = c;

                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!board.getCell(finalR, finalC).isRevealed()) {
                            btn.setBackground(new Color(180, 200, 255));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (!board.getCell(finalR, finalC).isRevealed()) {
                            btn.setBackground(new Color(150, 220, 80));
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            handleFlag(finalR, finalC);
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            if (swingTimer == null) startTimer();
                            handleClick(finalR, finalC);
                        }
                    }
                });

                buttons[r][c] = btn;
                gridPanel.add(btn);
            }
        }

        add(gridPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void startTimer() {
        swingTimer = new Timer(1000, e -> {
            secondsElapsed++;
            int minutes = secondsElapsed / 60;
            int seconds = secondsElapsed % 60;
            timerLabel.setText(String.format("Timer: %02d:%02d", minutes, seconds));
        });
        swingTimer.start();
    }

    private void stopTimer() {
        if (swingTimer != null) {
            swingTimer.stop();
            swingTimer = null;
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

            stopTimer();
            animateRevealMines();
        } else {
            board.revealRecursively(row, col);
            updateButtons();

            if (board.checkVictory()) {
                stopTimer();
                JOptionPane.showMessageDialog(this, "Parabéns, você venceu!");
                // addRestartButton();
            }
        }
    }

    private void handleFlag(int row, int col) {
        Cell cell = board.getCell(row, col);

        if (cell.isRevealed()) return;

        cell.toggleFlag();

        if (cell.isFlagged()) {
            buttons[row][col].setIcon(ImageAssets.FLAG_ICON);
            buttons[row][col].setText("");
        } else {
            buttons[row][col].setIcon(null);
            buttons[row][col].setText("");
        }
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
                        if (count > 0) {
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
                    buttons[r][c].setIcon(null);
                }
            }
        }
    }

    private void animateRevealMines() {
        List<Point> minePoints = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = board.getCell(r, c);
                if (cell.isMine() && !cell.isRevealed()) {
                    minePoints.add(new Point(r, c));
                }
            }
        }

        // setFieldEnabled(false);

        revealTimer = new Timer(200, null);
        revealTimer.addActionListener(new ActionListener() {
            int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < minePoints.size()) {
                    Point p = minePoints.get(index);
                    buttons[p.x][p.y].setIcon(ImageAssets.MINE_ICON);
                    buttons[p.x][p.y].setBackground(Color.RED);
                    index++;
                } else {
                    revealTimer.stop();
                    JOptionPane.showMessageDialog(MyJPanel.this, "BOOM! Você perdeu.");
                    // addRestartButton();
                }
            }
        });
        revealTimer.start();
    }

    // private void addRestartButton() {
    //     JButton restartButton = new JButton("Reiniciar");
    //     restartButton.addActionListener(e -> {
    //         String level = (String) difficultySelector.getSelectedItem();
    //         createField(level);
    //     });

    //     Container parent = this.getParent();
    //     if (parent instanceof JPanel) {
    //         ((JPanel) parent).add(restartButton, BorderLayout.SOUTH);
    //         parent.revalidate();
    //         parent.repaint();
    //     }
    // }

    private void restartWithDifficulty(String level) {
        createField(level);
    }

    private Color getColorForNumber(int number) {
        switch (number) {
            case 1: return Color.BLUE;
            case 2: return new Color(0, 128, 0);
            case 3: return Color.RED;
            case 4: return new Color(0, 0, 128);
            case 5: return new Color(128, 0, 0);
            case 6: return Color.CYAN;
            case 7: return Color.BLACK;
            case 8: return Color.GRAY;
            default: return Color.BLACK;
        }
    }
}

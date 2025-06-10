package view;

import javax.swing.*;
import java.awt.*;

public class MyJPanel extends JPanel {
    private final int rows = 5;
    private final int cols = 5;
    private final JButton[][] buttons;

    public MyJPanel() {
        this.setLayout(new GridLayout(rows, cols));
        buttons = new JButton[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(40, 40));
                buttons[r][c] = btn;
                this.add(btn);
            }
        }
    }
}
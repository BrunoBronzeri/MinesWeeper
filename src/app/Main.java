package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;
import view.MyJPanel;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Minesweeper");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Info panel on top
            JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            infoPanel.setPreferredSize(new Dimension(400, 50));
            infoPanel.setBackground(Color.LIGHT_GRAY);

            // example of components that will be added later
            JLabel timerLabel = new JLabel("Timer: 0");
            JComboBox<String> difficultySelector = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
            difficultySelector.setSelectedItem("Medium");

            infoPanel.add(timerLabel);
            infoPanel.add(Box.createHorizontalStrut(20)); // space between
            infoPanel.add(new JLabel("Dificulty:"));
            infoPanel.add(difficultySelector);

            // Filed's panel
            JPanel fieldJPanel = new MyJPanel(timerLabel, difficultySelector);

            // Add to Frame
            frame.add(infoPanel, BorderLayout.NORTH);
            frame.add(fieldJPanel, BorderLayout.CENTER);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        }); 
        // JFrame frame = new JFrame("MinesWeeper");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.add(new MyJPanel());
        // frame.pack();
        // frame.setLocationRelativeTo(null);
        // frame.setVisible(true);
    }
}
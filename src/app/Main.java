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

            // Field frame
            JFrame frame = new JFrame("Minesweeper");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setPreferredSize(new Dimension(700, 780));

            // Info panel on top - Header
            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setPreferredSize(new Dimension(400, 50));
            infoPanel.setBackground(Color.LIGHT_GRAY);

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            leftPanel.setBackground(Color.LIGHT_GRAY);
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rightPanel.setBackground(Color.LIGHT_GRAY);

            // Info on bottom - Footer
            JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            footerPanel.setBackground(Color.LIGHT_GRAY);

            // Components in Header
            JLabel timerLabel = new JLabel("Timer: 00:00");
            JComboBox<String> difficultySelector = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
            difficultySelector.setSelectedItem("Medium");
            JButton restartButton = new JButton("Restart");

            leftPanel.add(timerLabel);
            leftPanel.add(Box.createHorizontalStrut(20)); // space between
            leftPanel.add(new JLabel("Dificulty:"));
            leftPanel.add(difficultySelector);
            leftPanel.add(Box.createHorizontalStrut(20));
            rightPanel.add(restartButton);

            infoPanel.add(leftPanel, BorderLayout.WEST);
            infoPanel.add(rightPanel, BorderLayout.EAST);


            JLabel authorLabel = new JLabel("Author: Bruno Bronzeri");
            authorLabel.setFont(authorLabel.getFont().deriveFont(10f));
            footerPanel.add(authorLabel);

            // Filed's panel
            JPanel fieldJPanel = new MyJPanel(timerLabel, difficultySelector, restartButton);

            // Add to Frame
            frame.add(infoPanel, BorderLayout.NORTH);
            frame.add(fieldJPanel, BorderLayout.CENTER);
            frame.add(footerPanel, BorderLayout.SOUTH);

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
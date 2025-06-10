import javax.swing.*;
import view.MyJPanel;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("MinesWeeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MyJPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
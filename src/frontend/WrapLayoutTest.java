package frontend;
import java.awt.FlowLayout;

import javax.swing.*;

public class WrapLayoutTest {
    public static void main(String[] args) {
            JFrame frame = new JFrame("Wrapping Layout Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new WrappingLayout(5, 5));

            for (int i = 0; i < 100; i++) {
                panel.add(new JButton("Btn " + i));
            }

            JScrollPane scrollPane = new JScrollPane(panel);
            frame.add(scrollPane);
            frame.setSize(400, 300);
            frame.setVisible(true);
    }
}

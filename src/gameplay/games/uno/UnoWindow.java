package gameplay.games.uno;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UnoWindow {
    private final JFrame frame;
    private JPanel mainPanel = new JPanel();
    private JPanel cardPanel = new JPanel();
    private JPanel handPanel = new JPanel();
    private Card topCard;
    private GridBagConstraints gbc = new GridBagConstraints();

    public UnoWindow() {
        frame = new JFrame("Uno Game");
        initialize();
    }

    public void initialize() {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(new BorderLayout());
        cardPanel.setLayout(new GridBagLayout());
        handPanel.setLayout(new GridBagLayout());
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(handPanel, BorderLayout.SOUTH);
    }

    public void updateTopCard(Card card) {
        cardPanel.removeAll();
        topCard = card;
        JLabel cardLabel = new JLabel(new ImageIcon(topCard.getImageFile().getAbsolutePath()));
        JLabel deck = new JLabel(new ImageIcon("src\\gameplay\\games\\uno\\assets\\Deck.png"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        cardPanel.add(deck, gbc);
        gbc.gridx = 1;
        cardPanel.add(cardLabel, gbc);
        cardPanel.revalidate();
        cardPanel.repaint();
    }
}

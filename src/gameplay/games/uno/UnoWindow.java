package gameplay.games.uno;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

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
    private ArrayList<Card> hand = new ArrayList<>();
    private GridBagConstraints gbc = new GridBagConstraints();

    public UnoWindow() {
        frame = new JFrame("Uno Game");
        initialize();
    }

    public void initialize() {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(1600, 900);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(new BorderLayout());
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setOpaque(false);
        handPanel.setLayout(new GridBagLayout());
        handPanel.setOpaque(false);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(handPanel, BorderLayout.SOUTH);
        mainPanel.add(new JLabel(new ImageIcon("src\\gameplay\\games\\uno\\assets\\Table.png")));
    }

    public void updateHand(ArrayList<Card> newHand) {
        handPanel.removeAll();
        hand = newHand;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        for (Card card : hand) {
            JLabel cardLabel = new JLabel(new ImageIcon(card.getImageFile().getAbsolutePath()));
            handPanel.add(cardLabel, gbc);
            gbc.gridx++;
        }
        handPanel.revalidate();
        handPanel.repaint();
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

    public void reDraw() {
        updateTopCard(topCard);
        updateHand(hand);
    }
}

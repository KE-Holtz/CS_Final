package gameplay.games.uno;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
        JLabel background = new JLabel(new ImageIcon("src\\gameplay\\games\\uno\\assets\\Table.png"));
        background.setBounds(0,0, mainPanel.getSize().width, mainPanel.getSize().height);
        mainPanel.add(background);
    }

    public void updateHand(ArrayList<Card> newHand) {
        handPanel.removeAll();
        hand = newHand;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        for (Card card : hand) {
            JButton cardButton = new JButton();
            cardButton.setIcon(scaleImage(new ImageIcon(card.getImageFile().getAbsolutePath()), 150));
            cardButton.setContentAreaFilled(false);
            cardButton.setFocusPainted(false);
            Dimension size = new Dimension(cardButton.getIcon().getIconWidth(), cardButton.getIcon().getIconHeight());
            cardButton.setPreferredSize(size);
            cardButton.setMaximumSize(size);
            cardButton.setMinimumSize(size);
            handPanel.add(cardButton, gbc);
            gbc.gridx++;
        }
        handPanel.revalidate();
        handPanel.repaint();
    }

    public void updateTopCard(Card card) {
        cardPanel.removeAll();
        topCard = card;
        JLabel cardLabel = new JLabel(new ImageIcon(topCard.getImageFile().getAbsolutePath()));
        JButton deck = new JButton();
        deck.setIcon(new ImageIcon("src\\gameplay\\games\\uno\\assets\\Deck.png"));
        deck.setContentAreaFilled(false);
        deck.setFocusPainted(false);
        Dimension size = new Dimension(deck.getIcon().getIconWidth(), deck.getIcon().getIconHeight());
        deck.setPreferredSize(size);
        deck.setMaximumSize(size);
        deck.setMinimumSize(size);
        gbc.gridx = 0;
        gbc.gridy = 0;
        cardPanel.add(deck, gbc);
        gbc.gridx = 1;
        cardPanel.add(cardLabel, gbc);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public ImageIcon scaleImage(ImageIcon icon, int width) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, -1, Image.SCALE_SMOOTH));
    }
}

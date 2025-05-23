package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import backend.Lobby;
import gameplay.Player;
import gameplay.games.Game;

public class LobbyWindow {
    private JFrame frame = new JFrame("Lobby");

    private Lobby lobby;
    private HashMap<String, Game> games = new HashMap<>();

    final private int[] currentPanel = { 0 };
    final private boolean[] clicked = { false };
    final private String[] selectedGameName = { "" };

    private JPanel playerPanel = new JPanel();
    private JPanel gamePanel = new JPanel();
    private JPanel buttons = new JPanel();
    private JPanel backPanel = new JPanel();

    private Font buttonFont = new Font("Arial", Font.PLAIN, 40);
    private Font playerFont = new Font("Arial", Font.PLAIN, 20);
    private Font gameFont = new Font("Arial", Font.PLAIN, 20);

    private GridBagConstraints gbc = new GridBagConstraints();

    public LobbyWindow(Lobby lobby, HashMap<String, Game> games) {
        this.lobby = lobby;
        this.games = games;
        initialize();
    }

    public void initialize() {
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        JPanel selectedGamePanel = new JPanel();

        backPanel.setLocation(10, 10);

        JButton startButton = new JButton("Start Game");
        JButton playerButton = new JButton("Players");
        JButton gameButton = new JButton("Games");
        JButton backButton = new JButton("Back");

        JLabel selectedGame = new JLabel("Selected Game: ");
        selectedGame.setFont(playerFont);
        selectedGame.setVisible(true);
        selectedGamePanel.add(selectedGame);
        selectedGamePanel.setVisible(true);

        gbc.gridx = 0;
        gbc.gridy = 0;

        frame.add(panel, BorderLayout.CENTER);
        frame.add(backPanel, BorderLayout.NORTH);

        panel.add(selectedGamePanel, gbc);

        backButton.setBackground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(buttonFont);
        backButton.setVisible(true);

        startButton.setBackground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setFont(buttonFont);

        playerButton.setBackground(Color.WHITE);
        playerButton.setFocusPainted(false);
        playerButton.setFont(buttonFont);

        gameButton.setBackground(Color.WHITE);
        gameButton.setFocusPainted(false);
        gameButton.setFont(buttonFont);

        playerPanel.setLayout(new GridBagLayout());
        gamePanel.setLayout(new GridBagLayout());
        buttons.setLayout(new GridBagLayout());
        panel.setLayout(new GridBagLayout());
        backPanel.setLayout(new BorderLayout());
        backPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttons.add(startButton, gbc);
        gbc.gridy++;
        buttons.add(playerButton, gbc);
        gbc.gridy++;
        buttons.add(gameButton, gbc);
        gbc.gridy = 1;
        backPanel.add(backButton, BorderLayout.WEST);

        panel.add(buttons, gbc);

        gbc.gridy = 0;
        playerPanel = getPlayerPanel(playerFont, gbc);
        gbc.gridy++;
        panel.add(playerPanel, gbc);

        gbc.gridy = 0;
        gamePanel = getGamePanel(gameFont, gbc);
        gbc.gridy++;
        panel.add(gamePanel, gbc);

        playerPanel.setVisible(false);
        gamePanel.setVisible(false);
        buttons.setVisible(true);
        backPanel.setVisible(false);

        frame.pack();
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        for (Component c : gamePanel.getComponents()) {
            if (c instanceof JButton) {
                JButton button = (JButton) c;
                button.addActionListener(e -> {
                    currentPanel[0] = 3;
                    selectedGameName[0] = button.getText();
                    selectedGame.setText("Selected Game: " + selectedGameName[0]);
                    selectedGamePanel.revalidate();
                    selectedGamePanel.repaint();
                });
            }
        }

        startButton.addActionListener(e -> {
            if (!selectedGameName[0].equals("")) {
                clicked[0] = true;
                currentPanel[0] = 3;
            }
        });
        playerButton.addActionListener(e -> {
            clicked[0] = true;
            currentPanel[0] = 1;
        });
        gameButton.addActionListener(e -> {
            clicked[0] = true;
            currentPanel[0] = 2;
        });
        backButton.addActionListener(e -> {
            clicked[0] = false;
            currentPanel[0] = 0;
        });
    }

    public void updateCurrentPanel(int panel) {
        if (panel == 1) {
            playerPanel = getPlayerPanel(playerFont, gbc);
            playerPanel.revalidate();
            playerPanel.repaint();
            playerPanel.setVisible(true);
            gamePanel.setVisible(false);
            buttons.setVisible(false);
            backPanel.setVisible(true);
        } else if (panel == 2) {
            playerPanel.setVisible(false);
            gamePanel.setVisible(true);
            buttons.setVisible(false);
            backPanel.setVisible(true);
        } else if (panel == 3) {
            frame.dispose();
            clicked[0] = false;
        } else {
            playerPanel.setVisible(false);
            gamePanel.setVisible(false);
            buttons.setVisible(true);
            backPanel.setVisible(false);
        }
    }

    public int getCurrentPanel() {
        return currentPanel[0];
    }

    public boolean isClicked() {
        return clicked[0];
    }

    public String getSelectedGameName() {
        return selectedGameName[0];
    }

    public JPanel getPlayerPanel(Font playerFont, GridBagConstraints gbc) {
        Player[] players = lobby.getPlayers();
        JPanel playerPanel = new JPanel();
        for (Player p : players) {
            JLabel label = new JLabel(p.getName());
            label.setFont(playerFont);
            playerPanel.add(label, gbc);
            gbc.gridy++;
        }
        return playerPanel;
    }

    public JPanel getGamePanel(Font gameFont, GridBagConstraints gbc) {
        JPanel gamePanel = new JPanel();
        for (String gameName : games.keySet()) {
            JButton button = new JButton(gameName);
            button.setFont(gameFont);
            button.setBackground(Color.WHITE);
            button.setFocusPainted(false);
            gamePanel.add(button, gbc);
            gbc.gridy++;
        }
        return gamePanel;
    }

}

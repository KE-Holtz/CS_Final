package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import backend.Lobby;
import backend.Session;
import backend.globalvars.GlobalBoolean;
import backend.globalvars.GlobalString;
import gameplay.Player;
import gameplay.games.Game;

public class HostLobbyWindow {
    private JFrame frame = new JFrame("Lobby");

    private Lobby lobby;
    private Session session;
    private HashMap<String, Game> games = new HashMap<>();

    final private int[] currentPanel = { 0 };
    final private boolean[] clicked = { false };

    private GlobalString selectedGameName;
    private GlobalBoolean startGame;
    private GlobalBoolean closeLobby;
    private GlobalBoolean hostConnected;

    private JPanel panel = new JPanel();

    private JPanel playerPanel = new JPanel();
    private JPanel gamePanel = new JPanel();
    private JPanel buttons = new JPanel();
    private JPanel backPanel = new JPanel();
    private JPanel selectedGamePanel = new JPanel();

    private Font buttonFont = new Font("Arial", Font.PLAIN, 40);
    private Font playerFont = new Font("Arial", Font.PLAIN, 20);
    private Font gameFont = new Font("Arial", Font.PLAIN, 20);

    private GridBagConstraints gbc = new GridBagConstraints();

    public HostLobbyWindow(Lobby lobby, Session session, HashMap<String, Game> games, String gameName) {
        this.lobby = lobby;
        this.session = session;
        this.games = games;
        selectedGameName = new GlobalString(session, "selectedGameName");
        selectedGameName.setValue(gameName);
        startGame = new GlobalBoolean(session, "startGame");
        startGame.setValue(false);
        closeLobby = new GlobalBoolean(session, "closeLobby");
        closeLobby.setValue(false);
        hostConnected = new GlobalBoolean(session, "hostConnected");
        hostConnected.setValue(true);
        initialize();
    }

    public void initialize() {
        frame.setLayout(new BorderLayout());

        backPanel.setLocation(10, 10);

        JButton startButton = new JButton("Start Game");
        JButton playerButton = new JButton("Players");
        JButton gameButton = new JButton("Games");
        JButton closeLobbyButton = new JButton("Close Lobby");
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

        closeLobbyButton.setBackground(Color.WHITE);
        closeLobbyButton.setFocusPainted(false);
        closeLobbyButton.setFont(buttonFont);

        startButton.setBackground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setFont(buttonFont);

        playerButton.setBackground(Color.WHITE);
        playerButton.setFocusPainted(false);
        playerButton.setFont(buttonFont);

        gameButton.setBackground(Color.WHITE);
        gameButton.setFocusPainted(false);
        gameButton.setFont(buttonFont);

        playerPanel.setLayout(new WrappingLayout(7,5, WrappingLayout.CENTER));
        gamePanel.setLayout(new WrappingLayout(5,5, WrappingLayout.CENTER));
        buttons.setLayout(new GridBagLayout());
        panel.setLayout(new GridBagLayout());
        backPanel.setLayout(new BorderLayout());
        backPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttons.add(startButton, gbc);
        gbc.gridy++;
        buttons.add(playerButton, gbc);
        gbc.gridy++;
        buttons.add(gameButton, gbc);
        gbc.gridy++;
        buttons.add(closeLobbyButton, gbc);
        gbc.gridy = 1;
        backPanel.add(backButton, BorderLayout.WEST);

        panel.add(buttons, gbc);

        gbc.gridy = 0;
        updatePlayerPanel();
        gbc.gridy++;
        panel.add(playerPanel, gbc);

        gbc.gridy = 0;
        gamePanel = getGamePanel();
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
                    selectedGameName.setValue(button.getText());
                    selectedGame.setText("Selected Game: " + selectedGameName.getValue());
                    selectedGamePanel.revalidate();
                    selectedGamePanel.repaint();
                });
            }
        }

        startButton.addActionListener(e -> {
            if (!selectedGameName.getValue().equals("")) {
                currentPanel[0] = 3;
                clicked[0] = true;
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
        closeLobbyButton.addActionListener(e -> {
            clicked[0] = true;
            currentPanel[0] = 4;
        });
    }

    public void closeLobby() {
        JFrame frame = new JFrame("Closing");
        frame.setLayout(new BorderLayout());

        Font font = new Font("Arial", Font.PLAIN, 20);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Closing lobby...");
        JLabel label2 = new JLabel("Please wait for all players to leave...");

        label.setFont(font);
        label2.setFont(font);

        panel.add(label, gbc);
        gbc.gridy++;
        panel.add(label2, gbc);

        frame.add(panel, BorderLayout.CENTER);

        frame.pack();
        frame.setSize(600, 200);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        while (lobby.getPlayers().length > 1 || new File(session.getSessionSpace()).exists()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (!session.clean() || new File(session.getSessionSpace()).exists()) {
            System.out.println(
                    "[DEBUG] Failed to clean up session. trying again...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        frame.dispose();
    }

    public void updateCurrentPanel(int panel) {
        if (panel == 1) {
            updatePlayerPanel();
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
            clicked[0] = false;
            startGame.setValue(true);
        } else if (panel == 4) {
            clicked[0] = false;
            closeLobby.setValue(true);
        } else {
            playerPanel.setVisible(false);
            gamePanel.setVisible(false);
            buttons.setVisible(true);
            backPanel.setVisible(false);
        }
    }

    public void dispose() {
        frame.dispose();
    }

    public int getCurrentPanel() {
        return currentPanel[0];
    }

    public boolean isClicked() {
        return clicked[0];
    }

    public boolean isStarted() {
        return startGame.getValue();
    }

    public void setStarted(boolean value) {
        startGame.setValue(value);
    }

    public boolean isClosed() {
        return closeLobby.getValue();
    }

    public String getSelectedGameName() {
        return selectedGameName.getValue();
    }

    public void setHostConnected(boolean value) {
        hostConnected.setValue(value);
    }

    public void updatePlayerPanel() {
        Player[] players = lobby.getPlayers();
        ArrayList<JLabel> playerLabels = new ArrayList<>();
        for (Component c : playerPanel.getComponents()) {
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                playerLabels.add(label);
            }
        }
        int labelIndex = playerLabels.size();
        for (Player p : players) {
            boolean found = false;
            for (JLabel label : playerLabels) {
                JLabel tempLabel = label;
                if (label.getText().endsWith(",")) {
                    tempLabel.setText(tempLabel.getText().substring(0, tempLabel.getText().length() - 1));
                }
                if (tempLabel.getText().equals(p.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                JLabel label = new JLabel(p.getName());
                label.setFont(playerFont);
                playerLabels.add(label);
            }
        }
        for (int i = 0; i < playerLabels.size() - 1; i++) {
            if (!playerLabels.get(i).getText().endsWith(",")) {
                playerLabels.get(i).setText(playerLabels.get(i).getText() + ",");
            }
        }
        for (int i = labelIndex; i < playerLabels.size(); i++) {
            playerPanel.add(playerLabels.get(i));
        }
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    public JPanel getGamePanel() {
        JPanel tempGamePanel = new JPanel();
        for (String gameName : games.keySet()) {
            JButton button = new JButton(gameName);
            button.setFont(gameFont);
            button.setBackground(Color.WHITE);
            button.setFocusPainted(false);
            tempGamePanel.add(button);
        }
        return tempGamePanel;
    }

}

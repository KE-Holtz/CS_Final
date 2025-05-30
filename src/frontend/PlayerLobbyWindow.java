package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import backend.Lobby;
import backend.Session;
import backend.globalvars.GlobalBoolean;
import backend.globalvars.GlobalString;
import gameplay.Player;

public class PlayerLobbyWindow {
    private JFrame frame = new JFrame("Lobby");

    private Lobby lobby;
    private Session session;

    private GlobalString selectedGameName;
    private GlobalBoolean startGame;
    private GlobalBoolean closeLobby;
    private GlobalBoolean hostConnected;

    private Font font = new Font("Arial", Font.PLAIN, 20);

    private JPanel panel = new JPanel();
    private JPanel playerPanel = new JPanel();

    private JLabel selectedGame = new JLabel("Selected Game: ");

    private GridBagConstraints gbc = new GridBagConstraints();

    private int numOfPlayers = 0;

    public PlayerLobbyWindow(Lobby lobby, Session session) {
        this.lobby = lobby;
        this.session = session;
        selectedGameName = new GlobalString(session, "selectedGameName");
        selectedGameName.getValue();
        startGame = new GlobalBoolean(session, "startGame");
        startGame.getValue();
        closeLobby = new GlobalBoolean(session, "closeLobby");
        closeLobby.getValue();
        hostConnected = new GlobalBoolean(session, "hostConnected");
        hostConnected.getValue();
        initialize();
    }

    public void initialize() {
        frame.setLayout(new BorderLayout());

        panel.setLayout(new GridBagLayout());
        JPanel selectedGamePanel = new JPanel();

        playerPanel.setLayout(new WrappingLayout(7, 5, WrappingLayout.CENTER));

        selectedGame.setFont(font);
        selectedGame.setVisible(true);
        selectedGamePanel.add(selectedGame);
        selectedGamePanel.setVisible(true);

        updatePlayerPanel();

        gbc.gridx = 0;
        gbc.gridy = 0;

        frame.add(panel, BorderLayout.NORTH);

        panel.add(selectedGamePanel, gbc);
        gbc.gridy++;
        panel.add(playerPanel, gbc);

        frame.pack();
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void hostNotConnected() {
        JFrame frame = new JFrame("Waiting");
        frame.setLayout(new BorderLayout());

        Font font = new Font("Arial", Font.PLAIN, 20);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Host not connected...");
        JLabel label2 = new JLabel("Please wait for the host to connect...");

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

        while (!hostConnected.getValue().orElse(false)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        frame.dispose();
    }

    public void closeLobby() {
        JFrame frame = new JFrame("Closing");
        frame.setLayout(new BorderLayout());

        Font font = new Font("Arial", Font.PLAIN, 20);
        Font buttonFont = new Font("Arial", Font.PLAIN, 40);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Host is closing lobby...");
        JLabel label2 = new JLabel("Please click leave to exit...");

        JButton leaveButton = new JButton("Leave");

        label.setFont(font);
        label2.setFont(font);

        leaveButton.setFont(buttonFont);
        leaveButton.setBackground(Color.WHITE);
        leaveButton.setFocusPainted(false);

        panel.add(label, gbc);
        gbc.gridy++;
        panel.add(label2, gbc);
        gbc.gridy++;
        panel.add(leaveButton, gbc);

        frame.add(panel, BorderLayout.CENTER);

        frame.pack();
        frame.setSize(600, 200);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        leaveButton.addActionListener(e -> {
            frame.dispose();
        });

        while (frame.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (!session.clean()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        updatePlayerPanel();
        selectedGame.setText("Selected game: " + selectedGameName.getValue());
        startGame.getValue();
        panel.revalidate();
        panel.repaint();
    }

    public void dispose() {
        frame.dispose();
    }

    public boolean isStarted() {
        return startGame.getValue().orElse(false);
    }

    public String getSelectedGameName() {
        return selectedGameName.getValue().orElse("");
    }

    public boolean isClosed() {
        return closeLobby.getValue().orElse(false);
    }

    public boolean isHostConnected() {
        return hostConnected.getValue().orElse(true);
    }

    public void updatePlayerPanel() {
        if (lobby.getPlayersArray().length == numOfPlayers)
            return;
        numOfPlayers = lobby.getPlayersArray().length;
        Player[] players = lobby.getPlayersArray();
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
                label.setFont(font);
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

}

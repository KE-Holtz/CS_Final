package frontend;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import backend.Lobby;
import gameplay.Player;
import gameplay.games.Game;

public class lobbyWindow {
    private Lobby lobby;
    private HashMap<String, Game> games = new HashMap<>();
    public lobbyWindow(Lobby lobby, HashMap<String, Game> games) {
        this.lobby = lobby;
        this.games = games;
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

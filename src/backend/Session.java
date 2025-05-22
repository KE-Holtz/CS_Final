package backend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

import gameplay.Player;
import gameplay.games.Game;

public class Session {
    private final String sessionName;
    private final String sessionSpacePath; // Path to the folder where the
                                           // session is stored.

    private final boolean isHost;
    private final Player clientPlayer;

    private final Lobby lobby;

    private HashMap<String, Game> games = new HashMap<>();
    // Constructor ONLY FOR HOSTING
    // Host has extra responsibilities - add files for games, etc

    public Session(String sessionName, String sessionSpacePath, String clientName,
            ArrayList<Game> games, boolean hosting) {

        this.sessionName = sessionName;
        this.sessionSpacePath = sessionSpacePath;

        this.clientPlayer = new Player(clientName, getPlayerSpacePath());
        isHost = hosting;

        this.lobby = new Lobby(this);
        this.games = mapGames(games);

        if (hosting) {
            hostInitialize();
        } else {
            joinInitialize();
        }
    }

    public void hostInitialize() {
        // Create the session folder
        File sessionFolder = new File(sessionSpacePath + "\\" + sessionName);
        File playerSpaceFolder = new File(sessionFolder.getAbsolutePath() + "\\" + "players");
        if (sessionFolder.mkdir()) {
            System.out.println("[DEBUG] Session folder created");
        } else {
            System.out.println("[DEBUG] Session folder failed to create at "
                    + sessionFolder.getAbsolutePath());
        }
        if (playerSpaceFolder.mkdir()) {
            System.out.println("[DEBUG] Player space folder created");
        } else {
            System.out.println("[DEBUG] Player space folder failed to create at "
                    + playerSpaceFolder.getAbsolutePath());
        }

        lobby.makeClientFiles();
    }

    public void joinInitialize() {
        lobby.makeClientFiles();
        // TODO: synchronize
    }

    public HashMap<String, Game> mapGames(ArrayList<Game> games) {
        HashMap<String, Game> map = new HashMap<>();
        for (Game game : games) {
            map.put(game.getName(), game);
        }
        return map;
    }

    public void synchronize() {
        // TODO: implement
        /*
         * Synchronizing makes me think that we are going to need more custom
         * data structures - A custom tree structure for files - A 'Lobby'
         * Structure that can store players in a way that is easy to
         * synchronize
         */
    }

    public boolean clean() {
        if (isHost) {
            File sessionFolder = new File(sessionSpacePath + "\\" + sessionName);
            if (!deleteRecursively(sessionFolder)) {
                return false;
            }
        } else {
            lobby.deleteClientFiles();
        }
        return true;
    }

    // Delete the folder and all of its contents
    // TODO: handle failing to delete a file
    private boolean deleteRecursively(File dir) {
        for (File file : dir.listFiles()) {
            deleteRecursively(file);
        }
        if (dir.exists()) {
            if (!dir.delete()) {
                return false;
            }
        }
        return true;
    }

    // TODO: temporary
    public void runGame(String gameName) {
        Game game = games.get(gameName);
        game.initialize(this);
        game.startGame();
        while (game.periodic())
            ;
        game.endGame();
        System.out.println("Game ended");
    }

    public String getSessionSpace() {
        return sessionSpacePath;
    }

    public String getSessionName() {
        return sessionName;
    }

    public String sessionFolder() {
        return sessionSpacePath + "\\" + sessionName;
    }

    public String getPlayerSpacePath() {
        return sessionFolder() + "\\" + "players";
    }

    public Player getClientPlayer() {
        return clientPlayer;
    }

    public boolean clientIsHost() {
        return isHost;
    }

    public void host() {
        Font buttonFont = new Font("Arial", Font.PLAIN, 40);
        Font playerFont = new Font("Arial", Font.PLAIN, 20);
        Font gameFont = new Font("Arial", Font.PLAIN, 20);

        JFrame frame = new JFrame("Lobby");

        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        JPanel playerPanel = new JPanel();
        JPanel gamePanel = new JPanel();
        JPanel buttons = new JPanel();
        JPanel backPanel = new JPanel();
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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        frame.add(panel, BorderLayout.CENTER);
        frame.add(backPanel, BorderLayout.NORTH);

        panel.add(selectedGamePanel, gbc);

        backButton.setBackground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(buttonFont);
        backButton.setVisible(false);

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
        backPanel.setVisible(true);

        frame.pack();
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        final boolean[] clicked = { false };
        final int[] currentPanel = { 0 };
        final String[] selectedGameName = { "" };

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

        while (currentPanel[0] != 3 || clicked[0]) {

            if (currentPanel[0] == 1) {
                playerPanel = getPlayerPanel(playerFont, gbc);
                playerPanel.revalidate();
                playerPanel.repaint();
                playerPanel.setVisible(true);
                gamePanel.setVisible(false);
                buttons.setVisible(false);
                backButton.setVisible(true);
                while (clicked[0]) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (currentPanel[0] == 2) {
                playerPanel.setVisible(false);
                gamePanel.setVisible(true);
                buttons.setVisible(false);
                backButton.setVisible(true);
                while (clicked[0]) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (currentPanel[0] == 3) {
                frame.dispose();
                clicked[0] = false;
            } else {
                playerPanel.setVisible(false);
                gamePanel.setVisible(false);
                buttons.setVisible(true);
                backButton.setVisible(false);
                while (!clicked[0]) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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


    public void join() {

    }
}

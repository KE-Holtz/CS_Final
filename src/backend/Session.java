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

import frontend.LobbyWindow;
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
        LobbyWindow lw = new LobbyWindow(lobby, games);

        while (lw.getCurrentPanel() != 3 || lw.isClicked()) {

            if (lw.getCurrentPanel() == 1) {
                lw.updateCurrentPanel(1);
                while (lw.isClicked()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (lw.getCurrentPanel() == 2) {
                lw.updateCurrentPanel(2);
                while (lw.isClicked()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (lw.getCurrentPanel() == 3) {
                lw.updateCurrentPanel(3);
            } else {
                lw.updateCurrentPanel(0);
                while (!lw.isClicked()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void join() {

    }
}

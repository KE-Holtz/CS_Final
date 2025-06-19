package backend;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import frontend.HostLobbyWindow;
import frontend.PlayerLobbyWindow;
import gameplay.Player;
import gameplay.games.Game;

public class Session {
    private final String sessionName;
    private final String sessionSpacePath; // Path to the folder where the
                                           // session is stored.

    private final boolean isHost;
    private final Player clientPlayer;

    private final Lobby lobby;

    private final String delimiter;

    private HashMap<String, Game> games = new HashMap<>();
    // Constructor ONLY FOR HOSTING
    // Host has extra responsibilities - add files for games, etc

    public Session(String sessionName, String clientName,
            ArrayList<Game> games, boolean hosting, HashMap<String, String> opts) {

        this.sessionName = sessionName;
        this.sessionSpacePath = opts.get("session_space");
        delimiter = opts.get("os").equals("windows")? "\\" : "/";

        this.clientPlayer = new Player(clientName, this);
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
        File sessionFolder = new File(sessionSpacePath + delimiter + sessionName);
        File playerSpaceFolder = new File(sessionFolder.getAbsolutePath() + delimiter + "players");
        if (!sessionFolder.mkdir()) {
            System.out.println("[DEBUG] Session folder failed to create at "
                    + sessionFolder.getAbsolutePath());
        }
        if (!playerSpaceFolder.mkdir()) {
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

    public boolean clean() {
        if (isHost) {
            File sessionFolder = new File(sessionSpacePath + delimiter + sessionName);
            if (!deleteRecursively(sessionFolder)) {
                return false;
            }
            return true;
        } else {
            return lobby.deleteClientFiles();
        }
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
    public void runGame(String gameName, boolean isHost) {
        Game game = games.get(gameName);
        game.initialize(this);
        game.startGame();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            lobby.synchronize();
            if(!game.periodic())
                scheduler.shutdown();
        }, 0, 50, TimeUnit.MILLISECONDS);
        while(!scheduler.isShutdown());
        game.endGame();
        if (isHost) {
            host(game.getName());
        } else {
            join();
        }

    }

    public String getSessionSpace() {
        return sessionSpacePath;
    }

    public String getSessionName() {
        return sessionName;
    }

    public String sessionFolder() {
        return sessionSpacePath + delimiter + sessionName;
    }

    public String getPlayerSpacePath() {
        return sessionFolder() + delimiter + "players";
    }

    public Player getClientPlayer() {
        return clientPlayer;
    }

    public boolean clientIsHost() {
        return isHost;
    }

    public void host(String gameName) {
        HostLobbyWindow lw = new HostLobbyWindow(lobby, this, games, gameName);

        while (!lw.isStarted() && !lw.isClosed()) {

            if (lw.getCurrentPanel() == 1) {
                lw.updateCurrentPanel(1);
                while (lw.isClicked()) {
                    lw.updatePlayerPanel();
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
                while (lw.isClicked()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (lw.getCurrentPanel() == 4) {
                lw.updateCurrentPanel(4);
                while (lw.isClicked()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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
        lw.dispose();
        if (lw.isClosed()) {
            lw.closeLobby();
        } else {
            lw.setHostConnected(false);
            runGame(lw.getSelectedGameName(), true);
        }
    }

    public void join() {
        PlayerLobbyWindow lw = new PlayerLobbyWindow(lobby, this);
        if (!lw.isHostConnected())
            lw.hostNotConnected();
        while (!lw.isStarted() && !lw.isClosed()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lw.update();
        }
        lw.dispose();
        if (lw.isClosed()) {
            lw.closeLobby();
        } else {
            runGame(lw.getSelectedGameName(), false);
        }
    }

    public Lobby getLobby() {
        return lobby;
    }

    public String getDelimiter(){
        return delimiter;
    }
}

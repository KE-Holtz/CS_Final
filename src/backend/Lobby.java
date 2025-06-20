package backend;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import gameplay.Player;

public class Lobby {
    private final String playerSpacePath;
    private final HashMap<String, Player> players;
    private final Player clientPlayer;

    public Lobby(Session session) {
        playerSpacePath = session.getPlayerSpacePath();
        clientPlayer = session.getClientPlayer();
        players = new HashMap<String, Player>();
    }

    public void synchronize() {
        for (File playerFile : new File(playerSpacePath).listFiles()) {
            Player player = Player.fromFile(playerFile);
            if (!players.containsValue(player)) {
                players.put(player.getName(), player);
            }
        }
        for (int i = players.size() - 1; i >= 0; i--) {
            if (!players.values().toArray(new Player[0])[i].getPlayerFolder().exists()) {
                players.remove(players.values().toArray(new Player[0])[i].getName());
            }
        }
    }

    public void makeClientFiles() {
        for (File file : clientPlayer.files()) {
            if (!file.mkdir()) {
                System.out.println("[DEBUG] Failed to create directory: " + file.getName());
            }
        }
    }

    public boolean deleteClientFiles() {
        return deleteRecursively(clientPlayer.getPlayerFolder());
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

    public Player[] getPlayersArray() {
        return getPlayers().toArray(new Player[0]);
    }

    public Player getClientPlayer() {
        return clientPlayer;
    }

    public ArrayList<Player> getPlayers() {
        synchronize();
        return new ArrayList<Player>(players.values());
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }
}

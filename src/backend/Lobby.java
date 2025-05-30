package backend;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import gameplay.Player;

public class Lobby {
    private final String playerSpacePath;
    private final boolean clientIsHost;
    private final ArrayList<Player> players;
    private final Player clientPlayer;

    public Lobby(Session session) {
        playerSpacePath = session.getPlayerSpacePath();
        clientPlayer = session.getClientPlayer();
        clientIsHost = session.clientIsHost();
        players = new ArrayList<Player>();
    }

    public void synchronize() {
        for (File playerFile : new File(playerSpacePath).listFiles()) {
            Player player = Player.fromFile(playerFile);
            if (!players.contains(player)) {
                players.add(player);
            }
        }
        for (int i = players.size() - 1; i >= 0; i--) {
            if (!players.get(i).getPlayerFolder().exists()) {
                players.remove(i);
            }
        }
    }

    public void makeClientFiles() {
        for (File file : clientPlayer.files()) {
            if (file.mkdir()) {
                System.out.println("[DEBUG] Directory created: " + file.getName());
            } else {
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
        synchronize();
        Player[] playerNames = Arrays.stream(players.toArray()).toArray(Player[]::new);
        return playerNames;
    }

    public Player getClientPlayer() {
        return clientPlayer;
    }

    public ArrayList<Player> getPlayers() {
        return (ArrayList<Player>) players.clone();
    }
}

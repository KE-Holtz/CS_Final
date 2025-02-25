package backend;

import java.io.File;
import java.util.ArrayList;

import gameplay.Player;

public class Lobby {
    private final String            playerSpacePath;
    private final boolean           clientIsHost;
    private final ArrayList<Player> players;
    private final Player            clientPlayer;

    public Lobby(String playerSpacePath, String clientName, boolean clientIsHost) {
        this.playerSpacePath = playerSpacePath;
        clientPlayer = new Player(clientName, playerSpacePath);
        this.clientIsHost = clientIsHost;
        players = new ArrayList<Player>();
    }

    public void synchronize() {
        players.clear();
        players.add(clientPlayer);
        for (File playerFile : new File(playerSpacePath).listFiles()) {
            Player player = Player.fromFile(playerFile);
            if (!player.getName().equals(clientPlayer.getName())) {
                players.add(player);
            }
        }
    }

    public void makeClientFiles(){
        for (File file : clientPlayer.files()) {
            if(file.mkdir()){
                System.out.println("Directory created: " + file.getName());
            } else {
                System.out.println("Failed to create directory: " + file.getName());
            }
        }
    }

    public boolean deleteClientFiles(){
        return deleteRecursively(clientPlayer.getPlayerFolder());
    }

    // Delete the folder and all of its contents
    // TODO: handle failing to delete a file
    private boolean deleteRecursively(File dir) {
        for (File file : dir.listFiles()) {
            deleteRecursively(file);
        }
        if (dir.exists()) {
            if(!dir.delete()){
                return false;
            }
        }
        return true;
    }
}

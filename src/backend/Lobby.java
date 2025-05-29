package backend;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import gameplay.Player;

public class Lobby {
    private final String            playerSpacePath;
    private final boolean           clientIsHost;
    private final HashMap<String, Player> players;
    private final Player            clientPlayer;

    public Lobby(Session session) {
        playerSpacePath = session.getPlayerSpacePath();
        clientPlayer = session.getClientPlayer();
        clientIsHost = session.clientIsHost();
        players = new HashMap<String, Player>();
    }

    public void synchronize() {
        for (File playerFile : new File(playerSpacePath).listFiles()) {
            Player player = Player.fromFile(playerFile);
            if(!players.containsValue(player)){
                players.put(player.getName(), player);
            }
        }
        System.out.println(players);
    }

    public void makeClientFiles(){
        for (File file : clientPlayer.files()) {
            if(file.mkdir()){
                System.out.println("[DEBUG] Directory created: " + file.getName());
            } else {
                System.out.println("[DEBUG] Failed to create directory: " + file.getName());
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

    public Player getClientPlayer() {
        return clientPlayer;
    }

    public ArrayList<Player> getPlayers(){
        return new ArrayList<Player>(players.values());
    }

    public Player getPlayer(String name){
        return players.get(name);
    }
}

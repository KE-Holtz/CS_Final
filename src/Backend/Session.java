package Backend;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;

import gameplay.Player;
import gameplay.Game;

public class Session {
    private final String sessionName;
    private final String sessionSpacePath; // Path to the folder where the session is stored.

    private ArrayList<Player> players = new ArrayList<Player>();// TODO: consider - does the order of players matter?
    private Player clientPlayer; // This user's player object
    private boolean isHost = false;

    private ArrayList<Game> games;// ? List of all available games - TODO: Add an object that automatically
                                  // narrows down available games? could have other functionality as well

    // Constructor ONLY FOR HOSTING
    // Host has extra responsibilities - add files for games, etc
    // ? Make helper methods that return custom errors
    public Session(String name, String sessionSpacePath, String playerName, ArrayList<Game> games) {
        // Solution to the problem of overwriting sessions
        while(new File(sessionSpacePath + "\\" + name).exists()) {
            System.out.println("Session already exists. Please enter a new name.");
            name = System.console().readLine();
        }
        this.sessionName = name;
        this.sessionSpacePath = sessionSpacePath;

        clientPlayer = new Player(playerName, new File(sessionSpacePath + "\\" + name + "\\" + "players"));// TODO:
                                                                                                           // move?
                                                                                                           // rework?
        players.add(clientPlayer);
        isHost = true;

        hostInitialize();
    }

    public Session(String sessionName, String sessionSpacePath, String playerName) {
        this.sessionName = sessionName;
        this.sessionSpacePath = sessionSpacePath;

        clientPlayer = new Player(playerName, new File(sessionSpacePath + "\\" + sessionName + "\\" + "players"));// TODO:
                                                                                                           // move?
                                                                                                           // rework?
        players.add(clientPlayer);
        isHost = false;

        joinInitialize();
    }

    public void hostInitialize() {
        // Create the session folder
        File sessionFolder = new File(sessionSpacePath + "\\" + sessionName);
        File playerFolder = new File(sessionFolder.getAbsolutePath() + "\\" + "players");
        sessionFolder.mkdir();
        playerFolder.mkdir();

        for (Player player : players) {
            for (File file : player.files()) {
                file.mkdir();
            }
        }
    }

    public void joinInitialize() {
        for (Player player : players) {
            for (File file : player.files()) {
                file.mkdir();
            }
        }
        //TODO: syncronize
    }

    public void syncronize() {
        //TODO: implement
        /*Synchronizing makes me think that we are going to need more custom data structures
         * - A custom tree structure for files
         * - A 'Lobby' Structure that can store players in a way that is easy to syncronize
         */
    }
    public void clean() {
        if (isHost) {
            File sessionFolder = new File(sessionSpacePath + "\\" + sessionName);
            deleteRecursively(sessionFolder);
        } else {
            File playerFolder = new File(sessionSpacePath + "\\" + sessionName + "\\" + "players" + "\\" + clientPlayer.getName());
            deleteRecursively(playerFolder);
        }
    }

    // Delete the folder and all of its contents
    // TODO: handle failing to delete a file
    private void deleteRecursively(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                deleteRecursively(file);
            }
        }
        if (dir.exists()) {
            dir.delete();
        }
    }

    // Constructor ONLY FOR JOINING
    // TODO: Make the joining constuctor
    // TODO: Decide on a format for the metadata
    // public Session(File metadataFile){

    // }

    // public static Session joinSession(String name){
    // //TODO: Use the metadata file to get the session, add a new player and join
    // the session.
    // return new Session(name, name, name);
    // }
}

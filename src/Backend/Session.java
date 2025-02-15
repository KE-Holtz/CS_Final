package Backend;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import gameplay.Game;
import gameplay.Player;

public class Session {
    private final String  sessionName;
    private final String  sessionSpacePath; // Path to the folder where the
                                            // session is stored.
    private final boolean isHost;

    private Lobby lobby;

    private ArrayList<Game> games;// ? List of all available games - TODO: Add
                                  // an object that automatically
                                  // narrows down available games? could have
                                  // other functionality as well

    // Constructor ONLY FOR HOSTING
    // Host has extra responsibilities - add files for games, etc

    // ? Make helper methods that return custom errors
    public Session(String sessionName, String sessionSpacePath, String clientName,
                   ArrayList<Game> games) {
        this.sessionSpacePath = sessionSpacePath;
        this.sessionName = sessionName;
        this.lobby = new Lobby(sessionSpacePath + "\\" + sessionName + "\\" + "players", clientName,
                               true);
        isHost = true;
        hostInitialize();
    }

    public Session(String sessionName, String sessionSpacePath, String playerName) {
        this.sessionName = sessionName;
        this.sessionSpacePath = sessionSpacePath;

        lobby = new Lobby(sessionSpacePath + "\\" + sessionName + "\\" + "players", playerName,
                          false);
        isHost = false;
        joinInitialize();
    }

    public void hostInitialize() {
        // Create the session folder
        File sessionFolder = new File(sessionSpacePath + "\\" + sessionName);
        File playerSpaceFolder = new File(sessionFolder.getAbsolutePath() + "\\" + "players");
        if (sessionFolder.mkdir()){
            System.out.println("Session folder created");
        } else {
            System.out.println("Session folder failed to create at " + sessionFolder.getAbsolutePath());
        }
        if(playerSpaceFolder.mkdir()){
            System.out.println("Player space folder created");
        } else {
            System.out.println("Player space folder failed to create at " + playerSpaceFolder.getAbsolutePath());
        }

        lobby.makeClientFiles();
    }

    public void joinInitialize() {
        lobby.makeClientFiles();
        // TODO: synchronize
    }

    public void synchronize() {
        // TODO: implement
        /* Synchronizing makes me think that we are going to need more custom
         * data structures - A custom tree structure for files - A 'Lobby'
         * Structure that can store players in a way that is easy to
         * synchronize */
    }

    public boolean clean() {
        if (isHost) {
            File sessionFolder = new File(sessionSpacePath + "\\" + sessionName);
            if(!deleteRecursively(sessionFolder)){
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
            if(!dir.delete()){
                return false;
            }
        }
        return true;
    }

    public static String getSessionChoice(String sessionSpacePath, Scanner console) {
        if(new File(sessionSpacePath).list().length == 0)
            System.out.println("Waiting for available sessions...");
        while(new File(sessionSpacePath).list().length == 0);
        System.out.println("Available sessions:");
        for (String i : new File(sessionSpacePath).list()) {
            System.out.println(i);
        }
        System.out.print("Enter the name of the session you would like to join: ");
        String sessionName = console.nextLine();
        while (!new File(sessionSpacePath + "\\" + sessionName).exists()) {
            System.out.println("Session does not exist. Please enter a valid session name: ");
            sessionName = console.nextLine();
        }
        return sessionName;
    }

    // Constructor ONLY FOR JOINING
    // TODO: Make the joining constuctor
    // TODO: Decide on a format for the metadata
    // public Session(File metadataFile){

    // }

    // public static Session joinSession(String name){
    // //TODO: Use the metadata file to get the session, add a new player and
    // join
    // the session.
    // return new Session(name, name, name);
    // }
}

package Backend;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;

import gameplay.Player;
import gameplay.Game;


public class Session {
    private final String name;
    private final String id;
    private final String sessionSpacePath ; //Path to the folder where the session is stored.

    private ArrayList<Player> players = new ArrayList<Player>();
    private Player clientPlayer; //This user's player object
    private boolean isHost = false;

    private ArrayList<Game> games;//List of all available games - TODO: Add an object that automatically narrows down available games? could have other functionality as well

    //TODO: Figure out how to create files and folders
    //Constructor ONLY FOR HOSTING

    //Host has extra responsibilities - add files for games, etc


    //Make helper methods that return custom errors
    public Session(String name, String sessionSpacePath, String playerName, ArrayList<Game> games){
        this.name = name;
        this.id = Math.random() + "";//Generate a unique id for the session (Might be unnecessary)
        this.sessionSpacePath = sessionSpacePath;

        clientPlayer = new Player(playerName);
        players.add(clientPlayer);
        isHost = true;

        initialize();
    }

    public void initialize(){
        //Create the session folder
        File sessionFolder = new File(sessionSpacePath + "\\" + name);
        File playerFolder = new File(sessionFolder.getAbsolutePath() + "\\" + "players");
        //TODO: Check if the folder already exists
        sessionFolder.mkdir();
        playerFolder.mkdir();

        for (Player player : players) {
            for (File file : player.getPlayerFile(playerFolder)) {
                file.mkdir();
            }
        }
    }

    public void clean(){
        if (isHost) {
            File sessionFolder = new File(sessionSpacePath + "\\" + name);
            File playerFolder = new File(sessionFolder.getAbsolutePath() + "\\" + "players");
            //TODO: remove recursive (check if exists too)
            while(!playerFolder.delete())
                System.out.println("Failed to delete session folder. Retrying...");
            while(!sessionFolder.delete())
                System.out.println("Failed to delete session folder. Retrying...");
        }
    }

    //Constructor ONLY FOR JOINING
    //TODO: Make the joining constuctor
    //TODO: Decide on a format for the metadata
    // public Session(File metadataFile){

    // }

    // public static Session joinSession(String name){
    //     //TODO: Use the metadata file to get the session, add a new player and join the session.
    //     return new Session(name, name, name);
    // }
}

 package gameplay;

import java.io.File;
import java.util.ArrayList;

/*TODO: Implement the player class
-Name
-Player ID/number
-Wins/score?
-...?
*/
public class Player {
    private String name;
    private final String playerSpacePath;

    private ArrayList<File> playerFiles;// TODO: Tree? This could have some limitations.
    private File playerFolder;
    private File metadata;


    // Using this files must either be added in a valid order without fail or the
    // session must validate the arraylist
    // Could be validated by iterating over the arraylist and going back if there
    // are any extra files that have not been created
    // TODO: Create a validator method here or in the session class

    public Player(String name, String playerSpacePath) {
        this.name = name;

        playerFiles = new ArrayList<File>();

        this.playerSpacePath = playerSpacePath;

        playerFolder = new File(playerSpacePath + "\\" + name);
        playerFiles.add(playerFolder);

        metadata = new File(playerFolder.getAbsolutePath() + "\\" + "metadata");
        playerFiles.add(metadata);

        String metadataParent = metadata.getAbsolutePath() + "\\"; // * a tree would make this much simpler
        playerFiles.add(new File(metadataParent + "color:red"));//TODO: Decide where metadata comes from/what is stored, decide where to create it
        playerFiles.add(new File(metadataParent + "wins:99"));// Example metadata values - I think that the key:value
                                                              // format is a good idea, and if we want more subfolder in
                                                              // here they should just be named normally and not given
                                                              // the key:value format


    }

    public static Player fromFile(File playerFile){
        //Add more functionality when theres more than a name
        return new Player(playerFile.getName(), playerFile.getParentFile().getPath());
    }

    public File[] files() {
        return playerFiles.toArray(new File[playerFiles.size()]);
    }

    public String getName() {
        return name;
    }

    public File getPlayerFolder() {
        return playerFolder;
    }
}

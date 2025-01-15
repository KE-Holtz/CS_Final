package gameplay;

import java.io.File;

/*TODO: Implement the player class
-Name
-Player ID/number
-Wins/score?
-...?
*/
public class Player {
    private String name;
    public Player(String name){
        this.name = name;
    }

    public File[] getPlayerFile(File playerFolder){
        File[] playerFiles = new File[]{
            new File(playerFolder.getAbsolutePath() + "\\" + name),
            //TODO: add more files
        };
        return playerFiles;
    }
}

 package gameplay;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import javax.print.attribute.standard.MediaSize.Other;

import backend.publicVars.PublicVar;

public class Player {
    private String name;
    private final String playerSpacePath;

    private ArrayList<File> playerFiles;// TODO: Tree? This could have some limitations.
    private File playerFolder;
    private File globalVarsDir;
    private File publicVarsDir;

    private HashMap<String, PublicVar> publicVars;

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

        globalVarsDir = new File(playerFolder.getAbsolutePath() + "\\" + "globalVars");
        playerFiles.add(globalVarsDir);

        publicVarsDir = new File(playerFolder.getAbsolutePath() + "\\" + "publicVars");
        playerFiles.add(publicVarsDir);

        publicVars = new HashMap<>();
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

    public void addVariable(PublicVar var) {
        publicVars.put(var.getName(), var);
    }

    public Optional<PublicVar> getVariable(String name) {
        if (publicVars.get(name) == null){
            if(new File(publicVarsDir.getName() + "\\" + name).exists()){
                PublicVar importedVar = new PublicVar<>(this, name, null);
                publicVars.put(name, importedVar);
                return Optional.of(importedVar);
            } else {
                return Optional.empty();
            }
        }
        return Optional.ofNullable(publicVars.get(name));
    }

    @Override
    public boolean equals(Object other){
        return ((Player)other).getName().equals(name);
    }
}
